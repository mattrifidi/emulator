/*
 *  @(#)AutonomousSuspendedCommandControllerPowerState.java
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
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractSuspendedCommandControllerPowerState;

/**
 * Implements functions of the SUSPENDED power state for an
 * AutonomousCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AutonomousSuspendedCommandControllerPowerState extends
		AbstractSuspendedCommandControllerPowerState {
	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AutonomousSuspendedCommandControllerPowerState.class);
	
	/**
	 * The singleton instance for this class.
	 */
	private static final AutonomousSuspendedCommandControllerPowerState SINGLETON_INSTANCE = new AutonomousSuspendedCommandControllerPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static AutonomousSuspendedCommandControllerPowerState getInstance() {
		return AutonomousSuspendedCommandControllerPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private AutonomousSuspendedCommandControllerPowerState() {

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractSuspendedCommandControllerPowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void resume(PowerControllable pcObject) {
		/* Call the abstract implementation first */
		super.resume(pcObject);

		/* Cast to an AutonomousCommandController for specific action */
		AutonomousCommandController aCController = (AutonomousCommandController) pcObject;

		/* Change to on state */
		aCController
				.changeCommandControllerPowerState(AutonomousOnCommandControllerPowerState
						.getInstance());

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractSuspendedCommandControllerPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		
		logger.debug("turned off by" + callingClass);
		
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
