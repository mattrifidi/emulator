/*
 *  @(#)BufferedCommunicationTest.java
 *
 *  Created:	Oct 2, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.buffered;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.DataBufferInterruptedException;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.io.protocol.RawProtocol;

import junit.framework.TestCase;

/**
 * A collection of unit test cases for BufferedCommunication. <br>
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class BufferedCommunicationTest extends TestCase {

	/**
	 * A test implementation of the BufferedCommunication class.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestBufferedCommunication extends BufferedCommunication {

		/**
		 * A basic constructor for creating a TestBufferedCommunication.
		 * 
		 * @param initialPowerState
		 *            The initial power state.
		 * @param initialConnectionState
		 *            The initial connectionState.
		 * @param powerControlSignal
		 *            The power control signal.
		 * @param connectionControlSignal
		 *            The connection control signal.
		 */
		public TestBufferedCommunication(
				CommunicationPowerState initialPowerState,
				CommunicationConnectionState initialConnectionState,
				ControlSignal<Boolean> powerControlSignal,
				ControlSignal<Boolean> connectionControlSignal) {
			super(initialPowerState, initialConnectionState, new RawProtocol(),
					powerControlSignal, connectionControlSignal);

		}

	}

	/**
	 * A test implementation of a BufferedOnCommunicationPowerState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestBufferedOnCommunicationPowerState extends
			BufferedOnCommunicationPowerState {

	}

	/**
	 * A test implementation of a BufferedOffCommunicationPowerState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestBufferedOffCommunicationPowerState extends
			BufferedOffCommunicationPowerState {

	}

	/**
	 * A test implementation of a BufferedSuspendedCommunicationPowerState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestBufferedSuspendedCommunicationPowerState extends
			BufferedSuspendedCommunicationPowerState {

	}

	/**
	 * A test implementation of a BufferedConnectedCommunicationConnectionState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestBufferedConnectedCommunicationConnectionState extends
			BufferedConnectedCommunicationConnectionState {

	}

	/**
	 * A test implementation of a
	 * BufferedConnectionlessCommunicationConnectionState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	public class TestBufferedConnectionlessCommunicationConnectionState extends
			BufferedConnectionlessCommunicationConnectionState {

	}

	/**
	 * A test implementation of a
	 * BufferedDisconnectedCommunicationConnectionState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	public class TestBufferedDisconnectedCommunicationConnectionState extends
			BufferedDisconnectedCommunicationConnectionState {

	}

	/**
	 * The BufferedCommunication object under test in each test case.
	 */
	private BufferedCommunication bufferedCommunication;

	/**
	 * The power control signal that this uses for testing.
	 */
	private ControlSignal<Boolean> powerControlSignal;

	/**
	 * The connection control signal that this uses for testing.
	 */
	private ControlSignal<Boolean> connectionControlSignal;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.connectionControlSignal = new ControlSignal<Boolean>(false);
		this.powerControlSignal = new ControlSignal<Boolean>(false);

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this.connectionControlSignal.deleteObservers();
		this.connectionControlSignal = null;
		this.powerControlSignal.deleteObservers();
		this.powerControlSignal = null;
		this.bufferedCommunication = null;

	}

	/**
	 * Test method for BufferedCommunication's basic constructor.
	 * 
	 */
	public void testBufferedCommunication() {
		/* Make a new TestBufferedCommunication. */
		this.bufferedCommunication = new TestBufferedCommunication(
				new TestBufferedOnCommunicationPowerState(),
				new TestBufferedConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Make sure the BufferedCommunication was created successfully */
		assertNotNull(this.bufferedCommunication);

	}

	/**
	 * Tests turning on the BufferedCommunication from an off state.
	 */
	public void testTurnOnWhileOff() {
		/* Make a new TestBufferedCommunication */
		this.bufferedCommunication = new TestBufferedCommunication(
				new TestBufferedOffCommunicationPowerState(),
				new TestBufferedConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Add something to each buffer to make it non-empty */
		try {
			this.bufferedCommunication.getReceiveBuffer().addToBuffer(
					new byte[0]);
			this.bufferedCommunication.getSendBuffer().addToBuffer(new byte[0]);

		} catch (DataBufferInterruptedException e) {
			fail(e.getMessage());

		}

		/* Set the buffers to a suspended and interrupted state */
		this.bufferedCommunication.getReceiveBuffer().setInterrupted(true);
		this.bufferedCommunication.getSendBuffer().setInterrupted(true);
		this.bufferedCommunication.getReceiveBuffer().setSuspended(true);
		this.bufferedCommunication.getSendBuffer().setSuspended(true);

		/* Turn on */
		this.bufferedCommunication.turnOn();

		/* Make sure the buffers are not suspended and not interrupted */
		assertFalse(this.bufferedCommunication.getReceiveBuffer()
				.isInterrupted());
		assertFalse(this.bufferedCommunication.getReceiveBuffer().isSuspended());
		assertFalse(this.bufferedCommunication.getSendBuffer().isInterrupted());
		assertFalse(this.bufferedCommunication.getSendBuffer().isSuspended());

		/* Make sure the buffers are empty */
		assertEquals(this.bufferedCommunication.getReceiveBuffer()
				.getCurrentBufferSize(), 0);
		assertEquals(this.bufferedCommunication.getSendBuffer()
				.getCurrentBufferSize(), 0);

	}

	/**
	 * Tests turning off the BufferedCommunication from an on state.
	 */
	public void testTurnOffWhileOn() {
		/* Make a new TestBufferedCommunication */
		this.bufferedCommunication = new TestBufferedCommunication(
				new TestBufferedOnCommunicationPowerState(),
				new TestBufferedConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Set the buffers to a non-interrupted state */
		this.bufferedCommunication.getReceiveBuffer().setInterrupted(false);
		this.bufferedCommunication.getSendBuffer().setInterrupted(false);

		/* Turn on */
		this.bufferedCommunication.turnOff(this.getClass());

		/* Make sure the buffers become interrupted */
		assertTrue(this.bufferedCommunication.getReceiveBuffer()
				.isInterrupted());
		assertTrue(this.bufferedCommunication.getSendBuffer().isInterrupted());

	}

	/**
	 * Tests turning off the BufferedCommunication from a suspended state.
	 */
	public void testTurnOffWhileSuspended() {
		/* Make a new TestBufferedCommunication */
		this.bufferedCommunication = new TestBufferedCommunication(
				new TestBufferedSuspendedCommunicationPowerState(),
				new TestBufferedConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Set the buffers to a non-interrupted state */
		this.bufferedCommunication.getReceiveBuffer().setInterrupted(false);
		this.bufferedCommunication.getSendBuffer().setInterrupted(false);

		/* Turn off */
		this.bufferedCommunication.turnOff(this.getClass());

		/* Make sure the buffers become interrupted */
		assertTrue(this.bufferedCommunication.getReceiveBuffer()
				.isInterrupted());
		assertTrue(this.bufferedCommunication.getSendBuffer().isInterrupted());

	}

	/**
	 * Tests suspending the BufferedCommunication from an on state.
	 */
	public void testSuspendWhileOn() {
		/* Make a new TestBufferedCommunication */
		this.bufferedCommunication = new TestBufferedCommunication(
				new TestBufferedOnCommunicationPowerState(),
				new TestBufferedConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Set the buffers to a non-suspended state */
		this.bufferedCommunication.getReceiveBuffer().setSuspended(false);
		this.bufferedCommunication.getSendBuffer().setSuspended(false);

		/* Suspend */
		this.bufferedCommunication.suspend();

		/* Make sure the buffers become suspended */
		assertTrue(this.bufferedCommunication.getReceiveBuffer().isSuspended());
		assertTrue(this.bufferedCommunication.getSendBuffer().isSuspended());

	}

	/**
	 * Tests turning on the BufferedCommunication from an suspended state.
	 */
	public void testResumeWhileSuspended() {
		/* Make a new TestBufferedCommunication */
		this.bufferedCommunication = new TestBufferedCommunication(
				new TestBufferedSuspendedCommunicationPowerState(),
				new TestBufferedConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Set the buffers to a suspended state */
		this.bufferedCommunication.getReceiveBuffer().setSuspended(true);
		this.bufferedCommunication.getSendBuffer().setSuspended(true);

		/* Resume */
		this.bufferedCommunication.resume();

		/* Make sure the buffers become resumed (not suspended) */
		assertFalse(this.bufferedCommunication.getReceiveBuffer().isSuspended());
		assertFalse(this.bufferedCommunication.getSendBuffer().isSuspended());

	}

}
