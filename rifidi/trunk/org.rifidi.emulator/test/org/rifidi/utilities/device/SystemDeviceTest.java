/*
 *  @(#)SystemDeviceTest.java
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

import org.rifidi.utilities.device.SystemDevice;
import org.rifidi.utilities.device.SystemDevice.DeviceExistenceType;

import junit.framework.TestCase;

/**
 * A JUnit test class for SystemDevice.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class SystemDeviceTest extends TestCase {

	/**
	 * The system device used in this test suite.
	 */
	private SystemDevice systemDevice;

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
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.systemDevice = new SystemDevice(defaultDeviceName,
				defaultDeviceAddress, defaultDeviceExistenceType) {
			/* Do no other customization */
		};

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this.systemDevice = null;
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemDevice#SystemDevice(java.lang.String, java.lang.String, org.rifidi.utilities.device.SystemDevice.DeviceExistenceType)}.
	 */
	public void testSystemDevice() {
		/* Make a new system device */
		String deviceName = "aDevice";
		String deviceAddress = "anAddress";
		this.systemDevice = new SystemDevice(deviceName, deviceAddress,
				SystemDevice.DeviceExistenceType.NEWLY_CREATED) {
			/* Do no other customization */
		};

		/* Make sure the object was successfully created */
		assertNotNull(this.systemDevice);

		/* Make sure device name and address were set */
		assertEquals(deviceName, this.systemDevice.getDeviceName());
		assertEquals(deviceAddress, this.systemDevice.getAddress());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemDevice#getAddress()}.
	 */
	public void testGetAddress() {
		assertEquals(defaultDeviceAddress, this.systemDevice.getAddress());
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemDevice#toString()}.
	 */
	public void testToString() {
		String systemDeviceString = this.systemDevice.toString();
		assertNotNull(systemDeviceString);
		/* Make sure the toString contains the device name and address */
		assertTrue(systemDeviceString.indexOf(defaultDeviceName) != -1);
		assertTrue(systemDeviceString.indexOf(defaultDeviceAddress) != -1);
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemDevice#equals(java.lang.Object)}.
	 */
	public void testEqualsObject() {
		/* Make an equivalent device */
		SystemDevice anotherEquivalentDevice = new SystemDevice(
				defaultDeviceName, defaultDeviceAddress,
				defaultDeviceExistenceType) {
			/* Do no other customization */
		};

		/* Make a different device */
		SystemDevice nonEquivalentDevice = new SystemDevice("ZZZZZ", "ZZZZZ",
				defaultDeviceExistenceType) {
			/* Do no other customization */
		};

		/* Make sure the two equivalent things return true for equals */
		assertTrue(this.systemDevice.equals(anotherEquivalentDevice));
		/* Make sure the two different things return false for equals */
		assertFalse(this.systemDevice.equals(nonEquivalentDevice));

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemDevice#compareTo(org.rifidi.utilities.device.SystemDevice)}.
	 */
	public void testCompareTo() {
		/* Make an equivalent device */
		SystemDevice anotherEquivalentDevice = new SystemDevice(
				defaultDeviceName, defaultDeviceAddress,
				defaultDeviceExistenceType) {
			/* Do no other customization */
		};

		/* Make a different device */
		SystemDevice nonEquivalentDevice = new SystemDevice("ZZZZZ", "ZZZZZ",
				defaultDeviceExistenceType) {
			/* Do no other customization */
		};

		/* Make sure the two equivalent things compare as equal */
		assertTrue(this.systemDevice.compareTo(anotherEquivalentDevice) == 0);
		/* Make sure the two different things compare as differnent */
		assertFalse(this.systemDevice.compareTo(nonEquivalentDevice) == 0);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemDevice#getDeviceExistenceType()}.
	 */
	public void testGetDeviceExistenceType() {
		assertEquals(this.systemDevice.getDeviceExistenceType(),
				defaultDeviceExistenceType);
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemDevice#getDeviceName()}.
	 */
	public void testGetDeviceName() {
		assertEquals(this.systemDevice.getDeviceName(), defaultDeviceName);
	}

}
