/*
 *  @(#)BufferedOnCommunicationPowerState.java
 *
 *  Created:	Sep 28, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.buffered;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.abstract_.AbstractOnCommunicationPowerState;

/**
 * Implements functions of the ON power state for a BufferedCommunication.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class BufferedOnCommunicationPowerState extends
		AbstractOnCommunicationPowerState {
	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(BufferedOnCommunicationPowerState.class);

	
	/**
	 * Suspends the read/write buffers. <br>
	 * 
	 * Note that this does NOT switch the state to suspended, since there is no
	 * concrete implementation known.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	public void suspend(PowerControllable pcObject) {
		/* Cast the pcObject to a BufferedCommunication object */
		BufferedCommunication curComm = (BufferedCommunication) pcObject;

		/* Suspend the send/receive buffers */
		curComm.getReceiveBuffer().setSuspended(true);
		curComm.getSendBuffer().setSuspended(true);

	}

	/**
	 * Turns off (interrupts) the read/write buffers. <br>
	 * 
	 * Note that this does NOT switch the state to off, since there is no
	 * concrete implementation known.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOff(PowerControllable pcObject) {
		
		
		/* Cast the pcObject to a BufferedCommunication object */
		BufferedCommunication curComm = (BufferedCommunication) pcObject;

		/* Interrupt the send/receive buffers */
		curComm.getReceiveBuffer().setInterrupted(true);
		curComm.getSendBuffer().setInterrupted(true);

	}

}
