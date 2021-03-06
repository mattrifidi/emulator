/*
 *  ThingMagicReaderSharedResources.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandDigester;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.emulator.reader.thingmagic.automodecontoler.AutoModeControler;
import org.rifidi.emulator.reader.thingmagic.commandregistry.CursorCommandRegistry;
import org.rifidi.emulator.reader.thingmagic.database.DataBase;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class ThingMagicReaderSharedResources extends
		AbstractReaderSharedResources {
	private static Log logger = LogFactory.getLog(ThingMagicReaderSharedResources.class);

	private ControlSignal<Boolean> interactiveRQLPowerSignal;

	private ControlSignal<Boolean> interactiveRQLConnectionSignal;

	public DataBase dataBase;

	private CursorCommandRegistry cursorCommandRegistry;

	private AutoModeControler autoModeControler;
	
	public AutoModeControler getAutoModeControler() {
		return autoModeControler;
	}



	public CursorCommandRegistry getCursorCommandRegistry() {
		return cursorCommandRegistry;
	}



	public void setCursorCommandRegistry(CursorCommandRegistry cursorCommandRegistry) {
		this.cursorCommandRegistry = cursorCommandRegistry;
	}



	public ThingMagicReaderSharedResources(GenericRadio radio,
			TagMemory tagMemory, ControlSignal<Boolean> readerPowerSignal,
			String readerName, GenericExceptionHandler exc,
			CommandDigester dig, int numAntennas, DataBase base,
			CursorCommandRegistry cursorCommandRegistry, ControlSignal<Boolean> interactiveRQLPowerSignal,
			ControlSignal<Boolean> interactiveRQLConnectionSignal) {
		super(radio, tagMemory, readerPowerSignal, readerName, exc, dig, numAntennas);
		
		this.cursorCommandRegistry = cursorCommandRegistry;
		
		this.dataBase = base;
		this.interactiveRQLPowerSignal = interactiveRQLPowerSignal;
		this.interactiveRQLConnectionSignal = interactiveRQLConnectionSignal;
		
		logger.debug("ThingMagicReaderSharedResources() was called.");
		
	}



	public void setDataBase(DataBase base){
		dataBase = base;
	}

	public DataBase getDataBase(){
		return dataBase;
	}
	
	/**
	 * @return the interactiveBytePowerSignal
	 */
	public ControlSignal<Boolean> getInteractiveRQLPowerSignal() {
		return interactiveRQLPowerSignal;
	}

	/**
	 * @return the interactiveHttpConnectionSignal
	 */
	public ControlSignal<Boolean> getInteractiveRQLConnectionSignal() {
		return interactiveRQLConnectionSignal;
	}



	public void setAutoModeControler(AutoModeControler autoModeControler) {
		// TODO Auto-generated method stub
		this.autoModeControler = autoModeControler;
	}
	
	
}
