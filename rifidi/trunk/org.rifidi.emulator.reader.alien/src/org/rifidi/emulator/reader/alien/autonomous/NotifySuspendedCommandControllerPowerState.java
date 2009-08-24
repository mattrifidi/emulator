/*
 *  @(#)NotifySuspendedCommandControllerPowerState.java
 *
 *  Created:	Oct 17, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.alien.autonomous;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractOffCommandControllerPowerState;
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
public class NotifySuspendedCommandControllerPowerState extends
		AbstractSuspendedCommandControllerPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(AbstractOffCommandControllerPowerState.class);
	
	/**
	 * The singleton instance for this class.
	 */
	private static final NotifySuspendedCommandControllerPowerState SINGLETON_INSTANCE = new NotifySuspendedCommandControllerPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static NotifySuspendedCommandControllerPowerState getInstance() {
		return NotifySuspendedCommandControllerPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private NotifySuspendedCommandControllerPowerState() {

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractSuspendedCommandControllerPowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void resume(PowerControllable pcObject) {
		logger.debug("Autonomous Controller suspended");
		
		/* Call the abstract implementation first */
		super.resume(pcObject);

		/* Cast to an AutonomousCommandController for specific action */
		NotifyController aCController = (NotifyController) pcObject;

		/* Change to on state */
		aCController
				.changeCommandControllerPowerState(NotifyOnCommandControllerPowerState
						.getInstance());

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractSuspendedCommandControllerPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOff(PowerControllable pcObject) {
		logger.debug("Autonomous mode turned off");
		
		/* Call the abstract implementation first */
		super.turnOff(pcObject, this.getClass());

		/* Cast to an AutonomousCommandController for specific action */
		NotifyController aCController = (NotifyController) pcObject;

		/* Change to off state */
		aCController
				.changeCommandControllerPowerState(NotifyOffCommandControllerPowerState
						.getInstance());
	}

}
