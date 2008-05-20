/*
 *  @(#)AbstractSuspendedCommunicationPowerState.java
 *
 *  Created:	Sep 28, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.abstract_;

import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.reader.module.abstract_.AbstractSuspendedPowerState;

/**
 * Implements functions of the SUSPENDED power state for an
 * AbstractCommunication.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractSuspendedCommunicationPowerState extends
		AbstractSuspendedPowerState implements CommunicationPowerState {

	/**
	 * Allow the recieve to go through. The transition to suspend should force
	 * the underlying methods to suspend until resumed or interrupted.
	 * 
	 * @throws CommunicationException
	 *             If the receive is interrupted.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationPowerState#receiveBytes(org.rifidi.emulator.io.comm.Communication)
	 */
	public byte[] receiveBytes(Communication callingComm)
			throws CommunicationException {
		/* Allow the transaction to proceed on to the connection state */
		return ((AbstractCommunication) callingComm).getCurConnectionState()
				.receiveBytes(callingComm);

	}

	/**
	 * Allow the recieve to go through. The transition to suspend should force
	 * the underlying methods to suspend until resumed or interrupted.
	 * 
	 * @throws CommunicationException
	 *             If the send is interrupted.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationPowerState#sendBytes(byte[],
	 *      org.rifidi.emulator.io.comm.Communication)
	 */
	public void sendBytes(byte[] data, Communication callingComm)
			throws CommunicationException {
		/* Call the power state's corresponding method */
		((AbstractCommunication) callingComm).getCurConnectionState()
				.sendBytes(data, callingComm);

	}

}
