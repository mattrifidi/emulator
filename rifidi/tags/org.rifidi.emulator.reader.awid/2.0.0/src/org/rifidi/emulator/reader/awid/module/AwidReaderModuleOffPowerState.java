/*
 *  AwidReaderModuleOffPowerState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.module;

import org.rifidi.emulator.common.PowerControllable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.rifidi.emulator.reader.module.abstract_.AbstractOffPowerState;

/**
 * Represents a reader that is turned off.  The reader can be turned
 * on from this state.  
 *
 * @author Matthew Dean
 * @since  <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidReaderModuleOffPowerState extends AbstractOffPowerState {

	/**
	 * The log4j logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger =
		 LogFactory.getLog(AwidReaderModuleOffPowerState.class);
	
	/**
	 * The singleton instance for this class.
	 */
	private static final AwidReaderModuleOffPowerState SINGLETON_INSTANCE 
				= new AwidReaderModuleOffPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static AwidReaderModuleOffPowerState getInstance() {
		return AwidReaderModuleOffPowerState.SINGLETON_INSTANCE;
	}
	
	
	/**
	 * This represents the reader module in the "off" state
	 */
	private AwidReaderModuleOffPowerState() {
	}
	


	/* (non-Javadoc)
	 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOn(PowerControllable pcObject) {
		AwidReaderModule awidModule = (AwidReaderModule) pcObject;

		awidModule.getInteractiveCommunication().turnOn();
		awidModule.getInteractiveCommandController().turnOn();

		awidModule.changePowerState(AwidReaderModuleOnPowerState
				.getInstance());
		
		String readername = awidModule.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " on");
	}

}
