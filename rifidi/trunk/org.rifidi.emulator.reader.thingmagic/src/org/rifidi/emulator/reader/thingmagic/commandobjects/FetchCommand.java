/*
 *  FetchCommand.java
 *
 *  Created:	August 7, 2008
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
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.commandregistry.CursorCommandRegistry;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class FetchCommand extends Command {
	private static Log logger = LogFactory.getLog(FetchCommand.class);
	
	
	private String command;
	private ThingMagicReaderSharedResources tmsr;
	
	List<String> fetchList = new ArrayList<String>();

	public FetchCommand(String command, ThingMagicReaderSharedResources tmsr) throws CommandCreationExeption {
		// TODO Auto-generated constructor stub
		this.command = command;
		this.tmsr = tmsr;
		
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
				"[^\\s\\w,]+|" +
				// groups we are looking for...
				"\\w+|" +
				"\\s*,\\s*|" +
				"\\s?+", Pattern.CASE_INSENSITIVE
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

		if (!token.equals("fetch"))
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");

		try {
			token = tokenIterator.next();
			
			if (!token.matches(WHITE_SPACE))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			
			do {
				token = tokenIterator.next();
				if (!token.matches(A_WORD)){
					throw new CommandCreationExeption(
							"Error 0100:     syntax error at '" + token + "'");
				}
				
				
				if (!tmsr.getCursorCommandRegistry().containsKey(token)){
					throw new CommandCreationExeption(
							"Error 0100:	Cursor does not exist");
				}
				
				fetchList.add(token);
				
				if (!tokenIterator.hasNext()){
					break;
				}
				
				token = tokenIterator.next();
				if (!token.matches(COMMA_WITH_WS)){
					throw new CommandCreationExeption(
							"Error 0100:     syntax error at '" + token + "'");
				}
				
			} while (true);
			
			
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
			
		} catch (NoSuchElementException e) {
			/*
			 * if we get here... we run out of tokens prematurely... Our job now
			 * is to walk backwards to find the last non space tokens and throw
			 * an exception saying that there is an syntax error at that point.
			 */

			/*
			 * look for the last offending command block that is not a series of
			 * whitespaces.
			 */

			token = tokenIterator.previous();
			while (token.matches(WHITE_SPACE)) {
				token = tokenIterator.previous();
			}
			logger.debug("Premature end of token list detected.");
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");

		}
		
	}

	@Override
	public ArrayList<Object> execute() {
		// TODO Auto-generated method stub
		ArrayList<Object> retVal = new ArrayList<Object>();
		CursorCommandRegistry regsitry = tmsr.getCursorCommandRegistry();
		
		for(String fetching: fetchList){
			retVal.addAll(regsitry.get(fetching).execute());
		}
		
		return retVal;
	}

	@Override
	public String toCommandString() {
		// TODO Auto-generated method stub
		return command;
	}

}
