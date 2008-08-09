/**
 * 
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
import org.rifidi.emulator.reader.thingmagic.database.DataBase;

/**
 * @author jmaine
 *
 */
public class ThingMagicReaderSharedResources extends
		AbstractReaderSharedResources {
	private static Log logger = LogFactory.getLog(ThingMagicReaderSharedResources.class);

	private ControlSignal<Boolean> interactiveRQLPowerSignal;

	private ControlSignal<Boolean> interactiveRQLConnectionSignal;

	public DataBase dataBase;
	
	public ThingMagicReaderSharedResources(GenericRadio radio,
			TagMemory tagMemory, ControlSignal<Boolean> readerPowerSignal,
			String readerName, GenericExceptionHandler exc,
			CommandDigester dig, int numAntennas, DataBase base,
			ControlSignal<Boolean> interactiveRQLPowerSignal,
			ControlSignal<Boolean> interactiveRQLConnectionSignal) {
		super(radio, tagMemory, readerPowerSignal, readerName, exc, dig, numAntennas);
		
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
	
	
}
