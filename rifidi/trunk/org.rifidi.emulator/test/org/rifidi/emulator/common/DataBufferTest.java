/*
 *  @(#)DataBufferTest.java
 *
 *  Created:	Oct 3, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.common;

import org.rifidi.emulator.common.DataBuffer;

import junit.framework.TestCase;

/**
 * A set of JUnit test cases for the DataBuffer class.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class DataBufferTest extends TestCase {

	/**
	 * The DataBuffer object to use in each test method.
	 */
	private DataBuffer<String> dataBuffer;

	/**
	 * The default maximum buffer size for each test.
	 */
	private static final int TEST_BUFFER_SIZE = 4;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.dataBuffer = new DataBuffer<String>(TEST_BUFFER_SIZE);

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this.dataBuffer = null;

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#DataBuffer(int)}.
	 */
	public void testDataBuffer() {
		/* Make sure the data buffer was created successfully */
		assertNotNull(this.dataBuffer);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#addToBuffer(java.lang.Object)}.
	 */
	public void testAddToBuffer() {
		String testString = "hello";

		/* Add something to the buffer */
		try {
			this.dataBuffer.addToBuffer(testString);
		} catch (DataBufferInterruptedException e) {
			fail(e.getMessage());

		}

		/* Make sure the buffer size is now one */
		assertEquals(this.dataBuffer.getCurrentBufferSize(), 1);

		/* Make sure we can now get it from the buffer */
		try {
			assertEquals(this.dataBuffer.takeNextFromBuffer(), testString);
		} catch (DataBufferInterruptedException e) {
			fail(e.getMessage());

		}

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#clearBuffer()}.
	 */
	public void testClearBuffer() {
		String testStringOld = "hello";
		String testStringNew = "there";

		/* Add something to the buffer */
		try {
			this.dataBuffer.addToBuffer(testStringOld);
		} catch (DataBufferInterruptedException e) {
			fail(e.getMessage());

		}

		/* Clear the buffer */
		this.dataBuffer.clearBuffer();

		/* Make sure the buffer size is now zero */
		assertEquals(this.dataBuffer.getCurrentBufferSize(), 0);

		/* Now, add the new string */
		try {
			this.dataBuffer.addToBuffer(testStringNew);
		} catch (DataBufferInterruptedException e) {
			fail(e.getMessage());

		}

		/* Make sure we get the NEW string from the buffer */
		try {
			assertEquals(this.dataBuffer.takeNextFromBuffer(), testStringNew);
		} catch (DataBufferInterruptedException e) {
			fail(e.getMessage());

		}

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#setInterrupted(boolean)}.
	 */
	public void testSetInterrupted() {
		/* Set interrupted to a new value, make sure we get the new value. */
		this.dataBuffer.setInterrupted(true);
		assertTrue(this.dataBuffer.isInterrupted());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#setSuspended(boolean)}.
	 */
	public void testSetSuspended() {
		/* Set suspended to a new value, make sure we get the new value. */
		this.dataBuffer.setSuspended(true);
		assertTrue(this.dataBuffer.isSuspended());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#takeNextFromBuffer()}.
	 */
	public void testTakeNextFromBuffer() {
		String testString = "hello";

		/* Add something to the buffer */
		try {
			this.dataBuffer.addToBuffer(testString);
		} catch (DataBufferInterruptedException e) {
			fail(e.getMessage());

		}

		/* Make sure we can now get it from the buffer */
		try {
			assertEquals(this.dataBuffer.takeNextFromBuffer(), testString);
		} catch (DataBufferInterruptedException e) {
			fail(e.getMessage());

		}

		/* Make sure the buffer size is now zero */
		assertEquals(this.dataBuffer.getCurrentBufferSize(), 0);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#isInterrupted()}.
	 */
	public void testIsInterrupted() {
		/* Set interrupted to a new value, make sure we get the new value. */
		this.dataBuffer.setInterrupted(true);
		assertTrue(this.dataBuffer.isInterrupted());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#isSuspended()}.
	 */
	public void testIsSuspended() {
		/* Set suspended to a new value, make sure we get the new value. */
		this.dataBuffer.setSuspended(true);
		assertTrue(this.dataBuffer.isSuspended());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#takeNextFromBuffer()}.
	 */
	public void testTakeNextFromBufferWhileSuspended() {
		String testString = "hello";

		/* Add something to the buffer */
		try {
			this.dataBuffer.addToBuffer(testString);
		} catch (DataBufferInterruptedException e1) {
			fail(e1.getMessage());

		}

		/* Suspend the buffer */
		this.dataBuffer.setSuspended(true);

		/* Make a runnable which eventually removes the suspension */
		Runnable suspendedRemover = new Runnable() {
			public void run() {
				synchronized (this) {
					/* Wait for a little while */
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						/* Do nothing */
					}

					/* Remove suspended status */
					DataBufferTest.this.dataBuffer.setSuspended(false);
				}
			}
		};

		/* Kick off a new suspend removal thread */
		new Thread(suspendedRemover, "Suspend Removal Thread").start();

		/* Make sure we can now get it from the buffer */
		try {
			assertEquals(this.dataBuffer.takeNextFromBuffer(), testString);
		} catch (DataBufferInterruptedException e) {
			fail(e.getMessage());

		}

		/* Make sure we are not suspended when we got this far */
		assertFalse(this.dataBuffer.isSuspended());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.common.DataBuffer#takeNextFromBuffer()}.
	 */
	public void testTakeNextFromBufferWhileSuspendedAndInterrupted() {
		String testString = "hello";

		/* Add something to the buffer */
		try {
			this.dataBuffer.addToBuffer(testString);
		} catch (DataBufferInterruptedException e1) {
			fail(e1.getMessage());

		}

		/* Suspend the buffer */
		this.dataBuffer.setSuspended(true);

		/* Make a runnable which eventually interrupts the buffer. */
		Runnable interrupter = new Runnable() {
			public void run() {
				synchronized (this) {
					/* Wait for a little while */
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						/* Do nothing */
					}

					/* Interrupt the buffer. */
					DataBufferTest.this.dataBuffer.setInterrupted(true);
				}
			}
		};

		/* Kick off a new interrupter thread */
		new Thread(interrupter, "Interrupting Thread").start();

		/* Make sure we do not get it from the buffer */
		try {
			this.dataBuffer.takeNextFromBuffer();
			fail("An exception should have been thrown, but wasn't.");

		} catch (DataBufferInterruptedException e) {
			/* This exception was expected -- do nothing. */

		}

		/* Make sure we are interrupted when we get this far */
		assertTrue(this.dataBuffer.isInterrupted());

	}

}
