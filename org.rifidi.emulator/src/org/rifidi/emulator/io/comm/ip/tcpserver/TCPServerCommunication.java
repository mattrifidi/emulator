/*
 *  @(#)TCPServerCommunication.java
 *
 *  Created:	Sep 25, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.ip.tcpserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.DataBuffer;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.io.comm.ip.IPCommunication;
import org.rifidi.emulator.io.comm.logFormatter.LogFormatter;
import org.rifidi.emulator.io.protocol.Protocol;

/**
 * An extension of the IPCommunication class which communicates using TCP
 * (Transmission Control Protocol) sockets. This class acts as a single-client
 * TCP server.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPServerCommunication extends IPCommunication {

	/**
	 * The client socket for this TCPServerCommunication.
	 */
	private Socket clientSocket;

	/**
	 * The server socket for this TCPServerCommunication.
	 */
	private ServerSocket serverSocket;

	/**
	 * The logger class for the console
	 */
	private Log consoleLogger = null;

	/**
	 * An abstract Stream reader class to handle reading from the socket
	 */
	@SuppressWarnings("unchecked")
	private Class abstractStreamReader = null;
	
	/**
	 * A class that handles how the console output should be formatted
	 */
	private LogFormatter logFormatter;

	/**
	 * Creates a new TCPServerCommunication which is initially off and
	 * disconnected, bound the passed local IP / port combination.
	 * 
	 * @param protocol
	 *            The protocol to use when sending and receiving data. This
	 *            protocol may be used to guarantee that only well-formed
	 *            messages enter the system. Note that this is independent of
	 *            the TCP.
	 * @param powerControlSignal
	 *            The control signal for this TCPServerCommunication to observe.
	 *            If <i>null</i> is passed, this will not observe a signal.
	 * @param connectionControlSignal
	 *            The ControlSignal that this will update when client connects
	 *            or disconnects.
	 * @param localIP
	 *            The IP address to bind the server to.
	 * @param localPort
	 *            The port to bind the server to.
	 * @param readerName
	 *            The name of the reader
	 * @param abstractStreamReader
	 *            a class to handle reading from the socket
	 * @param logFormatter
	 * 			  A class that handles how the console output should be formatted
	 */
	@SuppressWarnings("unchecked")
	public TCPServerCommunication(Protocol protocol,
			ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal, String localIP,
			int localPort, String readerName, Class abstractStreamReader, LogFormatter logFormatter) {
		/* Create a new IP Communication with TCP off and disconnect states */
		super(
				TCPServerOffCommunicationPowerState.getInstance(),
				TCPServerDisconnectedCommunicationConnectionState.getInstance(),
				protocol, powerControlSignal, connectionControlSignal, localIP,
				localPort, null, 0);
		this.abstractStreamReader = abstractStreamReader;
		this.logFormatter = logFormatter;

		this.consoleLogger = LogFactory.getLog("console." + readerName);
	}

	/**
	 * Allows the use of the AbstractCommunication method by TCPServer-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#changeConnectionState(org.rifidi.emulator.io.comm.CommunicationConnectionState)
	 */
	@Override
	protected void changeConnectionState(
			CommunicationConnectionState anotherConnectionState) {
		/* Invoke the super-class implementation. */
		super.changeConnectionState(anotherConnectionState);

	}

	/**
	 * Allows the use of the AbstractCommunication method by TCPServer-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#changePowerState(org.rifidi.emulator.io.comm.CommunicationPowerState)
	 */
	@Override
	protected void changePowerState(CommunicationPowerState anotherPowerState) {
		/* Invoke the super-class implementation. */
		super.changePowerState(anotherPowerState);

	}

	/**
	 * Returns the clientSocket.
	 * 
	 * @return Returns the clientSocket.
	 */
	Socket getClientSocket() {
		return this.clientSocket;
	}

	/**
	 * Allows the use of the AbstractCommunication method by TCPServer-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.abstract_.AbstractCommunication#getConnectionControlSignal()
	 */
	@Override
	protected ControlSignal<Boolean> getConnectionControlSignal() {
		/* Invoke the super-class implementation. */
		return super.getConnectionControlSignal();

	}

	/**
	 * Allows the use of the BufferedCommunication method by TCPServer-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedCommunication#getReceiveBuffer()
	 */
	@Override
	protected DataBuffer<byte[]> getReceiveBuffer() {
		return super.getReceiveBuffer();
	}

	/**
	 * Allows the use of the BufferedCommunication method by TCPServer-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedCommunication#getSendBuffer()
	 */
	@Override
	protected DataBuffer<byte[]> getSendBuffer() {
		return super.getSendBuffer();
	}

	/**
	 * Returns the serverSocket.
	 * 
	 * @return Returns the serverSocket.
	 */
	ServerSocket getServerSocket() {
		return this.serverSocket;
	}

	/**
	 * Sets clientSocket to the passed parameter, clientSocket.
	 * 
	 * @param clientSocket
	 *            The clientSocket to set.
	 */
	void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * Sets serverSocket to the passed parameter, serverSocket.
	 * 
	 * @param serverSocket
	 *            The serverSocket to set.
	 */
	void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * @return the consoleLogger
	 */
	public Log getConsoleLogger() {
		return consoleLogger;
	}

	/**
	 * @return the abstractStreamReader
	 */
	@SuppressWarnings("unchecked")
	public Class getAbstractStreamReader() {
		return abstractStreamReader;
	}

	public LogFormatter getLogFormatter() {
		return logFormatter;
	}

}
