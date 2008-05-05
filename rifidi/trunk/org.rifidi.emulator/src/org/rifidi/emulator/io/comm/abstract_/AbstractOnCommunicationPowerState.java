/*
 *  @(#)AbstractOnCommunicationPowerState.java
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
import org.rifidi.emulator.reader.module.abstract_.AbstractOnPowerState;

/**
 * Implements functions of the ON power state for an
 * AbstractOnCommunicationPowerState.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractOnCommunicationPowerState extends
		AbstractOnPowerState implements CommunicationPowerState {

	/**
	 * Allows bytes to be received, passing through to the current connection
	 * state.
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
	 * Allows bytes to be sent, passing through to the current connection state.
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
