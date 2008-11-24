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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

// import java.util.Set;
// import java.util.Iterator;

/**
 * This class is a holding class that represents a HashMap of CommandObjects.<br />
 * <br />
 * It also contains some convenience methods to lookup CommandObjects. <br />
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class CommandObjectHashMap extends CommandObjectHolder {

	public static final String NO_CATEGORY = "NONE";

	// The collection that will hold all of the commands
	private LinkedHashMap<String, ArrayList<CommandObject>> commandMap;

	/**
	 * Default constructor
	 */
	public CommandObjectHashMap() {
		commandMap = new LinkedHashMap<String, ArrayList<CommandObject>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.CommandObjectHolder#addCommandObject(org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public void addCommandObject(CommandObject newObject) {
		if (commandMap.containsKey(newObject.getCommandState())) {
			ArrayList<CommandObject> tempList = commandMap.get(newObject
					.getCommandState());
			tempList.add(newObject);
		} else {
			ArrayList<CommandObject> tempList = new ArrayList<CommandObject>();
			tempList.add(newObject);
			commandMap.put(newObject.getCommandState(), tempList);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.CommandObjectHolder#getAllQueryCommands()
	 */
	@Override
	public HashMap<String, HashMap<String, CommandObject>> getAllQueryCommands() {
		HashMap<String, HashMap<String, CommandObject>> displayCommands = new HashMap<String, HashMap<String, CommandObject>>();

		HashSet<String> stateSet = new HashSet<String>();
		ArrayList<CommandObject> allCommands = this.getAllCommands();
		
		for(CommandObject i:allCommands) {
			stateSet.add(i.getCommandState());
		}
		
		for(String state:stateSet) {
			displayCommands.put(state, new HashMap<String,CommandObject>());
			for(CommandObject i:commandMap.get(state)) {
				for(String x:i.getName()) {
					displayCommands.get(state).put(x.toLowerCase(), i);
				}
			}
		}
		return displayCommands;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.CommandObjectHolder#getAllCommands()
	 */
	@Override
	public ArrayList<CommandObject> getAllCommands() {
		ArrayList<CommandObject> retVal = new ArrayList<CommandObject>();
		for (ArrayList<CommandObject> i : commandMap.values()) {
			retVal.addAll(i);
		}
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.CommandObjectHolder#getCommandsByState(java.lang.String)
	 */
	@Override
	public Collection<CommandObject> getCommandsByState(String state) {
		return commandMap.get(state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.CommandObjectHolder#getCommandsByCatagory(java.lang.String)
	 */
	@Override
	public Collection<CommandObject> getCommandsByCategory(String category) {
		ArrayList<CommandObject> retVal = new ArrayList<CommandObject>();
		ArrayList<CommandObject> temp = new ArrayList<CommandObject>();

		for (ArrayList<CommandObject> i : commandMap.values()) {
			temp.addAll(i);
		}

		for (CommandObject i : temp) {
			if (i.getCategory().equalsIgnoreCase(category)) {
				retVal.add(i);
			}
		}

		return retVal;
	}

	/**
	 * 
	 */
	public Collection<String> getAllCategories() {
		ArrayList<String> retVal = new ArrayList<String>();
		HashSet<String> newSet = new HashSet<String>();

		ArrayList<CommandObject> allCommands = this.getAllCommands();
		for (CommandObject i : allCommands) {
			if (!newSet.contains(i.getCategory())
					&& !i.getCategory().equalsIgnoreCase(NO_CATEGORY)) {
				newSet.add(i.getCategory());
				retVal.add(i.getCategory());
			}
		}
		return retVal;
	}

}
