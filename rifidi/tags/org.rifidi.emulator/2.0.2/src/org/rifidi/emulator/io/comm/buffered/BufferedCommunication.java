/*
 *  @(#)BufferedCommunication.java
 *
 *  Created:	Sep 19, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.buffered;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.DataBuffer;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.io.comm.abstract_.AbstractCommunication;
import org.rifidi.emulator.io.protocol.Protocol;

/**
 * A BufferedCommunication is an AbstractCommunication which uses queues to
 * store data which is to be sent and is received. Thus, data is sent and
 * received in first-in, first-out manner.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class BufferedCommunication extends AbstractCommunication {

	/**
	 * The maximum number of byte arrays which can be held by the receive queue.
	 */
	private static final int MAX_RECEIVE_BUFFER_LENGTH = 1;

	/**
	 * The maximum number of byte arrays which can be held by the send queue.
	 */
	private static final int MAX_SEND_BUFFER_LENGTH = 1;

	/**
	 * The buffer for holding data which was received.
	 */
	private DataBuffer<byte[]> receiveBuffer;

	/**
	 * The buffer for holding data which needs to be sent.
	 */
	private DataBuffer<byte[]> sendBuffer;

	/**
	 * Basic constructor. Creates empty receive and send queues.
	 * 
	 * @param initialPowerState
	 *            The desired initial power state of the BufferedCommunication.
	 * @param initialConnectionState
	 *            The desired initial connection state of the
	 *            BufferedCommunication.
	 * @param protocol
	 *            The protocol that this BufferedCommunication will use.
	 * @param powerControlSignal
	 *            The control signal for this BufferedCommunication to observe.
	 *            If <i>null</i> is passed, this will not observe a signal.
	 * @param connectionControlSignal
	 *            The ControlSignal that this will touch when client connects.
	 */
	public BufferedCommunication(CommunicationPowerState initialPowerState,
			CommunicationConnectionState initialConnectionState,
			Protocol protocol, ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal) {
		/* Invoke super constructor */
		super(initialPowerState, initialConnectionState, protocol,
				powerControlSignal, connectionControlSignal);

		/* Create the receive queue */
		this.receiveBuffer = new DataBuffer<byte[]>(
				BufferedCommunication.MAX_RECEIVE_BUFFER_LENGTH);

		/* Create the send queue */
		this.sendBuffer = new DataBuffer<byte[]>(
				BufferedCommunication.MAX_SEND_BUFFER_LENGTH);

	}

	/**
	 * Returns the receiveBuffer.
	 * 
	 * @return Returns the receiveBuffer.
	 */
	protected DataBuffer<byte[]> getReceiveBuffer() {
		return this.receiveBuffer;

	}

	/**
	 * Returns the sendBuffer.
	 * 
	 * @return Returns the sendBuffer.
	 */
	protected DataBuffer<byte[]> getSendBuffer() {
		return this.sendBuffer;

	}
	
	public void suspendAddingToSendBuffer(){
		this.getSendBuffer().setSuspendAdds(true);
	}
	
	public void resumeAddingToSendBuffer(){
		this.getSendBuffer().setSuspendAdds(false);
	}

}
