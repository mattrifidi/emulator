/*
 *  SerialConnectionlessCommunicationConnectionState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.serial;

import org.rifidi.emulator.io.comm.buffered.BufferedConnectionlessCommunicationConnectionState;

/**
 * The single Connectionless state that the Serial module will use.  
 *
 * @author Matthew Dean
 * @since  <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class SerialConnectionlessCommunicationConnectionState extends
		BufferedConnectionlessCommunicationConnectionState {

	/**
	 * The singleton instance for this state.
	 */
	private static final SerialConnectionlessCommunicationConnectionState SINGLETON_INSTANCE = 
		new SerialConnectionlessCommunicationConnectionState();
	
	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * Serial package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	public static SerialConnectionlessCommunicationConnectionState getInstance() {
		return SerialConnectionlessCommunicationConnectionState.SINGLETON_INSTANCE;

	}
	
	/*
	 *  Nothing happens in the constructor 
	 */
	public SerialConnectionlessCommunicationConnectionState() {
	}

}
