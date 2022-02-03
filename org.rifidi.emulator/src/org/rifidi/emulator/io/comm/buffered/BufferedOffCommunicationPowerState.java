/*
 *  @(#)BufferedOffCommunicationPowerState.java
 *
 *  Created:	Sep 28, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.buffered;

import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.abstract_.AbstractOffCommunicationPowerState;

/**
 * Implements functions of the OFF power state for a BufferedCommunication.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class BufferedOffCommunicationPowerState extends
		AbstractOffCommunicationPowerState {

	/**
	 * Turns on the reader from a buffer standpoint (clears the buffers). <br>
	 * 
	 * Note that this does NOT switch the state to on, since there is no
	 * concrete implementation known.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOn(PowerControllable pcObject) {
		/* Cast the pcObject to a BufferedCommunication object */
		BufferedCommunication curComm = (BufferedCommunication) pcObject;

		/* Clear the buffers. */
		curComm.getReceiveBuffer().clearBuffer();
		curComm.getSendBuffer().clearBuffer();

		/* Remove any suspentions */
		curComm.getReceiveBuffer().setSuspended(false);
		curComm.getSendBuffer().setSuspended(false);

		/* Remove any interrupts */
		curComm.getReceiveBuffer().setInterrupted(false);
		curComm.getSendBuffer().setInterrupted(false);

	}

}
