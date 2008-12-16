/*
 *  @(#)InteractiveOffCommandControllerPowerState.java
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
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractOffCommandControllerPowerState;

/**
 * Implements functions of the OFF power state for an
 * InteractiveCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class InteractiveOffCommandControllerPowerState extends
		AbstractOffCommandControllerPowerState {

	private static Log logger = LogFactory.getLog(InteractiveOffCommandControllerPowerState.class);
	
	/**
	 * The singleton instance for this class.
	 */
	private static final InteractiveOffCommandControllerPowerState SINGLETON_INSTANCE = new InteractiveOffCommandControllerPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static InteractiveOffCommandControllerPowerState getInstance() {
		return InteractiveOffCommandControllerPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private InteractiveOffCommandControllerPowerState() {

	}

	/**
	 * Kicks off an InteractiveCommandExecuter thread and changes to the on
	 * state.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractOffCommandControllerPowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOn(PowerControllable pcObject) {
		/* Call AbstractOffCommunicationPowerState's turnOn implementation */
		super.turnOn(pcObject);

		/* Cast to an InteractiveCommandController */
		InteractiveCommandController intController = (InteractiveCommandController) pcObject;

		logger.debug("changing to interactive on command controller power state");
		
		/* Change to on power state */
		intController
				.changeCommandControllerPowerState(InteractiveOnCommandControllerPowerState
						.getInstance());

		/* Kick off the executer */
		new Thread(new InteractiveCommandExecuter(intController),
				"Interactive Command Executer Thread").start();

	}

}
