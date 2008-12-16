/*
 *  @(#)IPCommunication.java
 *
 *  Created:	Jun 9, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.ip;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.io.comm.buffered.BufferedCommunication;
import org.rifidi.emulator.io.protocol.Protocol;

/**
 * An extension of the Communication class which adds Internet Protocol (IP)
 * specific fields with corresponding accessors. Additional constructors are
 * defined for these variables.
 * 
 * @author Mike Graupner - mike@pramari.com
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class IPCommunication extends BufferedCommunication {

	/**
	 * The remote IP address to connect to.
	 */
	private String remoteIPAddress;

	/**
	 * The remote to connect to.
	 */
	private int remotePort;

	/**
	 * The local IP address to bind to.
	 */
	private String localIPAddress;

	/**
	 * The local port to bind to.
	 */
	private int localPort;
	
	/**
	 * The name of the reader.
	 */
	private String readerName;

	/**
	 * This creates an IPCommunication object which initially is set to be bound
	 * to the passed local IP/port and set to connect to the passed remote
	 * IP/port.
	 * 
	 * @param initialPowerState
	 *            The desired initial power state of the IPCommunication.
	 * @param initialConnectionState
	 *            The desired initial connection state of the IPCommunication.
	 * @param protocol
	 *            The protocol that this IP Communication will be using. Note
	 *            that this is a protocol in addition to whatever underlying
	 *            protocol the IP Communication is using (e.g., UDP or TCP).<br>
	 *            For example, a telnet protocol may be attached on top to
	 *            handle telnet negotiations.
	 * @param powerControlSignal
	 *            The control signal for this IPCommunication to observe. If
	 *            <i>null</i> is passed, this will not observe a signal.
	 * @param connectionControlSignal
	 *            The ControlSignal that this will update when client connects
	 *            or disconnects.
	 * @param localIP
	 *            The local IP address to bind to.
	 * @param localPort
	 *            The local port to bind to.
	 * @param remoteIP
	 *            The remote IP address to connect to.
	 * @param remotePort
	 *            The remote port to connect to.
	 */
	public IPCommunication(CommunicationPowerState initialPowerState,
			CommunicationConnectionState initialConnectionState,
			Protocol protocol, ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal, String localIP,
			int localPort, String remoteIP, int remotePort) {
		/* Invoke super constructor */
		super(initialPowerState, initialConnectionState, protocol,
				powerControlSignal, connectionControlSignal);

		/* Set instance variables */
		this.localIPAddress = localIP;
		this.localPort = localPort;
		this.remoteIPAddress = remoteIP;
		this.remotePort = remotePort;
	}

	/**
	 * Returns the localIPAddress.
	 * 
	 * @return Returns the localIPAddress.
	 */
	public String getLocalIPAddress() {
		return this.localIPAddress;
	}

	/**
	 * Sets localIPAddress to the passed parameter, localIPAddress.
	 * 
	 * @param localIPAddress
	 *            The localIPAddress to set.
	 */
	public void setLocalIPAddress(String localIPAddress) {
		this.localIPAddress = localIPAddress;
	}

	/**
	 * Returns the localPort.
	 * 
	 * @return Returns the localPort.
	 */
	public int getLocalPort() {
		return this.localPort;
	}

	/**
	 * Sets localPort to the passed parameter, localPort.
	 * 
	 * @param localPort
	 *            The localPort to set.
	 */
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	/**
	 * Returns the remoteIPAddress.
	 * 
	 * @return Returns the remoteIPAddress.
	 */
	public String getRemoteIPAddress() {
		return this.remoteIPAddress;
	}

	/**
	 * Sets remoteIPAddress to the passed parameter, remoteIPAddress.
	 * 
	 * @param remoteIPAddress
	 *            The remoteIPAddress to set.
	 */
	public void setRemoteIPAddress(String remoteIPAddress) {
		this.remoteIPAddress = remoteIPAddress;
	}

	/**
	 * Returns the remotePort.
	 * 
	 * @return Returns the remotePort.
	 */
	public int getRemotePort() {
		return this.remotePort;
	}

	/**
	 * Sets remotePort to the passed parameter, remotePort.
	 * 
	 * @param remotePort
	 *            The remotePort to set.
	 */
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}
	
	/**
	 * @return the readerName
	 */
	public String getReaderName() {
		return readerName;
	}

}
