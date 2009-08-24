/*
 *  @(#)TCPServerOnCommunicationPowerState.java
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

import java.io.IOException;

import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class represents the ON power state of a TCPServerCommunication. It
 * implements the turnOff and suspend method, which properly turn off the server
 * and suspend the server operation, respectively.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPServerOnCommunicationPowerState extends
		BufferedOnCommunicationPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(TCPServerOnCommunicationPowerState.class);

	/**
	 * The singleton instance for this state.
	 */
	private static final TCPServerOnCommunicationPowerState SINGLETON_INSTANCE = new TCPServerOnCommunicationPowerState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * tcpserver package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static TCPServerOnCommunicationPowerState getInstance() {
		return TCPServerOnCommunicationPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private TCPServerOnCommunicationPowerState() {
		/* Do nothing special, no class variables are being used. */
	}

	/**
	 * Suspends the server. <br>
	 * 
	 * Currently does nothing to the actual server socket (that is, connections
	 * will still be accepted), but the underlying BufferedCommunication state
	 * takes care of suspending the flow of information.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void suspend(PowerControllable pcObject) {
		/* Invoke buffered handlers. */
		super.suspend(pcObject);

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
		TCPServerCommunication curTCPComm = (TCPServerCommunication) pcObject;

		/* TODO: Decide whether or not to suspend the actual socket somehow */

		/* Change to suspended state */
		curTCPComm.changePowerState(TCPServerSuspendedCommunicationPowerState
				.getInstance());

	}

	/**
	 * Turns off the server.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject) {

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
		TCPServerCommunication curTCPComm = (TCPServerCommunication) pcObject;	

		/* Change to the off state */
		curTCPComm.changePowerState(TCPServerOffCommunicationPowerState
				.getInstance());

		/* Disconnect */
		curTCPComm.disconnect();

		/* Invoke buffered handlers (not first, since disconnect will change). */
		super.turnOff(pcObject);

		/* Close the server socket */
		if (curTCPComm.getServerSocket() != null) {
			try {
				curTCPComm.getServerSocket().close();

			} catch (IOException e) {
				/* Nothing else should be touching this... log it as a debug */
				logger.error(e.getMessage());

			}

		}

	}

}
