/*
 *  @(#)NotifyOnCommandControllerPowerState.java
 *
 *  Created:	Oct 17, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.alien.autonomous;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.io.comm.ip.tcpclient.TCPClientCommunication;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractOffCommandControllerPowerState;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractOnCommandControllerPowerState;

/**
 * Implements functions of the ON power state for an
 * AutonomousCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class NotifyOnCommandControllerPowerState extends
		AbstractOnCommandControllerPowerState {

	private static Log logger = LogFactory
			.getLog(NotifyOnCommandControllerPowerState.class);

	/**
	 * The singleton instance for this class.
	 */
	private static final NotifyOnCommandControllerPowerState SINGLETON_INSTANCE = new NotifyOnCommandControllerPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static NotifyOnCommandControllerPowerState getInstance() {
		return NotifyOnCommandControllerPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private NotifyOnCommandControllerPowerState() {

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractOnCommandControllerPowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void suspend(PowerControllable pcObject) {
		logger.debug("Autonmous controller suspended");

		/* Call the abstract implementation first */
		super.suspend(pcObject);

		/* Cast to an AutonomousCommandController for specific action */
		NotifyController aCController = (NotifyController) pcObject;

		/* Change to suspended state */
		aCController
				.changeCommandControllerPowerState(NotifySuspendedCommandControllerPowerState
						.getInstance());

	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractOnCommandControllerPowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		logger.debug("Autonomous Controller turned off");
		
		/* Call the abstract implementation first */
		super.turnOff(pcObject, this.getClass());

		/* Cast to an AutonomousCommandController for specific action */
		NotifyController aCController = (NotifyController) pcObject;

		/* Change to off state */
		aCController
				.changeCommandControllerPowerState(NotifyOffCommandControllerPowerState
						.getInstance());
	}

}
