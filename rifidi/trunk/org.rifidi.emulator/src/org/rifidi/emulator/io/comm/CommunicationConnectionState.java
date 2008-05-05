/*
 *  @(#)CommunicationConnectionState.java
 *
 *  Created:	Sep 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm;

/**
 * An interface for a CommunicationConnectionState, which is used to dictate the
 * behavior of a Communication object's isConnected and disconnect methods.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public interface CommunicationConnectionState {

	/**
	 * Attempt to connect the current Communication. The behavior of this method
	 * is dictated by the implementation.
	 * 
	 * @param curComm
	 *            The Communication object which holds this state.
	 */
	public void connect(Communication curComm);

	/**
	 * Attempt to disconnect the current Communication. The behavior of this
	 * method is dictated by the implementation.
	 * 
	 * @param curComm
	 *            The Communication object which holds this state.
	 */
	public void disconnect(Communication curComm);

	/**
	 * Return whether or not the Communication has an active connection.
	 * 
	 * @param curComm
	 *            The Communication object which holds this state.
	 * @return True if the state implementation models Connected, false
	 *         otherwise.
	 */
	public boolean isConnected(Communication curComm);

	/**
	 * Receives an array of bytes. The implementation of the
	 * CommunicationConnectionState determines the behavior of this method.
	 * 
	 * @param callingComm
	 *            The Communication object which called this state object's
	 *            method.
	 * 
	 * @return An array of bytes or null, depending on the implementation.
	 * @throws CommunicationException
	 *             If the receive is interrupted.
	 */
	public byte[] receiveBytes(Communication callingComm)
			throws CommunicationException;

	/**
	 * Sends an array of bytes. The implementation of the
	 * CommunicationConnectionState determines the behavior of this method.
	 * 
	 * @param callingComm
	 *            The Communication object which called this state object's
	 *            method.
	 * @param data
	 *            The array of bytes to send.
	 * @throws CommunicationException
	 *             If the receive is interrupted.
	 */
	public void sendBytes(byte[] data, Communication callingComm)
			throws CommunicationException;

}
