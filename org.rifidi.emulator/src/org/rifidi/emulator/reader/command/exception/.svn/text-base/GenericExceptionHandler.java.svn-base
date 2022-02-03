/*
 *  GenericExceptionHandler.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.command.exception;

import java.util.ArrayList;

import org.rifidi.emulator.reader.command.CommandObject;

/**
 * 
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public abstract class GenericExceptionHandler {

	/**
	 * Error for if the user types in a command that is not recognized.
	 * 
	 * @param arg
	 *            The command that was entered
	 * @return The command that was entered plus a message detailing what the
	 *         user did.
	 */
	public abstract ArrayList<Object> commandNotFoundError(
			ArrayList<Object> arg, CommandObject obj);

	/**
	 * Error if an incorrect argument was given to the command
	 * 
	 * @param arg
	 *            The command that was entered
	 * @return The command that was entered plus a message detailing what the
	 *         user did.
	 */
	public abstract ArrayList<Object> invalidCommandError(
			ArrayList<Object> arg, String value, CommandObject obj);

	/**
	 * Error if the user types in a correct message that is missing a required
	 * argument, such as "set PersistTime"
	 * 
	 * @param arg
	 *            The command that was entered
	 * @return The command that was entered plus a message detailing what the
	 *         user did.
	 */
	public abstract ArrayList<Object> malformedMessageError(
			ArrayList<Object> arg, CommandObject obj);

	/**
	 * Error that is thrown in the CommandHandlerInovker. Often happens when
	 * there is a null pointer error in the command handler method somwhere
	 * 
	 * @param arg
	 * @param obj
	 * @return
	 */
	public CommandObject methodInvocationError(
			ArrayList<Object> arg, CommandObject obj){
		return obj;
	}
}
