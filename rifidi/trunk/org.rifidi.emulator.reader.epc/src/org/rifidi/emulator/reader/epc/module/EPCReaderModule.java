/*
 *  EPCReaderModule.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.epc.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.PowerState;
import org.rifidi.emulator.io.comm.ip.tcpserver.TCPServerCommunication;
import org.rifidi.emulator.io.comm.logFormatter.GenericStringLogFormatter;
import org.rifidi.emulator.io.comm.streamreader.GenericCharStreamReader;
import org.rifidi.emulator.io.protocol.RawProtocol;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.controller.interactive.InteractiveCommandController;
import org.rifidi.emulator.reader.command.controller.interactive.LoginUnconnectedCommandControllerOperatingState;
import org.rifidi.emulator.reader.command.xml.CommandXMLDigester;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;
import org.rifidi.emulator.reader.control.adapter.ReflectiveCommandAdapter;
import org.rifidi.emulator.reader.control.adapter.searcher.RawCommandSearcher;
import org.rifidi.emulator.reader.control.adapter.searcher.SelectiveCommandSearcher;
import org.rifidi.emulator.reader.epc.command.exception.EPCExceptionHandler;
import org.rifidi.emulator.reader.epc.formatter.EPCCommandFormatter;
import org.rifidi.emulator.reader.epc.formatter.EPCLoginFormatter;
import org.rifidi.emulator.reader.epc.sharedrc.memory.EPCTagMemory;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;

/**
 * This is a reader module for an implementation of the EPC reader protocol
 * version 1.1.
 * 
 * @author matt
 */
public class EPCReaderModule extends AbstractPowerModule implements
		ReaderModule {

	public static final String startupText = "<STARTUP>";

	public static final String READERTYPE = "Reader Protocol v1.1";

	public static final String XMLLOCATION = "org/rifidi/emulator/reader/epc/module/";

	public static final int MAX_LOGIN_ATTEMPTS = 3;

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(EPCReaderModule.class);

	/**
	 * Console logger for this class
	 */
	private Log consoleLogger = null;

	/**
	 * The digester.
	 */
	private CommandXMLDigester digester;

	private EPCReaderSharedResources sharedResources;

	private String name;

	private HashMap<String, CommandObject> commandMap;

	private InteractiveCommandController interactiveCommandController;

	private CommandAdapter interactiveCommandAdapter;

	/**
	 * The communication which the interactive controller uses.
	 */
	private TCPServerCommunication interactiveCommunication;

	/**
	 * The command adapter which is used to allow for login session-based
	 * support.
	 */
	private CommandAdapter loginCommandAdapter;
	
	/**
	 * Blank constructor that should ONLY be used by sun services API!!!
	 *
	 */
	public EPCReaderModule(){
		
	}
	
	/**
	 * Constructor. Required properties: <br />
	 * inet_address: a String containing a valid ipaddress in the format
	 * &lt;ip&gt;:&lt;port&gt;
	 * 
	 * @param powerControlSignal
	 * @param properties
	 */
	public EPCReaderModule(ControlSignal<Boolean> powerControlSignal,
			GeneralReaderPropertyHolder properties) {
		super(EPCReaderModuleOffPowerState.getInstance(), powerControlSignal);

		logger.debug("Creating an EPC reader");
		
		consoleLogger = LogFactory.getLog("console."
				+ properties.getReaderName());
		consoleLogger.info(EPCReaderModule.startupText
				+ "Instantiated Reader Protocol v1.1 with name: "
				+ properties.getReaderName());
		consoleLogger.info(EPCReaderModule.startupText
				+ properties.getReaderName() + " IP Address: "
				+ properties.getProperty("inet_address"));
		consoleLogger.info(EPCReaderModule.startupText
				+ properties.getReaderName() + " has "
				+ properties.getNumAntennas() + " antennas");

		this.name = properties.getReaderName();

		HashMap<Integer, Antenna> antennaHash = new HashMap<Integer, Antenna>();
		for (int i = 0; i < properties.getNumAntennas(); i++) {
			// start antenna number at 1.
			antennaHash.put(i, new Antenna(i, name));
		}
		/* Make a Radio for the reader */
		GenericRadio epcRadio = new GenericRadio(antennaHash, 25, name);

		this.digester = new CommandXMLDigester();
		this.digester.parseToCommand(this.getClass().getClassLoader()
				.getResourceAsStream(XMLLOCATION + "reader.xml"));

		this.commandMap = digester.getAllCommands();

		EPCTagMemory tagMemory = new EPCTagMemory();

		EPCExceptionHandler exc = new EPCExceptionHandler();

		this.sharedResources = new EPCReaderSharedResources(epcRadio,
				tagMemory, powerControlSignal,
				new ControlSignal<Boolean>(false), new ControlSignal<Boolean>(
						false), properties.getReaderName(), digester
						.getAllCommands(), digester, exc, properties.getNumAntennas());

		this.loginCommandAdapter = new ReflectiveCommandAdapter("Login",
				new EPCLoginFormatter(), exc, this.sharedResources,
				new RawCommandSearcher());

		/*
		 * Make a new interactive communication w/raw protocol
		 */
		this.interactiveCommunication = new TCPServerCommunication(
				new RawProtocol(), this.sharedResources
						.getInteractivePowerSignal(), this.sharedResources
						.getInteractiveConnectionSignal(), ((String) properties
						.getProperty("inet_address")).split(":")[0], Integer
						.parseInt(((String) properties
								.getProperty("inet_address")).split(":")[1]),
				this.name, GenericCharStreamReader.class, new GenericStringLogFormatter());

		this.interactiveCommandAdapter = new ReflectiveCommandAdapter(
				"Interactive", new EPCCommandFormatter(), exc,
				this.sharedResources, new SelectiveCommandSearcher());

		this.interactiveCommandController = new InteractiveCommandController(
				new LoginUnconnectedCommandControllerOperatingState(
						this.loginCommandAdapter,
						this.interactiveCommandAdapter,
						EPCReaderModule.MAX_LOGIN_ATTEMPTS),
				this.sharedResources.getInteractivePowerSignal(),
				this.sharedResources.getInteractiveConnectionSignal(),
				this.interactiveCommunication);

		// new TagManager(properties.getXmlRpcAddress());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getAllCategories()
	 */
	public Collection<String> getAllCategories() {
		return digester.getAllCategories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getCommandList()
	 */
	public HashMap<String, CommandObject> getCommandList() {
		return this.commandMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getCommandsByCategory(java.lang.String)
	 */
	public Collection<CommandObject> getCommandsByCategory(String category) {
		return digester.getCommandsByCategory(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getSharedResources()
	 */
	public EPCReaderSharedResources getSharedResources() {
		return this.sharedResources;
	}

	/**
	 * @return the interactiveCommandController
	 */
	public InteractiveCommandController getInteractiveCommandController() {
		return interactiveCommandController;
	}

	/**
	 * @return the interactiveCommunication
	 */
	public TCPServerCommunication getInteractiveCommunication() {
		return interactiveCommunication;
	}

	/**
	 * @return the loginCommandAdapter
	 */
	public CommandAdapter getLoginCommandAdapter() {
		return loginCommandAdapter;
	}

	/*
	 * Exposes the underlying method to package members.
	 * 
	 * @see org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#changePowerState(org.rifidi.emulator.common.PowerState)
	 */
	@Override
	protected void changePowerState(PowerState anotherPowerState) {
		super.changePowerState(anotherPowerState);
	}

	/**
	 * Finalize the reader
	 */
	public void finalize() {

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
