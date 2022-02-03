/*
 *  CommandDigester.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.command.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.CommandObjectHolder;

/**
 * @author Matthew Dean
 *
 */
public interface CommandDigester {
	/**
	 * Parse an xml file to command objects given an xmlStream.
	 * 
	 * @param xmlFileInputStream
	 */
	public void parseToCommand(InputStream xmlFileInputStream);
	
	/**
	 * 
	 * 
	 * @param category
	 * @return
	 */
	public Collection<CommandObject> getCommandsByCategory(String category);
	
	/**
	 * 
	 * @return
	 */
	public Collection<String> getAllCategories();
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, CommandObject> getQueryCommandsByState(String state);
	
	/**
	 * 
	 * @param state
	 * @return
	 */
	public ArrayList<CommandObject> getCommandsByState(String state);
	
	/**
	 * 
	 * @return
	 */
	public CommandObjectHolder getHolder();
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String,CommandObject> getAllCommands();
	
}
