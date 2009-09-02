/*
 *  AwidReaderModule.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.PowerState;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.io.comm.ip.udp.UDPCommunication;
import org.rifidi.emulator.io.protocol.RawProtocol;
import org.rifidi.emulator.reader.awid.command.exception.AwidExceptionHandler;
import org.rifidi.emulator.reader.awid.formatter.AwidAutonomousCommandFormatter;
import org.rifidi.emulator.reader.awid.formatter.AwidCommandFormatter;
import org.rifidi.emulator.reader.awid.sharedrc.tagmemory.AwidTagMemory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.controller.BasicCommandControllerOperatingState;
import org.rifidi.emulator.reader.command.controller.autonomous.AutonomousCommandController;
import org.rifidi.emulator.reader.command.controller.interactive.InteractiveCommandController;
import org.rifidi.emulator.reader.command.controller.interactive.LoginAuthenticatedCommandControllerOperatingState;
import org.rifidi.emulator.reader.command.xml.CommandXMLDigester;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;
import org.rifidi.emulator.reader.control.adapter.ReflectiveCommandAdapter;
import org.rifidi.emulator.reader.control.adapter.searcher.RelativeCommandSearcher;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;

/**
 * This class represents an AWID MPR reader.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidReaderModule extends AbstractPowerModule implements
		ReaderModule {

	public static final String startupText = "<STARTUP>";

	public static final String READERTYPE = "AwidMPR";

	public static final String XMLLOCATION = "org/rifidi/emulator/reader/awid/module/";

	private boolean rf_power;

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AwidReaderModule.class);

	/**
	 * The baud to transfer at
	 */
	// private static final int BAUD = 9600;

	/**
	 * The number of data bits to use
	 */
	// private static final int DATA_BITS = SerialPort.DATABITS_8;

	/**
	 * The number of stop bits available
	 */
	// private static final int STOP_BITS = SerialPort.STOPBITS_1;

	/**
	 * The number of parity bits available
	 */
	// private static final int PARITY_BITS = SerialPort.PARITY_NONE;

	/**
	 * The flow control used
	 */
	// private static final int FLOW_CONTROL = SerialPort.FLOWCONTROL_NONE;

	/**
	 * The max message length of the packet
	 */
	// private static final int MAX_MESSAGE_LENGTH = 1024;

	/**
	 * The command adapter which the interactive mode uses when a user is in
	 * authenticated mode.
	 */
	private CommandAdapter interactiveCommandAdapter;

	/**
	 * The command adapter for autonomous methods.
	 */
	private CommandAdapter autonomousCommandAdapter;

	/**
	 * The communication which the interactive controller uses.
	 */
	private UDPCommunication interactiveCommunication;

	/**
	 * The interactive command controller for this. This allows the reader to
	 * accept and process commands interactively.
	 */
	private InteractiveCommandController interactiveCommandController;

	/**
	 * The command controller for autonomous methods
	 */
	private AutonomousCommandController autonomousCommandController;

	/**
	 * The resources for this reader which are shared among controllers.
	 */
	private AwidReaderSharedResources sharedResources;

	/**
	 * The name of the reader
	 */
	private String name;

	/**
	 * The digester, gets and calls all commands.
	 */
	private CommandXMLDigester digester;

	/**
	 * Console logger for this class
	 */
	private Log consoleLogger = null;

	/**
	 * Blank constructor that should ONLY be used by sun services API!!!
	 * 
	 */
	public AwidReaderModule() {

	}

	/**
	 * Constructor for AwidReaderModule. This represents an AWID MPR reader.
	 * Properties: serial_port: a String that matches COM<number>
	 * 
	 * @param initialPowerState
	 * @param powerControlSignal
	 */
	public AwidReaderModule(ControlSignal<Boolean> powerControlSignal,
			GeneralReaderPropertyHolder properties) {
		super(AwidReaderModuleOffPowerState.getInstance(), powerControlSignal);
		rf_power = true;
		this.name = properties.getReaderName();

		// Get the digester ready
		digester = new CommandXMLDigester();
		digester.parseToCommand(this.getClass().getClassLoader()
				.getResourceAsStream(XMLLOCATION + "reader.xml"));

		consoleLogger = LogFactory.getLog("console."
				+ properties.getReaderName());
		consoleLogger.info(AwidReaderModule.startupText
				+ "Instantiated AWID MPR with name: "
				+ properties.getReaderName());
		consoleLogger.info(AwidReaderModule.startupText
				+ properties.getReaderName() + " Serial Port: "
				+ properties.getProperty("serial_port"));
		consoleLogger.info(AwidReaderModule.startupText
				+ properties.getReaderName() + " has "
				+ properties.getNumAntennas() + " antennas");

		HashMap<Integer, Antenna> antennaHash = new HashMap<Integer, Antenna>();
		String antennaSequence = "";
		for (int i = 0; i < properties.getNumAntennas(); i++) {
			// start antenna number at 1.
			antennaHash.put(i, new Antenna(i, name));
			antennaSequence += " " + i;
		}
		GenericRadio newRadio = new GenericRadio(antennaHash, 25, name);
		// Map<String, Antenna> antennas = new HashMap<String, Antenna>();
		// antennas.put("antenna1", new Antenna(255, null));
		AwidTagMemory newTagMemory = new AwidTagMemory();

		// Start the shared resources
		AwidReaderSharedResources awidSharedResources = new AwidReaderSharedResources(
				newRadio, newTagMemory, new ControlSignal<Boolean>(false),
				new ControlSignal<Boolean>(false), new ControlSignal<Boolean>(
						false), powerControlSignal, properties.getReaderName(),
				digester.getAllCommands(), digester,
				new AwidExceptionHandler(), properties.getNumAntennas());

		this.sharedResources = awidSharedResources;

		// this.interactiveCommunication = new UDPCommunication(
		// new RawProtocol(), this.sharedResources
		// .getInteractivePowerSignal(), this.sharedResources
		// .getInteractiveConnectionSignal(), ((String) properties
		// .getProperty("inet_address")).split(":")[0], Integer
		// .parseInt(((String) properties
		// .getProperty("inet_address")).split(":")[1]), this.name,
		// GenericByteStreamReader.class, new GenericStringLogFormatter());
		try {
			this.interactiveCommunication = new UDPCommunication(
					new RawProtocol(), this.sharedResources
							.getInteractivePowerSignal(), this.sharedResources
							.getInteractiveConnectionSignal(),
					((String) properties.getProperty("inet_address"))
							.split(":")[0],
					Integer.parseInt(((String) properties
							.getProperty("inet_address")).split(":")[1]), false);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CommunicationException e) {
			e.printStackTrace();
		}

		interactiveCommandAdapter = new ReflectiveCommandAdapter("Interactive",
				new AwidCommandFormatter(), new AwidExceptionHandler(),
				this.sharedResources, new RelativeCommandSearcher());

		autonomousCommandAdapter = new ReflectiveCommandAdapter("Autonomous",
				new AwidAutonomousCommandFormatter(),
				new AwidExceptionHandler(), this.sharedResources,
				new RelativeCommandSearcher());

		interactiveCommandController = new InteractiveCommandController(
				new LoginAuthenticatedCommandControllerOperatingState(
						interactiveCommandAdapter), this.sharedResources
						.getInteractivePowerSignal(), this.sharedResources
						.getInteractiveConnectionSignal(),
				this.interactiveCommunication);

		Map<byte[], Integer> autoCommands = new HashMap<byte[], Integer>();
		autoCommands.put(null, 300);

		autonomousCommandController = new AutonomousCommandController(
				new BasicCommandControllerOperatingState(
						autonomousCommandAdapter), this.sharedResources
						.getAutonomousPowerSignal(), null,
				this.interactiveCommunication, autoCommands);

		this.sharedResources.saveAllSharedProperties();
		// start the xmlrpc-server for this reader
		// logger.info("Starting XmlRpc-server on: "
		// + properties.getXmlRpcAddress());
		// tagManager = new TagManager(properties.getXmlRpcAddress());
		// logger.info("Started XmlRpc-server on: "
		// + properties.getXmlRpcAddress());
	}

	/**
	 * Exposes the underlying method to package members.
	 * 
	 * @see org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#changePowerState(org.rifidi.emulator.common.PowerState)
	 */
	@Override
	protected void changePowerState(PowerState anotherPowerState) {
		logger.debug("Changing power state...");
		super.changePowerState(anotherPowerState);
	}

	/**
	 * @return the interactiveCommandAdapter
	 */
	public CommandAdapter getInteractiveCommandAdapter() {
		return interactiveCommandAdapter;
	}

	/**
	 * Exposes the underlying method to package members.
	 * 
	 * @see org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#getPowerControlSignal()
	 */
	@Override
	protected ControlSignal<Boolean> getPowerControlSignal() {
		return super.getPowerControlSignal();
	}

	/**
	 * Returns the sharedResources for the AWID reader.
	 * 
	 * @return the sharedResources
	 */
	public AwidReaderSharedResources getSharedResources() {
		return sharedResources;
	}

	/**
	 * @return the serialCommunication
	 */
	public UDPCommunication getInteractiveCommunication() {
		return interactiveCommunication;
	}

	/**
	 * @return the serialCommunication
	 */
	public InteractiveCommandController getInteractiveCommandController() {
		return interactiveCommandController;
	}

	/**
	 * Returns the Autonomous Command Controller.
	 * 
	 * @return the Autonomous Command Controller.
	 */
	public AutonomousCommandController getAutonomousCommandController() {
		return autonomousCommandController;
	}

	/**
	 * Returns the Autonomous Command Adapter.
	 * 
	 * @return the Autonomous Command Adapter.
	 */
	public CommandAdapter getAutonomousCommandAdapter() {
		return autonomousCommandAdapter;
	}

	/**
	 * Returns the name of the reader.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public HashMap<String, CommandObject> getCommandList() {
		return digester.getAllCommands();
	}

	public Collection<String> getAllCategories() {
		return digester.getAllCategories();
	}

	public Collection<CommandObject> getCommandsByCategory(String category) {
		return digester.getCommandsByCategory(category);
	}

	/**
	 * Finalize the reader
	 */
	public void finalize() {
	}

	public boolean isRf_power() {
		return rf_power;
	}

	public void setRf_power(boolean rf_power) {
		this.rf_power = rf_power;
	}

	@Override
	public List<Integer> getGPIPortNumbers() {
		return null;
	}

	@Override
	public List<Integer> getGPOPortNumbers() {
		// TODO Auto-generated method stub
		return null;
	}

}
