/*
 *  @(#)TCPServerCommunicationIncomingConnectionHandler.java
 *
 *  Created:	Sep 28, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.ip.tcpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerState;


/**
 * A runnable which monitors for incoming TCP connections and creates
 * appropriate handlers as needed.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPServerCommunicationIncomingConnectionHandler implements
		Runnable {

	/* Current error state and message */
	private boolean hasNoError = false;
	private String errorMessage = null;

	/* The server socket this thread creates and monitors */
	private ServerSocket curServerSocket = null;
	
	
	/**
	 * The logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(TCPServerCommunicationIncomingConnectionHandler.class);

	/**
	 * The TCPServerCommunication this object is bound to.
	 */
	private TCPServerCommunication hostCommunication;

	/**
	 * Creates a TCPServerCommunicationIncomingConnectionHandler which is bound
	 * to the passed TCPServerCommunication. This constructor has default access
	 * so that only tcpserver-package classes can create these.
	 * 
	 * @param hostCommunication
	 *            The TCPServerCommunication this object is bound to.
	 */
	TCPServerCommunicationIncomingConnectionHandler(
			TCPServerCommunication hostCommunication) {
		this.hostCommunication = hostCommunication;

	}

	/**
	 * The main logic of this monitor, which creates a new server socket bound
	 * to the current local IP / port combination and listens for clients to
	 * connect until explictly unbound.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.debug("Attempting to create TCPServer...");

		/* Create the ServerSocket and check to see 
		 * if the server socket was made successfully */
		
		hasNoError = bindServerSocket();
		
		if(hasNoError) {
			logger.debug("No error creating server, proceeding.");

			/* A string which will be used multiple times in log statements. */
			String serverString = "["
					+ curServerSocket.getInetAddress().getHostAddress() + ":"
					+ curServerSocket.getLocalPort() + "]";

			/* Keep running while the server socket is open. */
			while (!curServerSocket.isClosed() && hasNoError) {

				/* Try to accept a connection */
				Socket curClientSocket = null;
				try {
					logger.debug(serverString + " - Waiting for client...");
					curClientSocket = curServerSocket.accept();
					curClientSocket.setKeepAlive(true);
					//TODO Maybe we should do a disconnect 
				} catch (IOException e) {
					logger.debug(serverString + " - Server accept interrupted.");
					// Retry, because no Socket was created
					continue;
				}
				/* set the new Socket */
				this.hostCommunication.setClientSocket(curClientSocket);
				
				/* Check to see if a client successfully connected */
				if (curClientSocket != null) {
					final String connectionMessage = serverString
							+ " - Client connected ("
							+ curClientSocket.getInetAddress().getHostAddress()
							+ ":" + curClientSocket.getPort() + ")";

					/* Log the connection */
					logger.info(connectionMessage);

					/* Call connect on the current communication. */
					this.hostCommunication.connect();

					/* Wait until the client socket is disconnected */
					synchronized (curClientSocket) {
						while (!curClientSocket.isClosed()
								&& curClientSocket.isConnected()) {
							try {
								/* Close the ServerSocket so that he couldn't accept 
								 * more than one connections a time (SYN/SYN ACK - Problem)
								 */
								curServerSocket.close();
								/* wait until the client connection is closed */
								curClientSocket.wait();
								/* bind the ServerSocket again, so that 
								 * new Connections can be made
								 */
								PowerState powerState = this.hostCommunication.getPowerState();
								logger.debug("Comm power state is " + powerState);
								if(powerState != TCPServerOffCommunicationPowerState.getInstance()){
								hasNoError = bindServerSocket();
								}
							}
							catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} 
							catch (InterruptedException e) {
								logger.debug("Interrupted Exception happened.");
							}
						}

						/* Done waiting */

					}

				}

			} /* while (!serverSocket.isClosed())... */

			/* Server socket closed */

		} else {
			/* Log the error message. */
			logger.error(errorMessage);
			/* Force a shutdown of the component. */
			this.hostCommunication.turnOff(this.getClass());
		}
		/* All done running. */
	}
	
	private boolean bindServerSocket()
	{
		/* Make a new server socket with basic settings and bind. */
		try {
			curServerSocket = new ServerSocket();
			curServerSocket.setSoTimeout(0);
			curServerSocket.bind(
					new InetSocketAddress(this.hostCommunication
							.getLocalIPAddress(), this.hostCommunication
							.getLocalPort()), 1);

			/* Set the server socket in the host communication */
			this.hostCommunication.setServerSocket(curServerSocket);

		} catch (IllegalArgumentException e1) {
			/* Record that an error occured */
			errorMessage = e1.getMessage();
			return false;

		} catch (IOException e2) {
			/* Record that an error occured */
			errorMessage = e2.getMessage();
			return false;
		}
		return true;
	}
}
