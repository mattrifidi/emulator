/*
 *  @(#)UDPCommunicationTest.java
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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.DataBufferInterruptedException;
import org.rifidi.emulator.io.protocol.RawProtocol;
/**
 * A collection of test cases for the UDPCommunication class.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class UDPCommunicationTest extends TestCase {

	/**
	 * The hostname where the communication's server will run on.
	 */
	private static final String SERVER_HOSTNAME = "0.0.0.0";

	/**
	 * The port the communication's server will run on.
	 */
	private static final int SERVER_PORT = 0;

	/**
	 * The hostname where the communication's server will broadcast on.
	 */
	private static final String REMOTE_HOSTNAME = "255.255.255.255";

	/**
	 * The port the communication's server will broadcast on.
	 */
	private static final int REMOTE_PORT = 3988;

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(UDPCommunicationTest.class);

	/**
	 * The TCPServerCommunication under test.
	 */
	private UDPCommunication udpComm;

	/**
	 * Constant message to send and receive
	 */
	private final String message = "YOU ARE NOOB HAXOR";

	/**
	 * Basic constructor which initializes log4j.
	 */
	public UDPCommunicationTest() {

	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		/* Create a new udp communication object with both incoming and outgoing */
		this.udpComm = new UDPCommunication(new RawProtocol(), null, null,
				SERVER_HOSTNAME, SERVER_PORT, REMOTE_HOSTNAME, REMOTE_PORT, false);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		/* Force a turn-off */
		this.udpComm.turnOff(this.getClass());
		this.udpComm = null;

	}

	/**
	 * Tests turning on the UDPCommunication while it is off.
	 * 
	 *  
	 */
	public void testTurnOnWhenOff() {
		
		/* Data to send */
		byte[] dataToSend = message.getBytes();

		/* Error code -- gets modified if exceptions occur. */
		boolean error = false;

		/* Attempt to connect to the specified IP/Port using UDP */
		this.udpComm.turnOn();
		
		/* Allow server socket to fully start */
		synchronized (this) {
			try {
				this.wait(1000);
			} catch (InterruptedException e2) {
				/* Do nothing */
			}
			this.notifyAll();
		}

		/* Make a client */
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket(this.udpComm.getRemotePort());
		} catch (SocketException e) {
			logger.debug(this.getName() + ": " + e.getMessage());
			error = true;
		}

		/* Send out a packet of data */
		try {
				this.udpComm.getSendBuffer().addToBuffer(dataToSend);
		}
		catch (DataBufferInterruptedException dbie){
			logger.debug(this.getName() + ": " + dbie.getMessage());
			error = true;
		}
		
		/* Receive the packet of data */
		if (clientSocket != null) {
			/* Set a timeout for receiving data */
			try {
				clientSocket.setSoTimeout(1000);
			} catch (SocketException e) {
				logger.debug(this.getName() + ": " + e.getMessage());
				error = true;
			}

			/* Make a new packet to hold the received data */
			DatagramPacket dataPacket = new DatagramPacket(new byte[1024], 1024);

			/* Attempt to receive the data */
			try {
				clientSocket.receive(dataPacket);
			} catch (IOException e) {
				logger.debug(this.getName() + ": " + e.getMessage());
				error = true;
			}

			/* Check that the data was received succesfully */
			if (!error) {
				logger.debug("Client received: "
						+ new String(dataPacket.getData()));
			} else {
				logger.debug(this.getName()
						+ ": client did not receive message.");
				error = true;
			}
			
			clientSocket.disconnect();

			clientSocket.close();
			clientSocket = null;
			
		}

		/* Check to see if any errors happened. */
		assertFalse(error);
			
	}

	/**
	 * Tests suspending the UDPCommunication while it is on.
	 * 
	 *  
	 */
	public void testSuspendWhileOn() {
		fail("Implement me!");

	}

	/**
	 * Tests turning off the UDPCommunication while it is on.
	 * 
	 *  
	 */
	public void testTurnOffWhileOn() {
		fail("Implement me!");

	}

	/**
	 * Tests turning off the UDPCommunication while it is suspended.
	 * 
	 *  
	 */
	public void testTurnOffWhileSuspended() {
		fail("Implement me!");

	}

	/**
	 * Tests resuming the UDPCommunication while it is suspended.
	 * 
	 *  
	 */
	public void testResumeWhileSuspended() {
		logger.debug("Inside testResumeWhileSuspended()");
		fail("Implement me!");

	}

}
