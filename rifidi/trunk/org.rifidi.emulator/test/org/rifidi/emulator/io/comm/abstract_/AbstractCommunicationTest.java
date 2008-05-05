/*
 *  @(#)AbstractCommunicationTest.java
 *
 *  Created:	Oct 2, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.abstract_;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.io.protocol.RawProtocol;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * A collection of unit tests for the AbstractCommunication class.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AbstractCommunicationTest extends TestCase {

	/**
	 * A test implementation of AbstractCommunication.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractCommunication extends AbstractCommunication {

		/**
		 * A basic constructor for a test implementation.
		 * 
		 * @param initialPowerState
		 *            The initial power state.
		 * @param initialConnectionState
		 *            The initial connection state.
		 * @param powerControlSignal
		 *            The power control signal.
		 * @param connectionControlSignal
		 *            The connection control signal.
		 */
		public TestAbstractCommunication(
				CommunicationPowerState initialPowerState,
				CommunicationConnectionState initialConnectionState,
				ControlSignal<Boolean> powerControlSignal,
				ControlSignal<Boolean> connectionControlSignal) {

			super(initialPowerState, initialConnectionState, new RawProtocol(),
					powerControlSignal, connectionControlSignal);
			/* Do nothing, this is simply a test implementation. */
		}

	}

	/**
	 * A test implementation of AbstractConnectedCommunicationConnectionState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractConnectedCommunicationConnectionState extends
			AbstractConnectedCommunicationConnectionState {

		/**
		 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#disconnect(org.rifidi.emulator.io.comm.Communication)
		 */
		public void disconnect(Communication curComm) {
			/* Fake a disconnection */
			AbstractCommunicationTest.this.disconnectionOccurred = true;

		}

		/**
		 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#receiveBytes(org.rifidi.emulator.io.comm.Communication)
		 */
		public byte[] receiveBytes(Communication callingComm) {
			/* Fake a receive */
			AbstractCommunicationTest.this.receiveOccurred = true;
			return null;
		}

		/**
		 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#sendBytes(byte[],
		 *      org.rifidi.emulator.io.comm.Communication)
		 */
		public void sendBytes(byte[] data, Communication callingComm) {
			/* Fake a send. */
			AbstractCommunicationTest.this.sendOccurred = true;

		}

	}

	/**
	 * A test implementation of
	 * AbstractConnectionlessCommunicationConnectionState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractConnectionlessCommunicationConnectionState extends
			AbstractConnectionlessCommunicationConnectionState {

		/**
		 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#receiveBytes(org.rifidi.emulator.io.comm.Communication)
		 */
		public byte[] receiveBytes(Communication callingComm) {
			/* Fake a receive */
			AbstractCommunicationTest.this.receiveOccurred = true;
			return null;
		}

		/**
		 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#sendBytes(byte[],
		 *      org.rifidi.emulator.io.comm.Communication)
		 */
		public void sendBytes(byte[] data, Communication callingComm) {
			/* Fake a send. */
			AbstractCommunicationTest.this.sendOccurred = true;

		}

	}

	/**
	 * A test implementation of
	 * AbstractDisconnectedCommunicationConnectionState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractDisconnectedCommunicationConnectionState extends
			AbstractDisconnectedCommunicationConnectionState {

		/**
		 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#connect(org.rifidi.emulator.io.comm.Communication)
		 */
		public void connect(Communication curComm) {
			/* Fake a connection */
			AbstractCommunicationTest.this.connectionOccurred = true;

		}

		/**
		 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#receiveBytes(org.rifidi.emulator.io.comm.Communication)
		 */
		public byte[] receiveBytes(Communication callingComm) {
			/* Fake a receive */
			AbstractCommunicationTest.this.receiveOccurred = true;
			return null;
		}

	}

	/**
	 * A test implementation of AbstractOffCommunicationPowerState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractOffCommunicationPowerState extends
			AbstractOffCommunicationPowerState {

		/**
		 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
		 */
		public void turnOn(PowerControllable pcObject) {
			/* Do nothing, simply a test implementation */

		}
	}

	/**
	 * A test implementation of AbstractOnCommunicationPowerState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractOnCommunicationPowerState extends
			AbstractOnCommunicationPowerState {

		/**
		 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
		 */
		public void suspend(PowerControllable pcObject) {
			/* Do nothing, simply a test implementation */

		}

		/**
		 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
		 */
		public void turnOff(PowerControllable pcObject, Class callingClass) {
			/* Do nothing, simply a test implementation */

		}

	}

	/**
	 * A test implementation of AbstractOffCommunicationPowerState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractSuspendedCommunicationPowerState extends
			AbstractSuspendedCommunicationPowerState {

		/**
		 * @see org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common.PowerControllable)
		 */
		public void resume(PowerControllable pcObject) {
			/* Do nothing, simply a test implementation */

		}

		/**
		 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
		 */
		public void turnOff(PowerControllable pcObject, Class callingClass) {
			/* Do nothing, simply a test implementation */

		}

	}

	/**
	 * The AbstractCommunication which each test case uses.
	 */
	private AbstractCommunication abstractCommunication;

	/**
	 * The power control signal that this uses for testing.
	 */
	private ControlSignal<Boolean> powerControlSignal;

	/**
	 * The connection control signal that this uses for testing.
	 */
	private ControlSignal<Boolean> connectionControlSignal;

	/**
	 * A flag variable to see if a connection actually occurred.
	 */
	private boolean connectionOccurred;

	/**
	 * A flag variable to see if a disconnection actually occurred.
	 */
	private boolean disconnectionOccurred;

	/**
	 * A flag variable to see if data would actually be received.
	 */
	private boolean receiveOccurred;

	/**
	 * A flag variable to see if data would actually be sent.
	 */
	private boolean sendOccurred;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.connectionControlSignal = new ControlSignal<Boolean>(false);
		this.powerControlSignal = new ControlSignal<Boolean>(false);
		/* Reset flags */
		this.connectionOccurred = false;
		this.receiveOccurred = false;
		this.receiveOccurred = false;
		this.sendOccurred = false;

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.connectionControlSignal.deleteObservers();
		this.connectionControlSignal = null;
		this.powerControlSignal.deleteObservers();
		this.powerControlSignal = null;
		this.abstractCommunication = null;

	}

	/**
	 * Test method for AbstractCommunication's basic constructor.
	 */
	public void testAbstractCommunication() {
		/* Create a new AbstractCommunication using the test implementation */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOffCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Check to make sure it created successfully */
		Assert.assertNotNull(this.abstractCommunication);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#changeConnectionState(org.rifidi.emulator.io.comm.CommunicationConnectionState)}.
	 */
	public void testChangeConnectionState() {
		/* Make a state to pass into the constructor. */
		CommunicationConnectionState passedConnectionState = new TestAbstractConnectedCommunicationConnectionState();

		/* Make a power state to pass into the constructor. */
		CommunicationPowerState passedPowerState = new TestAbstractOffCommunicationPowerState();

		/* Make a new AbstractCommunication object. */
		this.abstractCommunication = new TestAbstractCommunication(
				passedPowerState, passedConnectionState,
				this.powerControlSignal, this.connectionControlSignal);

		/* Change the connection state */
		CommunicationConnectionState anotherConnectionState = new TestAbstractDisconnectedCommunicationConnectionState();
		this.abstractCommunication
				.changeConnectionState(anotherConnectionState);

		/* Verify the change. */
		Assert
				.assertTrue(this.abstractCommunication.getCurConnectionState() == anotherConnectionState);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#changePowerState(org.rifidi.emulator.io.comm.CommunicationPowerState)}.
	 */
	public void testChangePowerState() {
		/* Make a state to pass into the constructor. */
		CommunicationConnectionState passedConnectionState = new TestAbstractConnectedCommunicationConnectionState();

		/* Make a power state to pass into the constructor. */
		CommunicationPowerState passedPowerState = new TestAbstractOffCommunicationPowerState();

		/* Make a new AbstractCommunication object. */
		this.abstractCommunication = new TestAbstractCommunication(
				passedPowerState, passedConnectionState,
				this.powerControlSignal, this.connectionControlSignal);

		/* Change the connection state */
		CommunicationPowerState anotherPowerState = new TestAbstractOnCommunicationPowerState();
		this.abstractCommunication.changePowerState(anotherPowerState);

		/* Verify the change. */
		Assert
				.assertTrue(this.abstractCommunication.getPowerState() == anotherPowerState);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#connect()}.
	 */
	public void testConnectWhileConnected() {
		/* Make a new TestAbstractCommunication with a connected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Try connecting */
		this.abstractCommunication.connect();

		/* Make sure no connection happens, as we are already connected. */
		Assert.assertFalse(this.connectionOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#connect()}.
	 */
	public void testConnectWhileConnectionless() {
		/* Make a new TestAbstractCommunication with a connectionless state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectionlessCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Try connecting */
		this.abstractCommunication.connect();

		/* Make sure no connection happens, as this is connectionless. */
		Assert.assertFalse(this.connectionOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#connect()}.
	 */
	public void testConnectWhileDisconnected() {
		/* Make a new TestAbstractCommunication with a disconnected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractDisconnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Try connecting */
		this.abstractCommunication.connect();

		/* Make sure a connection happened */
		Assert.assertTrue(this.connectionOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#disconnect()}.
	 */
	public void testDisconnectWhileConnected() {
		/* Make a new TestAbstractCommunication with a connected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Try disconnecting */
		this.abstractCommunication.disconnect();

		/* Make sure a disconnection occured. */
		Assert.assertTrue(this.disconnectionOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#disconnect()}.
	 */
	public void testDisconnectWhileConnectionless() {
		/* Make a new TestAbstractCommunication with a connectionless state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectionlessCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Try disconnecting */
		this.abstractCommunication.disconnect();

		/* Make sure no disconnection happens, as this is connectionless. */
		Assert.assertFalse(this.disconnectionOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#disconnect()}.
	 */
	public void testDisconnectWhileDisconnected() {
		/* Make a new TestAbstractCommunication with a disconnected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractDisconnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Try disconnecting */
		this.abstractCommunication.disconnect();

		/* Make sure a disconnection did not happen. */
		Assert.assertFalse(this.disconnectionOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#getCurConnectionState()}.
	 */
	public void testGetCurConnectionState() {
		/* Make a state to pass into the constructor. */
		CommunicationConnectionState passedConnectionState = new TestAbstractConnectedCommunicationConnectionState();
		/* Make a power state to pass into the constructor. */
		CommunicationPowerState passedPowerState = new TestAbstractOffCommunicationPowerState();

		/* Make a new AbstractCommunication object. */
		this.abstractCommunication = new TestAbstractCommunication(
				passedPowerState, passedConnectionState,
				this.powerControlSignal, this.connectionControlSignal);

		/* Make sure that we get back the same connection state */
		Assert
				.assertTrue(this.abstractCommunication.getCurConnectionState() == passedConnectionState);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#getPowerState()}.
	 */
	public void testGetPowerState() {
		/* Make a state to pass into the constructor. */
		CommunicationConnectionState passedConnectionState = new TestAbstractConnectedCommunicationConnectionState();
		/* Make a power state to pass into the constructor. */
		CommunicationPowerState passedPowerState = new TestAbstractOffCommunicationPowerState();

		/* Make a new AbstractCommunication object. */
		this.abstractCommunication = new TestAbstractCommunication(
				passedPowerState, passedConnectionState,
				this.powerControlSignal, this.connectionControlSignal);

		/* Make sure that we get back the same power state */
		Assert
				.assertTrue(this.abstractCommunication.getPowerState() == passedPowerState);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#disconnect()}.
	 */
	public void testIsConnectedWhileConnected() {
		/* Make a new TestAbstractCommunication with a connected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Make sure that isConnected returns true. */
		Assert.assertTrue(this.abstractCommunication.isConnected());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#disconnect()}.
	 */
	public void testIsConnectedWhileConnectionless() {
		/* Make a new TestAbstractCommunication with a connectionless state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectionlessCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Make sure that isConnected returns false. */
		Assert.assertFalse(this.abstractCommunication.isConnected());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#disconnect()}.
	 */
	public void testIsConnectedWhileDisconnected() {
		/* Make a new TestAbstractCommunication with a disconnected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractDisconnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Make sure that isConnected returns false. */
		Assert.assertFalse(this.abstractCommunication.isConnected());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#receiveBytes()}.
	 */
	public void testReceiveBytesWhileOffAndConnected() {
		/* Make a new TestAbstractCommunication with an off/connected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOffCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a receive bytes */
		try {
			this.abstractCommunication.receiveBytes();
			Assert.fail("Did not throw expected exception.");

		} catch (CommunicationException e) {
			/* This is expected, do nothing. */

		}

		/* Make sure a receive did not occur */
		assertFalse(this.receiveOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#receiveBytes()}.
	 */
	public void testReceiveBytesWhileOffAndConnectionless() {
		/* Make a new TestAbstractCommunication with an off/connectionless state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOffCommunicationPowerState(),
				new TestAbstractConnectionlessCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a receive bytes */
		try {
			this.abstractCommunication.receiveBytes();
			Assert.fail("Did not throw expected exception.");

		} catch (CommunicationException e) {
			/* This is expected, do nothing. */

		}

		/* Make sure a receive did not occur */
		assertFalse(this.receiveOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#receiveBytes()}.
	 */
	public void testReceiveBytesWhileOffAndDisconnected() {
		/* Make a new TestAbstractCommunication with an off/disconnected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOffCommunicationPowerState(),
				new TestAbstractDisconnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a receive bytes */
		try {
			this.abstractCommunication.receiveBytes();
			Assert.fail("Did not throw expected exception.");

		} catch (CommunicationException e) {
			/* This is expected, do nothing. */

		}

		/* Make sure a receive did not occur */
		assertFalse(this.receiveOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#receiveBytes()}.
	 */
	public void testReceiveBytesWhileOnAndConnected() {
		/* Make a new TestAbstractCommunication with an on/connected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a receive bytes */
		try {
			this.abstractCommunication.receiveBytes();
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a receive did occur */
		assertTrue(this.receiveOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#receiveBytes()}.
	 */
	public void testReceiveBytesWhileOnAndConnectionless() {
		/* Make a new TestAbstractCommunication with an on/connectionless state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectionlessCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a receive bytes */
		try {
			this.abstractCommunication.receiveBytes();
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a receive did occur */
		assertTrue(this.receiveOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#receiveBytes()}.
	 */
	public void testReceiveBytesWhileOnAndDisconnected() {
		/* Make a new TestAbstractCommunication with an on/disconnected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractDisconnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a receive bytes */
		try {
			this.abstractCommunication.receiveBytes();
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a receive did occur */
		assertTrue(this.receiveOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#receiveBytes()}.
	 */
	public void testReceiveBytesWhileSuspendedAndConnected() {
		/* Make a new TestAbstractCommunication with an on/connected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractSuspendedCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a receive bytes */
		try {
			this.abstractCommunication.receiveBytes();
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a receive did occur */
		assertTrue(this.receiveOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#receiveBytes()}.
	 */
	public void testReceiveBytesWhileSuspendedAndConnectionless() {
		/* Make a new TestAbstractCommunication with an on/connectionless state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractSuspendedCommunicationPowerState(),
				new TestAbstractConnectionlessCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a receive bytes */
		try {
			this.abstractCommunication.receiveBytes();
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a receive did occur */
		assertTrue(this.receiveOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#receiveBytes()}.
	 */
	public void testReceiveBytesWhileSuspendedAndDisconnected() {
		/* Make a new TestAbstractCommunication with an on/disconnected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractSuspendedCommunicationPowerState(),
				new TestAbstractDisconnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a receive bytes */
		try {
			this.abstractCommunication.receiveBytes();
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a send did not occur */
		assertTrue(this.receiveOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#sendBytes(byte[])}.
	 */
	public void testSendBytesWhileOffAndConnected() {
		/* Make a new TestAbstractCommunication with an off/connected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOffCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a send bytes */
		try {
			this.abstractCommunication.sendBytes(null);
			Assert.fail("Did not throw expected exception.");

		} catch (CommunicationException e) {
			/* This is expected, do nothing. */

		}

		/* Make sure a send did not occur */
		assertFalse(this.sendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#sendBytes(byte[])}.
	 */
	public void testSendBytesWhileOffAndConnectionless() {
		/* Make a new TestAbstractCommunication with an off/connectionless state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOffCommunicationPowerState(),
				new TestAbstractConnectionlessCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a send bytes */
		try {
			this.abstractCommunication.sendBytes(null);
			Assert.fail("Did not throw expected exception.");

		} catch (CommunicationException e) {
			/* This is expected, do nothing. */

		}

		/* Make sure a send did not occur */
		assertFalse(this.sendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#sendBytes(byte[])}.
	 */
	public void testSendBytesWhileOffAndDisconnected() {
		/* Make a new TestAbstractCommunication with an off/disconnected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOffCommunicationPowerState(),
				new TestAbstractDisconnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a send bytes */
		try {
			this.abstractCommunication.sendBytes(null);
			Assert.fail("Did not throw expected exception.");

		} catch (CommunicationException e) {
			/* This is expected, do nothing. */

		}

		/* Make sure a send did not occur */
		assertFalse(this.sendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#sendBytes(byte[])}.
	 */
	public void testSendBytesWhileOnAndConnected() {
		/* Make a new TestAbstractCommunication with an on/connected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a send bytes */
		try {
			this.abstractCommunication.sendBytes(null);
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a send did occur */
		assertTrue(this.sendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#sendBytes(byte[])}.
	 */
	public void testSendBytesWhileOnAndConnectionless() {
		/* Make a new TestAbstractCommunication with an on/connectionless state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractConnectionlessCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a send bytes */
		try {
			this.abstractCommunication.sendBytes(null);
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a send did occur */
		assertTrue(this.sendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#sendBytes(byte[])}.
	 */
	public void testSendBytesWhileOnAndDisconnected() {
		/* Make a new TestAbstractCommunication with an on/disconnected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractOnCommunicationPowerState(),
				new TestAbstractDisconnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a send bytes */
		try {
			this.abstractCommunication.sendBytes(null);
			Assert.fail("Did not throw expected exception.");

		} catch (CommunicationException e) {
			/* This is expected, do nothing. */

		}

		/* Make sure a send did not occur */
		assertFalse(this.sendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#sendBytes(byte[])}.
	 */
	public void testSendBytesWhileSuspendedAndConnected() {
		/* Make a new TestAbstractCommunication with an on/connected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractSuspendedCommunicationPowerState(),
				new TestAbstractConnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a send bytes */
		try {
			this.abstractCommunication.sendBytes(null);
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a send did occur */
		assertTrue(this.sendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#sendBytes(byte[])}.
	 */
	public void testSendBytesWhileSuspendedAndConnectionless() {
		/* Make a new TestAbstractCommunication with an on/connectionless state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractSuspendedCommunicationPowerState(),
				new TestAbstractConnectionlessCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a send bytes */
		try {
			this.abstractCommunication.sendBytes(null);
		} catch (CommunicationException e) {
			/* Not expected, fail. */
			fail(e.getMessage());

		}

		/* Make sure a send did occur */
		assertTrue(this.sendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#sendBytes(byte[])}.
	 */
	public void testSendBytesWhileSuspendedAndDisconnected() {
		/* Make a new TestAbstractCommunication with an on/disconnected state */
		this.abstractCommunication = new TestAbstractCommunication(
				new TestAbstractSuspendedCommunicationPowerState(),
				new TestAbstractDisconnectedCommunicationConnectionState(),
				this.powerControlSignal, this.connectionControlSignal);

		/* Attempt a send bytes */
		try {
			this.abstractCommunication.sendBytes(null);
			Assert.fail("Did not throw expected exception.");

		} catch (CommunicationException e) {
			/* This is expected, do nothing. */

		}

		/* Make sure a send did not occur */
		assertFalse(this.sendOccurred);

	}

}
