/*
 *  @(#)TCPClientDisconnectedCommunicationConnectionState.java
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
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.buffered.BufferedDisconnectedCommunicationConnectionState;

/**
 * This class represents the disconnected state of a TCPClientCommunication. It
 * implements the connect method, which launches threads for means of useful
 * communication with the current server connection.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPClientDisconnectedCommunicationConnectionState extends
		BufferedDisconnectedCommunicationConnectionState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(TCPClientConnectedCommunicationConnectionState.class);
	
	/**
	 * The singleton instance for this state.
	 */
	private static final TCPClientDisconnectedCommunicationConnectionState SINGLETON_INSTANCE 
		= new TCPClientDisconnectedCommunicationConnectionState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * tcpserver package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static TCPClientDisconnectedCommunicationConnectionState getInstance() {
		return TCPClientDisconnectedCommunicationConnectionState.SINGLETON_INSTANCE;

	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private TCPClientDisconnectedCommunicationConnectionState() {
		/* Do nothing special, no class variables are being used. */
	}

	/**
	 * Since when in the ON power state a server socket is listening for a
	 * client socket connection, this does not connect the client. Instead, it
	 * handles launching necessary threads to communicate with the client. <br>
	 * 
	 * Note that is only to be used with TCPServerCommunications, and will fail
	 * with a CastCastException if used with other Communications.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedDisconnectedCommunicationConnectionState#connect(org.rifidi.emulator.io.comm.Communication)
	 */
	@Override
	public void connect(Communication curComm) {
		logger.debug("TCPClient is connected");
		
		/* Invoke buffered handlers. */
		super.connect(curComm);

		/* Cast the passed Communication to a TCPServerCommmunication. */
		TCPClientCommunication curTCPComm = (TCPClientCommunication) curComm;

		/* Switch to the connected state */
		curTCPComm
				.changeConnectionState(TCPClientConnectedCommunicationConnectionState
						.getInstance());
	
		/* Make a new outgoing thread and start it */
		new Thread(
				new TCPClientCommunicationOutgoingMessageHandler(curTCPComm),
				"TCPClient Outgoing Message Handler").start();
		
		/* Make a new incoming thread and start it */
		new Thread(new TCPClientCommunicationIncomingMessageHandler(curTCPComm),
				"TCPServer Incoming Message Handler").start();

		/* Connect to server */

		/* Set connection control signal to true. */
		//curTCPComm.getConnectionControlSignal().setControlVariableValue(true);

	}

}
