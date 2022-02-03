/*
 *  SelectiveReflectiveCommandAdapter.java
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
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class SelectiveReflectiveCommandAdapter implements CommandAdapter {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory
			.getLog(SelectiveReflectiveCommandAdapter.class);

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
	
	private GenericExceptionHandler geh;
	
	private String state;

	/**
	 * 
	 * 
	 * @param state
	 * @param newFormatter
	 * @param geh
	 * @param asr
	 */
	public SelectiveReflectiveCommandAdapter(String state,
			CommandFormatter newFormatter, GenericExceptionHandler geh,
			AbstractReaderSharedResources asr) {
		this.state = state;
		this.newFormatter = newFormatter;
		this.sharedResources = asr;
		this.commandHashMap = asr.getCommandsByState(state);
		this.geh = geh;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.control.adapter.CommandAdapter#executeCommand(byte[])
	 */
	public ArrayList<Object> executeCommand(byte[] command) {
		// Encode the command into an ArrayList by using the Formatter
		
		commandHashMap = this.sharedResources.getCommandsByState(state);

		logger.debug(new String(command));

		ArrayList<Object> tempFormattedCommand = this.newFormatter
				.decode(command);

		for (Object i : tempFormattedCommand) {
			logger.debug(i.toString());
		}

		ArrayList<Object> commandResponse = new ArrayList<Object>();

		// the command name is the first element
		String commandName = (String) tempFormattedCommand.get(0);
		commandName = commandName.toLowerCase();
		logger.debug("The lookup CommandName is: " + commandName);
//		 Look up Command Object by using the command from the ArrayList
		// First element is always a command
		CommandObject inputCommand = (CommandObject) commandHashMap
				.get(commandName);
		try {
			
			if (inputCommand == null) {
				logger.debug("in the command==null, commandname = " +commandName);
				String addString = commandName.split("\\.")[0];				
				logger.debug("addString = " + addString);
				
				inputCommand=(CommandObject)commandHashMap.get(addString);
			}
			
			if (inputCommand == null) {
				throw new CommandNotFoundException("Command not found: "
						+ commandName);
			}

			logger.debug(inputCommand.getDisplayName());

			/* Get the actual query name */
			inputCommand.setCurrentQueryName(newFormatter
					.getActualCommand(command));

			logger.debug("Array list size is: " + tempFormattedCommand.size());
			logger.debug("The name is: " + inputCommand.getName().get(0));
			// Remove the first element in the arraylist because it is the
			// actual
			// command
			tempFormattedCommand.remove(0);

			// Now you are left with the arguments to the command
			if (tempFormattedCommand.size() > 0) {
				inputCommand.setArguments(tempFormattedCommand);
			}

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
			commandResponse.addAll(this.newFormatter.encode(inputCommand
					.getReturnValue()));
			
		} catch (CommandNotFoundException e) {
			logger.warn(e.getMessage());
			logger.debug(e.getCause());
			inputCommand = new CommandObject();
			inputCommand.setCurrentQueryName(new String(command));
			commandResponse = geh.commandNotFoundError(commandResponse,inputCommand);
		}
		// Resets the arguments and the return values for the next method
		// call
		inputCommand.reset();
		return commandResponse;
	}
}
