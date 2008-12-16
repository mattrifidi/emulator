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
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.rifidi.emulator.reader.formatter.CommandFormatter;

/**
 * This class represents a formatter for the Alien 9800 series of readers. It
 * may work for other Alien readers as well.
 * 
 * @author Matthew Dean
 */
public class AlienLoginFormatter implements CommandFormatter {

	/**
	 * Message logger
	 */
	private static Log logger =
		 LogFactory.getLog(AlienLoginFormatter.class);

	private boolean console;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#encode(byte[])
	 */
	public ArrayList<Object> decode(byte[] arg) {

		String newString = new String(arg);

		logger.debug("Formatter arg: " + newString);

		ArrayList<Object> retVal = this.parseCommand(newString);

		return retVal;
	}

	public String getActualCommand(byte[] arg) {

		String fullInput = new String(arg);

		logger.debug("Actual Command is: " + fullInput);

		/* The command is either split by an equal sign or space */
		if (fullInput.contains("=")) {
			String[] tempArray = new String(arg).split("=");
			return tempArray[0].trim();
		} else {
			/*
			 * Just return the first element as seperated by a space.
			 */
			StringTokenizer st = new StringTokenizer(fullInput, " ");
			String fullCmd = st.nextToken();
			return (fullCmd);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(java.util.ArrayList)
	 */
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		/*
		 * byte[] retVal; String tempString = ""; for (Object i : arg) {
		 * tempString += (String) i; }
		 * 
		 * retVal = tempString.getBytes(); return retVal;
		 */
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
	 * and
	 * 
	 * 
	 * returns: An ArrayList containing 1. The command name (ie
	 * "setPersistTime") 2. The console prompt flag (0 if the prompt is to be
	 * displayed, 1 otherwise) 3. The Argument(s) (For the AlienReader there is
	 * at most 1 of these)
	 */
	private ArrayList<Object> parseCommand(String command) {
		/*
		 * The default is to always show a prompt and thsi will make sure that
		 * suppress prompt is false
		 */
		console = false;

		logger.debug("Inside Parse Command - The command value is: " + command);

		ArrayList<Object> retVal = new ArrayList<Object>();

		/* Is there a '\1' in the string or not? */
		if (command.contains("\1")) {
			/* If there is, set the resulting flag to "1" */
			logger.debug("In the CONTAINS if for the \\1");
			console = true;
			command = command.replace("\1", "");
		}
		command = command.trim();

		/* The command part of the string */
		String first = "";
		/* The argument portion of the string */
		ArrayList<String> arg = new ArrayList<String>();

		/* i is position holder, must be declared outside of for loop */
		int i = 0;

		/* This loop removes the first word and sets it as the command */
		for (; i < command.length() && command.charAt(i) != ' '; ++i) {
			first += command.charAt(i);
		}
		command = command.substring(i);
		command = command.trim();

		logger.debug("First element is : " + first);
		logger.debug("The other elements are: " + command);

		/*
		 * This seperates if it is a 1-word command such as "info" or a 2-word
		 * command such as "set PersistTime"
		 */
		if (command.length() != 0) {
			if (command.contains("=")) {
				/*
				 * This logic is only executed if it is a command that uses
				 * equals to pass arguments
				 */
				for (int x = 0; command.charAt(x) != ' '
						&& command.charAt(x) != '='; ++x) {
					first += command.charAt(x);
				}
			} else {
				/*
				 * This is for commands that pass in an argument directly after
				 * the command seperated only by a space and no equals.
				 */
				StringTokenizer st = new StringTokenizer(command, " ");
				while (st.hasMoreTokens()) {
					arg.add(st.nextToken());
				}
			}
		}

		if (first.length() > 0) {
			/* Add the command as the first value */
			retVal.add(first);
			/* Only add an argument if there is a command */
			if (arg.size() > 0)
				for (String x : arg) {
					retVal.add(x);
				}
		}

		/* If there is an argument with an equal sign, this takes care of it */
		if (command.contains("=")) {
			String newString[] = command.split("=");
			retVal.add(newString[1]);
		}
		return retVal;
	}
}
