/*
 *  @(#)AbstractOnCommandControllerPowerState.java
 *
 *  Created:	Oct 11, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.abstract_;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.abstract_.AbstractOnCommunicationPowerState;
import org.rifidi.emulator.reader.command.controller.CommandController;
import org.rifidi.emulator.reader.command.controller.CommandControllerPowerState;

/**
 * Implements functions of the ON power state for an GenericCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractOnCommandControllerPowerState extends
		AbstractOnCommunicationPowerState implements
		CommandControllerPowerState {

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(AbstractOnCommandControllerPowerState.class);

	
	/**
	 * @see org.rifidi.emulator.reader.command.controller.CommandControllerPowerState#processCommand(byte[],
	 *      org.rifidi.emulator.reader.command.controller.CommandController)
	 */
	public ArrayList<Object> processCommand(byte[] command, CommandController controller) {
		/* Cast to an abstract command controller */
		AbstractCommandController abstractController = (AbstractCommandController) controller;

		/* Grab the operating state and call it's process method. */
		return abstractController.getCurOperatingState().processCommand(
				command, controller);

	}

	/**
	 * Suspends the command controller, updating suspended/interruption
	 * variables as necessary. <br>
	 * 
	 * Note that this does NOT switch the power state, since there is no
	 * concrete implementation known.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	public void suspend(PowerControllable pcObject) {
		/* Cast to an AbstractCommandController */
		AbstractCommandController abstractController = (AbstractCommandController) pcObject;

		/* Synchronize on the passed processor's suspension lock */
		Object lock = abstractController.getSuspensionLock();
		synchronized (lock) {
			/* Clear suspension status */
			abstractController.setSuspended(true);

			/* Notify anything waiting */
			lock.notifyAll();

		}

	}

	/**
	 * Turns off the command controller, updating suspended/interruption
	 * variables as necessary. <br>
	 * 
	 * Note that this does NOT switch the power state, since there is no
	 * concrete implementation known.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		
		/* Cast to an AbstractCommandController */
		AbstractCommandController abstractController = (AbstractCommandController) pcObject;

		/* Synchronize on the passed processor's suspension lock */
		Object lock = abstractController.getSuspensionLock();
		synchronized (lock) {
			/* Turn on interruption status */
			abstractController.setInterrupted(true);

			/* Notify anything waiting */
			lock.notifyAll();

		}

	}

}
