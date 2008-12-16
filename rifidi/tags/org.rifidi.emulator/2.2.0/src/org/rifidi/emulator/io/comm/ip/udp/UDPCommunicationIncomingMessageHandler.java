/*
 *  UDPCommunicationIncomingMessageHandler.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.ip.udp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.rifidi.emulator.common.DataBufferInterruptedException;
import org.rifidi.emulator.io.protocol.ProtocolValidationException;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.List;

/**
 * This class handles incoming messages.
 * 
 * @author Matthew Dean
 */
public class UDPCommunicationIncomingMessageHandler implements Runnable {

	/**
	 * Logger for problems
	 */
	private static Log logger = LogFactory
			.getLog(UDPCommunicationIncomingMessageHandler.class);

	/**
	 * The size of the packet (anything after is ignored)
	 */
	private static final int PACKET_SIZE = 1024;

	/**
	 * The UDPCommunication object that contains the socket
	 */
	private UDPCommunication host;

	/**
	 * DatagramSocket for the incoming messages.
	 */
	private DatagramSocket newSock;

	/**
	 * Creates a UDPCommunicationIncomingMessageHandler which is bound to the
	 * passed UDPCommunication. This constructor has default access so that only
	 * udpserver-package classes can create these.
	 * 
	 * @param hostCommunication
	 *            The UDPServerCommunication this object is bound to.
	 */
	public UDPCommunicationIncomingMessageHandler(UDPCommunication host) {
		this.host = host;
		newSock = host.getDatagramSocket();

		/*
		 * No need to connect or bind because this already happens in the power
		 * state.
		 */

	}

	/**
	 * The main logic of the monitor. Reads in data from the client socket until
	 * the client socket is closed/disconnected.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		/* Create a Datagram packet to hold the recieved message */
		DatagramPacket pack = new DatagramPacket(new byte[PACKET_SIZE],
				PACKET_SIZE);

		/* Should the loop keep running? */
		boolean keepRunning = true;

		/* This part loops until we catch an exception */
		while (keepRunning) {

			/* receive messages on the socket */
			try {
				newSock.receive(pack);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}

			/* put messages into the buffer */
			if (pack.getData() != null) {
				try {
					List<byte[]> listOfBytes = this.host.getProtocol()
							.removeProtocol(pack.getData());
					for (byte[] b : listOfBytes) {
						this.host.getReceiveBuffer().addToBuffer(b);
					}
				} catch (DataBufferInterruptedException e) {
					/* Thrown because socket was interrupted */
					logger.warn(e.getMessage());
					keepRunning = false;
				} catch (ProtocolValidationException e) {
					/* Thrown because of a problem with the protocol */
					logger.warn(e.getMessage());
				}
			}
		}
	}
}
