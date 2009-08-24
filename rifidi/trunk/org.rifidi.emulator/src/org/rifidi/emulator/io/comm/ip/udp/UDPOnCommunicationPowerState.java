/*
 *  UDPOnCommunicationPowerState.java
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
import org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Matthew Dean
 *
 */
public class UDPOnCommunicationPowerState extends
	BufferedOnCommunicationPowerState {
	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(UDPOnCommunicationPowerState.class);

	/**
	 * The singleton instance for this state.
	 */
	private static final UDPOnCommunicationPowerState SINGLETON_INSTANCE 
		= new UDPOnCommunicationPowerState();

	/**
	 * A default-access method for getting the singleton instance of this state
	 * class. The default access is designed so that only other members of the
	 * UDP package may use this class.
	 * 
	 * @return The singleton instance of this state class.
	 */
	static UDPOnCommunicationPowerState getInstance() {
		return UDPOnCommunicationPowerState.SINGLETON_INSTANCE;
	}

	/**
	 * A private constructor used for making the singleton instance of this
	 * class.
	 */
	private UDPOnCommunicationPowerState() {
		/* Do nothing special, no class variables are being used. */
	}
	
	/**
	 * Suspends the server. <br>
	 * 
	 * Currently does nothing to the actual server socket (that is, connections
	 * will still be accepted), but the underlying BufferedCommunication state
	 * takes care of suspending the flow of information.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void suspend(PowerControllable pcObject) {
		logger.debug("Suspend...");

		/* Invoke buffered handlers. */
		super.suspend(pcObject);

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
		UDPCommunication curUDPComm = (UDPCommunication) pcObject;

		/* Change to suspended state */
		curUDPComm.changePowerState(UDPSuspendedCommunicationPowerState.getInstance());
	}
	
	/**
	 * Turns off the UDP communication object.
	 * 
	 * @see org.rifidi.emulator.io.comm.buffered.BufferedOnCommunicationPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOff(PowerControllable pcObject) {

		/* Invoke buffered handlers. */
		super.turnOff(pcObject);

		/* Cast the passed PowerControllable to a TCPServerCommmunication. */
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
