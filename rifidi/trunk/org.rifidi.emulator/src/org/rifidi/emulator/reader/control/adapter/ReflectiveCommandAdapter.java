/*
 *  ReflectiveCommandAdapter.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.control.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.commandhandler.CommandHandlerInvoker;
import org.rifidi.emulator.reader.control.adapter.searcher.CommandSearcher;
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * This is a specific implementation of the CommandAdapter Interface. <br />
 * The Reflective Command Adapter uses reflection to look up the correct handler
 * for the given Command. It requires a formatter object and a pre-populated set
 * of commands via the CommandXMLDigester. <br />
 * This Adapter is mapped on a one-to-one basis with a ReaderControl.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class ReflectiveCommandAdapter implements CommandAdapter {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory
			.getLog(ReflectiveCommandAdapter.class);

	/**
	 * The Formatter Class used to pre/post format all commands
	 */
	private CommandFormatter newFormatter;

	/**
	 * The Shared Resource Class used for all common resources for this reader
	 */
	private AbstractReaderSharedResources sharedResources;

	/**
	 * The collection that will be storing the commands for the given state
	 */
	private HashMap<String, CommandObject> commandHashMap;

	/**
	 * The error handler for this class
	 */
	private GenericExceptionHandler geh;

	/**
	 * A searcher to find the commands and set the arguments.
	 */
	private CommandSearcher search;

	/**
	 * This method constructs the Reflective Command Adapter with the correct
	 * parameters.
	 * 
	 * The adapter looks up all the commands pertaining to it's state and stores
	 * it in a map that is keyed of the query names for that command object.
	 * 
	 * 
	 * @param state
	 *            The state that all commands must follow
	 * @param newFormatter
	 *            The formatter class to use for the Adapter
	 * @param dig
	 *            The CommandXMLDigester class that is instantiated in the
	 *            ReaderModule for the whole reader
	 */
	public ReflectiveCommandAdapter(String state,
			CommandFormatter newFormatter, GenericExceptionHandler geh,
			AbstractReaderSharedResources asr, CommandSearcher search) {

		this.newFormatter = newFormatter;
		this.sharedResources = asr;
		this.geh = geh;
		this.search = search;

		commandHashMap = asr.getCommandsByState(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pramari.sandbox.ICommandAdapter#executeCMD(java.lang.String)
	 */
	public ArrayList<Object> executeCommand(byte[] command) {
		// Encode the command into an ArrayList by using the Formatter

		ArrayList<Object> tempFormattedCommand = this.newFormatter
				.decode(command);

		ArrayList<Object> commandResponse = new ArrayList<Object>();

		// the command name is the first element
		String commandName = (String) tempFormattedCommand.get(0);
		commandName = commandName.toLowerCase();

		logger.debug("The CommandName is: " + commandName);
		// Look up Command Object by using the command from the ArrayList
		// First element is always a command
		CommandObject inputCommand = search.search(tempFormattedCommand,
				this.sharedResources, commandHashMap);

		try {

			if (inputCommand == null) {
				throw new CommandNotFoundException("Command not found: "
						+ commandName);
			}

			logger.debug("Command is: " + inputCommand.getDisplayName());

			/* Get the actual query name */
			inputCommand.setCurrentQueryName(newFormatter
					.getActualCommand(command));

			/*
			 * This will be handled by the Handler and they need to look at this
			 * variable and determine if they should suppress the prompt
			 */
			// Handle whether or not the prompt will be displayed for the next
			// input
			if (this.newFormatter.promptSuppress()) {
				inputCommand.setPromptSuppress(true);
			} else {
				inputCommand.setPromptSuppress(false);
			}

			// Invoke the correct handler
			inputCommand = CommandHandlerInvoker.invokeHandler(inputCommand,
					this.sharedResources, geh);

			/* Get the response and decode */
			if (inputCommand != null) {
				if (inputCommand.getReturnValue() != null) {
					commandResponse.addAll(this.newFormatter
							.encode(inputCommand.getReturnValue()));
				}
			}

		} catch (CommandNotFoundException e) {
			logger.warn(e.getMessage());
			logger.debug(e.getCause());
			inputCommand = new CommandObject();
			inputCommand.setCurrentQueryName(new String(command));
			commandResponse = geh.commandNotFoundError(commandResponse,
					inputCommand);
		}
		// Resets the arguments and the return values for the next method
		// call
		if (inputCommand != null) {
			inputCommand.reset();
		}
		return commandResponse;
	}
}
