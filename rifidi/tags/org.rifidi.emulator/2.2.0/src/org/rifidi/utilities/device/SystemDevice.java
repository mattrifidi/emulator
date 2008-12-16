/*
 *  @(#)SystemDevice.java
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

/**
 * A class which represents a device in the system. A device must have an
 * address and can either be real (previously existing) or virtual (newly
 * created by the calling program).
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class SystemDevice implements Comparable<SystemDevice> {

	/**
	 * Enumeration for the different types of device existence.
	 */
	public enum DeviceExistenceType {
		/**
		 * Devices which existed previous to this program (that is to say, this
		 * program did not create this address).
		 */
		PREVIOUSLY_EXISTING {
			public String toString() {
				return "Previously Existing";
			}
		},

		/**
		 * Devices which were created by a special call and would not typically
		 * be in the system if not for a special program (created by
		 * DeviceAddressManager).
		 */
		NEWLY_CREATED {
			public String toString() {
				return "Newly Created";
			}
		}

	}

	/**
	 * The address of the device.
	 */
	private String address;

	/**
	 * The name of the device.
	 */
	private String deviceName;

	/**
	 * Whether or not this device is a virtual device (created by calling
	 * program).
	 */
	private DeviceExistenceType deviceExistenceType;

	/**
	 * Creates a basic system device.
	 * 
	 * @param deviceName
	 *            The name of the system device.
	 * @param anAddress
	 *            The address which this device sits at.
	 * @param deviceExistenceType
	 *            The existence type of this device (typically previously
	 *            existing or newly created)
	 * 
	 */
	public SystemDevice(String deviceName, String anAddress,
			DeviceExistenceType deviceExistenceType) {
		this.deviceName = deviceName;
		this.address = anAddress;
		this.deviceExistenceType = deviceExistenceType;

	}

	/**
	 * Returns the address.
	 * 
	 * @return Returns the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * Gives back a representation of the SystemDevice in String format.
	 * 
	 * @return A representation of the SystemDevice in String format.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String retVal = "Device: " + this.getDeviceName() + " | Address: "
				+ this.getAddress() + " | " + this.getDeviceExistenceType();
		return retVal;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean retVal = false;
		try {
			if (this.getAddress().equals(((SystemDevice) obj).getAddress())) {
				retVal = true;
			}
		} catch (ClassCastException e) {
			/* TDO: Decide if catching the exception is the best option */
			e.printStackTrace();
		}

		return retVal;
	}

	/**
	 * Compares two SystemDevices by address.
	 * 
	 * @param aDevice
	 *            The device to comare to.
	 * @return The same codes as String.compareTo for the addresses.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SystemDevice aDevice) {
		return this.getAddress().compareTo(aDevice.getAddress());
	}

	/**
	 * Returns the deviceExistanceType.
	 * 
	 * @return Returns the deviceExistanceType.
	 */
	public DeviceExistenceType getDeviceExistenceType() {
		return this.deviceExistenceType;
	}

	/**
	 * Returns the deviceName.
	 * 
	 * @return Returns the deviceName.
	 */
	public String getDeviceName() {
		return this.deviceName;
	}

}
