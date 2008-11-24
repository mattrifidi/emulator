/*
 *  @(#)AlienReaderModuleOffPowerState.java
 *
 *  Created:	Oct 27, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.alien.module;

import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.module.abstract_.AbstractOffPowerState;

/**
 * The off state for an Alien reader.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AlienReaderModuleOffPowerState extends AbstractOffPowerState {

	/**
	 * The singleton instance for this class.
	 */
	private static final AlienReaderModuleOffPowerState SINGLETON_INSTANCE = new AlienReaderModuleOffPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static AlienReaderModuleOffPowerState getInstance() {
		return AlienReaderModuleOffPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private AlienReaderModuleOffPowerState() {

	}

	/**
	 * Turns on all Alien Reader components which are initially switched on by
	 * default.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOn(PowerControllable pcObject) {
		/* Cast to an AlienReaderModule object. */
		AlienReaderModule alienModule = (AlienReaderModule) pcObject;
		
		alienModule.getSharedResources().getHearbeatController().startHeartbeat();

		//If the autonomous mode is "on", we have to turn on the automode again
		if(alienModule.getSharedResources().getPropertyMap().get("automode").getPropertyStringValue().equalsIgnoreCase("on")) {
			alienModule.getSharedResources().getAutoStateController().startAutoMode();
		}
		
		/* Set interactive and heartbeat power signals to appropriate values. */

		alienModule.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(true);
		
		alienModule.getSharedResources().getInteractiveConnectionSignal()
		.setControlVariableValue(false);


		/* Reset all properties */
		//alienModule.getSharedResources().resetAllSharedProperties();

		/* Switch to the on state */
		alienModule.changePowerState(AlienReaderModuleOnPowerState
				.getInstance());
		
		String readername = alienModule.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " on");

	}

}
