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

package org.rifidi.emulator.io.comm.ip.tcpclient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState;

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
public class TCPClientOnCommunicationPowerState extends
		BufferedOnCommunicationPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(TCPClientOnCommunicationPowerState.class);

	/**
	 * The singleton instance for this state.
	 */
	private static final TCPClientOnCommunicationPowerState SINGLETON_INSTANCE = new TCPClientOnCommunicationPowerState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * tcpserver package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static TCPClientOnCommunicationPowerState getInstance() {
		return TCPClientOnCommunicationPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private TCPClientOnCommunicationPowerState() {
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
		logger.debug("TCPClient suspended");

		/* Invoke buffered handlers. */
		super.suspend(pcObject);

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
		TCPClientCommunication curTCPComm = (TCPClientCommunication) pcObject;

		/* Change to suspended state */
		curTCPComm.changePowerState(TCPClientSuspendedCommunicationPowerState
				.getInstance());
	}

	/**
	 * Turns off the server.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOff(PowerControllable pcObject) {
		logger.debug("Turning off TCPClient...");

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
		TCPClientCommunication curTCPComm = (TCPClientCommunication) pcObject;

		/*
		 * Change to the off state before we disconnect so that disconnect does
		 * not call this method
		 */
		curTCPComm.changePowerState(TCPClientOffCommunicationPowerState
				.getInstance());

		// if connected, disconnect
		if (curTCPComm.getConnectionControlSignal().getControlVariableValue() == true) {
			curTCPComm.disconnect();
		}

	}

}
