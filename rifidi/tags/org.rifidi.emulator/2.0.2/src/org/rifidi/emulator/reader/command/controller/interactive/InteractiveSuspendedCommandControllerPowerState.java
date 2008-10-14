/*
 *  @(#)InteractiveSuspendedCommandControllerPowerState.java
 *
 *  Created:	Oct 25, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.interactive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractSuspendedCommandControllerPowerState;

/**
 * Implements functions of the SUSPENDED power state for an
 * InteractiveCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class InteractiveSuspendedCommandControllerPowerState extends
		AbstractSuspendedCommandControllerPowerState {
	

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(InteractiveSuspendedCommandControllerPowerState.class);
	
	/**
	 * The singleton instance for this class.
	 */
	private static final InteractiveSuspendedCommandControllerPowerState SINGLETON_INSTANCE = new InteractiveSuspendedCommandControllerPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static InteractiveSuspendedCommandControllerPowerState getInstance() {
		return InteractiveSuspendedCommandControllerPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private InteractiveSuspendedCommandControllerPowerState() {

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractSuspendedCommandControllerPowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void resume(PowerControllable pcObject) {
		/* Call the super resume implementation */
		super.resume(pcObject);

		/* Cast to an InteractiveCommandController */
		InteractiveCommandController intController = (InteractiveCommandController) pcObject;

		/* Change to on power state */
		intController
				.changeCommandControllerPowerState(InteractiveOnCommandControllerPowerState
						.getInstance());

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractSuspendedCommandControllerPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		
		logger.debug("turned off by "+ callingClass);
		
		/* Call the super turnOff implementation */
		super.turnOff(pcObject, this.getClass());

		/* Cast to an InteractiveCommandController */
		InteractiveCommandController intController = (InteractiveCommandController) pcObject;

		/* Change to off power state */
		intController
				.changeCommandControllerPowerState(InteractiveOffCommandControllerPowerState
						.getInstance());

	}

}
