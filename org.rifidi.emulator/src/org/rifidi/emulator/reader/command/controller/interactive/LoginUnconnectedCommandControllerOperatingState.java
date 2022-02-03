/*
 *  @(#)LoginUnconnectedCommandControllerOperatingState.java
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

import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.reader.command.controller.CommandController;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandController;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;

/**
 * The unconnected, or default, state of a login process. This state returns a
 * welcome message and login prompt to the caller of the processCommand method,
 * then switches to the username state for login processing.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class LoginUnconnectedCommandControllerOperatingState extends
		AbstractCommandControllerOperatingState {

	/**
	 * The command which is invoked by this state in order to display a login
	 * prompt.
	 */
	public static final byte[] USERNAME_PROMPT_COMMAND = "usernamePrompt"
			.getBytes();

	/**
	 * The command which is invoked by this state in order to return a welcome
	 * message.
	 */
	public static final byte[] WELCOME_COMMAND = "welcome".getBytes();

	/**
	 * The adapter to hold to eventually pass to the authenticated state.
	 */
	private CommandAdapter authenticatedAdapter;

	/**
	 * The maximum number of attempts allowed.
	 */
	private int maxAttempts;

	/**
	 * A constructor for the unconnected state in the login process.
	 * 
	 * @param unauthenticatedAdapter
	 *            The adapter to use to aid in the invocation of login commands.
	 * @param authenticatedAdapter
	 *            The adapter to hold to eventually pass to the authenticated
	 *            state.
	 * @param maxAttempts
	 *            The maximum amount of unsuccessful attempts one may make
	 *            before dropping the session.
	 */
	public LoginUnconnectedCommandControllerOperatingState(
			CommandAdapter unauthenticatedAdapter,
			CommandAdapter authenticatedAdapter, int maxAttempts) {
		super(unauthenticatedAdapter);
		this.authenticatedAdapter = authenticatedAdapter;
		this.maxAttempts = maxAttempts;

	}

	/**
	 * Processes a connection message, returning a welcome message and username
	 * prompt before switching to the username state.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState#processCommand(byte[],
	 *      org.rifidi.emulator.reader.command.controller.CommandController)
	 */
	@Override
	public ArrayList<Object> processCommand(byte[] command,
			CommandController controller) {
		ArrayList<Object> retList;
		String tempMessage = "";

		/* Combine the welcome command handler and login prompt handlers values */
		retList = this
				.getCommandAdapter()
				.executeCommand(
						LoginUnconnectedCommandControllerOperatingState.WELCOME_COMMAND);

		for (Object obj : retList) {
			tempMessage += (String) obj;
		}

		retList = this
				.getCommandAdapter()
				.executeCommand(
						LoginUnconnectedCommandControllerOperatingState.USERNAME_PROMPT_COMMAND);

		for (Object obj : retList) {
			tempMessage += (String) obj;
		}

		retList.clear();
		retList.add(tempMessage);

		/* Cast to an interactive controller */
		InteractiveCommandController interactiveController = (InteractiveCommandController) controller;

		/* Change state to username state, first attempt */
		interactiveController
				.changeCommandControllerOperatingState(new LoginUsernameCommandControllerOperatingState(
						this.getCommandAdapter(), this.authenticatedAdapter,
						this.maxAttempts, 1));

		/* Return the welcome message / login prompt */
		return retList;

	}

	/**
	 * This method initializes the LoginUnconnected Operating State by sending
	 * the connect command.
	 */
	public void initialize(AbstractCommandController controller,
			Communication comm) {
		/* Generate a connection command */

		ArrayList<Object> respond = this.processCommand(
				AbstractCommandController.CONNECTION_COMMAND, controller);

		for (Object obj : respond) {
			try {
				comm.sendBytes(((String) obj).getBytes());
			} catch (CommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
