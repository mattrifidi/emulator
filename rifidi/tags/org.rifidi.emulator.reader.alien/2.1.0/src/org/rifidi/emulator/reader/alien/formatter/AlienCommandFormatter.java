/*
 *  AlienCommandFormatter.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.formatter;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.rifidi.emulator.reader.formatter.CommandFormatter;

/**
 * This class represents a formatter for the Alien 9800 series of readers. It
 * may work for other Alien readers as well.
 * 
 * @author Matthew Dean
 */
public class AlienCommandFormatter implements CommandFormatter {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(AlienCommandFormatter.class);

	private boolean console;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#encode(byte[])
	 */
	public ArrayList<Object> decode(byte[] arg) {
		if (arg.length == 0) {
			ArrayList<Object> argArray = new ArrayList<Object>();
			argArray.add("");
			console = false;
			return argArray;
		}
		String newString = new String(arg);

		logger.debug("Formatter arg: " + newString);

		ArrayList<Object> retVal = this.parseCommand(newString);

		return retVal;
	}

	public String getActualCommand(byte[] arg) {

		String fullInput = new String(arg);

		/* The command is either split by an equal sign or space */
		if (fullInput.contains("=")) {
			String[] tempArray = new String(arg).split("=");
			return tempArray[0].trim();
		} else {
			/*
			 * Just return the first element as seperated by a space.
			 */
			return (fullInput);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(java.util.ArrayList)
	 */
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		StringBuffer retVal = new StringBuffer();
		for (Object i : arg) {
			retVal.append(i);
		}
		arg.clear();
		arg.add(retVal.toString());

		logger.debug("COMMAND IS: " + retVal);

		return arg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#promptSupress()
	 */
	public boolean promptSuppress() {
		return console;
	}

	/*
	 * (non-javadoc) A private method that sorts through a command given to it
	 * and returns: An ArrayList containing 1. The command name (ie
	 * "setPersistTime") 2. The console prompt flag (0 if the prompt is to be
	 * displayed, 1 otherwise) 3. The Argument(s) (For the AlienReader there is
	 * at most 1 of these)
	 */
	private ArrayList<Object> parseCommand(String command) {
		console = false;
		ArrayList<Object> retVal = new ArrayList<Object>();
		// Is there a '\1' in the string or not?
		if (command.charAt(0) == '\1') {
			// If there is, set the resulting flag to "1"
			console = true;
		}
		command = command.trim();
		// The first part of the string, probably 'set' or 'get'
		String first = "";
		// i is position holder, must be declared outside of for loop
		int i = 0;
		for (; i < command.length() && command.charAt(i) != ' '
				&& command.charAt(i) != '='; ++i) {
			first += command.charAt(i);
		}
		command = command.substring(i);
		command = command.trim();
		// This seperates if it is a 1-word command such as "info" or a 2-word
		// command
		// such as "set PersistTime"
		if (command.length() != 0) {
			for (int x = 0; x < command.length() && command.charAt(x) != ' '
					&& command.charAt(x) != '='; ++x) {
				first += command.charAt(x);
			}
		}
		retVal.add(first);
		// If there is an argument, this takes care of it
		if (command.contains("=") && command.split("=").length > 1) {
			String newString[] = command.split("=");
			String arg = newString[1];
			arg = arg.trim();
			retVal.add(arg);
		}
		return retVal;
	}
}
