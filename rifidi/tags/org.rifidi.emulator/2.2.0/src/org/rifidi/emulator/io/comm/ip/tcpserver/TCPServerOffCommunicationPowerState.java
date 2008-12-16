/*
 *  @(#)TCPServerOffCommunicationPowerState.java
 *
 *  Created:	Sep 29, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.ip.tcpserver;

import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.buffered.BufferedOffCommunicationPowerState;

/**
 * This class represents the OFF power state of a TCPServerCommunication. It
 * implements the turnOn method, which properly turns on the server for
 * listening.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPServerOffCommunicationPowerState extends
		BufferedOffCommunicationPowerState {

	/**
	 * The singleton instance for this state.
	 */
	private static final TCPServerOffCommunicationPowerState SINGLETON_INSTANCE = new TCPServerOffCommunicationPowerState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * tcpserver package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static TCPServerOffCommunicationPowerState getInstance() {
		return TCPServerOffCommunicationPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private TCPServerOffCommunicationPowerState() {
		/* Do nothing special, no class variables are being used. */
	}

	/**
	 * Turns on the TCP server, allowing client connections.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOffCommunicationPowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOn(PowerControllable pcObject) {
		/* Invoke buffered handlers. */
		super.turnOn(pcObject);

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
		TCPServerCommunication curTCPComm = (TCPServerCommunication) pcObject;

		/* Launch the server thread */
		new Thread(new TCPServerCommunicationIncomingConnectionHandler(
				curTCPComm), "TCPServer Incoming Connection Handler").start();

		/* Change state to on */
		curTCPComm.changePowerState(TCPServerOnCommunicationPowerState
				.getInstance());

	}

}
