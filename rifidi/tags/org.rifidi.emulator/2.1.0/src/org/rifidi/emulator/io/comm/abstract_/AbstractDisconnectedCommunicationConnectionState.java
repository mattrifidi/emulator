/*
 *  @(#)AbstractDisconnectedCommunicationConnectionState.java
 *
 *  Created:	Sep 27, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.abstract_;

import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;
import org.rifidi.emulator.io.comm.CommunicationException;

/**
 * An abstract implementation of a disconnected connection state.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractDisconnectedCommunicationConnectionState
		implements CommunicationConnectionState {

	/**
	 * Since this is already a disconnected state, this method effectively does
	 * nothing.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#disconnect(org.rifidi.emulator.io.comm.Communication)
	 */
	public void disconnect(Communication curComm) {
		/* Already disconnected -- do nothing. */

	}

	/**
	 * Since this represents a state of disconnection, this method always
	 * returns false.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#isConnected(org.rifidi.emulator.io.comm.Communication)
	 */
	public boolean isConnected(Communication curComm) {
		return false;

	}

	/**
	 * Deny the receive, throwing an exception.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#receiveBytes(org.rifidi.emulator.io.comm.Communication)
	 */
	public byte[] receiveBytes(Communication callingComm)
			throws CommunicationException {
		throw new CommunicationException("Cannot receive -- disconnected.");

	}

	/**
	 * Deny the send, throwing an exception.
	 * 
	 * @throws CommunicationException
	 *             Since this is disconnected, it throws an exception.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#sendBytes(byte[],
	 *      org.rifidi.emulator.io.comm.Communication)
	 */
	public void sendBytes(byte[] data, Communication callingComm)
			throws CommunicationException {
		throw new CommunicationException("Cannot send -- disconnected.");

	}

}
