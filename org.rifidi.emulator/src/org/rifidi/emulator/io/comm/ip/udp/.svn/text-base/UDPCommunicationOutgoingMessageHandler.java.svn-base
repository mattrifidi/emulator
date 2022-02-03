/*
 *  UDPCommunicationOutgoingMessageHandler.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.ip.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.DataBufferInterruptedException;

/**
 * This class handles outgoing messages from the reader.
 * It takes messages off the buffer and sends them as UDP messages
 * to the remote ip and port. 
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class UDPCommunicationOutgoingMessageHandler implements Runnable {
	/**
	 * Logger for problems
	 */
	private static Log logger =
		 LogFactory.getLog(UDPCommunicationIncomingMessageHandler.class);

	/**
	 * The size of the packet (anything after is ignored)
	 */
	//private static final int PACKET_SIZE = 1024;

	/**
	 * The UDPCommunication object that contains the socket
	 */
	private UDPCommunication host;

	/**
	 * local reference to the datagram object
	 */
	private DatagramSocket newSock;

	/**
	 * Creates a UDPCommunicationOutgoingMessageHandler which is bound to
	 * the passed UDPCommunication. This constructor has default access so
	 * that only udpserver-package classes can create these.
	 * 
	 * @param hostCommunication
	 *            The UDPServerCommunication this object is bound to.
	 */
	UDPCommunicationOutgoingMessageHandler(UDPCommunication host) {
		this.host = host;
		newSock = host.getDatagramSocket();
		
		/* We need to connect the DataGram Socket to a target address
		 * this is the remote address in the UDPCommunication object
		 */
		
		InetAddress remoteIP = null;
		
		try {
			remoteIP = InetAddress.getByName(host.getRemoteIPAddress());
		}
		catch (Exception e) {
			logger.debug(e);
		}
		
		newSock.connect(remoteIP, host.getRemotePort());
		
	}

	/**
	 * The main logic of this monitor. Takes data from the send buffer and sends
	 * it to the client.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		/* the packet object to recieve the messages from*/
		DatagramPacket pack;
		
		/* Should the loop keep running? */
		boolean keepRunning = true;
		
		/* The data that will be sent */
		byte[] data;
		
		/*  This part loops until we catch an exception */
		while (keepRunning) {
			try {
				//Take the next packet from the buffer
				data = host.getSendBuffer().takeNextFromBuffer();

				//create a DatagramPacket out of it
				pack = new DatagramPacket(data, data.length);

				//Send it over the network
				newSock.send(pack);

			} catch (DataBufferInterruptedException e) {
				//if we catch an exception then the loop stops
				logger.warn(e.getMessage());
				keepRunning = false;	
			} catch (IOException e) {
				//if we catch an exception then the loop stops
				logger.warn(e.getMessage());
				keepRunning = false;
			}
		}
	}

}
