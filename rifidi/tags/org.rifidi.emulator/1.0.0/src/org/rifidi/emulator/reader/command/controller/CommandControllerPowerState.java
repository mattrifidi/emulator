/*
 *  @(#)CommandControllerPowerState.java
 *
 *  Created:	Oct 11, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller;

import java.util.ArrayList;

import org.rifidi.emulator.common.PowerState;

/**
 * An extension of the PowerState interface which provides one more method,
 * parseCommand. This is added so that the behavior of a
 * AbstractCommandController's basic operation may be dictated by its current
 * power state.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public interface CommandControllerPowerState extends PowerState {

	/**
	 * Processes the current command. The behavior of this method is dictated by
	 * the current CommandControllerPowerState implementation.
	 * 
	 * @param command
	 *            The command the process.
	 * @param controller
	 *            The controller which invoked this method.
	 * 
	 * @return The response of the command invocation.
	 * @throws CommandControllerException
	 *             If this method is called an the processCommand cannot be
	 *             completed.
	 */
	public ArrayList<Object> processCommand(byte[] command, CommandController controller)
			throws CommandControllerException;

}
