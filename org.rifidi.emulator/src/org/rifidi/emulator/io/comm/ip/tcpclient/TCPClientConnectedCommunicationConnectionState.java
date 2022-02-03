/*
 *  @(#)TCPServerConnectedCommunicationConnectionState.java
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
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.buffered.BufferedConnectedCommunicationConnectionState;

/**
 * This class represents the connected state of a TCPServerCommunication. It
 * implements the disconnect method, which properly disconnects the current
 * client connection.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPClientConnectedCommunicationConnectionState extends
		BufferedConnectedCommunicationConnectionState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(TCPClientConnectedCommunicationConnectionState.class);

	/**
	 * The singleton instance for this state.
	 */
	private static final TCPClientConnectedCommunicationConnectionState SINGLETON_INSTANCE = new TCPClientConnectedCommunicationConnectionState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * tcpserver package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static TCPClientConnectedCommunicationConnectionState getInstance() {
		return TCPClientConnectedCommunicationConnectionState.SINGLETON_INSTANCE;

	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private TCPClientConnectedCommunicationConnectionState() {
		/* Do nothing special, no class variables are being used. */
	}

	/**
	 * Disconnects the client socket of the passed Communication. Note that is
	 * only to be used with TCPServerCommunications, and will fail with a
	 * CastCastException if used with other Communications.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#disconnect(org.rifidi.emulator.io.comm.Communication)
	 */
	@Override
	public void disconnect(Communication curComm) {

		/* Cast the passed Communication to a TCPServerCommmunication. */
		TCPClientCommunication curTCPComm = (TCPClientCommunication) curComm;
		
		logger.debug("Disconnecting TCPClient...");
		logger.debug("Connection Signal is " + curTCPComm.getConnectionControlSignal().getControlVariableValue());
		logger.debug("Power State is " + curTCPComm.getPowerState());
		
		/* Switch to disconnected state */
		curTCPComm
				.changeConnectionState(TCPClientDisconnectedCommunicationConnectionState
						.getInstance());
		
		/* Invoke buffered handlers. */
		super.disconnect(curComm);
		
		/* Close the client socket */
		if (curTCPComm.getClientSocket() != null) {
			try {
				curTCPComm.getClientSocket().close();

			} catch (IOException e) {
				e.printStackTrace();

			}

		}
		
		//set power signal to false
		curTCPComm.getConnectionControlSignal().setControlVariableValue(false);

		//turn the power off it is not already off
		if(!curTCPComm.getPowerState().equals(TCPClientOffCommunicationPowerState.getInstance())){
			curTCPComm.turnOff();
		}

		logger.debug("Connection Signal is " + curTCPComm.getConnectionControlSignal().getControlVariableValue());
		logger.debug("Power State is " + curTCPComm.getPowerState());
		logger.debug("TCPClient is disconnected");
		

	}

}
