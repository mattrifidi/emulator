/*
 *  @(#)TCPClientCommunication.java
 *
 *  Created:	Sep 25, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.ip.tcpclient;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.DataBuffer;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.io.comm.ip.IPCommunication;
import org.rifidi.emulator.io.comm.logFormatter.LogFormatter;
import org.rifidi.emulator.io.comm.streamreader.AbstractStreamReader;
import org.rifidi.emulator.io.protocol.Protocol;

/**
 * An extension of the IPCommunication class which communicates using TCP
 * (Transmission Control Protocol) sockets. This class acts as a client.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPClientCommunication extends IPCommunication {

	/**
	 * Message logger
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory
			.getLog(TCPClientCommunication.class);
	
	/**
	 * The client socket for this TCPServerCommunication.
	 */
	private Socket clientSocket;

	/**
	 * The logger class for the console
	 */
	private Log consoleLogger = null;
	
	private Class readerClass;
	
	/**
	 * A class that handles how the console output should be formatted
	 */
	private LogFormatter logFormatter;

	/**
	 * Creates a new TCPClientCommunication which is initially off and
	 * disconnected, bound the passed local IP / port combination.
	 * 
	 * @param protocol
	 *            The protocol to use when sending data. This protocol may be
	 *            used to guarantee that only well-formed messages enter the
	 *            system. Note that this is independent of the TCP.
	 * @param powerControlSignal
	 *            The control signal for this TCPClientCommunication to observe.
	 *            If <i>null</i> is passed, this will not observe a signal.
	 * @param connectionControlSignal
	 *            The ControlSignal that this will update when client connects
	 *            or disconnects.
	 * @param localIP
	 *            The IP address to bind the client to.
	 * @param localPort
	 *            The port to bind the , AbstractStreamReader readerClass client to.
	 */
	public TCPClientCommunication(Protocol protocol,
			ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal, String localIP,
			int localPort, String readerName, Class readerclass, LogFormatter logFormatter) {
		/* Create a new IP Communication with TCP off and disconnect states */
		super(
				TCPClientOffCommunicationPowerState.getInstance(),
				TCPClientDisconnectedCommunicationConnectionState.getInstance(),
				protocol, powerControlSignal, connectionControlSignal, localIP,
				localPort, null, 0);
		
		this.clientSocket = null;
		this.consoleLogger = LogFactory.getLog("console." + readerName);
		this.readerClass=readerclass;
		this.logFormatter = logFormatter;
	}

	/**
	 * Allows the use of the AbstractCommunication method by TCPClient-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.abstract_.
	 * AbstractCommunication#changeConnectionState
	 * (org.rifidi.emulator.io.comm.CommunicationConnectionState)
	 */
	@Override
	protected void changeConnectionState(
			CommunicationConnectionState anotherConnectionState) {
		/* Invoke the super-class implementation. */
		super.changeConnectionState(anotherConnectionState);
	}

	/**
	 * Allows the use of the AbstractCommunication method by TCPClient-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.abstract_.
	 * AbstractCommunication#changePowerState
	 * (org.rifidi.emulator.io.comm.CommunicationPowerState)
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
	 * Allows the use of the AbstractCommunication method by TCPClient-package
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
	 * Allows the use of the BufferedCommunication method by TCPClient-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedCommunication#getReceiveBuffer()
	 */
	@Override
	protected DataBuffer<byte[]> getReceiveBuffer() {
		return super.getReceiveBuffer();
	}

	/**
	 * Allows the use of the BufferedCommunication method by TCPClient-package
	 * classes.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedCommunication#getSendBuffer()
	 */
	@Override
	protected DataBuffer<byte[]> getSendBuffer() {
		return super.getSendBuffer();
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
	 * @return the consoleLogger
	 */
	public Log getConsoleLogger() {
		return consoleLogger;
	}

	public Class getAbstractStreamReader() {
		return readerClass;
	}

	public LogFormatter getLogFormatter() {
		return logFormatter;
	}
}
