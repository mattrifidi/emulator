/*
 *  @(#)TCPServerSuspendedCommunicationPowerState.java
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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.buffered.BufferedSuspendedCommunicationPowerState;

/**
 * This class represents the SUSPENDED power state of a TCPServerCommunication.
 * It implements the turnOff and resume method, which properly turn off the
 * server and resume server operation, respectively.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPClientSuspendedCommunicationPowerState extends
		BufferedSuspendedCommunicationPowerState {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(TCPClientSuspendedCommunicationPowerState.class);

	/**
	 * The singleton instance for this state.
	 */
	private static final TCPClientSuspendedCommunicationPowerState SINGLETON_INSTANCE = new TCPClientSuspendedCommunicationPowerState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * tcpserver package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static TCPClientSuspendedCommunicationPowerState getInstance() {
		return TCPClientSuspendedCommunicationPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private TCPClientSuspendedCommunicationPowerState() {
		/* Do nothing special, no class variables are being used. */
	}

	/**
	 * Resumes server operation.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedSuspendedCommunicationPowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void resume(PowerControllable pcObject) {
		logger.debug("TCPClient resumed");
		
		/* Invoke buffered handlers. */
		super.resume(pcObject);

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
		TCPClientCommunication curTCPComm = (TCPClientCommunication) pcObject;

		/* Change to the on state */
		curTCPComm.changePowerState(TCPClientOnCommunicationPowerState
				.getInstance());

	}

	/**
	 * Turns off the server.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedSuspendedCommunicationPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {

		logger.debug("TCPClient turned off by " + callingClass);

		/* Invoke buffered handlers. */
		super.turnOff(pcObject, this.getClass());

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
		TCPClientCommunication curTCPComm = (TCPClientCommunication) pcObject;

		/* Close the server socket */
		try {
			curTCPComm.getClientSocket().close();
		} catch (IOException e) {
			/* Do nothing */

		}

		/* Change to the off state */
		curTCPComm.changePowerState(TCPClientOffCommunicationPowerState
				.getInstance());

	}

}
