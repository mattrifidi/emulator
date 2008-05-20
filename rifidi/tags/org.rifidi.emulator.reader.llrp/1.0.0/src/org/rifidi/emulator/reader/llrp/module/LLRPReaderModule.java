/*
 *  LLRPReaderModule.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.module;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.PowerState;
import org.rifidi.emulator.io.comm.ip.IPCommunication;
import org.rifidi.emulator.io.comm.ip.tcpclient.TCPClientCommunication;
import org.rifidi.emulator.io.comm.ip.tcpserver.TCPServerCommunication;
import org.rifidi.emulator.io.comm.manager.TagManager;
import org.rifidi.emulator.io.comm.streamreader.GenericByteStreamReader;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.controller.interactive.InteractiveCommandController;
import org.rifidi.emulator.reader.command.controller.interactive.LoginAuthenticatedCommandControllerOperatingState;
import org.rifidi.emulator.reader.command.xml.CommandXMLDigester;
import org.rifidi.emulator.reader.control.adapter.ReflectiveCommandAdapter;
import org.rifidi.emulator.reader.control.adapter.searcher.RawCommandSearcher;
import org.rifidi.emulator.reader.llrp.admin.LLRPReaderModuleAdministration;
import org.rifidi.emulator.reader.llrp.admin.LLRPReaderModuleConnectionType;
import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolEnums;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2LLRPCapabilities;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._UHFC1G2RFModeTable;
import org.rifidi.emulator.reader.llrp.command.exception.LLRPExceptionHandler;
import org.rifidi.emulator.reader.llrp.formatter.LLRPCommandFormatter;
import org.rifidi.emulator.reader.llrp.formatter.LLRPLogFormatter;
import org.rifidi.emulator.reader.llrp.io.protocol.LLRPProtocol;
import org.rifidi.emulator.reader.llrp.properties.AntennaSettings;
import org.rifidi.emulator.reader.llrp.properties.GPISettings;
import org.rifidi.emulator.reader.llrp.properties.GPOSettings;
import org.rifidi.emulator.reader.llrp.properties.Properties;
import org.rifidi.emulator.reader.llrp.report.LLRPReportControllerFactory;
import org.rifidi.emulator.reader.llrp.report.ROReportFormat;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecControllerFactory;
import org.rifidi.emulator.reader.llrp.tagbuffer.LLRPTagMemory;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule;
import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;

/**
 * This is the main Module for the LLRP Reader. It's emulating a complete Reader
 * defined in the EPC Global LLRP ReaderProtocol Specification
 * 
 * @author Matt Dean - matt@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 */
public class LLRPReaderModule extends AbstractPowerModule implements
		ReaderModule {

	public static final String startupText = "<STARTUP>";

	public static final String READERTYPE = "LLRPReader";

	public static final String XMLLOCATION = "org/rifidi/emulator/reader/llrp/module/";

	private TagManager tagManager;

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(LLRPReaderModule.class);

	/**
	 * Console logger for this class
	 */
	public Log consoleLogger = null;

	/**
	 * The name of the reader.
	 */
	private String name;

	/**
	 * The digester.
	 */
	private CommandXMLDigester digester;

	/**
	 * The communication which the interactive controller uses.
	 */
	private IPCommunication interactiveCommunication;

	/**
	 * The shared resources for LLRP
	 */
	private LLRPReaderSharedResources llrpSR;

	/**
	 * The interactive command controller for this. This allows the reader to
	 * accept and process commands interactively.
	 */
	private InteractiveCommandController interactiveCommandController;

	/**
	 * Command adapter to use for LLRP
	 */
	private ReflectiveCommandAdapter interactiveCommandAdapter;

	/**
	 * ConnectionEventObserver sends out an event notification upon connection
	 */
	// This variable exists so that the garbage collector doesn't remove the
	// object
	@SuppressWarnings("unused")
	private ConnectionEventObserver connectionEventObserver;

	private LLRPReaderModuleAdministration adminInterface;
	public LLRPReaderModuleConnectionType connectionType;

	/**
	 * Empty constructor ONLY to be used by OSGi
	 * 
	 */
	public LLRPReaderModule() {
	}

	public LLRPReaderModule(ControlSignal<Boolean> powerControlSignal,
			GeneralReaderPropertyHolder properties) {

		super(LLRPReaderModuleOffPowerState.getInstance(), powerControlSignal);

		consoleLogger = LogFactory.getLog("console."
				+ properties.getReaderName());

		consoleLogger.info(LLRPReaderModule.startupText
				+ "Instantiated LLRPReader with name: "
				+ properties.getReaderName());

		consoleLogger.info(LLRPReaderModule.startupText
				+ properties.getReaderName() + " has "
				+ properties.getNumAntennas() + " antennas");
		this.name = properties.getReaderName();

		consoleLogger.info(LLRPReaderModule.startupText
				+ properties.getReaderName() + " has "
				+ properties.getNumGPIs() + " GPI Ports" + " and "
				+ properties.getNumGPOs() + " GPO Ports");

		digester = new CommandXMLDigester();
		digester.parseToCommand(this.getClass().getClassLoader()
				.getResourceAsStream(XMLLOCATION + "reader.xml"));

		HashMap<Integer, Antenna> antennaList = new HashMap<Integer, Antenna>();
		for (int i = 0; i < properties.getNumAntennas(); i++) {
			antennaList.put(i, new Antenna(i, name));
		}

		LLRPTagMemory tagMem = new LLRPTagMemory();

		/* Make a Radio for the reader */
		GenericRadio genericRadio = new GenericRadio(antennaList, 25, name);

		this.llrpSR = new LLRPReaderSharedResources(genericRadio, tagMem,
				powerControlSignal, properties.getReaderName(),
				new LLRPExceptionHandler(), this.digester,
				new ControlSignal<Boolean>(false), new ControlSignal<Boolean>(
						false), antennaList.size());

		int numGPOs = properties.getNumGPOs();
		int numGPIs = properties.getNumGPIs();

		for (int i = 0; i < numGPIs; i++) {
			llrpSR.getGpioController().addGPIPort(i);
		}
		for (int i = 0; i < numGPOs; i++) {
			llrpSR.getGpioController().addGPOPort(i);
		}

		setDefaultProperties(llrpSR);

		String inet = ((String) properties.getProperty("inet_address"))
				.split(":")[0];
		int port = Integer.parseInt(((String) properties
				.getProperty("inet_address")).split(":")[1]);

		adminInterface = new LLRPReaderModuleAdministration(this, inet, port);

		// Observer for Connection
		connectionEventObserver = new ConnectionEventObserver(llrpSR
				.getInteractiveConnectionSignal(), name);

		// Create a ReportController for sending Reports
		LLRPReportControllerFactory.getInstance().createController(name);

		ROSpecControllerFactory.getInstance().createController(name);

		this.interactiveCommandAdapter = new ReflectiveCommandAdapter(
				"Interactive", new LLRPCommandFormatter(), llrpSR
						.getExceptionHandler(), this.llrpSR,
				new RawCommandSearcher());

		this.interactiveCommandController = new InteractiveCommandController(
				new LoginAuthenticatedCommandControllerOperatingState(
						this.interactiveCommandAdapter), llrpSR
						.getInteractivePowerSignal(), llrpSR
						.getInteractiveConnectionSignal(), null);
		// Remember to set the Communication at the moment it's
		// available [null = this.interactiveCommunication]

		boolean enableServerMode = new Boolean((String) properties
				.getProperty("servermode"));
		if (enableServerMode) {
			logger.debug("ServerMode enabled by wizard. "
					+ "=> Startup in ServerMode");
			String llrpInet = ((String) properties.getProperty("inet_address"))
					.split(":")[0];
			int llrpPort = Integer.parseInt(((String) properties
					.getProperty("llrp_inet_address")).split(":")[1]);
			InetAddress inetAddr = null;
			try {
				inetAddr = Inet4Address.getByName(llrpInet);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			LLRPReaderModuleConnectionType connectionType = new LLRPReaderModuleConnectionType(
					true, inetAddr, llrpPort);
			adminInterface.preEnabledServerMode(connectionType);
		}
	}

	public boolean setConnection(LLRPReaderModuleConnectionType connectionType) {
		if (interactiveCommunication == null) {
			initializeConnection(connectionType);
			if (connectionType.isServer) {
				// Turn on Reader
				this.getSharedResources().getInteractivePowerSignal()
						.setControlVariableValue(true);

				this.getSharedResources().getInteractiveConnectionSignal()
						.setControlVariableValue(false);

			} else {
				// Turn on Reader
				this.getSharedResources().getInteractivePowerSignal()
						.setControlVariableValue(true);
				// Turn on Communication
				this.getInteractiveCommunication().turnOn(connectionType);
				this.getInteractiveCommandController().turnOn(connectionType);

			}
		} else {

/*			this.getSharedResources().getInteractiveConnectionSignal()
					.setControlVariableValue(false);*/

			initializeConnection(connectionType);

			if (connectionType.isServer) {
				this.getSharedResources().getInteractivePowerSignal()
						.setControlVariableValue(true);
				this.getSharedResources().getInteractiveConnectionSignal()
						.setControlVariableValue(false);

			} else {
				this.getInteractiveCommunication().turnOn(connectionType);
				this.getInteractiveCommandController().turnOn(connectionType);

			}
		}
		// in client mode check if connection attempt was successfull
		if (!connectionType.isServer) {
			return getSharedResources().getInteractiveConnectionSignal()
					.getControlVariableValue();
		}
		return true;
	}

	private void initializeConnection(
			LLRPReaderModuleConnectionType connectionType) {

		String inet = connectionType.getAddress().getCanonicalHostName();
		int port = connectionType.getPort();

		if (connectionType.isServer) {
			consoleLogger.info("++ ServerMode initialized ++");
			// The server connection is build
			this.interactiveCommunication = new TCPServerCommunication(
					new LLRPProtocol(), llrpSR.getInteractivePowerSignal(),
					llrpSR.getInteractiveConnectionSignal(), inet, port,
					this.name, GenericByteStreamReader.class,
					new LLRPLogFormatter());
			// give the Connection to the Controllers
			publishConnection();
		} else {
			consoleLogger.info("++ ClientMode initialized ++");
			this.interactiveCommunication = new TCPClientCommunication(
					new LLRPProtocol(), llrpSR.getInteractivePowerSignal(),
					llrpSR.getInteractiveConnectionSignal(), inet, port,
					this.name, GenericByteStreamReader.class,
					new LLRPLogFormatter());
			// give the Connection to the Controllers
			publishConnection();
		}
		consoleLogger.info(LLRPReaderModule.startupText + name
				+ " ConnectionParameter " + inet + ":" + port);
	}

	private void publishConnection() {
		// Give the Communication to the depending parts (CommandController /
		// LLRPReportController)
		this.interactiveCommandController
				.setCurCommunication(this.interactiveCommunication);

		LLRPReportControllerFactory.getInstance().getReportController(name)
				.setComm(this.interactiveCommunication);
	}

	@SuppressWarnings("unused")
	private void turnOffSignals() {
		this.getInteractiveCommandController().turnOff(this.getClass());
		this.getInteractiveCommunication().turnOff(this.getClass());

		this.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(false);
		// Not sure if we should do this in ServerMode?
		this.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);

		this.changePowerState(LLRPReaderModuleOffPowerState.getInstance());
	}

	public LLRPReaderModuleAdministration getAdminInterface() {
		return adminInterface;
	}

	// public void createAdminInterface()
	// {
	// adminInterface = new LLRPReaderModuleAdministrationInterface(this);
	// }
	/**
	 * This method is a hack that sets default properties. It will need to be
	 * handled in the xml in the future.
	 * 
	 * @param properties
	 */
	private static void setDefaultProperties(LLRPReaderSharedResources llrpsr) {
		Properties properties = llrpsr.getProperties();

		properties.accessReportTrigger = 0;

		GenericRadio genericRadio = llrpsr.getRadio();
		GPIOController gpioController = llrpsr.getGpioController();

		ArrayList<AirProtocolEnums> protosSupported = new ArrayList<AirProtocolEnums>();
		protosSupported.add(AirProtocolEnums.C1G2);
		logger
				.debug("Number of antennas: "
						+ genericRadio.getAntennas().size());
		int numantennas = genericRadio.getAntennas().size();
		for (int i = 0; i < numantennas; i++) {
			AntennaSettings as = new AntennaSettings(0, 0, protosSupported);
			as.setAntennaConnected(true);
			properties.antennaList.addAntenna(as);
		}

		_C1G2LLRPCapabilities apc = new _C1G2LLRPCapabilities();
		apc.canSupportBlockErase = false;
		apc.canSupportBlockWrite = false;
		apc.maxNumSelectFiltersPerQuery = 0;
		properties.addAirProtocolLLRPCapability(apc);

		properties.canDoRFSurvey = false;
		properties.canDoTagInventoryStateAwareSingulation = false;
		properties.canReportBufferFilledWarnings = false;
		properties.canSetAntennaProps = false;
		properties.clientOpSpecTimeout = 0;
		properties.communicationStandard = 0;
		properties.countryCode = 0;
		properties.deviceManufacturername = 0;

		for (int i = 0; i < 8; i++) {
			properties.eventNotificaionTable.setEventNotificaiton(i, false);
		}

		properties.firmwareVersion = "LLRP_Reader1.4";
		properties.fixedFrequencyTable.addTableEntry(0);
		properties.hasUTCClockCapabilities = true;
		properties.holdEventsAndReportsUponReconnected = false;
		properties.hopping = false;
		properties.IDType = 0;
		properties.keepAliveTriggerType = 0;
		_UHFC1G2RFModeTable entry = new _UHFC1G2RFModeTable();
		entry.addTableEntry(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
		properties.listOfUHFTable.add(entry);
		properties.LLRPConfiguraitonStateVariable = 0;
		properties.maxNumAccessSpecs = 0;
		properties.maxNumInventoryParameterSpecsPerAISpec = 0;
		properties.maxNumOpSpecsPerAccessSpec = 0;
		properties.maxNumROSpecs = 1;
		properties.maxNumSpecsPerROSpec = 1;
		properties.maxPriorityLevelSupported = 0;
		properties.modelName = 0;

		for (int i = 0; i < gpioController.getNumGPIPorts(); i++) {
			properties.addGPISetting(new GPISettings(i + 1, true, llrpsr));
			properties.getGPISettingAt(i + 1).setGPIConfig(false);
		}

		for (int i = 0; i < gpioController.getNumGPOPorts(); i++) {
			properties.addGPOSetting(new GPOSettings(i + 1, gpioController));
		}

		byte[] temp = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00 };
		properties.readerID = temp;
		properties.receiveSensitiviyTable.addTableEntry(0);
		properties.roReportFormat_Global = new ROReportFormat();
		properties.supportsEventAndReportHolding = false;
		properties.supportsClientRequestOpSpec = false;
		properties.transmitPowerTable.addTableEntry(0);
	}

	/**
	 * A public static to allow a handler to reset the properties to the factory
	 * settings. Will need to use the xml settings in the future.
	 * 
	 * @param props
	 */
	public static void resetToFactorySetting(LLRPReaderSharedResources llrpsr) {
		setDefaultProperties(llrpsr);
	}

	/**
	 * Finalize the reader
	 */
	public void finalize() {
		tagManager.stop();
	}

	public Collection<String> getAllCategories() {
		return digester.getAllCategories();
	}

	public HashMap<String, CommandObject> getCommandList() {
		return digester.getAllCommands();
	}

	public Collection<CommandObject> getCommandsByCategory(String category) {
		return digester.getCommandsByCategory(category);
	}

	public String getName() {
		return name;

	}

	public LLRPReaderSharedResources getSharedResources() {
		return llrpSR;
	}

	/**
	 * Returns the interactiveCommunication.
	 * 
	 * @return Returns the interactiveCommunication.
	 */
	// Former TCPServerCommunication changed to IPCommunication
	protected final IPCommunication getInteractiveCommunication() {
		return this.interactiveCommunication;
	}

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
	 * @return the interactiveCommandController
	 */
	public InteractiveCommandController getInteractiveCommandController() {
		return interactiveCommandController;
	}

	/**
	 * @return the interactiveCommandAdapter
	 */
	public ReflectiveCommandAdapter getInteractiveCommandAdapter() {
		return interactiveCommandAdapter;
	}

}
