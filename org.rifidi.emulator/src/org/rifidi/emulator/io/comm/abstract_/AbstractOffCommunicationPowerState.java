/*
 *  @(#)AbstractOffCommunicationPowerState.java
 *
 *  Created:	Sep 28, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.abstract_;

import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.reader.module.abstract_.AbstractOffPowerState;

/**
 * Implements functions of the OFF power state for an AbstractCommunication.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractOffCommunicationPowerState extends
		AbstractOffPowerState implements CommunicationPowerState {

	/**
	 * Deny the receive attempt, throwing an exception.
	 * 
	 * @throws CommunicationException
	 *             Since this is off, throws an exception.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationPowerState#receiveBytes(org.rifidi.emulator.io.comm.Communication)
	 */
	public byte[] receiveBytes(Communication callingComm)
			throws CommunicationException {
		throw new CommunicationException("Cannot receive - off.");

	}

	/**
	 * Deny the send attempt, throwing an exception.
	 * 
	 * @throws CommunicationException
	 *             Since this is off, throws an exception.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationPowerState#sendBytes(byte[],
	 *      org.rifidi.emulator.io.comm.Communication)
	 */
	public void sendBytes(byte[] data, Communication callingComm)
			throws CommunicationException {
		throw new CommunicationException("Cannot send - off.");

	}

}
