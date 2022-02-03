/*
 *  CommandXMLDigester.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.xml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.CommandObjectHashMap;
import org.rifidi.emulator.reader.command.CommandObjectHolder;
import org.xml.sax.SAXException;

/**
 * The CommandXMLDigester is a specific implementation of the CommandDigester
 * interface for processing xml-based commands. <br />
 * This class uses the Apache digester libraries to translate a specific Command
 * XML for a reader into a collection of Command Objects.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class CommandXMLDigester implements CommandDigester {
	
	/**
	 * The log4j logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(CommandXMLDigester.class);

	private CommandObjectHashMap newHolder = null;

	private HashMap<String, CommandObject> commandList;
	
	private HashMap<String,HashMap<String,CommandObject>> displayList;

	/**
	 * Default Constructor
	 */
	public CommandXMLDigester() {
		// default constructor
	}

	/**
	 * Returns the Command Holder
	 * 
	 * @return
	 */
	public CommandObjectHolder getHolder() {
		return newHolder;
	}

	/**
	 * Parses a Reader Command XML file and converts them to Command Objects.
	 * Takes in a filename to identify the location of the xml files.
	 * 
	 * @param xmlFileInputStream
	 */
	public void parseToCommand(InputStream xmlFileInputStream) {
		try {
			Digester dig = new Digester();
			dig.setClassLoader(this.getClass().getClassLoader());
			dig.setValidating(false);

			// Create the CommandObjectHashMap
			dig.addObjectCreate("ReaderCommand", CommandObjectHashMap.class);
			// Create the CommandObject
			dig.addObjectCreate("ReaderCommand/Command", CommandObject.class);

			// Add the query names to the ArrayList
			dig.addCallMethod("ReaderCommand/Command/queryName", "addName", 1); 
			dig.addCallParam("ReaderCommand/Command/queryName", 0);

			// set the displayName
			dig.addBeanPropertySetter("ReaderCommand/Command/displayName");

			// set the state
			dig.addBeanPropertySetter("ReaderCommand/Command/commandState");

			// set the handler class
			dig.addBeanPropertySetter("ReaderCommand/Command/commandHandlerDetails/handlerClass");
			
			// set the handler method
			dig.addBeanPropertySetter("ReaderCommand/Command/commandHandlerDetails/handlerMethod");
			
			// set the arguemnt type
			dig.addBeanPropertySetter("ReaderCommand/Command/commandHandlerDetails/argumentType");
			dig.addBeanPropertySetter("ReaderCommand/Command/actionTag");
			dig.addBeanPropertySetter("ReaderCommand/Command/category");
			
			// set the default value
			dig.addBeanPropertySetter("ReaderCommand/Command/defaultValue");

			dig.addSetNext("ReaderCommand/Command", "addCommandObject");
			
			newHolder = (CommandObjectHashMap) dig.parse(xmlFileInputStream);
			
			commandList = new HashMap<String, CommandObject>();
			ArrayList<CommandObject> tempList = new ArrayList<CommandObject>(
					newHolder.getAllCommands());
			for (CommandObject i : tempList) {
				commandList.put(i.getDisplayName(), i);
			}

			displayList = newHolder.getAllQueryCommands();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The method finds all Commands for a given state and returns them as an
	 * ArrayList.
	 * 
	 * @param state
	 * @return An ArrayList of command objects for the given state
	 */
	public ArrayList<CommandObject> getCommandsByState(String state) {
		ArrayList<CommandObject> retVal;
		retVal = (ArrayList<CommandObject>) newHolder.getCommandsByState(state);
		return retVal;
	}

	/**
	 * Returns the CommandObjects mapped to the query ames of the commands.
	 * 
	 * @return
	 */
	public HashMap<String, CommandObject> getQueryCommandsByState(String arg) {
		//logger.debug("Returning query for + " + arg + "commands are: " + this.displayList.keySet());
		return this.displayList.get(arg);
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection<String> getAllCategories() {
		return newHolder.getAllCategories();
	}
	
	/**
	 * 
	 * @param category
	 * @return
	 */
	public Collection<CommandObject> getCommandsByCategory(String category){
		return newHolder.getCommandsByCategory(category);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String,CommandObject> getAllCommands() {
		return commandList;
	}
}
