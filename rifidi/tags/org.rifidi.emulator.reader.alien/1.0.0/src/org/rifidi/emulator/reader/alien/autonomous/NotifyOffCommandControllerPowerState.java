/*
 *  @(#)NotifyOffCommandControllerPowerState.java
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
import org.rifidi.emulator.extra.CommandInformation;
import org.rifidi.emulator.extra.ExtraInformation;
import org.rifidi.emulator.io.comm.ip.tcpclient.TCPClientCommunication;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractOffCommandControllerPowerState;

/**
 * Implements functions of the OFF power state for an
 * AutonomousCommandController.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class NotifyOffCommandControllerPowerState extends
		AbstractOffCommandControllerPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(AbstractOffCommandControllerPowerState.class);
	
	/**
	 * The singleton instance for this class.
	 */
	private static final NotifyOffCommandControllerPowerState SINGLETON_INSTANCE = new NotifyOffCommandControllerPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static NotifyOffCommandControllerPowerState getInstance() {
		return NotifyOffCommandControllerPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private NotifyOffCommandControllerPowerState() {
		
	}

	/**
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.
	 * AbstractOffCommandControllerPowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void turnOn(PowerControllable pcObject) {
		/* Call the abstract implementation first */
		super.turnOn(pcObject);

		logger.debug("Autonomous Controller on");
		
		/* Cast to an AutonomousCommandController for specific action */
		NotifyController aCController = (NotifyController) pcObject;

		/* Change to on state */
		aCController
				.changeCommandControllerPowerState(NotifyOnCommandControllerPowerState
						.getInstance());

	}
	
	/**
	 * Turns on the command with extra information set for the command.  This 
	 * will erase any previous commands stored.  
	 */
	public void turnOn(PowerControllable pcObject, ExtraInformation extraInfo) {
		/* Call the abstract implementation first */
		super.turnOn(pcObject);
		
		logger.debug("Autonomous mode on w/ ExtraInfo");

		/* Cast to an AutonomousCommandController for specific action */
		NotifyController aCController = (NotifyController) pcObject;

		/* Change to on state */
		aCController
				.changeCommandControllerPowerState(NotifyOnCommandControllerPowerState
						.getInstance());

	}



}
