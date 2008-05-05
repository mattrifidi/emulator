/*
 *  @(#)UDPCommunication.java
 *
 *  Created:	Jun 6, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.ip.udp;

import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.io.comm.ip.IPCommunication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.rifidi.emulator.io.protocol.Protocol;
import org.rifidi.emulator.common.DataBuffer;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.utilities.formatting.GeneralFormattingUtility;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * An extension of the IPCommunication class which communicates using UDP (User
 * Data Protocol) datagrams. <br>
 * 
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class UDPCommunication extends IPCommunication {

	/**
	 * Message logger
	 */
	private static Log logger =
		 LogFactory.getLog(UDPCommunicationIncomingMessageHandler.class);

	/**
	 * The socket that the reader will be sending information via UDP with
	 */
	private DatagramSocket newSock;

	private Protocol prot;

	private boolean outputOnly;

	/**
	 * A constructor for a UDPCommunication. Takes in the local IP/port to bind
	 * to and and the remote IP/port to send data to.
	 * 
	 * @param initialPowerState
	 *            The initial power state.
	 * @param initialConnectionState
	 *            The initial connection state.
	 * @param localIP
	 *            The local IP to bind to.
	 * @param localPort
	 *            The local port to bind to.
	 * @param remoteIP =
	 *            new DatagramSocket() The remote IP to send data to.
	 * @param remotePort
	 *            The remote port to send data to.
	 * @param protocol
	 *            The protocol that this session will be using.
	 */
	public UDPCommunication(Protocol prot,
			ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal, String localIP,
			int localPort, String remoteIP, int remotePort, boolean outputOnly)
			throws CommunicationException {

		/* call the super constructor for ip communication and set vars */
		super(UDPOffCommunicationPowerState.getInstance(),
				UDPConnectionlessCommunicationConnectionState.getInstance(),
				prot, powerControlSignal, connectionControlSignal, localIP,
				localPort, remoteIP, remotePort);

		/* Validate that the ip parameters passed in are not null */
		if (localIP == null
				|| !GeneralFormattingUtility.isValidIPPort(localPort)
				|| remoteIP == null
				|| !GeneralFormattingUtility.isValidIPPort(remotePort)) {
			throw new CommunicationException("Invalid ip Parameters");
		}

		/* This variable is used to indicate outgoing UPD packets only */
		this.outputOnly = outputOnly;

		try {
			newSock = new DatagramSocket(null);
		} catch (SocketException e) {
			logger.warn(e.getMessage());
		}
		this.prot = prot;
	}

	/**
	 * Returns the socket for this object
	 * 
	 * @return The socket for this object.
	 */
	public DatagramSocket getDatagramSocket() {
		return newSock;
	}

	/**
	 * Allows the use of the BufferedCommunication method by UDP-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedCommunication#getReceiveBuffer()
	 */
	@Override
	protected DataBuffer<byte[]> getReceiveBuffer() {
		return super.getReceiveBuffer();
	}

	/**
	 * Allows the use of the BufferedCommunication method by UDP-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedCommunication#getSendBuffer()
	 */
	@Override
	protected DataBuffer<byte[]> getSendBuffer() {
		return super.getSendBuffer();
	}

	/**
	 * Returns the protocol that this object contains.
	 * 
	 * @return the protocol
	 */
	@Override
	public Protocol getProtocol() {
		return prot;
	}

	/**
	 * @param newSock
	 *            The newSock to set.
	 */
	public void setDatagramSocket(DatagramSocket newSock) {
		this.newSock = newSock;
	}

	/**
	 * @return Returns the output.
	 */
	public boolean isOutputOnly() {
		return outputOnly;
	}

	/**
	 * Changes the power state of the UDP module.  
	 * 
	 * @see org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#changePowerState(org.rifidi.emulator.io.comm.CommunicationPowerState)
	 */
	@Override
	protected void changePowerState(CommunicationPowerState anotherPowerState) {
		/* Invoke the super-class implementation. */
		super.changePowerState(anotherPowerState);
	}

}
