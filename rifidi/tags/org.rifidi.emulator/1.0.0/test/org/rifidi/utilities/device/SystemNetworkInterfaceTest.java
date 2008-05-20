/*
 *  @(#)SystemNetworkInterfaceTest.java
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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.rifidi.utilities.device.SystemNetworkInterface;
import org.rifidi.utilities.device.SystemDevice.DeviceExistenceType;


import junit.framework.TestCase;

/**
 * A JUnit test class for SystemNetworkInterface.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class SystemNetworkInterfaceTest extends TestCase {

	/**
	 * The system network interface used in this test suite.
	 */
	private SystemNetworkInterface systemNetworkInterface;

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
	 * The network addresses to use for all tests.
	 */
	private static final String networkAddresses[] = { "127.0.0.1", "127.0.0.2" };

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		/* Create a network address map for the interface */
		Map<String, SystemNetworkInterface.NetworkAddressType> networkAddressMap = new TreeMap<String, SystemNetworkInterface.NetworkAddressType>();

		/* Add all networkAddresses */
		for (int i = 0; i < networkAddresses.length; i++) {
			networkAddressMap.put(networkAddresses[i],
					SystemNetworkInterface.NetworkAddressType.NEWLY_CREATED);
		}

		/* Create the network interface */
		this.systemNetworkInterface = new SystemNetworkInterface(
				defaultDeviceName, defaultDeviceAddress,
				DeviceExistenceType.NEWLY_CREATED, networkAddressMap);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		this.systemNetworkInterface.getNetworkAddresses().clear();
		this.systemNetworkInterface = null;
	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemNetworkInterface#toString()}.
	 */
	public void testToString() {
		String systemNetworkInterfaceString = this.systemNetworkInterface
				.toString();
		assertNotNull(systemNetworkInterfaceString);

		/* Make sure the toString contains the device name and address */
		assertTrue(systemNetworkInterfaceString.indexOf(defaultDeviceName) != -1);
		assertTrue(systemNetworkInterfaceString.indexOf(defaultDeviceAddress) != -1);

		/* Make sure toString contains each network address */
		Iterator<String> iter = this.systemNetworkInterface
				.getNetworkAddresses().keySet().iterator();
		while (iter.hasNext()) {
			String curAddress = iter.next();
			assertTrue(systemNetworkInterfaceString.indexOf(curAddress) != -1);
		}

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemNetworkInterface#SystemNetworkInterface(java.lang.String, java.lang.String, org.rifidi.utilities.device.SystemDevice.DeviceExistenceType, java.util.Map)}.
	 */
	public void testSystemNetworkInterface() {
		/* Make sure the constructor worked (called in setUp) */
		assertEquals(defaultDeviceAddress, this.systemNetworkInterface
				.getAddress());
		assertEquals(defaultDeviceName, this.systemNetworkInterface
				.getDeviceName());
		assertEquals(defaultDeviceExistenceType, this.systemNetworkInterface
				.getDeviceExistenceType());
		assertNotNull(this.systemNetworkInterface.getNetworkAddresses());

	}

	/**
	 * Test method for
	 * {@link org.rifidi.utilities.device.SystemNetworkInterface#getNetworkAddresses()}.
	 */
	public void testGetNetworkAddresses() {
		assertNotNull(this.systemNetworkInterface.getNetworkAddresses());

	}
}
