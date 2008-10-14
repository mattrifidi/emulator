/*
 *  @(#)CommunicationPowerState.java
 *
 *  Created:	Sep 21, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm;

import org.rifidi.emulator.common.PowerState;

/**
 * An extension of the PowerState interface for Communication-specific methods
 * which depend on the current power state.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public interface CommunicationPowerState extends PowerState {

	/**
	 * Receives an array of bytes. The implementation of the
	 * CommunicationPowerState determines the behavior of this method.
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
	 * CommunicationPowerState determines the behavior of this method.
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
