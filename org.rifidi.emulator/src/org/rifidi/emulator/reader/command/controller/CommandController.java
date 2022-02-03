/*
 *  @(#)CommandController.java
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

/**
 * A CommandController is an interface for processing commands. It is used as an
 * entry point into the overall command processor.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public interface CommandController {

	/**
	 * Processes the passed command and returns a response.
	 * 
	 * @param command
	 *            The command to process.
	 * @return A response message for the command.
	 * @throws CommandControllerException
	 *             If this method is called an the processCommand cannot be
	 *             completed.
	 */
	public ArrayList<Object> processCommand(byte[] command)
			throws CommandControllerException;

}
