/*
 *  ResetCommand.java
 *
 *  Created:	September 4, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class ResetCommand extends Command {

	private String command;
	private ThingMagicReaderSharedResources tmsr;

	public ResetCommand(String command, ThingMagicReaderSharedResources tmsr)
	throws CommandCreationExeption {
		this.tmsr = tmsr;
		this.command = command;

		List<String> tokens = new ArrayList<String>();
		/*
		 * This regex looks for a Word, or a series of spaces on either side of
		 * any single comparison operator or comma, or a single parentheses
		 * (opening or closing). At the last ... any dangling spaces not
		 * attached to the above groups and then anything else as a single
		 * group.
		 * 
		 * This makes it really easy to parse the command string as it becomes
		 * really predictable tokens.
		 */
		Pattern tokenizer = Pattern.compile(
		// anything less...
				"[^\\s\\w]+|" +
				// groups we are looking for...
						"\\w+|" + "\\s+", Pattern.CASE_INSENSITIVE
						| Pattern.DOTALL);
		Matcher tokenFinder = tokenizer.matcher(command.toLowerCase().trim());

		while (tokenFinder.find()) {
			String temp = tokenFinder.group();
			/*
			 * no need to add empty strings at tokens.
			 */
			// TODO: Figure out why we are getting empty stings as tokens.
			if (temp.equals(""))
				continue;
			tokens.add(temp);
		}

		ListIterator<String> tokenIterator = tokens.listIterator();

		String token = tokenIterator.next();

		if (!token.equals("reset"))
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");
		
		// check if the command correctly ends in a semicolon
		if (tokenIterator.hasNext()){
			token = tokenIterator.next();
			
			if (token.matches(WHITE_SPACE)){
				token = tokenIterator.next();
			}
			
			if (!token.equals(";")){
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}
		} else {
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '\n'");
		}
	}
	
	
	@Override
	public ArrayList<Object> execute() {
		
		tmsr.getCursorCommandRegistry().clear();
		//TODO add more things that need to be reset
		
		ArrayList<Object> retVal = new ArrayList<Object>();

		/*
		 * there must be a blank line at the end.. even if we didn't send
		 * something useful back.
		 * 
		 * When the messages are formated for return (in
		 * ThingMagicRQLCommandFormatter) a new line is appended to each string
		 * even if it is an empty string.
		 */
		// place holder for newline.
		retVal.add("");

		return retVal;
	}

	@Override
	public String toCommandString() {
		// TODO Auto-generated method stub
		return command;
	}

}
