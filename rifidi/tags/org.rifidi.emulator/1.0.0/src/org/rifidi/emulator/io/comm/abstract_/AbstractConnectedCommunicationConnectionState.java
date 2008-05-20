/*
 *  @(#)AbstractConnectedCommunicationConnectionState.java
 *
 *  Created:	Sep 27, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.abstract_;

import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;

/**
 * An abstract implementation of a connected connection state.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractConnectedCommunicationConnectionState implements
		CommunicationConnectionState {

	/**
	 * This is already connected, so this method does nothing.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#connect(org.rifidi.emulator.io.comm.Communication)
	 */
	public void connect(Communication curComm) {
		/* Invalid transition -- do nothing. */

	}

	/**
	 * Since this is a connected state, always return true.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#isConnected(org.rifidi.emulator.io.comm.Communication)
	 */
	public boolean isConnected(Communication curComm) {
		return true;

	}

}
