/*
 *  @(#)LoginPasswordCommandControllerOperatingState.java
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
 * The password state of a login process. This state handles an incoming command
 * by passing it to the adapter as an argument of the password command. Then,
 * the passed command is compared to the password stored in the reader. If the
 * passwords match, the state will switch to the authenticated state. <br>
 * 
 * If the maximum number of tries have not been attempted, then the number of
 * tried is incremented and the state is set to the username state once again.
 * If the maximum number of tries have been reached, then the disconnect command
 * is called.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class LoginPasswordCommandControllerOperatingState extends
		AbstractCommandControllerOperatingState {

	/**
	 * Message logger
	 */
	private static Log logger =
		 LogFactory.getLog(LoginPasswordCommandControllerOperatingState.class);

	/**
	 * The command which is invoked by this state in order to authenticate the
	 * username / password combo. The command will be sent in the following
	 * format: 'authenticate username password'. An expected return for the
	 * command is either "true" or "false" in bytes.
	 */
	public static final byte[] AUTHENTICATE_COMMAND = "authenticate".getBytes();

	/**
	 * The command which is invoked by this state in order to return an
	 * authentication failure message.
	 */
	public static final byte[] AUTHENTICATION_FAILURE_COMMAND = "authenticationError"
			.getBytes();

	/**
	 * The command which is invoked by this state in order to return an
	 * authentication success message.
	 */
	public static final byte[] AUTHENTICATION_SUCCESS_COMMAND = "authenticationSuccess"
			.getBytes();

	/**
	 * The command which is invoked by this state in order to disconnect the
	 * user if they've attempted unsuccessfully to login too many times.
	 */
	public static final byte[] DISCONNECT_USER_COMMAND = "disconnectUser"
			.getBytes();

	/**
	 * The command which is invoked by this state in order to return the
	 * password with was sent. This command can return clear text, obfuscate the
	 * password, refuse to return anything, etc..
	 */
	public static final byte[] SHOW_PASSWORD_COMMAND = "showPassword"
			.getBytes();

	/**
	 * The command which is invoked by this state in order show a command
	 * prompt.
	 */
	public static final byte[] COMMAND_PROMPT_COMMAND = "commandPrompt"
			.getBytes();

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
	 * The username which the password is being validated for.
	 */
	private byte[] username;

	/**
	 * Creates a password state with the passed adapter, the maximum number of
	 * attempts, the current number of attempts, and username to check the
	 * password against.
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
	 * @param username
	 *            The username which the password will be checked against.
	 */
	public LoginPasswordCommandControllerOperatingState(
			CommandAdapter unauthenticatedAdapter,
			CommandAdapter authenticatedAdapter, int maxAttempts,
			int curNumberOfAttempts, byte[] username) {
		super(unauthenticatedAdapter);
		this.authenticatedAdapter = authenticatedAdapter;
		this.maxAttempts = maxAttempts;
		this.curNumberOfAttempts = curNumberOfAttempts;
		this.username = username;

	}

	/**
	 * Takes in the passed command (password) and passes it to the adapter with
	 * a validation command. If the validation command returns a true value,
	 * then the state is changed to the authenticated state. <br>
	 * 
	 * If the maximum number of tries have not been attempted, then the number
	 * of tried is incremented and the state is set to the username state once
	 * again. If the maximum number of tries have been reached, then the
	 * disconnect command is called.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState#processCommand(byte[],
	 *      org.rifidi.emulator.reader.command.controller.CommandController)
	 */
	@Override
	public ArrayList<Object> processCommand(byte[] command,
			CommandController controller) {
		ArrayList<Object> tempList;

		logger.debug("LOGINPASSWORDCOMMAND + command = " + new String(command));

		/* Construct a full authentication request */
		byte[] authenticateRequeust = (new String(
				LoginPasswordCommandControllerOperatingState.AUTHENTICATE_COMMAND)
				+ " " + new String(this.username) + " " + new String(command))
				.getBytes();

		/* Parse the return value of executing the authentication request */
		tempList = this.getCommandAdapter()
				.executeCommand(authenticateRequeust);

		String tempAuth = "";

		for (Object obj : tempList) {
			tempAuth += (String) obj;
		}

		logger.debug("Authenticated: " + tempAuth);

		boolean authenticated = Boolean.parseBoolean(tempAuth);

		/* The return message */
		tempList = this.getCommandAdapter().executeCommand(
				(new String(SHOW_PASSWORD_COMMAND) + " " + new String(command))
						.getBytes());
		String tempMessage = "";

		for (Object obj : tempList) {
			tempMessage += (String) obj;
		}

		/* Cast to an interactive controller */
		InteractiveCommandController interactiveController = (InteractiveCommandController) controller;

		if (authenticated) {
			/* Authenticated -- switch to the authenticated state */

			String successString = (new String(AUTHENTICATION_SUCCESS_COMMAND)
					+ " " + new String(command));

			tempList = this.getCommandAdapter().executeCommand(
					successString.getBytes());

			for (Object obj : tempList) {
				tempMessage += (String) obj;
			}

			/* Add a command prompt. */
			String promptCommand = new String(
					new String(COMMAND_PROMPT_COMMAND) + " "
							+ new String(command));

			tempList = this.getCommandAdapter().executeCommand(
					promptCommand.getBytes());

			for (Object obj : tempList) {
				tempMessage += (String) obj;
			}

			/* Change state to password state */
			interactiveController
					.changeCommandControllerOperatingState(new LoginAuthenticatedCommandControllerOperatingState(
							this.authenticatedAdapter));

		} else {
			/* Get the failure message */

			tempList = this
					.getCommandAdapter()
					.executeCommand(
							LoginPasswordCommandControllerOperatingState.AUTHENTICATION_FAILURE_COMMAND);

			for (Object obj : tempList) {
				tempMessage += (String) obj;
			}

			/* Not authenticated... see if we should disconnect or try again. */
			if (this.curNumberOfAttempts < this.maxAttempts) {
				/* Allow a retry -- append the login prompt. */
				tempList = this
						.getCommandAdapter()
						.executeCommand(
								LoginUnconnectedCommandControllerOperatingState.USERNAME_PROMPT_COMMAND);

				for (Object obj : tempList) {
					tempMessage += (String) obj;
				}

				/* Switch to username state, adjusting number of attempts by 1. */
				interactiveController
						.changeCommandControllerOperatingState(new LoginUsernameCommandControllerOperatingState(
								this.getCommandAdapter(),
								this.authenticatedAdapter, this.maxAttempts,
								this.curNumberOfAttempts + 1));

			} else {
				/* Past maximum number of attempts, disconnect */
				tempList = this
						.getCommandAdapter()
						.executeCommand(
								LoginPasswordCommandControllerOperatingState.DISCONNECT_USER_COMMAND);

				for (Object obj : tempList) {
					tempMessage += (String) obj;
				}
			}

		}

		/* Convert to bytes */
		tempList.clear();
		tempList.add(tempMessage);

		/* Return username display and password prompt. */
		return tempList;
	}
}
