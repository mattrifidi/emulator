/*
 *  @(#)AbstractConnectionlessCommunicationConnectionState.java
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
 * An abstract implementation of a connectionless connection state.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractConnectionlessCommunicationConnectionState
		implements CommunicationConnectionState {

	/**
	 * Since this state is considered connectionless, there is no concept of a
	 * connection, and thus there can never be any connection to connect. Does
	 * nothing.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#connect(org.rifidi.emulator.io.comm.Communication)
	 */
	public void connect(Communication curComm) {
		/* Cannot disconnect, do nothing. */

	}

	/**
	 * Since this state is considered connectionless, there is no concept of a
	 * connection, and thus there can never be any connection to disconnect.
	 * Does nothing.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#disconnect(org.rifidi.emulator.io.comm.Communication)
	 */
	public void disconnect(Communication curComm) {
		/* Cannot disconnect, do nothing. */

	}

	/**
	 * Since this state is considered connectionless, there is no concept of a
	 * connection, and thus this cannot be connected. Returns false for every
	 * call.
	 * 
	 * @see org.rifidi.emulator.io.comm.CommunicationConnectionState#isConnected(org.rifidi.emulator.io.comm.Communication)
	 */
	public boolean isConnected(Communication curComm) {
		return false;
	}

}
