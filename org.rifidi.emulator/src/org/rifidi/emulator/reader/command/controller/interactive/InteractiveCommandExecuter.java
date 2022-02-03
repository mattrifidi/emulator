/*
 *  @(#)InteractiveCommandExecuter.java
 *
 *  Created:	Oct 25, 2006
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
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.reader.command.controller.CommandControllerException;

/**
 * This class represents is Runnable which listens for incoming commands on a
 * Communication and executes those commands.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class InteractiveCommandExecuter implements Runnable {

	/**
	 * The InteractiveCommandController which this executer is bound to and uses
	 * to execute commands.
	 */
	private InteractiveCommandController controller;

	/**
	 * Creates an InteractiveCommandExecuter which is bound to the passed
	 * InteractiveCommandController.
	 * 
	 * @param controller
	 *            The InteractiveCommandController which this executer uses to
	 *            execute commands.
	 */
	public InteractiveCommandExecuter(InteractiveCommandController controller) {
		this.controller = controller;
	}

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory
			.getLog(InteractiveCommandExecuter.class);

	/**
	 * Runs until an exception occurs, typically by someone turning off the
	 * controller or turning off / disconnecting the controller's underlying
	 * Communication.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		/* Run as long as this condition variable is true */
		boolean keepRunning = true;

		/* Process commands until an exception occurs. */
		while (keepRunning) {
			byte[] curCommand;
			try {
				curCommand = this.controller.getCurCommunication()
						.receiveBytes();

				StringBuffer byteString = new StringBuffer();
				for (byte b : curCommand) {
					byteString.append(b);
					byteString.append(" ");
				}
				logger.debug("curCommand in the executer is: "
						+ byteString.toString());

				/*
				 * Check to see if the current command is a special case command
				 * with the sentinel.
				 */
				boolean sentinelBool = controller.getSentinel()
						.containsCommand(curCommand);

				/* If the command is not recognized by the sentinel */
				if (!sentinelBool) {
					ArrayList<Object> curResponse = this.controller
							.processCommand(curCommand);

					/*
					 * This is where the response is processed and sent. It
					 * works because the Controller choses whether to use bytes
					 * or strings to read from the socket (ByteStreamReader or
					 * CharStreamReader)
					 */
					//FIXME: there may be a better way to do this
					if (curResponse != null) {
						for (Object obj : curResponse) {
							byte[] bytes;

							// TODO: not a great solution

							if (obj instanceof String) {
								bytes = ((String) obj).getBytes();
							} else {
								bytes = (byte[]) obj;
							}
							this.controller.getCurCommunication().sendBytes(
									bytes);
						}
					}
				} else {

					/* If it is recognized by the sentinel */

					logger.debug("Starting the getCommandList");
					/* Get the list of commands to execute */
					ArrayList<byte[]> newCommandList = controller.getSentinel()
							.getCommandList(curCommand);

					logger.debug("just before the command for loop size="
							+ newCommandList.size());
					/* Now loop through the list and execute the commands */
					for (byte[] command : newCommandList) {
						logger.debug("processing command:"
								+ new String(command));
						ArrayList<Object> curResponse = this.controller
								.processCommand(command);

						logger.debug("The (sentinel) return data is : "
								+ curResponse);

						/*
						 * This is where the response is processed and sent
						 */
						for (Object obj : curResponse) {
							this.controller.getCurCommunication().sendBytes(
									((String) obj).getBytes());
							logger.debug("The (sentinel) bytes sent are: "
									+ ((String) obj).getBytes());
						}
					}
				}

			} catch (CommunicationException e) {
				/* Communication interrupted -- stop running. */
				keepRunning = false;

			} catch (CommandControllerException e) {
				/* Controller interrupted -- stop running. */
				keepRunning = false;

			}

		}

	}

	/**
	 * This method is a Hack to send out data during a command is working
	 * 
	 * @param data
	 *            The data to send
	 */
}
