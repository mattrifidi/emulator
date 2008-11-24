/*
 *  @(#)AutonomousOnCommandControllerPowerState.java
 *
 *  Created:	Oct 17, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.autonomous;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractOnCommandControllerPowerState;

/**
 * Implements functions of the ON power state for an
 * AutonomousCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AutonomousOnCommandControllerPowerState extends
		AbstractOnCommandControllerPowerState {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AutonomousOnCommandControllerPowerState.class);
	
	/**
	 * The singleton instance for this class.
	 */
	private static final AutonomousOnCommandControllerPowerState SINGLETON_INSTANCE = new AutonomousOnCommandControllerPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static AutonomousOnCommandControllerPowerState getInstance() {
		return AutonomousOnCommandControllerPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private AutonomousOnCommandControllerPowerState() {

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractOnCommandControllerPowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void suspend(PowerControllable pcObject) {
		/* Call the abstract implementation first */
		super.suspend(pcObject);

		/* Cast to an AutonomousCommandController for specific action */
		AutonomousCommandController aCController = (AutonomousCommandController) pcObject;

		/* Change to suspended state */
		aCController
				.changeCommandControllerPowerState(AutonomousSuspendedCommandControllerPowerState
						.getInstance());

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractOnCommandControllerPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		
		logger.debug("turned off by "+ callingClass);
		
		/* Call the abstract implementation first */
		super.turnOff(pcObject, this.getClass());

		/* Cast to an AutonomousCommandController for specific action */
		AutonomousCommandController aCController = (AutonomousCommandController) pcObject;

		/* Change to off state */
		aCController
				.changeCommandControllerPowerState(AutonomousOffCommandControllerPowerState
						.getInstance());

		/* Stop all of the executers */
		aCController.stopAutonomousExecuters();

	}

}
