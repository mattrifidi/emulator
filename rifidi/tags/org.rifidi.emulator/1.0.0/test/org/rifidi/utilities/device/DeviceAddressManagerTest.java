/*
 *  @(#)DeviceAddressManagerTest.java
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

import org.rifidi.utilities.device.DeviceAddressManager;
import org.rifidi.utilities.device.SystemNetworkInterface;

import junit.framework.TestCase;

/**
 * A JUnit test class for DeviceAddressManager.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class DeviceAddressManagerTest extends TestCase {

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.DeviceAddressManager#getInstance()}.
	 */
	public void testGetInstance() {
		/* Make sure the instance exists */
		assertNotNull(DeviceAddressManager.getInstance());

		/* Ensure its singleton status */
		assertEquals(DeviceAddressManager.getInstance(), DeviceAddressManager
				.getInstance());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.DeviceAddressManager#canCreatelIPAddress()}.
	 */
	public void testCanCreateVirtualIPAddress() {
		/* TDO: Update this method as support for different platforms change */
		boolean supported = DeviceAddressManager.getInstance()
				.canCreatelIPAddress();

		/* Change the OS and see if the support matches what we expect */
		/* Windows */
		System.setProperty("os.name", "Windows XP");
		assertTrue(supported);

		/* Linux */
		System.setProperty("os.name", "Linux");
		assertFalse(supported);

		/* Other */
		System.setProperty("os.name", "FakeOS");
		assertFalse(supported);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.DeviceAddressManager#canCreateVirtualSerialPort()}.
	 */
	public void testCanCreateVirtualSerialPort() {
		/* TDO: Update this method as support for different platforms change */
		boolean supported = DeviceAddressManager.getInstance()
				.canCreateVirtualSerialPort();

		/* Change the OS and see if the support matches what we expect */
		/* Windows */
		System.setProperty("os.name", "Windows");
		assertFalse(supported);

		/* Linux */
		System.setProperty("os.name", "Linux");
		assertFalse(supported);

		/* Other */
		System.setProperty("os.name", "FakeOS");
		assertFalse(supported);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.DeviceAddressManager#createIPAddress(SystemNetworkInterface, java.lang.String, String)}.
	 */
	public void testCreateVirtualIPAddress() {
		/* TDO: Figure out how to do this in a very safe way */
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.DeviceAddressManager#createVirtualSerialPort(java.lang.String, int, int, int, int)}.
	 */
	public void testCreateVirtualSerialPort() {
		/* TDO: Figure out how to do this in a very safe way */
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.DeviceAddressManager#deleteIPAddress(SystemNetworkInterface, java.lang.String)}.
	 */
	public void testDeleteVirtualIPAddress() {
		/* TDO: Figure out how to do this in a very safe way */
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.DeviceAddressManager#deleteVirtualSerialPort(java.lang.String)}.
	 */
	public void testDeleteVirtualSerialPort() {
		/* TDO: Figure out how to do this in a very safe way */
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.DeviceAddressManager#getSystemDevices()}.
	 */
	public void testGetSystemDevices() {
		assertNotNull(DeviceAddressManager.getInstance().getSystemDevices());

		/*
		 * were updated.
		 */

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.DeviceAddressManager#refresh()}.
	 */
	public void testRefresh() {
		/*
		 * TDO: Examine whether or not this is truly testable. This would
		 * require a fairly involved testing procedure. Basically, a system
		 * device has to be added before the refresh and the refresh needs to
		 * pick it up. Thus, this really cannot be automated without bypassing
		 * the deviceAddressManager. (and thus needs to be considered VERY safe
		 * before doing)
		 */
		fail("Not yet implemented");
	}

}
