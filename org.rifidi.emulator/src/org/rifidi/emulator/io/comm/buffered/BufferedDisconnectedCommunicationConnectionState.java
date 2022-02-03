/*
 *  @(#)BufferedDisconnectedCommunicationConnectionState.java
 *
 *  Created:	Sep 27, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.buffered;

import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.abstract_.AbstractDisconnectedCommunicationConnectionState;

/**
 * Implements functions of the DISCONNECTED connection state for a
 * BufferedCommunication.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class BufferedDisconnectedCommunicationConnectionState extends
		AbstractDisconnectedCommunicationConnectionState {

	/**
	 * Clears the buffers for the new connection. <br>
	 * 
	 * Note that this does NOT switch the state to connected, since there is no
	 * concrete implementation known.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#connect(org.rifidi.emulator.io.comm.Communication)
	 */
	public void connect(Communication curComm) {
		/* Cast the Comm object to a BufferedCommunication object. */
		BufferedCommunication buffComm = (BufferedCommunication) curComm;

		/* Clear the buffers */
		buffComm.getReceiveBuffer().clearBuffer();
		buffComm.getSendBuffer().clearBuffer();

		/* Clear interrupts from buffers */
		buffComm.getReceiveBuffer().setInterrupted(false);
		buffComm.getSendBuffer().setInterrupted(false);

	}

}
