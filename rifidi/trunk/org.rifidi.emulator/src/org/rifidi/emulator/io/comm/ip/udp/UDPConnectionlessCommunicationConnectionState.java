/*
 *  UDPConnectionlessCommunicationConnectionState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.ip.udp;

import org.rifidi.emulator.io.comm.buffered.BufferedConnectionlessCommunicationConnectionState;

/**
 * The connectionless communication state for UDP.  
 * 
 * @author Matthew Dean
 */
public class UDPConnectionlessCommunicationConnectionState extends
		BufferedConnectionlessCommunicationConnectionState {

	/**
	 * The singleton instance for this state.
	 */
	private static final UDPConnectionlessCommunicationConnectionState SINGLETON_INSTANCE = 
		new UDPConnectionlessCommunicationConnectionState();
	
	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * udp package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static UDPConnectionlessCommunicationConnectionState getInstance() {
		return UDPConnectionlessCommunicationConnectionState.SINGLETON_INSTANCE;

	}
	
	/*
	 *  Nothing happens in the constructor 
	 */
	public UDPConnectionlessCommunicationConnectionState() {
	}

}
