/*
 *  @(#)LoginUsernameCommandControllerOperatingState.java
 *
 *  Created:	Oct 26, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.interactive;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.controller.CommandController;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;

/**
 * The username state of a login process. This state handles an incoming command
 * by passing it to the adapter as an argument of the username command, then
 * passing it as an argument to the next state, the password state.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class LoginUsernameCommandControllerOperatingState extends
		AbstractCommandControllerOperatingState {

	/**
	 * Message logger
	 */
	private static Log logger =
		 LogFactory.getLog(LoginUsernameCommandControllerOperatingState.class);

	/**
	 * The command which is invoked by this state in order to return a password
	 * prompt to the user.
	 */
	public static final String PASSWORD_PROMPT_COMMAND = "passwordPrompt";

	/**
	 * The command which is invoked by this state in order to return the entered
	 * username back to the user.
	 */
	public static final String SHOW_USERNAME_COMMAND = "showUsername";

	/**
	 * The adapter to hold to eventually pass to the authenticated state.
	 */
	private CommandAdapter authenticatedAdapter;

	/**
	 * The current number of attempts made.
	 */
	private int curNumberOfAttempts;

	/**
	 * The maximum number of attempts allowed.
	 */
	private int maxAttempts;

	/**
	 * Creates a username state with the maximum attempt count and current
	 * number of attempts.
	 * 
	 * @param unauthenticatedAdapter
	 *            The adapter to use to aid in the invocation of login commands.
	 * @param authenticatedAdapter
	 *            The adapter to hold to eventually pass to the authenticated
	 *            state.
	 * @param maxAttempts
	 *            The maximum amount of unsuccessful attempts one may make
	 *            before dropping the session.
	 * @param curNumberOfAttempts
	 *            The current number of attempts which have been made.
	 */
	public LoginUsernameCommandControllerOperatingState(
			CommandAdapter unauthenticatedAdapter,
			CommandAdapter authenticatedAdapter, int maxAttempts,
			int curNumberOfAttempts) {
		super(unauthenticatedAdapter);
		this.authenticatedAdapter = authenticatedAdapter;
		this.maxAttempts = maxAttempts;
		this.curNumberOfAttempts = curNumberOfAttempts;

	}

	/**
	 * Takes in the passed command and passes it along to the password state.
	 * Returns the results a username display command and a password prompt
	 * display command.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState#processCommand(byte[],
	 *      org.rifidi.emulator.reader.command.controller.CommandController)
	 */
	@Override
	public ArrayList<Object> processCommand(byte[] command,
			CommandController controller) {
		ArrayList<Object> retList = new ArrayList<Object>();

		/* Construct a full showUsername request */
		byte[] showUsernameRequest = (LoginUsernameCommandControllerOperatingState.SHOW_USERNAME_COMMAND
				+ " " + new String(command)).getBytes();

		/* Construct the return message */
		String tempUser = "";

		ArrayList<Object> tempList = this.getCommandAdapter().executeCommand(
				showUsernameRequest);

		for (Object obj : tempList) {
			tempUser += (String) obj;
		}

		logger.debug("Username: " + tempUser);

		/* Construct the getPassword request */

		String passCommand = new String(
				LoginUsernameCommandControllerOperatingState.PASSWORD_PROMPT_COMMAND
						+ " " + new String(command));

		tempList = this.getCommandAdapter().executeCommand(
				passCommand.getBytes());

		String tempPass = "";

		for (Object obj : tempList) {
			tempPass += (String) obj;
		}

		logger.debug("Password: " + tempPass);

		String tempMessage = tempUser + tempPass;

		/* Add return message to List */
		retList.add(tempMessage);

		/* Cast to an interactive controller */
		InteractiveCommandController interactiveController = (InteractiveCommandController) controller;

		/* Change state to password state */
		interactiveController
				.changeCommandControllerOperatingState(new LoginPasswordCommandControllerOperatingState(
						this.getCommandAdapter(), this.authenticatedAdapter,
						this.maxAttempts, this.curNumberOfAttempts, command));

		/* Return username display and password prompt. */
		return retList;
	}
}
