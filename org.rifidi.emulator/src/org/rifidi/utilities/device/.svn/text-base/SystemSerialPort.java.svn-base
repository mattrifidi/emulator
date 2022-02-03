/*
 *  @(#)SystemSerialPort.java
 *
 *  Created:	Aug 8, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

/**
 * 
 */
package org.rifidi.utilities.device;

/**
 * A class which represents a serial port in system. Contains basic information
 * which pertains to serial ports.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class SystemSerialPort extends SystemDevice {

	/**
	 * The baud this serial port runs at.
	 */
	private int baud;

	/**
	 * The number of data bits sent per cycle.
	 */
	private int dataBits;

	/**
	 * The parity bits sent per cycle.
	 */
	private int parity;

	/**
	 * The stop bits sent per cycle.
	 */
	private int stopBits;

	/**
	 * A basic constructor for a serial device.
	 * 
	 * @param deviceName
	 *            The name of the system device.
	 * @param anAddress
	 *            The address which this device sits at.
	 * @param deviceExistenceType
	 *            The existence type of this device (typically previously
	 *            existing or newly created)
	 * @param baud
	 *            The baud this serial port runs at.
	 * @param dataBits
	 *            The number of data bits sent per cycle.
	 * @param parity
	 *            The parity bits sent per cycle.
	 * @param stopBits
	 *            The stop bits sent per cycle.
	 */
	public SystemSerialPort(String deviceName, String anAddress,
			DeviceExistenceType deviceExistenceType, int baud, int dataBits,
			int parity, int stopBits) {
		super(deviceName, anAddress, deviceExistenceType);
		this.baud = baud;
		this.dataBits = dataBits;
		this.parity = parity;
		this.stopBits = stopBits;

	}

	/**
	 * Returns the baud.
	 * 
	 * @return Returns the baud.
	 */
	public int getBaud() {
		return this.baud;
	}

	/**
	 * Returns the dataBits.
	 * 
	 * @return Returns the dataBits.
	 */
	public int getDataBits() {
		return this.dataBits;
	}

	/**
	 * Returns the parity.
	 * 
	 * @return Returns the parity.
	 */
	public int getParity() {
		return this.parity;
	}

	/**
	 * Returns the stopBits.
	 * 
	 * @return Returns the stopBits.
	 */
	public int getStopBits() {
		return this.stopBits;
	}

}
