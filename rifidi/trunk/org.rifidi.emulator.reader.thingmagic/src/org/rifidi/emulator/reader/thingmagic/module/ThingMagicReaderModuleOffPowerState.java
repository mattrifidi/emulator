/*
 *  ThingMagicReaderModuleOffPowerState.java
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
import org.rifidi.emulator.common.PowerState;
import org.rifidi.emulator.reader.module.abstract_.AbstractOffPowerState;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class ThingMagicReaderModuleOffPowerState extends AbstractOffPowerState {
	//private static Log logger = LogFactory.getLog(ThingMagicReaderModuleOffPowerState.class);
	
    /* private instance. */
	private static ThingMagicReaderModuleOffPowerState instance = new ThingMagicReaderModuleOffPowerState();
	
	/**
	 * 
	 */
	public ThingMagicReaderModuleOffPowerState() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOn(PowerControllable pcObject) {
		
		/* cast the object to something we could use. */
		ThingMagicReaderModule rm = (ThingMagicReaderModule) pcObject;
		
		/* turn on the tcp connection... */
		//rm.getRQLComm().turnOn();
		//rm.getInteractiveRQLController().turnOn();

		/* more things to do to turn on the tcp connection to the reader??? */
		rm.getSharedResources().getInteractiveRQLPowerSignal().setControlVariableValue(true);
		rm.getSharedResources().getInteractiveRQLConnectionSignal().setControlVariableValue(false);

		/* seems we have to send a new power class to our reader to tell it that it
		 * has been turned on... 
		 */
		rm.changePowerState(ThingMagicReaderModuleOnPowerState.getInstance());
		
		/* now we send a log message to the console that we are turned on. */
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " on");
	}

	public static PowerState getInstance() {
		//logger.debug("ThingMagicReaderModuleOffPowerState.getInstance() was called.");
		return instance;
	}

}
