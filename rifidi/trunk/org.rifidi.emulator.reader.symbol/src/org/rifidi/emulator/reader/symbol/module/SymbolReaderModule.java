/*
 *  SymbolReaderModule.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.symbol.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.PowerState;
import org.rifidi.emulator.io.comm.ip.tcpserver.TCPServerCommunication;
import org.rifidi.emulator.io.comm.logFormatter.GenericByteLogFormatter;
import org.rifidi.emulator.io.comm.streamreader.GenericByteStreamReader;
import org.rifidi.emulator.io.protocol.RawProtocol;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.controller.interactive.InteractiveCommandController;
import org.rifidi.emulator.reader.command.controller.interactive.LoginAuthenticatedCommandControllerOperatingState;
import org.rifidi.emulator.reader.command.xml.CommandXMLDigester;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;
import org.rifidi.emulator.reader.control.adapter.ReflectiveCommandAdapter;
import org.rifidi.emulator.reader.control.adapter.searcher.RawCommandSearcher;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.symbol.command.exception.SymbolBitExceptionHandler;
import org.rifidi.emulator.reader.symbol.formatter.SymbolBitCommandFormatter;
import org.rifidi.emulator.reader.symbol.tagbuffer.SymbolTagMemory;

/**
 * This is a class that represents a reader module for the Symbol XR400/440.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class SymbolReaderModule extends AbstractPowerModule implements
		ReaderModule {

	/**
	 * The startup text
	 */
	public static final String startupText = "<STARTUP>";

	/**
	 * The type of reader
	 */
	public static final String READERTYPE = "SymbolXR400";

	/**
	 * The location of the xml for this reader.  
	 */
	public static final String XMLLOCATION = "org/rifidi/emulator/reader/symbol/module/";

	/**
	 * The name of the reader.  
	 */
	private String name;

	/**
	 * The controller that will control the bit-level commands for the reader.  
	 */
	private InteractiveCommandController interactiveBitController;

	/**
	 * The controller that will control the http commands for the reader.  
	 */
	private InteractiveCommandController interactiveHttpController;

	/**
	 * The command adapter for the bit level commands.  
	 */
	private CommandAdapter interactiveBitAdapter;

	/**
	 * The command adapter for the http commands.  
	 */
	private CommandAdapter interactiveHttpAdapter;

	/**
	 * The shared resources for the reader.  
	 */
	private SymbolReaderSharedResources srsr;

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(SymbolReaderModule.class);

	/**
	 * Console logger for this class
	 */
	private Log consoleLogger = null;

	/**
	 * The digester.
	 */
	private CommandXMLDigester digester;

	/**
	 * The Communication for the http level.  
	 */
	private TCPServerCommunication httpComm;

	/**
	 * The communication for the bit level.  
	 */
	private TCPServerCommunication byteComm;

	/**
	 * Empty constructor, here for reasons too stupid to explain.  Every reader 
	 * module needs one.  
	 */
	public SymbolReaderModule(){
	}
	/**
	 * 
	 * 
	 * @param powerControlSignal
	 * @param properties
	 */
	public SymbolReaderModule(ControlSignal<Boolean> powerControlSignal,
			GeneralReaderPropertyHolder properties) {
		super(SymbolReaderModuleOffPowerState.getInstance(),powerControlSignal);
		consoleLogger = LogFactory.getLog("console."
				+ properties.getReaderName());
		consoleLogger.info(SymbolReaderModule.startupText
				+ "Instantiated Symbol Reader with name: "
				+ properties.getReaderName());
		consoleLogger.info(SymbolReaderModule.startupText
				+ properties.getReaderName() + " IP Address: "
				+ properties.getProperty("byte_address"));
		consoleLogger.info(SymbolReaderModule.startupText
				+ properties.getReaderName() + " has "
				+ properties.getNumAntennas() + " antennas");

		this.name = properties.getReaderName();

		digester = new CommandXMLDigester();
		digester.parseToCommand(this.getClass().getClassLoader()
				.getResourceAsStream(XMLLOCATION + "reader.xml"));

		SymbolTagMemory tagMemory = new SymbolTagMemory();

		HashMap<Integer, Antenna> antennaList = new HashMap<Integer, Antenna>();
		for (int i = 0; i < properties.getNumAntennas(); i++) {
			logger.debug("creating an antenna: " + i);
			antennaList.put(i, new Antenna(i, name));
		}

		/* Make a Radio for the reader */
		GenericRadio genericRadio = new GenericRadio(antennaList, 25, name, tagMemory);

		String byte_address = ((String) properties.getProperty("byte_address"))
				.split(":")[0];
		int byte_port = Integer.parseInt(((String) properties
				.getProperty("byte_address")).split(":")[1]);

		String http_address = ((String) properties.getProperty("http_address"))
				.split(":")[0];
		int http_port = Integer.parseInt(((String) properties
				.getProperty("http_address")).split(":")[1]);

		this.srsr = new SymbolReaderSharedResources(genericRadio, tagMemory,
				new ControlSignal<Boolean>(false), name, null, digester,
				new ControlSignal<Boolean>(false), new ControlSignal<Boolean>(
						false), new ControlSignal<Boolean>(false),
				new ControlSignal<Boolean>(false), antennaList.size());

		this.httpComm = new TCPServerCommunication(new RawProtocol(), this.srsr
				.getInteractiveHttpPowerSignal(), this.srsr
				.getInteractiveHttpConnectionSignal(), http_address, http_port,
				this.name, GenericByteStreamReader.class, new GenericByteLogFormatter());

		this.byteComm = new TCPServerCommunication(new RawProtocol(), this.srsr
				.getInteractiveBytePowerSignal(), this.srsr
				.getInteractiveByteConnectionSignal(), byte_address, byte_port,
				this.name, GenericByteStreamReader.class, new GenericByteLogFormatter());

		this.interactiveBitAdapter = new ReflectiveCommandAdapter(
				"Interactive", new SymbolBitCommandFormatter(), new SymbolBitExceptionHandler(),
				this.srsr, new RawCommandSearcher());

		this.interactiveHttpAdapter = new ReflectiveCommandAdapter(
				"Interactive", null, new SymbolBitExceptionHandler(), this.srsr, new RawCommandSearcher());

		this.interactiveBitController = new InteractiveCommandController(
				new LoginAuthenticatedCommandControllerOperatingState(
						interactiveBitAdapter), this.srsr
						.getInteractiveBytePowerSignal(), this.srsr
						.getInteractiveByteConnectionSignal(), this.byteComm);

		this.interactiveHttpController = new InteractiveCommandController(
				new LoginAuthenticatedCommandControllerOperatingState(
						interactiveHttpAdapter), this.srsr
						.getInteractiveHttpPowerSignal(), this.srsr
						.getInteractiveHttpConnectionSignal(), this.httpComm);
	}

	/**
	 * @return the interactiveBitAdapter
	 */
	public CommandAdapter getInteractiveBitAdapter() {
		return interactiveBitAdapter;
	}

	/**
	 * @return the interactiveBitController
	 */
	public InteractiveCommandController getInteractiveBitController() {
		return interactiveBitController;
	}

	/**
	 * @return the interactiveHttpAdapter
	 */
	public CommandAdapter getInteractiveHttpAdapter() {
		return interactiveHttpAdapter;
	}

	/**
	 * @return the interactiveHttpController
	 */
	public InteractiveCommandController getInteractiveHttpController() {
		return interactiveHttpController;
	}

	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getAllCategories()
	 */
	public Collection<String> getAllCategories() {
		return digester.getAllCategories();
	}

	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getCommandList()
	 */
	public HashMap<String, CommandObject> getCommandList() {
		return digester.getAllCommands();
	}

	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getCommandsByCategory(java.lang.String)
	 */
	public Collection<CommandObject> getCommandsByCategory(String category) {
		return digester.getCommandsByCategory(category);
	}

	/**
	 * Returns the name of the reader.  
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the shared resources for this reader.  
	 */
	public SymbolReaderSharedResources getSharedResources() {
		return srsr;
	}

	public void finalize() {
	}
	
	/**
	 * Changes the power state for this reader.  
	 */
	protected void changePowerState(PowerState anotherPowerState) {
		super.changePowerState(anotherPowerState);
	}
	/**
	 * @return the byteComm
	 */
	public TCPServerCommunication getByteComm() {
		return byteComm;
	}
	/**
	 * @return the httpComm
	 */
	public TCPServerCommunication getHttpComm() {
		return httpComm;
	}
	@Override
	public List<Integer> getGPIPortNumbers() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Integer> getGPOPortNumbers() {
		// TODO Auto-generated method stub
		return null;
	}
}
