/*
 *  @(#)InteractiveOnCommandControllerPowerState.java
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
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractOnCommandControllerPowerState;

/**
 * Implements functions of the ON power state for an
 * InteractiveCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class InteractiveOnCommandControllerPowerState extends
		AbstractOnCommandControllerPowerState {

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(InteractiveOnCommandControllerPowerState.class);
	
	/**
	 * The singleton instance for this class.
	 */
	private static final InteractiveOnCommandControllerPowerState SINGLETON_INSTANCE = new InteractiveOnCommandControllerPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static InteractiveOnCommandControllerPowerState getInstance() {
		return InteractiveOnCommandControllerPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private InteractiveOnCommandControllerPowerState() {

	}

	/**
	 * Suspend the reader.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractOnCommandControllerPowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void suspend(PowerControllable pcObject) {
		/* Call the super suspend implementation */
		super.suspend(pcObject);

		/* Cast to an InteractiveCommandController */
		InteractiveCommandController intController = (InteractiveCommandController) pcObject;

		/* Change to suspended power state */
		intController
				.changeCommandControllerPowerState(InteractiveSuspendedCommandControllerPowerState
						.getInstance());

	}

	/**
	 * Turn off the reader.  
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractOnCommandControllerPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		
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
