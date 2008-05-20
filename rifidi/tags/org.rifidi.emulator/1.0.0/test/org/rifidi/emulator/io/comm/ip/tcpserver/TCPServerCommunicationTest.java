/*
 *  @(#)TCPServerCommunicationTest.java
 *
 *  Created:	Jun 6, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.ip.tcpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.io.comm.logFormatter.GenericStringLogFormatter;
import org.rifidi.emulator.io.comm.streamreader.GenericCharStreamReader;
import org.rifidi.emulator.io.protocol.RawProtocol;

/**
 * A collection JUnit test cases for the TCPServerCommunication class.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPServerCommunicationTest extends TestCase {

	/**
	 * The hostname where the communication's server will run on.
	 */
	private static final String SERVER_HOSTNAME = "127.0.0.1";

	/**
	 * The port the communication's server will run on.
	 */
	private static final int SERVER_PORT = 30000;

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(TCPServerCommunicationTest.class);

	/**
	 * The power control signal that this uses for testing.
	 */
	private ControlSignal<Boolean> powerControlSignal;

	/**
	 * The connection control signal that this uses for testing.
	 */
	private ControlSignal<Boolean> connectionControlSignal;

	/**
	 * The TCPServerCommunication under test.
	 */
	private TCPServerCommunication tcpComm;

	/**
	 * Basic constructor which initializes log4j.
	 */
	public TCPServerCommunicationTest() {

	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.connectionControlSignal = new ControlSignal<Boolean>(false);
		this.powerControlSignal = new ControlSignal<Boolean>(false);

		/* Create a new tcp communication object */
		this.tcpComm = new TCPServerCommunication(new RawProtocol(),
				this.powerControlSignal, this.connectionControlSignal,
				SERVER_HOSTNAME, SERVER_PORT, "fasdf", GenericCharStreamReader.class, new GenericStringLogFormatter());

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		/* Force a turn-off */
		this.tcpComm.turnOff(this.getClass());
		this.tcpComm = null;
		this.connectionControlSignal.deleteObservers();
		this.connectionControlSignal = null;
		this.powerControlSignal.deleteObservers();
		this.powerControlSignal = null;
		logger.debug("turning off");
	}

	/**
	 * Tests the primary constructor of the TCPServerCommunication.
	 * 
	 */
	public void testTCPServerCommunicationStringInt() {
		assertNotNull(this.tcpComm);

	}

	/**
	 * Tests turning on the TCPServerCommunication while it is off.
	 * 
	 *  
	 */
	public void testTurnOnWhenOff() {
		/* Turn on */
		this.tcpComm.turnOn();

		/* Make a client to connect to the server. */
		Socket clientSocket = new Socket();
		InetSocketAddress clientAddress = new InetSocketAddress("127.0.0.1", 0);

		/* Allow server socket to fully start */
		synchronized (this) {
			try {
				this.wait(1000);
			} catch (InterruptedException e2) {
				/* Do nothing */
			}
			this.notifyAll();
		}

		/* Create a client to connect to the server */
		try {
			/* Bind */
			clientSocket.bind(clientAddress);
			try {
				/* Connect to the TCPServerCommunication. */
				InetSocketAddress serverAddress = new InetSocketAddress(
						SERVER_HOSTNAME, SERVER_PORT);
				clientSocket.connect(serverAddress);

			} catch (IOException e) {
				/* Failed */
				fail("Test client could not connect to Communication: "
						+ e.getMessage());

			}

			/* Allow server socket to fully start client services */
			synchronized (this) {
				try {
					this.wait(1000);
				} catch (InterruptedException e2) {
					/* Do nothing */
				}
				this.notifyAll();
			}

			/* Close the socket. */
			try {
				clientSocket.close();
			} catch (IOException ioe) {
				/* Do nothing */

			}

		} catch (IOException e) {
			fail("Cannot create client to test properly: " + e.getMessage());

		}

	}

	/**
	 * Tests suspending the TCPServerCommunication while it is on.
	 * 
	 *  
	 */
	public void testSuspendWhileOn() {
		fail("Implement me!");

	}

	/**
	 * Tests turning off the TCPServerCommunication while it is on.
	 * 
	 *  
	 */
	public void testTurnOffWhileOn() {
		fail("Implement me!");

	}

	/**
	 * Tests turning off the TCPServerCommunication while it is suspended.
	 * 
	 *  
	 */
	public void testTurnOffWhileSuspended() {
		fail("Implement me!");

	}

	/**
	 * Tests resuming the TCPServerCommunication while it is suspended.
	 * 
	 *  
	 */
	public void testResumeWhileSuspended() {
		fail("Implement me!");

	}

}
