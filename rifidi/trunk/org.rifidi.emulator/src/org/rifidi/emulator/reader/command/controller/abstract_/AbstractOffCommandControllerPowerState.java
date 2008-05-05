/*
 *  @(#)AbstractOffCommandControllerPowerState.java
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

import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.command.controller.CommandController;
import org.rifidi.emulator.reader.command.controller.CommandControllerException;
import org.rifidi.emulator.reader.command.controller.CommandControllerPowerState;
import org.rifidi.emulator.reader.module.abstract_.AbstractOffPowerState;

/**
 * Implements functions of the OFF power state for an GenericCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractOffCommandControllerPowerState extends
		AbstractOffPowerState implements CommandControllerPowerState {

	/**
	 * Deny the processing of the command, as this is off.
	 * 
	 * @throws CommandControllerException
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.CommandControllerPowerState#processCommand(byte[],
	 *      org.rifidi.emulator.reader.command.controller.CommandController)
	 */
	public ArrayList<Object> processCommand(byte[] command, CommandController controller)
			throws CommandControllerException {
		/* Deny command processing */
		throw new CommandControllerException(
				"Cannot process - controller is off.");

	}

	/**
	 * Turns on the command controller, updating suspended/interruption
	 * variables as necessary. <br>
	 * 
	 * Note that this does NOT switch the power state, since there is no
	 * concrete implementation known.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOn(PowerControllable pcObject) {
		/* Cast to an AbstractCommandController */
		AbstractCommandController abstractController = (AbstractCommandController) pcObject;

		/* Synchronize on the passed processor's suspension lock */
		Object lock = abstractController.getSuspensionLock();
		synchronized (lock) {
			/* Clear interruption status */
			abstractController.setInterrupted(false);

			/* Notify anything waiting */
			lock.notifyAll();

		}

		/* Set to the original operating state */
		abstractController
				.changeCommandControllerOperatingState(abstractController
						.getInitialOperatingState());

	}

}
