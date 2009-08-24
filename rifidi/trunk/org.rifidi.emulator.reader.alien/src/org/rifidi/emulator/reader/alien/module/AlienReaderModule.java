/*
 *  @(#)AlienReaderModule.java
 *
 *  Created:	Oct 26, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.alien.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.PowerState;
import org.rifidi.emulator.io.comm.ip.tcpclient.TCPClientCommunication;
import org.rifidi.emulator.io.comm.ip.tcpserver.TCPServerCommunication;
import org.rifidi.emulator.io.comm.logFormatter.GenericStringLogFormatter;
import org.rifidi.emulator.io.comm.streamreader.GenericCharStreamReader;
import org.rifidi.emulator.io.protocol.RawProtocol;
import org.rifidi.emulator.reader.alien.autonomous.NotifyController;
import org.rifidi.emulator.reader.alien.autonomous.states.AutoStateController;
import org.rifidi.emulator.reader.alien.command.exception.AlienAutoExceptionHandler;
import org.rifidi.emulator.reader.alien.command.exception.AlienExceptionHandler;
import org.rifidi.emulator.reader.alien.formatter.AlienAutonomousCommandFormatter;
import org.rifidi.emulator.reader.alien.formatter.AlienCommandFormatter;
import org.rifidi.emulator.reader.alien.formatter.AlienLoginFormatter;
import org.rifidi.emulator.reader.alien.formatter.AlienOutgoingMessageFormatter;
import org.rifidi.emulator.reader.alien.gpio.GPIController;
import org.rifidi.emulator.reader.alien.heartbeat.HeartbeatController;
import org.rifidi.emulator.reader.alien.io.protocol.AlienProtocol;
import org.rifidi.emulator.reader.alien.rssi.RSSIException;
import org.rifidi.emulator.reader.alien.rssi.RSSIFilter;
import org.rifidi.emulator.reader.alien.sharedrc.tagmemory.AlienTagMemory;
import org.rifidi.emulator.reader.alien.speed.SpeedException;
import org.rifidi.emulator.reader.alien.speed.SpeedFilter;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.controller.BasicCommandControllerOperatingState;
import org.rifidi.emulator.reader.command.controller.interactive.InteractiveCommandController;
import org.rifidi.emulator.reader.command.controller.interactive.LoginUnconnectedCommandControllerOperatingState;
import org.rifidi.emulator.reader.command.xml.CommandXMLDigester;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;
import org.rifidi.emulator.reader.control.adapter.ReflectiveCommandAdapter;
import org.rifidi.emulator.reader.control.adapter.searcher.RawCommandSearcher;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule;
import org.rifidi.emulator.reader.sharedrc.properties.IntegerReaderProperty;
import org.rifidi.emulator.reader.sharedrc.properties.StringReaderProperty;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.tags.enums.TagGen;

/**
 * A module which encapsulates an Alien reader. It extends AbstractPowerModule
 * so that standard power controls are available.
 * 
 * @author John Olender - john@pramari.com
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AlienReaderModule extends AbstractPowerModule implements
		ReaderModule {

	public static final String startupText = "<STARTUP>";

	public static final String READERTYPE = "AlienALR9800";

	public static final String XMLLOCATION = "org/rifidi/emulator/reader/alien/module/";

	/**
	 * Exposes the underlying method to package members.
	 * 
	 * @see org.rifidi.emulator.reader.module.abstract_.
	 *      AbstractPowerModule#changePowerState(org.rifidi.emulator.
	 *      common.PowerState)
	 */
	@Override
	protected void changePowerState(PowerState anotherPowerState) {
		super.changePowerState(anotherPowerState);
	}

	/**
	 * Exposes the underlying method to package members.
	 * 
	 * @see org.rifidi.emulator.reader.module.abstract_.
	 *      AbstractPowerModule#getPowerControlSignal()
	 */
	@Override
	protected ControlSignal<Boolean> getPowerControlSignal() {
		return super.getPowerControlSignal();
	}

	/**
	 * Console logger for this class
	 */
	private Log consoleLogger = null;

	/**
	 * The maximum number of unsuccessful login attempts one can make before
	 * being disconnected in interactive login mode.
	 */
	public static final int MAX_LOGIN_ATTEMPTS = 3;

	/**
	 * The command adapter which the interactive mode uses when a user is in
	 * authenticated mode.
	 */
	private CommandAdapter interactiveCommandAdapter;

	/**
	 * The interactive command controller for this. This allows the reader to
	 * accept and process commands interactively.
	 */
	private InteractiveCommandController interactiveCommandController;

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
	 * The resources for this reader which are shared among controllers.
	 */
	private AlienReaderSharedResources sharedResources;

	/**
	 * The name of the reader.
	 */
	private String name;

	/**
	 * The digester.
	 */
	private CommandXMLDigester digester;

	private NotifyController notifyController;

	private NotifyController autonomousController;

	private CommandAdapter autonomousCommandAdapter;

	private TCPClientCommunication autonomousCommunication;

	/**
	 * Blank constructor that should ONLY be used by sun services API!!!
	 * 
	 */
	public AlienReaderModule() {

	}

	/**
	 * Constructor. Required properties: inet_address: a String containing a
	 * valid ipaddress in the format <ip>:<port> heartbeat_address: a String
	 * containing a valid ipaddress in the format <ip>:<port> that is used for
	 * the heartbeat
	 * 
	 * @param powerControlSignal
	 * @param properties
	 */
	public AlienReaderModule(ControlSignal<Boolean> powerControlSignal,
			GeneralReaderPropertyHolder properties) {
		/* Invoke constructor with initial state to off */

		super(AlienReaderModuleOffPowerState.getInstance(), powerControlSignal);

		consoleLogger = LogFactory.getLog("console."
				+ properties.getReaderName());
		consoleLogger.info(AlienReaderModule.startupText
				+ "Instantiated Alien9800 with name: "
				+ properties.getReaderName());
		consoleLogger.info(AlienReaderModule.startupText
				+ properties.getReaderName() + " IP Address: "
				+ properties.getProperty("inet_address"));
		consoleLogger.info(AlienReaderModule.startupText
				+ properties.getReaderName() + " has "
				+ properties.getNumAntennas() + " antennas");
		consoleLogger.info(AlienReaderModule.startupText
				+ properties.getReaderName() + " has "
				+ properties.getNumGPIs() + " GPI Ports" + " and "
				+ properties.getNumGPOs() + " GPO Ports");

		this.name = properties.getReaderName();

		digester = new CommandXMLDigester();
		digester.parseToCommand(this.getClass().getClassLoader()
				.getResourceAsStream(XMLLOCATION + "reader.xml"));

		SpeedFilter sf = null;
		RSSIFilter rf = null;

		try {
			sf = new SpeedFilter(0.0f);
		} catch (SpeedException e) {
		}

		try {
			rf = new RSSIFilter(0.0f);
		} catch (RSSIException e) {
			e.printStackTrace();
		}

		/* Make the AlienTagMemory */
		AlienTagMemory alienTagMemory = new AlienTagMemory(TagGen.GEN1,
				TagGen.GEN2);

		HashMap<Integer, Antenna> antennaHash = new HashMap<Integer, Antenna>();
		String antennaSequence = "";
		for (int i = 0; i < properties.getNumAntennas(); i++) {
			// start antenna number at 1.
			antennaHash.put(i, new Antenna(i, name));
			antennaSequence += " " + i;
		}
		antennaSequence = antennaSequence.trim();

		/* Make a Radio for the reader */
		GenericRadio alienRadio = new GenericRadio(antennaHash, 25, name,
				alienTagMemory);

		/* Make new shared resources */
		AlienReaderSharedResources alienSharedResources = new AlienReaderSharedResources(
				alienRadio, null, alienTagMemory, powerControlSignal,
				new ControlSignal<Boolean>(false), new ControlSignal<Boolean>(
						false), new ControlSignal<Boolean>(false),
				new ControlSignal<Boolean>(false), new ControlSignal<Boolean>(
						false), properties.getReaderName(), digester
						.getAllCommands(), digester,
				new AlienExceptionHandler(), ((String) properties
						.getProperty("inet_address")).split(":")[0], Integer
						.parseInt(((String) properties
								.getProperty("inet_address")).split(":")[1]),
				sf, rf, antennaHash.size());

		int numGPOs = properties.getNumGPOs();
		int numGPIs = properties.getNumGPIs();

		for (int i = 0; i < numGPIs; i++) {
			alienSharedResources.getGpioController().addGPIPort(i);
		}
		for (int i = 0; i < numGPOs; i++) {
			alienSharedResources.getGpioController().addGPOPort(i);
		}

		/* Assign applicable properties to the AlienTagMemory. */
		IntegerReaderProperty pTime = (IntegerReaderProperty) alienSharedResources
				.getPropertyMap().get("persisttime");
		alienTagMemory.setPersistTime(pTime);

		// initialize antennas to scan to be all antennas.
		StringReaderProperty srp = new StringReaderProperty(antennaSequence);
		alienSharedResources.getPropertyMap().put("antennasequence", srp);

		/* Assign as the shared resources for this module. */
		this.sharedResources = alienSharedResources;

		/*
		 * Make a new interactive communication w/raw protocol
		 */
		this.interactiveCommunication = new TCPServerCommunication(
				new AlienProtocol(), this.sharedResources
						.getInteractivePowerSignal(), this.sharedResources
						.getInteractiveConnectionSignal(), ((String) properties
						.getProperty("inet_address")).split(":")[0], Integer
						.parseInt(((String) properties
								.getProperty("inet_address")).split(":")[1]),
				this.name, GenericCharStreamReader.class,
				new GenericStringLogFormatter());

		this.autonomousCommunication = new TCPClientCommunication(
				new RawProtocol(), this.sharedResources
						.getAutoCommCommunicationPower(), this.sharedResources
						.getAutoCommConnectionSignal(), ((String) properties
						.getProperty("inet_address")).split(":")[0], Integer
						.parseInt(((String) properties
								.getProperty("inet_address")).split(":")[1]),
				this.name, GenericCharStreamReader.class,
				new GenericStringLogFormatter());

		/* Create the command adapters for the interactive command controller */

		/* Unauthenticated */
		this.loginCommandAdapter = new ReflectiveCommandAdapter("Login",
				new AlienLoginFormatter(), new AlienExceptionHandler(),
				this.sharedResources, new RawCommandSearcher());

		/* Authenticated */
		this.interactiveCommandAdapter = new ReflectiveCommandAdapter(
				"Interactive", new AlienCommandFormatter(),
				new AlienExceptionHandler(), this.sharedResources,
				new RawCommandSearcher());

		this.autonomousCommandAdapter = new ReflectiveCommandAdapter(
				"Autonomous", new AlienAutonomousCommandFormatter(),
				new AlienAutoExceptionHandler(), this.sharedResources,
				new RawCommandSearcher());

		/* Create the interactive command controller */
		this.interactiveCommandController = new InteractiveCommandController(
				new LoginUnconnectedCommandControllerOperatingState(
						this.loginCommandAdapter,
						this.interactiveCommandAdapter,
						AlienReaderModule.MAX_LOGIN_ATTEMPTS),
				this.sharedResources.getInteractivePowerSignal(),
				this.sharedResources.getInteractiveConnectionSignal(),
				this.interactiveCommunication);

		this.notifyController = new NotifyController(
				new BasicCommandControllerOperatingState(
						this.autonomousCommandAdapter), this.sharedResources
						.getNotifyControlSignal(), this.sharedResources
						.getAutoCommConnectionSignal(),
				this.autonomousCommunication, this.sharedResources,
				new AlienOutgoingMessageFormatter());

		GPIController startTrig = new GPIController(true, alienSharedResources);
		sharedResources.getGpioController().addObserver(startTrig);
		sharedResources.setAutoStartTrigger(startTrig);

		GPIController stopTrig = new GPIController(false, alienSharedResources);
		sharedResources.getGpioController().addObserver(stopTrig);
		sharedResources.setAutoStopTrigger(stopTrig);

		AutoStateController asc = new AutoStateController(sharedResources,
				notifyController);
		sharedResources.setAutoStateController(asc);

		/* Make a new heartbeat communication which is output only */

		String heartbeatIp = ((String) properties
				.getProperty("heartbeat_address")).split(":")[0];
		Integer heartbeatPort = Integer.parseInt(((String) properties
				.getProperty("heartbeat_address")).split(":")[1]);

		int heartBeatTime = ((IntegerReaderProperty) alienSharedResources
				.getPropertyMap().get("heartbeattime")).getValue();

		int count = ((IntegerReaderProperty) alienSharedResources
				.getPropertyMap().get("heartbeatcount")).getValue();

		String heartbeatPower = "";

		heartbeatPower = properties.getPropertiesMap().get("heartbeat_power");
		if (heartbeatPower == null) {
			heartbeatPower = "false";
		}

		Integer heartbeatBindingPort = 3989;

		try {
			heartbeatBindingPort = Integer.parseInt(properties
					.getPropertiesMap().get("heartbeat_port"));
		} catch (NumberFormatException ex) {
			heartbeatBindingPort = 3989;
		}

		boolean power;
		if (heartbeatPower.equalsIgnoreCase("true")) {
			power = true;
		} else {
			power = false;
		}

		HeartbeatController hbController = new HeartbeatController(heartbeatIp,
				heartbeatPort, heartbeatBindingPort, alienSharedResources,
				heartBeatTime, count, power);
		alienSharedResources.setHearbeatController(hbController);

		/* Save all shared properties */
		this.sharedResources.saveAllSharedProperties();

		alienRadio.scan(null, alienTagMemory);

	}

	/**
	 * Returns the interactiveCommandAdapter.
	 * 
	 * @return Returns the interactiveCommandAdapter.
	 */
	protected final CommandAdapter getInteractiveCommandAdapter() {
		return this.interactiveCommandAdapter;
	}

	/**
	 * Returns the interactiveCommandController.
	 * 
	 * @return Returns the interactiveCommandController.
	 */
	protected final InteractiveCommandController getInteractiveCommandController() {
		return this.interactiveCommandController;
	}

	/**
	 * Returns the interactiveCommunication.
	 * 
	 * @return Returns the interactiveCommunication.
	 */
	protected final TCPServerCommunication getInteractiveCommunication() {
		return this.interactiveCommunication;
	}

	/**
	 * Returns the loginCommandAdapter.
	 * 
	 * @return Returns the loginCommandAdapter.
	 */
	protected final CommandAdapter getLoginCommandAdapter() {
		return this.loginCommandAdapter;
	}

	/**
	 * Returns the sharedResources.
	 * 
	 * @return Returns the sharedResources.
	 */
	public final AlienReaderSharedResources getSharedResources() {
		return this.sharedResources;
	}

	/**
	 * Returns the name of the reader
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getAllCategories()
	 */
	public Collection<String> getAllCategories() {
		return digester.getAllCategories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.module.ReaderModule
	 * #getCommandsByCategory(java.lang.String)
	 */
	public Collection<CommandObject> getCommandsByCategory(String category) {
		return digester.getCommandsByCategory(category);
	}

	/**
	 * @return the autonomousController
	 */
	public NotifyController getAutonomousController() {
		return autonomousController;
	}

	/**
	 * @return the notifyController
	 */
	public NotifyController getNotifyController() {
		return notifyController;
	}

	/**
	 * Finalize the reader
	 */
	public void finalize() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModule#getGPIPortNumbers(int)
	 */
	@Override
	public List<Integer> getGPIPortNumbers() {
		int numberOfPorts = this.getSharedResources().getGpioController()
				.getNumGPIPorts();
		List<Integer> retVal = new ArrayList<Integer>();
		int x = 1;
		for (int i = 0; i < numberOfPorts; i++) {
			retVal.add(x);
			x = x * 2;
		}
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModule#getGPOPortNumbers(int)
	 */
	@Override
	public List<Integer> getGPOPortNumbers() {
		int numberOfPorts = this.getSharedResources().getGpioController()
				.getNumGPOPorts();
		List<Integer> retVal = new ArrayList<Integer>();
		int x = 1;
		for (int i = 0; i < numberOfPorts; i++) {
			retVal.add(x);
			x = x * 2;
		}
		return retVal;
	}

}
