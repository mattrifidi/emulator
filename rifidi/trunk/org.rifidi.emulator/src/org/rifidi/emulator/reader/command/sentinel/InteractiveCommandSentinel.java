/*
 *  InteractiveCommandInterceptor.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.command.sentinel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * This is a class that will potentially break up a single command into several
 * commands. It is set when the reader is created and listens for any commands
 * that are given when the reader is turned on.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class InteractiveCommandSentinel implements CommandSentinel {
	
	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(InteractiveCommandSentinel.class);

	/**
	 * A list of commands mapped to a single reader command.
	 */
	private Map<String, ArrayList<byte[]>> sentinelList;

	/**
	 * Creates a list of commands that will be broken up into several commands
	 * 
	 * @param interceptorList
	 */
	public InteractiveCommandSentinel(String xmlFile) {
		this.parseToCommand(xmlFile);
//		ArrayList<byte[]> newList = new ArrayList<byte[]>();
//		newList.add("get TagList_Pre".getBytes());
//		newList.add("get TagList".getBytes());
//		this.sentinelList.put("t", newList);
//		this.sentinelList.put("gettaglist", newList);
	}

	/**
	 * Returns true if the interceptor contains the command given in the
	 * argument.
	 * 
	 * @param arg
	 *            The command to look up
	 * @return True if the command is found, false otherwise
	 */
	public boolean containsCommand(byte[] arg) {
		String formattedString = new String(arg);
		formattedString = formattedString.trim();
		formattedString = formattedString.toLowerCase();
		
		logger.debug("formattedString = " + formattedString);
		
		String stringArray[] = formattedString.split(" ");
		formattedString="";
		
		for(String i:stringArray) {
			formattedString+=i;
		}
		
		logger.debug("formattedString after = " + formattedString);
		
		return this.sentinelList.containsKey(formattedString);
	}

	/**
	 * Sets the sentinel list to the list contained in the argument's 
	 * CommandListHolder.  
	 * 
	 * @param arg
	 * @return
	 */
//	private void setSentinelList(CommandListHolder arg1) {
//		sentinelList = arg1.getCommandListHolder();
//	}

	/**
	 * Returns the list of commads corresponding to the byte array 
	 * given as an argument.  
	 * 
	 * @param arg
	 * @return
	 */
	public ArrayList<byte[]> getCommandList(byte[] arg) {
		
		String formattedString = new String(arg);
		formattedString = formattedString.trim();
		formattedString = formattedString.toLowerCase();
		
		logger.debug("formattedString = " + formattedString);
		
		String stringArray[] = formattedString.split(" ");
		formattedString="";
		
		for(String i:stringArray) {
			formattedString+=i;
		}
		ArrayList<byte[]> retVal = this.sentinelList.get(formattedString);
		logger.debug("retVal size=" + retVal.size());
	
		return this.sentinelList.get(formattedString);
	}
	
	/**
	 * Returns the Map associated with this sentinel object.  
	 * 
	 * @return	
	 */
	public Map<String, ArrayList<byte[]>> getCommandMap() {
		return sentinelList;
	}

	/**
	 * Parses a Reader Command XML file and converts them to Command Objects.
	 * Takes in a filename to identify the location of the xml files.
	 * 
	 * @param xmlFilename
	 */
	public void parseToCommand(String xmlFilename) {
		
		logger.debug("starting the parse");
		
		try {
			Digester dig = new Digester();
			dig.setValidating(false);

			// Create the CommandListHolde
			dig.addObjectCreate("SentinelCommand", CommandListHolder.class);
			
			dig.addCallMethod("SentinelCommand/Command/CommandList/CommandArrayObject", "addCommandListArg", 1);
			dig.addCallParam("SentinelCommand/Command/CommandList/CommandArrayObject", 0);
			
			dig.addCallMethod("SentinelCommand/Command/CommandName", "addCommand",1);
			dig.addCallParam("SentinelCommand/Command/CommandName", 0);

			File xmlFile = new File(xmlFilename);

			CommandListHolder newHolder = (CommandListHolder) dig
					.parse(xmlFile);

			this.sentinelList = newHolder.getCommandListHolder();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		logger.debug("finished the parse");
	}
}
