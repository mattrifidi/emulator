/*
 *  TCPExtraInformation.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.extra;

import java.net.InetAddress;

/**
 * 
 * 
 * @author matt
 */
public class TCPExtraInformation implements ExtraInformation {
	
	/**
	 * 
	 */
	private InetAddress address;
	
	private int port;
	
	/**
	 * This class holds an address which will reach one of the Communication 
	 * objects.  
	 * 
	 * @param address
	 */
	public TCPExtraInformation(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	/**
	 * Gets the address you wish to connect to.
	 * 
	 * @return
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Sets the addresss you wish to connect to.  
	 * 
	 * @param address
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	
}
