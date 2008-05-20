/*
 *  UDPSuspendedCommunicationPowerState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.ip.udp;

import java.net.DatagramSocket;

import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.buffered.BufferedSuspendedCommunicationPowerState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




/**
 * This class represents the SUSPENDED power state of a UDPCommunication.
 * It implements the turnOff and resume method, which properly turn off the
 * server and resume server operation, respectively.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 *
 */
public class UDPSuspendedCommunicationPowerState extends
		BufferedSuspendedCommunicationPowerState {
	
	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(UDPOnCommunicationPowerState.class);

	/**
	 * The singleton instance for this state.
	 */
	private static final UDPSuspendedCommunicationPowerState SINGLETON_INSTANCE = new UDPSuspendedCommunicationPowerState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * UDP package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static UDPSuspendedCommunicationPowerState getInstance() {
		return UDPSuspendedCommunicationPowerState.SINGLETON_INSTANCE;
	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private UDPSuspendedCommunicationPowerState() {
		/* Do nothing special, no class variables are being used. */
	}
	
	/**
	 * Resumes server operation.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedSuspendedCommunicationPowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void resume(PowerControllable pcObject) {
		logger.debug("Resuming...");

		/* Invoke buffered handlers. */
		super.resume(pcObject);

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
		UDPCommunication curUDPComm = (UDPCommunication) pcObject;

		/* Change to the on state */
		curUDPComm.changePowerState(UDPOnCommunicationPowerState.getInstance());

	}

	/**
	 * Turns off the server.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedSuspendedCommunicationPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		logger.debug("Turned off by " + callingClass);

		/* Invoke buffered handlers. */
		super.turnOff(pcObject, this.getClass());

		/* Cast the passed PowerControllable to a UDPCommmunication. */
		UDPCommunication curUDPComm = (UDPCommunication) pcObject;

		/* Disconnect and close the underlying socket */
		this.disconnect(curUDPComm);

		/* Change to the off state */
		curUDPComm.changePowerState(UDPOffCommunicationPowerState.getInstance());

	}
	
	/**
	 * Disconnects the socket. Doing this will also kill any threads that are
	 * running that are using this socket. In UDP sockets close is tightly bound
	 * to a disconnect and thus the socket is also closed as well.
	 */
	public void disconnect(UDPCommunication dconnudpComm) {
		
		DatagramSocket dgs = dconnudpComm.getDatagramSocket();
		boolean outputTest = dconnudpComm.isOutputOnly();
		
		if (outputTest) {
			/* If it is output only then the disconnect works fine */
			dgs.disconnect();
		}
		else {
			/* TODO Need to resolve bug in UDPCommunication where
			 * the disconnect hangs when there is both an incoming
			 * and outgoing message handler
			 */
			logger.warn("UDPCommunication was closed but not disconnected" +
					" due to UDPIncoming Handler Bug - results may vary");
		}
		
		/* close the datagram socket */
		dgs.close();
	}

}
