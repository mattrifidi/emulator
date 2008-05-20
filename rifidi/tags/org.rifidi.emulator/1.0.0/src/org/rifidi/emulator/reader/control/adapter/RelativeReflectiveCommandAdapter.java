/*
 *  RelativeReflectiveCommandAdapter.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.control.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandXMLDigester;
import org.rifidi.emulator.reader.commandhandler.CommandHandlerInvoker;
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * This is a relative command adapater, meaning that it will iteratively go
 * through a name to find a matching CommandObject. Anything after the name
 * (after the name is found) is treated as an argument. <br />
 * <br />
 * For example, if the command we are trying to look up is "05_11_17_08" and the
 * command given is "05_11_17_08_03_FF_A7_xx_xx" (where xx is the checksum), the
 * xx_xx will be stripped off in the formatter, leaving "05_11_17_08_03_05_07".
 * This will be iterated through as "05", "05_11", and "05_11_17" until a match
 * is found at "05_11_17_08". The arguments will then be set to "03_FF_A7".
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class RelativeReflectiveCommandAdapter implements CommandAdapter {

	/**
	 * Message logger
	 */
	private static Log logger =
		 LogFactory.getLog(RelativeReflectiveCommandAdapter.class);

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
	public RelativeReflectiveCommandAdapter(String state,
			CommandFormatter newFormatter, CommandXMLDigester dig,
			GenericExceptionHandler geh, AbstractReaderSharedResources asr) {
		logger.debug( "Starting RelativeReflectiveCommandAdapter" );
		this.newFormatter = newFormatter;
		this.sharedResources = asr;
		this.geh = geh;

		this.commandHashMap = asr.getCommandsByState(state);
		
		// ArrayList<CommandObject> tempList = dig.getCommandsByState(state);
		// commandHashMap = new HashMap<String, CommandObject>();
		// for (int i = 0; i < tempList.size(); i++) {
		// // List of query names
		// ArrayList<String> nameTemp = new ArrayList<String>(tempList.get(i)
		// .getName());
		// for (String tempName : nameTemp) {
		// logger.debug("Adding command: " + tempName.toLowerCase());
		// commandHashMap.put(tempName.toLowerCase(), tempList.get(i));
		//			}
		//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.control.adapter.CommandAdapter#executeCommand(byte[])
	 */
	public ArrayList<Object> executeCommand(byte[] command) {
		// Encode the command into an ArrayList by using the Formatter

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
		String currentCommandName = "";
		ArrayList<Object> commandArgs = new ArrayList<Object>();
		try {
			// Look up Command Object by using the command from the ArrayList
			// First element is always a command
			boolean keepRunning = true;
			CommandObject inputCommand = null;

			String[] tempStringTokens = commandName.split("_");
			int tempStringTokenNumber = 0;
			while (tempStringTokenNumber < tempStringTokens.length
					&& keepRunning) {
				if (tempStringTokenNumber != 0) {
					currentCommandName += "_";
				}

				currentCommandName += tempStringTokens[tempStringTokenNumber];
				logger.debug("The current command name is: "
						+ currentCommandName);
				inputCommand = (CommandObject) commandHashMap
						.get(currentCommandName);
				//inputCommand.reset();
				++tempStringTokenNumber;
				if (inputCommand != null) {
					keepRunning = false;
					for (int x = tempStringTokenNumber; x < tempStringTokens.length; x++) {
						commandArgs.add(tempStringTokens[x]);
					}
				}
			}

			if (inputCommand == null) {
				throw new CommandNotFoundException("Command not found: "
						+ commandName);
			}

			logger.debug(inputCommand.getDisplayName());

			/*
			 * Get the actual query name.
			 */
			inputCommand.setCurrentQueryName(commandName);

			logger.debug("Array list size is: " + tempFormattedCommand.size());
			logger.debug("The name is: " + inputCommand.getName().get(0));
			// Remove the first element in the arraylist because it is the
			// actual command

			// Now you are left with the arguments to the command
			inputCommand.setArguments(commandArgs);
			
			logger.debug("Got past the setArgs" + commandArgs);
					
			
			/*
			 * This will be handled by the Handler and they need to look at this
			 * variable and determine if they should suppress the prompt before 
			 * the next command.  
			 * 
			 * The Awid reader has no prompt suppress(indeed it 
			 * has no prompt at all), so this always returns false.  
			 */
			if (this.newFormatter.promptSuppress()) {
				inputCommand.setPromptSuppress(true);
			} else {
				inputCommand.setPromptSuppress(false);
			}

			// Invoke the correct handler
			
			logger.debug("before the invoker: " + commandName);
			
			inputCommand = CommandHandlerInvoker.invokeHandler(inputCommand,
					this.sharedResources, geh);
			

			// Get the response and decode into a byte array
			commandResponse = this.newFormatter.encode(inputCommand
					.getReturnValue());
			
		} catch (CommandNotFoundException e) {
			logger.warn(e.getMessage());
			logger.debug(e.getCause());
		}
		return commandResponse;
	}
}
