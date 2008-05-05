/*
 *  @(#)SystemSerialPortTest.java
 *
 *  Created:	Aug 31, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.utilities.device;

import org.rifidi.utilities.device.SystemSerialPort;
import org.rifidi.utilities.device.SystemDevice.DeviceExistenceType;

import junit.framework.TestCase;

/**
 * A JUnit test class for SystemSerialPort.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class SystemSerialPortTest extends TestCase {

	/**
	 * The system serial port used in this test suite.
	 */
	private SystemSerialPort systemSerialPort;

	/**
	 * The default device name for all tests.
	 */
	private static final String defaultDeviceName = "testName";

	/**
	 * The default device address for all tests.
	 */
	private static final String defaultDeviceAddress = "testAddress";

	/**
	 * The default device existence type for all tests.
	 */
	private static final DeviceExistenceType defaultDeviceExistenceType = DeviceExistenceType.NEWLY_CREATED;

	/**
	 * The default baud value for all tests.
	 */
	private static final int defaultBaud = 9600;

	/**
	 * The default data bits value for all tests.
	 */
	private static final int defaultDataBits = 8;

	/**
	 * The default parity value for all tests.
	 */
	private static final int defaultParity = 0;

	/**
	 * The default stop bits value for all tests.
	 */
	private static final int defaultStopBits = 1;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		/* Create a new system serial port */
		this.systemSerialPort = new SystemSerialPort(defaultDeviceName,
				defaultDeviceAddress, defaultDeviceExistenceType, defaultBaud,
				defaultDataBits, defaultParity, defaultStopBits);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this.systemSerialPort = null;
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemSerialPort#SystemSerialPort(java.lang.String, java.lang.String, org.rifidi.utilities.device.SystemDevice.DeviceExistenceType, int, int, int, int)}.
	 */
	public void testSystemSerialPort() {
		assertNotNull(this.systemSerialPort);
		/* Make sure device name and address were assigned correctly */
		assertEquals(defaultDeviceName, this.systemSerialPort.getDeviceName());
		assertEquals(defaultDeviceAddress, this.systemSerialPort.getAddress());
		assertEquals(defaultDeviceExistenceType, this.systemSerialPort
				.getDeviceExistenceType());

		/* Make sure components were assigned correctly */
		assertEquals(defaultBaud, this.systemSerialPort.getBaud());
		assertEquals(defaultDataBits, this.systemSerialPort.getDataBits());
		assertEquals(defaultParity, this.systemSerialPort.getParity());
		assertEquals(defaultStopBits, this.systemSerialPort.getStopBits());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemSerialPort#getBaud()}.
	 */
	public void testGetBaud() {
		assertEquals(defaultBaud, this.systemSerialPort.getBaud());
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemSerialPort#getDataBits()}.
	 */
	public void testGetDataBits() {
		assertEquals(defaultDataBits, this.systemSerialPort.getDataBits());
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemSerialPort#getParity()}.
	 */
	public void testGetParity() {
		assertEquals(defaultParity, this.systemSerialPort.getParity());
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemSerialPort#getStopBits()}.
	 */
	public void testGetStopBits() {
		assertEquals(defaultStopBits, this.systemSerialPort.getStopBits());
	}

}
