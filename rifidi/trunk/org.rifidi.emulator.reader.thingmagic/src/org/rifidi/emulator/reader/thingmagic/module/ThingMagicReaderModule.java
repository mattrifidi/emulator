/*
 *  ThingMagicReaderModule.java
 *
 *  Created:	May 5, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.module;

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
import org.rifidi.emulator.reader.thingmagic.automodecontoler.AutoModeControler;
import org.rifidi.emulator.reader.thingmagic.command.exception.ThingMagicRQLExceptionHandler;
import org.rifidi.emulator.reader.thingmagic.commandregistry.CursorCommandRegistry;
import org.rifidi.emulator.reader.thingmagic.database.DataBase;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.database.impl.DBIO;
import org.rifidi.emulator.reader.thingmagic.database.impl.DBSavedSettings;
import org.rifidi.emulator.reader.thingmagic.database.impl.DBSettings;
import org.rifidi.emulator.reader.thingmagic.database.impl.DBTagData;
import org.rifidi.emulator.reader.thingmagic.database.impl.DBTagID;
import org.rifidi.emulator.reader.thingmagic.formatter.ThingMagicRQLCommandFormatter;
import org.rifidi.emulator.reader.thingmagic.io.protocol.ThingMagicProtocol;


/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
/*
 * This class contains the startup code for the ThingMagic emulator.
 */
public class ThingMagicReaderModule extends AbstractPowerModule implements
		ReaderModule {
	
	/**
	 * The startup text
	 */
	public static final String startupText = "<STARTUP>";

	/**
	 * The type of reader
	 */
	public static final String READERTYPE = "ThingMagic";

	/**
	 * The location of the xml for this reader.  
	 */
	public static final String XMLLOCATION = "org/rifidi/emulator/reader/thingmagic/module/";

	/**
	 * The name of the reader.  
	 */
	private String name;

	/**
	 * The controller that will control the RQL-level commands for the reader.  
	 */
	private InteractiveCommandController interactiveRQLController;

	
	/**
	 * The command adapter for the RQL level commands.  
	 */
	private CommandAdapter interactiveRQLAdapter;

	/**
	 * The shared resources for the reader.  
	 */
	private ThingMagicReaderSharedResources tmsr;

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(ThingMagicReaderModule.class);

	/**
	 * Console logger for this class
	 */
	private Log consoleLogger = null;

	/**
	 * The digester.
	 */
	private CommandXMLDigester digester;

	/**
	 * The Communication for the RQL level.  
	 */
	private TCPServerCommunication RQLComm;
	


	public ThingMagicReaderModule (){
		logger.debug("ThingMagicReaderModule() dummy constructor called.");
	}

	/**
	 * 
	 * 
	 * @param powerControlSignal
	 * @param properties
	 */
	public ThingMagicReaderModule(ControlSignal<Boolean> powerControlSignal,
			GeneralReaderPropertyHolder properties) {
		super(ThingMagicReaderModuleOffPowerState.getInstance(),powerControlSignal);
		consoleLogger = LogFactory.getLog("console."
				+ properties.getReaderName());
		consoleLogger.info(ThingMagicReaderModule.startupText
				+ "Instantiated ThingMagic Reader with name: "
				+ properties.getReaderName());
		consoleLogger.info(ThingMagicReaderModule.startupText
				+ properties.getReaderName() + " IP Address: "
				+ properties.getProperty("rql_address"));
		consoleLogger.info(ThingMagicReaderModule.startupText
				+ properties.getReaderName() + " has "
				+ properties.getNumAntennas() + " antennas");

		this.name = properties.getReaderName();

		digester = new CommandXMLDigester();
		digester.parseToCommand(this.getClass().getClassLoader()
				.getResourceAsStream(XMLLOCATION + "reader.xml"));

		DBTagID tagMemory = new DBTagID();

		//TODO: Start naming antennas from 1 instead of 0 as with the real ThingMagic Reader.
		HashMap<Integer, Antenna> antennaList = new HashMap<Integer, Antenna>();
		for (int i = 0; i < properties.getNumAntennas(); i++) {
			logger.debug("creating an antenna: " + i);
			antennaList.put(i, new Antenna(i, name));
		}

		/* Make a Radio for the reader */
		GenericRadio genericRadio = new GenericRadio(antennaList, 25, name, tagMemory);

		tagMemory.setRadio(genericRadio);
		
		String rql_address = ((String) properties.getProperty("rql_address"))
				.split(":")[0];
		int rql_port = Integer.parseInt(((String) properties
				.getProperty("rql_address")).split(":")[1]);

		this.tmsr = new ThingMagicReaderSharedResources(genericRadio, tagMemory,
				new ControlSignal<Boolean>(false), name, null, digester,
				antennaList.size(),
				new DataBase(),
				new CursorCommandRegistry(),
				new ControlSignal<Boolean>(false),
				new ControlSignal<Boolean>(false));
		
		/* register the database tables */
		tmsr.getDataBase().addTable("tag_id", (IDBTable) tmsr.getTagMemory());
		tmsr.getDataBase().addTable("tag_data", new DBTagData((DBTagID) tmsr.getTagMemory()));
		
		tmsr.getDataBase().addTable("io", new DBIO(tmsr.getGpioController()));
		
		tmsr.getDataBase().addTable("settings", new DBSettings());
		tmsr.getDataBase().addTable("saved_settings", new DBSavedSettings());

		this.RQLComm = new TCPServerCommunication(new ThingMagicProtocol(), this.tmsr
				.getInteractiveRQLPowerSignal(), this.tmsr
				.getInteractiveRQLConnectionSignal(), rql_address, rql_port,
				this.name, GenericCharStreamReader.class, new GenericStringLogFormatter());

		tmsr.setAutoModeControler(new AutoModeControler(this.RQLComm));
		
		this.interactiveRQLAdapter = new ReflectiveCommandAdapter(
				"Interactive", new ThingMagicRQLCommandFormatter(this.tmsr),
				new ThingMagicRQLExceptionHandler(), this.tmsr, new RawCommandSearcher());


		this.interactiveRQLController = new InteractiveCommandController(
				new LoginAuthenticatedCommandControllerOperatingState(
						interactiveRQLAdapter), this.tmsr
						.getInteractiveRQLPowerSignal(), this.tmsr
						.getInteractiveRQLConnectionSignal(), this.RQLComm);
	}
	

	/* (non-Javadoc
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getAllCategories()
	 */
	@Override
	public Collection<String> getAllCategories() {
		logger.debug("ThingMagicReaderModule.getAllCategories() was called.");
		return digester.getAllCategories();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getCommandList()
	 */
	@Override
	public HashMap<String, CommandObject> getCommandList() {
		logger.debug("ThingMagicReaderModule.getCommandList() was called.");
		return digester.getAllCommands();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getCommandsByCategory(java.lang.String)
	 */
	@Override
	public Collection<CommandObject> getCommandsByCategory(String category) {
		logger.debug("ThingMagicReaderModule.getCommandsByCategory() was called.");
		return digester.getCommandsByCategory(category);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getName()
	 */
	@Override
	public String getName() {
		logger.debug("ThingMagicReaderModule.getName() was called.");
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModule#getSharedResources()
	 */
	@Override
	public ThingMagicReaderSharedResources getSharedResources() {
		logger.debug("ThingMagicReaderModule.getSharedResources() was called.");
		return tmsr;
	}
	
	public void finalize() {
	}
	

	/* begin specific ThingMagic getter methods...
	 * Used to access the TCP connection for this reader and turn it on and off.
	 */
	
	/**
	 * @return the interactiveRQLAdapter
	 */
	public CommandAdapter getInteractiveRQLAdapter() {
		return interactiveRQLAdapter;
	}

	/**
	 * @return the interactiveRQLController
	 */
	public InteractiveCommandController getInteractiveRQLController() {
		return interactiveRQLController;
	}
	
	/**
	 * @return the RQLComm
	 */
	public TCPServerCommunication getRQLComm() {
		return RQLComm;
	}
	
	
	/**
	 * Changes the power state for this reader.  
	 */
	protected void changePowerState(PowerState anotherPowerState) {
		super.changePowerState(anotherPowerState);
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
