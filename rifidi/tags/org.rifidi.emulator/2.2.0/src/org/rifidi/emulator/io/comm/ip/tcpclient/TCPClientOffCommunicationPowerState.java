/*
 *  @(#)TCPServerOffCommunicationPowerState.java
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
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.extra.ExtraInformation;
import org.rifidi.emulator.extra.TCPExtraInformation;
import org.rifidi.emulator.io.comm.buffered.BufferedOffCommunicationPowerState;

/**
 * This class represents the OFF power state of a TCPServerCommunication. It
 * implements the turnOn method, which properly turns on the server for
 * listening.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPClientOffCommunicationPowerState extends
		BufferedOffCommunicationPowerState {
	
	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(TCPClientOffCommunicationPowerState.class);

	/**
	 * The singleton instance for this state.
	 */
	private static final TCPClientOffCommunicationPowerState SINGLETON_INSTANCE = new TCPClientOffCommunicationPowerState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * tcpserver package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static TCPClientOffCommunicationPowerState getInstance() {
		return TCPClientOffCommunicationPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private TCPClientOffCommunicationPowerState() {
		/* Do nothing special, no class variables are being used. */
	}

	/**
	 * Turns on the TCP server, allowing client connections.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOffCommunicationPowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOn(PowerControllable pcObjectClient) {
		logger.debug("TCPClient turned on without extrainfo");
	}

	/**
	 * Turns on the command with extra information set for the command. This
	 * will erase any previous commands stored.
	 */
	public void turnOn(PowerControllable pcObject, ExtraInformation extraInfo) {
		logger.debug("TCPClient turned on with extraninfo");
		
		/* Call the abstract implementation first */
		boolean error = false;
		super.turnOn(pcObject);



		TCPExtraInformation extraTCP = (TCPExtraInformation) extraInfo;
		TCPClientCommunication newCom = ((TCPClientCommunication) pcObject);

		logger.debug("extraTCP.getPort = " + extraTCP.getPort());
		
		newCom.setRemoteIPAddress(extraTCP.getAddress().getHostAddress());
		newCom.setRemotePort(extraTCP.getPort());

		try {
			logger.debug("TCPCLIENT IP address is: " + newCom.getRemoteIPAddress() + " PORT IS: " + newCom.getRemotePort());
			Socket socket = new Socket(newCom.getRemoteIPAddress(), newCom
					.getRemotePort());
			
			newCom.setClientSocket(socket);
		} catch (IOException e) {
			logger.error("Couldn't establish connection");
			error = true;
		}

		if(! error)
		{
			/* Change state to on */
			((TCPClientCommunication) pcObject)
					.changePowerState(TCPClientOnCommunicationPowerState.getInstance());
			((TCPClientCommunication) pcObject).getConnectionControlSignal().setControlVariableValue(true);
		}
	}

}
