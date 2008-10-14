/*
 *  @(#)Communication.java
 *
 *  Created:	May 03, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm;

/**
 * This class acts as a portal to the outside of the program. This provides
 * general send and receive commands for a specific type of communication
 * interface, such as a serial port or multicast IP.
 * 
 * @author Mike Graupner - mike@pramari.com
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */

public interface Communication {

	/**
	 * Connects the current Communication. If the current Communication is
	 * connected or connectionless, this method call does nothing.
	 */
	public void connect();

	/**
	 * Disconnects the current Communication. If the current Communication is
	 * disconnected or connectionless, this method call does nothing.
	 */
	public void disconnect();

	/**
	 * Returns whether or not the current Communication has an active
	 * connection. Connectionless Communication implementations (such as UDP)
	 * should always return false for this method.
	 * 
	 * @return True if the current Communication has an active connection, false
	 *         otherwise.
	 */
	public boolean isConnected();

	/**
	 * Receives an array of bytes from this Communication. The implementation of
	 * this method shall block until either a non-empty array can be returned,
	 * or the connection is interrupted.
	 * 
	 * @return An array of bytes or null if the connection is interrupted.
	 * @throws CommunicationException
	 *             If the receive is interrupted.
	 */
	public byte[] receiveBytes() throws CommunicationException;

	/**
	 * Sends an array of bytes using this Communication.
	 * 
	 * @param data
	 *            The array of bytes to send.
	 * @throws CommunicationException
	 *             If the send is interrupted.
	 */
	public void sendBytes(byte[] data) throws CommunicationException;

}
