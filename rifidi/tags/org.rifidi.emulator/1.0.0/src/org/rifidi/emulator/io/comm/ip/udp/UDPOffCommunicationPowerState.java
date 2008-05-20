/*
 *  UDPOffCommunicationPowerState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.ip.udp;

import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.buffered.BufferedOffCommunicationPowerState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.io.IOException;

/**
 * This represents the "off" state for the communication module.  It can be
 * turned on.  
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 *
 */
public class UDPOffCommunicationPowerState extends
		BufferedOffCommunicationPowerState {

	
	/**
	 * Message logger
	 */
	private static Log logger =
		 LogFactory.getLog(UDPCommunicationIncomingMessageHandler.class);
	
	/**
	 * The singleton instance for this state.
	 */
	private static final UDPOffCommunicationPowerState SINGLETON_INSTANCE 
		= new UDPOffCommunicationPowerState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * UDP package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static UDPOffCommunicationPowerState getInstance() {
		return UDPOffCommunicationPowerState.SINGLETON_INSTANCE;
	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private UDPOffCommunicationPowerState() {
		/* Do nothing special, no class variables are being used. */
	}
	
	/** 
	 * Calls the method in the superclass and kicks off the threads making 
	 */
	@Override
	public void turnOn(PowerControllable pcObject) {
		logger.debug("Turning on...");
		
		/* Invoke buffered handlers. */
		super.turnOn(pcObject);
		
		/* Cast the passed PowerControllable to a UDPCommmunication. */
		UDPCommunication curUDPComm = (UDPCommunication)pcObject;

		/* Get the Datagram object from the communication object. */
		DatagramSocket tempSocket = curUDPComm.getDatagramSocket();

		/* bind the udp communication object to the local address
		 * this is the from address in a message */		
		try {
			tempSocket.bind(new InetSocketAddress(curUDPComm.getLocalIPAddress(),
			curUDPComm.getLocalPort()));
		} catch(IOException e) {
				logger.warn(e.getMessage());
		}
		
		/* If we are only doing output then we do not need an incoming
		 * message handler for udp */
		if( !curUDPComm.isOutputOnly() ) {
			new Thread(new UDPCommunicationIncomingMessageHandler(curUDPComm), 
					"Incoming Message Handler").start();
		}
		
		/* We will always have an outgoing UDP message handler to send
		 * udp messages to the remote address */
		new Thread(new UDPCommunicationOutgoingMessageHandler(curUDPComm),
				"Outgoing Message Handler").start();
		
		/* Change to the on state */
		curUDPComm.changePowerState(UDPOnCommunicationPowerState
				.getInstance());
		
	}

	
}
