/*
 *  @(#)SystemNetworkInterface.java
 *
 *  Created:	Aug 8, 2006
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
import java.util.Map.Entry;

/**
 * A class which represents a network interface in a system. Contains basic
 * information which pertains to network interfaces.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class SystemNetworkInterface extends SystemDevice {

	/**
	 * Enumeration for the different types of network addresses.
	 */
	public enum NetworkAddressType {
		/**
		 * Addresses which existed previous to this program (that is to say,
		 * this program did not create this address).
		 */
		PREVIOUSLY_EXISTING {
			public String toString() {
				return "Previously Existing";
			}
		},

		/**
		 * Addresses which were created by a special call and would not
		 * typically be in the system if not for a special program (created by
		 * DeviceAddressManager).
		 */
		NEWLY_CREATED {
			public String toString() {
				return "Newly Created";
			}
		}

	}

	/**
	 * The list of network addresses which this network interface currently
	 * holds. The key is the network address and the value is either existing or
	 * virtual.
	 */
	private Map<String, NetworkAddressType> networkAddresses;

	/**
	 * Creates a basic represenation of a network interface. This information
	 * contains the network interface's list of network addresses and its alias.
	 * 
	 * @param deviceName
	 *            The name of the system device.
	 * @param anAddress
	 *            The address which this device sits at.
	 * @param deviceExistanceType
	 *            The existence type of this device (typically previously
	 *            existing or newly created)
	 * @param networkAddresses
	 *            The map of network addresses which this network interface
	 *            currently holds. The key is the network address and the value
	 *            is either existing or virtual.
	 */
	public SystemNetworkInterface(String deviceName, String anAddress,
			DeviceExistenceType deviceExistanceType,
			Map<String, NetworkAddressType> networkAddresses) {
		super(deviceName, anAddress, deviceExistanceType);
		this.networkAddresses = networkAddresses;
	}

	/**
	 * Returns the networkAddresses.
	 * 
	 * @return Returns the networkAddresses.
	 */
	public Map<String, NetworkAddressType> getNetworkAddresses() {
		return this.networkAddresses;
	}

	/**
	 * Gives back a representation of the SystemNetworkInterface in String
	 * format.
	 * 
	 * @return A representation of the SystemNetworkInterface in String format.
	 * @see org.rifidi.utilities.device.SystemDevice#toString()
	 */
	@Override
	public String toString() {
		/* Grab the interface name in addition to standard info. */
		String retString = super.toString();

		/* List all addresses for this interface */
		Map<String, NetworkAddressType> tempMap = this.getNetworkAddresses();
		Iterator<Entry<String, NetworkAddressType>> iter = tempMap.entrySet()
				.iterator();

		int count = 0;
		while (iter.hasNext()) {
			count++;
			Entry<String, NetworkAddressType> curEntry = iter.next();
			retString += "\n NetworkAddress " + count + ": "
					+ curEntry.getKey() + " | " + curEntry.getValue();
		}

		return retString;

	}

}
