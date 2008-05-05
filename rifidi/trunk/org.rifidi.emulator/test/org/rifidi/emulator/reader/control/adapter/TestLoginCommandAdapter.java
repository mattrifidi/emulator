/*
 *  @(#)TestLoginCommandAdapter.java
 *
 *  Created:	Oct 27, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.control.adapter;

import java.util.ArrayList;

import org.rifidi.emulator.common.ControlSignal;

/**
 * A test adapter for the login (LoginCommandControllerState) process.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TestLoginCommandAdapter implements CommandAdapter {

	/**
	 * The signal for connections.
	 */
	private ControlSignal<Boolean> connectionControlSignal;

	/**
	 * Constructs a login test adapter.
	 * 
	 * @param powerControlSignal
	 *            The power signal to alert.
	 * @param connectionControlSignal
	 *            The connection signal to alert.
	 * 
	 */
	public TestLoginCommandAdapter(ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal) {
		/* Assign instance variables */
		this.connectionControlSignal = connectionControlSignal;

	}

	/**
	 * @see org.rifidi.emulator.reader.control.adapter.CommandAdapter#executeCommand(byte[])
	 */
	public ArrayList<Object> executeCommand(byte[] command) {
		/* The string to eventually return. */
		String commandString = new String(command);
		String retString = null;

		/* Simple command parser */
		if (commandString.startsWith("authenticate")) {
			if (commandString.endsWith("user pass")) {
				retString = "true";

			} else {
				retString = "false";

			}

		} else if (commandString.equalsIgnoreCase("authenticationError")) {
			retString = "Error! Bad User/Pass\n";

		} else if (commandString.equalsIgnoreCase("authenticationSuccess")) {
			retString = "Authenticated! Horray!\n";

		} else if (commandString.equalsIgnoreCase("disconnectUser")) {
			/* Force a disconnect */
			this.connectionControlSignal.setControlVariableValue(false);

		} else if (commandString.equalsIgnoreCase("passwordPrompt")) {
			retString = "Password> ";

		} else if (commandString.startsWith("showPassword")) {
			retString = commandString.substring("showPassword".length()).trim()
					+ "\n";

		} else if (commandString.startsWith("showUsername")) {
			retString = commandString.substring("showUsername".length()).trim()
					+ "\n";

		} else if (commandString.equalsIgnoreCase("usernamePrompt")) {
			retString = "Username> ";

		} else if (commandString.equalsIgnoreCase("welcome")) {
			retString = "Welcome to the "
					+ "InteractiveCommandControllerSampleServer. " + "\n";

		} else if (commandString.equalsIgnoreCase("commandPrompt")) {
			retString = "> ";

		} else {
			retString = "INVALID COMMAND CALLED!\n";
		}
		
		System.out.println("retString = " + retString);

		/* Return */
		//return retString.getBytes();
		return null;
	}

}
