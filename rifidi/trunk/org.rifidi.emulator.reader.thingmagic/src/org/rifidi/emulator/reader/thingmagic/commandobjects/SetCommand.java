/*
 *  SetCommand.java
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
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class SetCommand implements Command {
	private static Log logger = LogFactory.getLog(SetCommand.class);

	private ESetSubCommand commandSwitch;

	private String command;
	private ThingMagicReaderSharedResources tmsr;

	/*
	 * Used only to set the delay time between cursor list repeats.
	 */
	private long cursorListDelayRepeat;

	/*
	 * Used only when starting AutoMode
	 */
	private List<Command> cursorList = new ArrayList<Command>();

	/*
	 * Again... only used when starting AutoMode.
	 */
	private long repeat = 0;

	
	/*
	 * KLUGE: This is one of the hardest commands to understand (code wise).
	 * Don't modify unless you read and understand 
	 * the RQL Manual (Mercury 4, ThingMagic RFID Reader).
	 * 
	 * The reason is that there are three possible syntaxes depending on what
	 * one wants to do... This makes it slightly hard to code and understand.
	 */
	public SetCommand(String command, ThingMagicReaderSharedResources tmsr)
			throws CommandCreationExeption {
		// TODO Auto-generated constructor stub
		this.command = command;
		this.tmsr = tmsr;

		List<String> tokens = new ArrayList<String>();

		logger.debug("Parsing command: " + command);

		Pattern tokenizer = Pattern.compile(
		// anything less...
				"[^\\s\\w,<>=\\(\\)\\u0027]|"
						+
						// groups we are looking for...
						"\\w+|" + "\\u0027|" + "\\s*<>\\*|" + "\\s*>=\\s*|"
						+ "\\s*<=\\s*|" + "\\s*=\\s*|" + "\\s*,\\s*|"
						+ "\\s*>\\s*|" + "\\s*<\\s*|" + "\\s?+|" + "\\(|"
						+ "\\)|", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
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

		if (!token.equals("set"))
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");

		try {

			token = tokenIterator.next();

			if (!token.matches(WHITE_SPACE)) {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}

			/*
			 * here possible syntax can diverge into two possible directions...
			 */
			token = tokenIterator.next();
			if (token.matches("auto")) {
				logger.debug("Expecting to turn on or off AutoMode");

				/*
				 * here possible syntax can diverge into two possible directions
				 */
				token = tokenIterator.next();
				if (token.matches(EQUALS_WITH_WS)) {
					logger.debug("Expecting to turn of AutoMode");
					setUpStop(tokenIterator);
				} else if (token.matches(WHITE_SPACE)) {
					logger.debug("Expecting to turn of AutoMode");
					setUpStart(tokenIterator);
				} else {
					throw new CommandCreationExeption(
							"Error 0100:     syntax error at '" + token
									+ "'");
				}

			} else if (token.matches("repeat")) {
				logger.debug("Expecting to set cursorListRepeatDelay");
				setUpCursorListDelay(tokenIterator);
			} else {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}

			// check if the command correctly ends in a semicolon
			if (tokenIterator.hasNext()) {
				token = tokenIterator.next();

				if (token.matches(WHITE_SPACE)) {
					token = tokenIterator.next();
				}

				if (!token.equals(";")) {
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
	
	/*
	 * Helper methods to make reading code easer.
	 */
	
	private void setUpStart(ListIterator<String> tokenIterator) throws CommandCreationExeption{
		/*
		 * we are expecting a "turn on" command
		 */
		String token;
		do {

			token = tokenIterator.next();
			if (!token.matches(WHITE_SPACE)) {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token
								+ "'");
			}

			if (!tmsr.getCursorCommandRegistry().containsKey(token)) {
				throw new CommandCreationExeption(
						"Error 0100:	Cursor does not exist");
			}
			
			cursorList.add(tmsr.getCursorCommandRegistry().get(token));
			
			token = tokenIterator.next();
			
		} while (token.matches(COMMA_WITH_WS));
		
		if (!token.matches(EQUALS_WITH_WS)){
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token
							+ "'");
		}
		
		token = tokenIterator.next();
		
		if (!token.equalsIgnoreCase("ON")){
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token
							+ "'");
		}
		
		token = tokenIterator.next();
		if (token.matches(COMMA_WITH_WS)){
			
			token = tokenIterator.next();
			if (!token.equals("repeat")){
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token
								+ "'");
			}
			
			token = tokenIterator.next();
			
			if (token.matches(EQUALS_WITH_WS)){
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token
								+ "'");
			}
			
			token = tokenIterator.next();
			try {
				repeat = Long.parseLong(token);
			} catch (NumberFormatException e) {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}
		} else {
			/*
			 * rewind one token.
			 */
			tokenIterator.previous();
		}
		
		commandSwitch = ESetSubCommand.START;
	}
	
	private void setUpStop(ListIterator<String> tokenIterator) throws CommandCreationExeption{
		/*
		 * we are expecting a "turn off" command
		 */
		String token = tokenIterator.next();

		if (!token.equalsIgnoreCase("OFF")) {
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token
							+ "'");
		}

		/*
		 * Tell the execute method we are stopping.
		 */
		commandSwitch = ESetSubCommand.STOP;
	}

	private void setUpCursorListDelay(ListIterator<String> tokenIterator) throws CommandCreationExeption{
		
		String token = tokenIterator.next();
		if (!token.matches(EQUALS_WITH_WS)) {
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");
		}

		/*
		 * try to set the value on how long we delay before we repeat
		 * the cursor list again.
		 */
		token = tokenIterator.next();
		try {
			cursorListDelayRepeat = Long.parseLong(token);

			/*
			 * Tell the execute method that we are setting the cursor
			 * list delay repeat value.
			 */
			commandSwitch = ESetSubCommand.SET_CURSOR_LIST_REPEAT;
		} catch (NumberFormatException e) {
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");
		}
	}
	@Override
	public ArrayList<Object> execute() {
		// TODO Auto-generated method stub
		if (commandSwitch == ESetSubCommand.START) {
			logger.debug("Staring AutoMode");
			tmsr.getAutoModeControler().start(cursorList, repeat);
		}

		if (commandSwitch == ESetSubCommand.STOP) {
			logger.debug("Stopping AutoMode");
			tmsr.getAutoModeControler().stop();
		}

		if (commandSwitch == ESetSubCommand.SET_CURSOR_LIST_REPEAT) {
			logger.debug("Settig Cursor List Delay Repeat Value");
			tmsr.getAutoModeControler().setCursorListRepeat(
					cursorListDelayRepeat);
		}

		ArrayList<Object> retVal = new ArrayList<Object>();
		retVal.add("");
		return retVal;
	}

	@Override
	public String toCommandString() {
		// TODO Auto-generated method stub
		return command;
	}

}
