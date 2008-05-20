/*
 *  @(#)DeviceAddressManager.java
 *
 *  Created:	Jun 21, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.utilities.device;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.RXTXCommDriver;
import gnu.io.RXTXPort;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.rifidi.utilities.device.SystemNetworkInterface.NetworkAddressType;


/**
 * This is the Class which is responsible for the creation of Virtual Devices on
 * a low level and is dependent on the Operating System it is running on.
 * 
 * TODO: Modify these classes to make them easier to create IPs
 * 
 * @author Mike Graupner - mike@pramari.com
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class DeviceAddressManager {

	/**
	 * An enumeration of the variety of return codes that may be given when
	 * attempting to create, modify, or destroy a SystemDevice or network
	 * address using the DeviceAddressManager.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	public enum DeviceCreationReturnCode {
		/**
		 * Creation succeeded.
		 */
		SUCCESS,

		/**
		 * Creation failed (but under ideal conditions, should have succeeded).
		 */
		FAILURE,

		/**
		 * Creation failed due to an unsupported OS configuration.
		 */
		UNSUPPORTED_OS
	}

	/**
	 * The EmuLogger which this class uses.
	 */
	protected static Log logger = LogFactory
			.getLog(DeviceAddressManager.class);

	/**
	 * The singleton instance of the CommunicationManager.
	 */
	private static final DeviceAddressManager SINGLETON_INSTANCE = new DeviceAddressManager();

	/**
	 * Gets the singleton instance of DeviceAddressManager.
	 * 
	 * @return The singleton instance of DeviceAddressManager.
	 */
	public static DeviceAddressManager getInstance() {
		return SINGLETON_INSTANCE;
	}

	/**
	 * Are serial device supported?
	 */
	private boolean serialEnabled;

	/**
	 * The list of communication devices created by Rifidi.
	 */
	private Set<SystemDevice> systemDevices;

	/**
	 * The singleton constructor for the DeviceAddressManager class.
	 */
	private DeviceAddressManager() {
		super();
		this.systemDevices = new TreeSet<SystemDevice>();

		/* Check for RXTX working */
		try {
			RXTXCommDriver.nativeGetVersion();
			this.serialEnabled = true;

		} catch (UnsatisfiedLinkError e) {
			logger.warn("RXTX libraries missing -- "
					+ "serial device support disabled.");
			/* Error loading RXTX Library -- disable it! */
			this.serialEnabled = false;

		}

		/* Enumerate all existing devices */
		this.enumerateSystemDevices();

	}

	/**
	 * Returns whether or not it is possible to create a virtual serial port on
	 * the current operating system.
	 * 
	 * @return True if the program can create virtual serial ports under the
	 *         current operating system, false otherwise.
	 */
	public boolean canCreatelIPAddress() {
		boolean retVal = false;

		/* TDO: Be sure to update as necessary. */
		String operatingSystem = System.getProperty("os.name");

		/* Check for the user's OS and perform appropriate action. */
		if (operatingSystem.indexOf("Windows") != -1) {
			retVal = true;

		} else if (operatingSystem.indexOf("Linux") != -1) {
			retVal = false;

		} else {
			retVal = false;

		}

		return retVal;
	}

	/**
	 * Returns whether or not it is possible to create a virtual serial port on
	 * the current operating system.
	 * 
	 * @return True if the program can create virtual serial ports under the
	 *         current operating system, false otherwise.
	 */
	public boolean canCreateVirtualSerialPort() {
		boolean retVal = false;

		/* TDO: Be sure to update as necessary. */
		String operatingSystem = System.getProperty("os.name");

		/* Check for the user's OS and perform appropriate action. */
		if (operatingSystem.indexOf("Windows") != -1) {
			retVal = false;

		} else if (operatingSystem.indexOf("Linux") != -1) {
			retVal = false;

		} else {
			retVal = false;

		}

		return retVal;
	}

	/**
	 * Attempts to create a "virtual" IP address for the specified device
	 * address. This is a real IP address, but it is used only for the calling
	 * program.
	 * 
	 * @param anInterface
	 *            The address of the device to add the IP address to.
	 * @param networkAddress
	 *            The IP (network) address to add to the device.
	 * @param netMaskAddress
	 *            The netmask for the IP to add.
	 * @return True if network address addition is successful, false otherwise.
	 */
	public boolean createIPAddress(SystemNetworkInterface anInterface,
			String networkAddress, String netMaskAddress) {
		boolean retVal = false;

		/* Print out a warning. */
		/* TDO: Implement dynamic ip creation for each OS */
		String operatingSystem = System.getProperty("os.name");

		/* Check for the user's OS and perform appropriate action. */
		if (operatingSystem.indexOf("Windows") != -1) {
			/* Create an IP in Windows. */
			int ipCreatorReturnCode = IPCreator.getInstance()
					.createIP(anInterface.getDeviceName(), networkAddress,
							netMaskAddress);
			if (ipCreatorReturnCode == 0) {
				retVal = true;
				/* Add to the device set */
				anInterface.getNetworkAddresses().put(networkAddress,
						NetworkAddressType.NEWLY_CREATED);

			} else {
				retVal = false;

			}

		} else if (operatingSystem.indexOf("Linux") != -1) {
			/* Create an IP in Linux. */
			logger.warn("Dynamic virtual IP creation "
					+ "is currently not implemented for Linux.");
			logger.debug("Your operating system: " + operatingSystem);

		} else {
			/* Unsupported operating system. */
			logger.warn("Dynamic virtual IP creation "
					+ "is currently not implemented for your "
					+ "opeating system. (" + operatingSystem + ")");

		}

		return retVal;

	}

	/**
	 * Attempts to create a virtual serial port at the passed address using the
	 * passed parameters as port settings.
	 * 
	 * @param address
	 *            The address to use for this virtual port.
	 * @param baud
	 *            The baud setting for the port.
	 * @param dataBits
	 *            The data bits setting for the port.
	 * @param parity
	 *            The parity setting for the port.
	 * @param stopBits
	 *            The stopBits setting for the port.
	 * @return True if virtual port creation was successful, false otherwise.
	 */
	public boolean createVirtualSerialPort(String address, int baud,
			int dataBits, int parity, int stopBits) {
		boolean retVal = false;

		/* Print out a warning. */
		/* TDO: Implement dynamic virtual serial port creation for each OS */
		String operatingSystem = System.getProperty("os.name");

		/* Check for the user's OS and perform appropriate action. */
		if (operatingSystem.indexOf("Windows") != -1) {
			/* Create a serial port in Windows. */
			logger.warn("Dynamic virtual serial port creation "
					+ "is currently not implemented for Windows.");
			logger.debug("Your operating system: " + operatingSystem);

		} else if (operatingSystem.indexOf("Linux") != -1) {
			/* Create a serial port in Linux. */
			logger.warn("Dynamic virtual serial port creation "
					+ "is currently not implemented for Linux.");
			logger.debug("Your operating system: " + operatingSystem);

		} else {
			/* Unsupported operating system. */
			logger.warn("Dynamic virtual serial port creation "
					+ "is currently not implemented for your "
					+ "opeating system. (" + operatingSystem + ")");

		}

		return retVal;
	}

	/**
	 * Attempts to delete a virtual IP address from the system. This will only
	 * succeed if the IP address has been flagged as virtual (and thus,
	 * typically only by the program which called the corresponding create
	 * method).
	 * 
	 * @param anInterface
	 *            The address of the device to remove the IP address from.
	 * @param networkAddress
	 *            The IP (network) address to remove from the device.
	 * @return True if deletion was successful, false otherwise.
	 */
	public boolean deleteIPAddress(SystemNetworkInterface anInterface,
			String networkAddress) {
		boolean retVal = false;

		/* Print out a warning. */
		/* TDO: Implement dynamic ip deletion for each OS */
		String operatingSystem = System.getProperty("os.name");

		/* Check for the user's OS and perform appropriate action. */
		if (operatingSystem.indexOf("Windows") != -1) {
			/* TDO: Replace with individual removeIP when it is implemented */
			int ipRemoveRetCode = IPCreator.getInstance().resetConfiguration();
			if (ipRemoveRetCode == 0) {
				retVal = true;
				logger.info("All created IPs have been removed.");
				/* TDO: Don't forget to remove this call when above changes */
				/* TDO; Or when serial port creation is functional... */
				this.systemDevices.clear();
				this.enumerateSystemDevices();

			} else {
				retVal = false;
			}

		} else if (operatingSystem.indexOf("Linux") != -1) {
			/* Delete an IP in Linux. */
			logger.warn("Dynamic virtual IP deletion "
					+ "is currently not implemented for Linux.");
			logger.debug("Your operating system: " + operatingSystem);

		} else {
			/* Unsupported operating system. */
			logger.warn("Dynamic virtual IP deletion "
					+ "is currently not implemented for your "
					+ "opeating system. (" + operatingSystem + ")");

		}

		return retVal;

	}

	/**
	 * Attempts to delete a virtual serial port from the system. This will only
	 * succeed if the serial port has been flagged as virtual (and thus,
	 * typically only by the program which called the corresponding create
	 * method).
	 * 
	 * @param address
	 *            The address of the virtual port to delete.
	 * @return True if deletion was successful, false otherwise.
	 */
	public boolean deleteVirtualSerialPort(String address) {
		boolean retVal = false;

		/* Print out a warning. */
		/* TDO: Implement dynamic virtual serial port creation for each OS */
		String operatingSystem = System.getProperty("os.name");

		/* Check for the user's OS and perform appropriate action. */
		if (operatingSystem.indexOf("Windows") != -1) {
			/* Create a serial port in Windows. */
			logger.warn("Dynamic virtual serial port deletion "
					+ "is currently not implemented for Windows.");
			logger.debug("Your operating system: " + operatingSystem);

		} else if (operatingSystem.indexOf("Linux") != -1) {
			/* Create a serial port in Linux. */
			logger.warn("Dynamic virtual serial port deletion "
					+ "is currently not implemented for Linux.");
			logger.debug("Your operating system: " + operatingSystem);

		} else {
			/* Unsupported operating system. */
			logger.warn("Dynamic virtual serial port deletion "
					+ "is currently not implemented for your "
					+ "opeating system. (" + operatingSystem + ")");

		}

		return retVal;

	}

	/**
	 * This method enumerates all existing devices in the system.
	 */
	private void enumerateSystemDevices() {
		/* This is the set which will eventually become the new device set */
		Set<SystemDevice> enumeratedDevices = new TreeSet<SystemDevice>();

		/* Grab the previous devices and put them in an easy to use list form */
		List<SystemDevice> previouslyEnumeratedDevices = new ArrayList<SystemDevice>(
				this.systemDevices);

		/* Enumerate serial devices */
		if (this.serialEnabled) {
			Enumeration commPorts = CommPortIdentifier.getPortIdentifiers();
			while (commPorts.hasMoreElements()) {
				CommPortIdentifier curCommPort = (CommPortIdentifier) commPorts
						.nextElement();
				if (curCommPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					/* Make a serial port so that we can get baud settings, etc. */
					RXTXPort tempPort = null;

					try {
						/*
						 * Grab the serial port and get all of its current
						 * settings
						 */
						tempPort = new RXTXPort(curCommPort.getName());

						SystemSerialPort portToAdd = new SystemSerialPort(
								tempPort.getName(),
								tempPort.getName(),
								SystemDevice.DeviceExistenceType.PREVIOUSLY_EXISTING,
								tempPort.getBaudRate(), tempPort.getDataBits(),
								tempPort.getParity(), tempPort.getStopBits());

						/* Check and see if the device already exists */
						int existingDeviceIndex = previouslyEnumeratedDevices
								.indexOf(portToAdd);
						if (existingDeviceIndex != -1) {
							/* Grab that already existing device */
							SystemSerialPort existingSerialDevice = (SystemSerialPort) previouslyEnumeratedDevices
									.get(existingDeviceIndex);

							/* Copy the creation state over */
							portToAdd = new SystemSerialPort(
									tempPort.getName(), tempPort.getName(),
									existingSerialDevice
											.getDeviceExistenceType(), tempPort
											.getBaudRate(), tempPort
											.getDataBits(), tempPort
											.getParity(), tempPort
											.getStopBits());

						}

						tempPort.close();

						/* Add to enumerated devices */
						enumeratedDevices.add(portToAdd);

					} catch (PortInUseException e) {
						logger.info(e.getMessage() + " ("
								+ curCommPort.getName() + ")");

					}

				}

			}

		}

		/* Enumerate IP devices and all addresses. */
		/* Get all of the interfaces in the system and print them. */
		Enumeration<NetworkInterface> netInterfaces = null;

		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();

		} catch (SocketException e) {
			logger.warn(e.getMessage());

		}

		/* Check to see if we have interfaces to add to the device list. */
		if (netInterfaces != null) {
			/* Grab all of the interfaces and their network addresses */
			while (netInterfaces.hasMoreElements()) {
				/* Grab the current interface */
				NetworkInterface curInterface = netInterfaces.nextElement();

				/* Use a tree map for sorted IPs */
				Map<String, SystemNetworkInterface.NetworkAddressType> curInterfaceAddressMap;
				curInterfaceAddressMap = new TreeMap<String, SystemNetworkInterface.NetworkAddressType>();

				/* Get all of the addresses which this interface has */
				Enumeration<InetAddress> curInterfaceAddresses = curInterface
						.getInetAddresses();

				while (curInterfaceAddresses.hasMoreElements()) {
					/* Append the current address to map */
					InetAddress curAddress = curInterfaceAddresses
							.nextElement();
					curInterfaceAddressMap
							.put(
									curAddress.getHostAddress(),
									SystemNetworkInterface.NetworkAddressType.PREVIOUSLY_EXISTING);

				}

				/* Create the new device */
				SystemNetworkInterface interfaceToAdd = new SystemNetworkInterface(
						curInterface.getDisplayName(), curInterface.getName(),
						SystemDevice.DeviceExistenceType.PREVIOUSLY_EXISTING,
						curInterfaceAddressMap);

				/* Check and see if the device already exists */
				int existingDeviceIndex = previouslyEnumeratedDevices
						.indexOf(interfaceToAdd);
				if (existingDeviceIndex != -1) {
					/* Grab that already existing device */
					SystemNetworkInterface existingNetworkInterface = (SystemNetworkInterface) previouslyEnumeratedDevices
							.get(existingDeviceIndex);

					/* Copy over the existing device existence type */
					interfaceToAdd = null;
					interfaceToAdd = new SystemNetworkInterface(curInterface
							.getDisplayName(), curInterface.getName(),
							existingNetworkInterface.getDeviceExistenceType(),
							curInterfaceAddressMap);

					/* Copy over the address info */
					Iterator<String> addressIter = interfaceToAdd
							.getNetworkAddresses().keySet().iterator();
					/* Check each IP and see if it is previously existing */
					while (addressIter.hasNext()) {
						String curAddress = addressIter.next();
						SystemNetworkInterface.NetworkAddressType existingType = existingNetworkInterface
								.getNetworkAddresses().get(curAddress);
						/* Preserve the existing type */
						if (existingType != null) {
							interfaceToAdd.getNetworkAddresses().put(
									curAddress, existingType);

						}

					}

				}

				/* Add the new device to the existing devices list */
				enumeratedDevices.add(interfaceToAdd);

			}

		}

		/* Blow out the old system devices */
		this.systemDevices.clear();

		/* Assign the new system devices */
		this.systemDevices = enumeratedDevices;

	}

	/**
	 * Returns the systemDevices.
	 * 
	 * @return Returns the systemDevices.
	 */
	public Set<SystemDevice> getSystemDevices() {
		/* Give a copy back */
		return new TreeSet<SystemDevice>(this.systemDevices);
	}

	/**
	 * This method may be called from an external source as a request to refresh
	 * the device listing which the DeviceAddressManager holds.
	 */
	public void refresh() {
		/* Enumerate the System Devices again */
		this.enumerateSystemDevices();
	}

}
