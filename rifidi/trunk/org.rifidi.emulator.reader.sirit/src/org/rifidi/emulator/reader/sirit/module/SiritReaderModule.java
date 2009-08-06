/*
 *  SiritReaderModule.java
 *
 *  Created:	28.05.2009
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.module;

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
import org.rifidi.emulator.reader.sirit.command.exception.SiritExceptionHandler;
import org.rifidi.emulator.reader.sirit.formatter.SiritCommandFormatter;
import org.rifidi.emulator.reader.sirit.tagbuffer.SiritTagMemory;

/**
 * This is a class that represents a reader module for the Sirit INfinity 510
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 */

public class SiritReaderModule extends AbstractPowerModule implements
		ReaderModule {
	/**
	 * The startup text
	 */
	public static final String startupText = "<STARTUP>";

	/**
	 * The type of reader
	 */
	public static final String READERTYPE = "Sirit INfinity 510";

	/**
	 * The location of the xml for this reader.
	 */
	public static final String XMLLOCATION = "org/rifidi/emulator/reader/sirit/module/";

	/**
	 * The digester.
	 */
	private CommandXMLDigester digester;

	/**
	 * The name of the reader.
	 */
	private String name;

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(SiritReaderModule.class);

	/**
	 * Console logger for this class
	 */
	private Log consoleLogger = null;

	/**
	 * The Communication for the machine interface
	 */
	private TCPServerCommunication tcpComm;

	/**
	 * The controller that will control the commands for the reader.
	 */
	private InteractiveCommandController interactiveCommandController;

	/**
	 * The command adapter for the commands.
	 */
	private CommandAdapter interactiveCommandAdapter;

	/**
	 * The resources for this reader which are shared among controllers.
	 */
	private SiritReaderSharedResources sharedResources;

	/**
	 * an empty constructor that is needed for jaxb
	 */
	public SiritReaderModule() {
	}

	/**
	 * @param powerControlSignal
	 * @param properties
	 */
	public SiritReaderModule(ControlSignal<Boolean> powerControlSignal,
			GeneralReaderPropertyHolder properties) {

		super(SiritReaderModuleOffPowerState.getInstance(), powerControlSignal);

		// write some basic information to the console to let the user know that
		// the reader is starting
		consoleLogger = LogFactory.getLog("console."
				+ properties.getReaderName());
		consoleLogger.info(SiritReaderModule.startupText
				+ "Instantiated Sirit Reader with name: "
				+ properties.getReaderName());
		consoleLogger.info(SiritReaderModule.startupText
				+ properties.getReaderName() + " IP Address: "
				+ properties.getProperty("ipaddress") + ":"
				+ properties.getProperty("communicationport"));
		consoleLogger.info(SiritReaderModule.startupText
				+ properties.getReaderName() + " has "
				+ properties.getNumAntennas() + " antennas");

		// set the name of the reader equal to the name given in the
		// generalReaderPropertyHolder
		this.name = properties.getReaderName();

		// Initialize the digester and read in the command hander methods
		// supplied in the reader.xml file
		digester = new CommandXMLDigester();
		digester.parseToCommand(this.getClass().getClassLoader()
				.getResourceAsStream(XMLLOCATION + "reader.xml"));

		// Create a new tag memory
		SiritTagMemory tagMemory = new SiritTagMemory();

		// , create antennas
		HashMap<Integer, Antenna> antennaList = new HashMap<Integer, Antenna>();
		for (int i = 0; i < properties.getNumAntennas(); i++) {
			logger.debug("creating an antenna: " + i);
			antennaList.put(i, new Antenna(i, name));
		}
		// , create a new radio and supply the tagMemory and antennas to the
		// radio
		GenericRadio genericRadio = new GenericRadio(antennaList, 25, name);

		// Parse out the IP and port from the GeneralReaderPropertyHolder
		String ipAddress = ((String) properties.getProperty("ipaddress"));
		int tcpPort = Integer.parseInt(((String) properties
				.getProperty("communicationport")));

		// create the shared resources and supply necessary information to it
		this.sharedResources = new SiritReaderSharedResources(genericRadio,
				tagMemory, powerControlSignal,
				new ControlSignal<Boolean>(false), new ControlSignal<Boolean>(
						false), name, digester.getAllCommands(), digester,
				new SiritExceptionHandler(), ipAddress, tcpPort, antennaList
						.size());

		// Create the communication object
		this.tcpComm = new TCPServerCommunication(new RawProtocol(),
				this.sharedResources.getInteractivePowerSignal(),
				this.sharedResources.getInteractiveConnectionSignal(),
				ipAddress, tcpPort, this.name, GenericCharStreamReader.class,
				new GenericStringLogFormatter());

		// Build the adapters for this reader
		this.interactiveCommandAdapter = new ReflectiveCommandAdapter(
				"Interactive", new SiritCommandFormatter(),
				new SiritExceptionHandler(), this.sharedResources,
				new RawCommandSearcher());

		// Build the controllers
		this.interactiveCommandController = new InteractiveCommandController(
				new LoginAuthenticatedCommandControllerOperatingState(
						interactiveCommandAdapter), this.sharedResources
						.getInteractivePowerSignal(), this.sharedResources
						.getInteractiveConnectionSignal(), this.tcpComm);
	}

	/**
	 * @return the interactiveCommandAdapter
	 */
	public CommandAdapter getInteractiveCommandAdapter() {
		return interactiveCommandAdapter;
	}

	/**
	 * @return the interactiveCommandController
	 */
	public InteractiveCommandController getInteractiveCommandController() {
		return interactiveCommandController;
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
		return digester.getAllCommands();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModule#getCommandsByCategory(
	 * java.lang.String)
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
	public SiritReaderSharedResources getSharedResources() {
		return sharedResources;
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
	 * @return the tcpComm
	 */
	public TCPServerCommunication getTCPComm() {
		return tcpComm;
	}

	@Override
	public List<String> getGPIPortNumbers(int numberOfPorts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getGPOPortNumbers(int numberOfPorts) {
		// TODO Auto-generated method stub
		return null;
	}
}
