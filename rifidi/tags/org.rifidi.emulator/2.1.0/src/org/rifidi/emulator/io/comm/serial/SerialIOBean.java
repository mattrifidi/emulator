/*
 *  SerialIOBean.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.serial;

/**
 * This class stores all of the objects needed for a Serial port.  
 *
 * @author Matthew Dean
 * @since  <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class SerialIOBean {
	
	/**
	 * The baud to hold transactions at.
	 */
	private int baudRate;

	/**
	 * The number of data bits to use per transaction.
	 */
	private int dataBits;

	/**
	 * The type of flow control to use.
	 */
	private int flowControl;

	/**
	 * The maximum message size that will be sent / received by this
	 * Communication.
	 */
	private int maxMessageLength;

	/**
	 * The number of parity bits to use per transaction.
	 */
	private int parityBits;

	/**
	 * The number of stop bits to use per transaction.
	 */
	private int stopBits;

	/**
	 * The serial port to communicate through.
	 */
	private String thePort;

	/**
	 * The amount of time (in seconds) that this port will wait for
	 * sending/receiving before a transaction has been considered timed out.
	 */
	private int timeoutValue;
	
	/**
	 * Constructor for SerialIOBean class.  
	 * 
	 * @param port
	 *            The port to communicate through.
	 * @param baud
	 *            The baud to hold transactions at.
	 * @param data
	 *            The number of data bits to use per transaction.
	 * @param parity
	 *            The number of parity bits to use per transaction.
	 * @param stop
	 *            The number of stop bits to use per transaction.
	 * @param flowControl
	 *            The type of flow control to use.
	 * @param maxMessageLength
	 *            The maximum message size that will be sent / received.
	 */
	public SerialIOBean(String port, int baud, int data, int parity, int stop, 
			int flowControl, int maxMessageLength) {
		this.thePort = port;
		this.baudRate = baud;
		this.dataBits = data;
		this.parityBits = parity;
		this.stopBits = stop;
		this.flowControl = flowControl;
		this.maxMessageLength = maxMessageLength;
	}
	
	/**
	 * The baud to hold transactions at.
	 * 
	 * @return the baudRate
	 */
	public int getBaudRate() {
		return baudRate;
	}

	/**
	 * The number of data bits to use per transaction.
	 *
	 * @return the dataBits
	 */
	public int getDataBits() {
		return dataBits;
	}

	/**
	 * The type of flow control to use.
	 * 
	 * @return the flowControl
	 */
	public int getFlowControl() {
		return flowControl;
	}

	/**
	 * The maximum message size that will be sent / received by this
	 * Communication.
	 * 
	 * @return the maxMessageLength
	 */
	public int getMaxMessageLength() {
		return maxMessageLength;
	}

	/**
	 * The number of parity bits to use per transaction.
	 * 
	 * @return the parityBits
	 */
	public int getParityBits() {
		return parityBits;
	}

	/**
	 * The number of stop bits to use per transaction.
	 * 
	 * @return the stopBits
	 */
	public int getStopBits() {
		return stopBits;
	}

	/**
	 * The serial port to communicate through.
	 * 
	 * @return the thePort
	 */
	public String getThePort() {
		return thePort;
	}

	/**
	 * The amount of time (in seconds) that this port will wait for
	 * sending/receiving before a transaction has been considered timed out.
	 * 
	 * @return the timeoutValue
	 */
	public int getTimeoutValue() {
		return timeoutValue;
	}
}
