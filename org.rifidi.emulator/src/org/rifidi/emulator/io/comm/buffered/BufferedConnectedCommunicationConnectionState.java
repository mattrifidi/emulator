/*
 *  @(#)BufferedConnectedCommunicationConnectionState.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.DataBufferInterruptedException;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.io.comm.abstract_.AbstractConnectedCommunicationConnectionState;

/**
 * Implements functions of the CONNECTED connection state for a
 * BufferedCommunication.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class BufferedConnectedCommunicationConnectionState extends
		AbstractConnectedCommunicationConnectionState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(BufferedConnectedCommunicationConnectionState.class);

	/**
	 * Turns off (interrupts) the read/write buffers. <br>
	 * 
	 * Note that this does NOT switch the state to disconnected, since there is
	 * no concrete implementation known.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#disconnect(org.rifidi.emulator.io.comm.Communication)
	 */
	public void disconnect(Communication curComm) {
		/* Cast the pcObject to a BufferedCommunication object */
		BufferedCommunication buffComm = (BufferedCommunication) curComm;

		/* Interrupt the send/receive buffers */
		buffComm.getSendBuffer().setInterrupted(true);
		buffComm.getReceiveBuffer().setInterrupted(true);

	}

	/**
	 * Allow the receive to commence. Grabs the next piece of data from the
	 * receive buffer.
	 * 
	 * @throws CommunicationException
	 *             If the receive is interrupted.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#receiveBytes(org.rifidi.emulator.io.comm.Communication)
	 */
	public byte[] receiveBytes(Communication callingComm)
			throws CommunicationException {
		/* Grab from the receive buffer */
		try {
			return ((BufferedCommunication) callingComm).getReceiveBuffer()
					.takeNextFromBuffer();

		} catch (DataBufferInterruptedException e) {
			/* Throw a CommunicationException */
			throw new CommunicationException("Receive interrupted.", e);

		}

	}

	/**
	 * Allow the send to commence. Puts the passed data into the send buffer.
	 * 
	 * @throws CommunicationException
	 *             If the send is interrupted.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#sendBytes(byte[],
	 *      org.rifidi.emulator.io.comm.Communication)
	 */
	public void sendBytes(byte[] data, Communication callingComm)
			throws CommunicationException {
		BufferedCommunication buffComm = (BufferedCommunication) callingComm;
		/* Add to the send buffer, applying protocol. */

		logger.debug("sending bytes, buffer is: "
				+ buffComm.getSendBuffer().isSuspended()
				+ "  size of buffer is: "
				+ buffComm.getSendBuffer().getCurrentBufferSize());

		try {
			buffComm.getSendBuffer().addToBuffer(
					buffComm.getProtocol().addProtocol(data));

		} catch (DataBufferInterruptedException e) {
			/* Throw a CommunicationException */
			throw new CommunicationException("Send interrupted.", e);

		}

	}

}
