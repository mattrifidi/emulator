/*
 *  @(#)AbstractSuspendedCommandControllerPowerState.java
 *
 *  Created:	Oct 12, 2006
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
import org.rifidi.emulator.reader.command.controller.CommandController;
import org.rifidi.emulator.reader.command.controller.CommandControllerException;
import org.rifidi.emulator.reader.command.controller.CommandControllerPowerState;
import org.rifidi.emulator.reader.module.abstract_.AbstractSuspendedPowerState;

/**
 * Implements functions of the SUSPENDED power state for an
 * GenericCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractSuspendedCommandControllerPowerState extends
		AbstractSuspendedPowerState implements CommandControllerPowerState {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AbstractSuspendedCommandControllerPowerState.class);

	
	/**
	 * Waits until resumed to process the command. If the turnOff method is
	 * invoked while this is waiting, this method will return null.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.CommandControllerPowerState#processCommand(byte[],
	 *      org.rifidi.emulator.reader.command.controller.CommandController)
	 */
	public ArrayList<Object> processCommand(byte[] command,
			CommandController controller) throws CommandControllerException {
		ArrayList<Object> retList = null;

		AbstractCommandController abstractController = (AbstractCommandController) controller;

		/* Synchronize on the passed processor's suspension lock */
		Object lock = abstractController.getSuspensionLock();
		synchronized (lock) {
			/* Keep waiting while this is suspended but not interrupted */
			while (abstractController.isSuspended()
					&& !abstractController.isInterrupted()) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					/* Do nothing */
				}

			}

			/* Check to see why this dropped out of the loop */
			if (!abstractController.isInterrupted()) {
				/* Resumed -- allow processCommand to continue */
				abstractController.getCurOperatingState().processCommand(
						command, controller);

			} else {
				throw new CommandControllerException(
						"Command processing interrupted while suspended.");

			}

		}

		/* Return the command response */
		return retList;

	}

	/**
	 * Resumes the command controller, updating suspended/interruption variables
	 * as necessary. <br>
	 * 
	 * Note that this does NOT switch the power state, since there is no
	 * concrete implementation known.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	public void resume(PowerControllable pcObject) {
		/* Cast to an AbstractCommandController */
		AbstractCommandController abstractController = (AbstractCommandController) pcObject;

		/* Synchronize on the passed processor's suspension lock */
		Object lock = abstractController.getSuspensionLock();
		synchronized (lock) {
			/* Clear suspension status */
			abstractController.setSuspended(false);

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
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		
		logger.debug("turned off by "+ callingClass);
		
		/* Cast to an AbstractCommandController */
		AbstractCommandController abstractController = (AbstractCommandController) pcObject;

		/* Synchronize on the passed processor's suspension lock */
		Object lock = abstractController.getSuspensionLock();
		synchronized (lock) {
			/* Clear suspension status */
			abstractController.setSuspended(false);

			/* Turn on interruption status */
			abstractController.setInterrupted(true);

			/* Notify anything waiting */
			lock.notifyAll();

		}

	}

}
