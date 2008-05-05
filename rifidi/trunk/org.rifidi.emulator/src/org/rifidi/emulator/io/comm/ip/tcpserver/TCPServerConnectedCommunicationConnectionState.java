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

package org.rifidi.emulator.io.comm.ip.tcpserver;

import java.io.IOException;
import java.net.Socket;

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
public class TCPServerConnectedCommunicationConnectionState extends
		BufferedConnectedCommunicationConnectionState {

	/**
	 * The singleton instance for this state.
	 */
	private static final TCPServerConnectedCommunicationConnectionState SINGLETON_INSTANCE = new TCPServerConnectedCommunicationConnectionState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * tcpserver package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static TCPServerConnectedCommunicationConnectionState getInstance() {
		return TCPServerConnectedCommunicationConnectionState.SINGLETON_INSTANCE;

	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private TCPServerConnectedCommunicationConnectionState() {
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
		/* Invoke buffered handlers. */
		super.disconnect(curComm);

		/* Cast the passed Communication to a TCPServerCommmunication. */
		TCPServerCommunication curTCPComm = (TCPServerCommunication) curComm;

		/* Switch to disconnected state */
		curTCPComm
				.changeConnectionState(TCPServerDisconnectedCommunicationConnectionState
						.getInstance());

		/* Close the client socket. */
		Socket clientSocket = curTCPComm.getClientSocket();
		synchronized (clientSocket) {
			try {
				clientSocket.getInputStream().close();
				clientSocket.getOutputStream().close();
				clientSocket.close();

			} catch (IOException e) {
				/* Do nothing. */

			}

			/* Notify anything waiting on this socket */
			clientSocket.notifyAll();

		}

		/* Set connection control signal to false. */
		curTCPComm.getConnectionControlSignal().setControlVariableValue(false);

	}

}
