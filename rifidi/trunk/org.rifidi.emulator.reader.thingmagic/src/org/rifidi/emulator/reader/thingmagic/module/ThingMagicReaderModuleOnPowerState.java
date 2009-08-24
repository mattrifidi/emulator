/*
 *  ThingMagicReaderModuleOnPowerState.java
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

import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.module.abstract_.AbstractOnPowerState;



/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class ThingMagicReaderModuleOnPowerState extends AbstractOnPowerState {
	//private static Log logger = LogFactory.getLog(ThingMagicReaderModuleOnPowerState.class);
	
	
	private static ThingMagicReaderModuleOnPowerState instance = new ThingMagicReaderModuleOnPowerState();
	
	/**
	 * Get the instance that represents this reader being turned on. 
	 * @return The single instance of this class.
	 */
	public static ThingMagicReaderModuleOnPowerState getInstance(){
		return instance;
	}

	/**
	 * 
	 */
	public ThingMagicReaderModuleOnPowerState() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void suspend(PowerControllable pcObject) {
		// TODO What do we do with the Automode controller when we suspend?
		
		/* cast pcObject to something we can use */
		ThingMagicReaderModule rm = (ThingMagicReaderModule) pcObject;
		
		/* suspend the tcp connection */
		rm.getRQLComm().suspend();
		rm.getInteractiveRQLController().suspend();
		
		/* suspend the tag memory */
		ThingMagicReaderSharedResources tmsr = rm.getSharedResources();
		tmsr.getTagMemory().suspend();

		/* turn the reader state to suspened */
		rm.changePowerState(ThingMagicReaderSuspendedPowerState
						.getInstance());
		
		/* send to the console that we have been suspended */
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " suspended");
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable, java.lang.Class)
	 */
	@Override
	public void turnOff(PowerControllable pcObject) {
		//logger.debug("ThingMagicReaderModuleOnPowerState.turnOff() was called.");
		
		/* cast the pcObject to something we can use */
		ThingMagicReaderModule rm = (ThingMagicReaderModule) pcObject;
		
		rm.getSharedResources().getAutoModeControler().stop();
		
		/* clear cursor registry... as it is volatile in the real reader. */ 
		rm.getSharedResources().getCursorCommandRegistry().clear();
		
		/* turn off tcp connection */
		//rm.getInteractiveRQLController().turnOff(this.getClass());
		
		/* more things to do to turn off the tcp connection */
		rm.getSharedResources().getInteractiveRQLPowerSignal()
		.setControlVariableValue(false);
		rm.getSharedResources().getInteractiveRQLConnectionSignal()
		.setControlVariableValue(false);

		/* set the reader to the off position */
		rm.changePowerState(ThingMagicReaderModuleOffPowerState.getInstance());
		
		/* send to the console that this reader is not turned off. */
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");

	}

}
