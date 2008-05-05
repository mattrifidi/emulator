/*
 *  CommandObjectHolder.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command;

import java.util.Collection;
import java.util.HashMap;

/**
 * This class holds all of the commands gleaned from the XML file by the
 * XMLDigester. They will be added one at a time using the addCommandObject()
 * method and can be taken out either by state or all at once.
 * 
 * @author Matthew Dean
 */
public abstract class CommandObjectHolder {

	/**
	 * A HashMap consisting of a string representation of the state as the key,
	 * and a HashMap of all of the query names bound to command objects as the
	 * value.
	 * 
	 * @return All commands, bound by their display name
	 */
	public abstract HashMap<String, HashMap<String, CommandObject>> getAllQueryCommands();

	/**
	 * Add a new CommandObject to the holder.
	 * 
	 * @param newObject
	 *            The CommandObject to add
	 */
	public abstract void addCommandObject(CommandObject newObject);

	/**
	 * Get all of the commands stored.
	 * 
	 * @return All of the commands stored
	 */
	public abstract Collection<CommandObject> getAllCommands();

	/**
	 * Get a Collection of commands all sharing the specified state.
	 * 
	 * @param state
	 *            The state of the commands being looked for
	 * @return A collection of commands which share the state defined by the
	 *         parameter
	 */
	public abstract Collection<CommandObject> getCommandsByState(String state);

	/**
	 * Returns a Collection of CommandObjects which fit the given Category.
	 * 
	 * @param category
	 * @return
	 */
	public abstract Collection<CommandObject> getCommandsByCategory(
			String category);

	/**
	 * Returns a Collection of Strings representing every category present in
	 * the CommandObjectHolder.
	 * 
	 * @param category
	 * @return
	 */
	public abstract Collection<String> getAllCategories();

}
