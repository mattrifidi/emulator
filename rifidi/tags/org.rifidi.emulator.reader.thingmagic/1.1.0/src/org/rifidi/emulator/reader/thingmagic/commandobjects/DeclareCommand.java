/*
 *  DeclareCommand.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 * 
 */
public class DeclareCommand extends Command {
	private static Log logger = LogFactory.getLog(DeclareCommand.class);

	private String cursorName;

	private Command cursorCommand;

	private ThingMagicReaderSharedResources tmsr;

	String command;

	public DeclareCommand(String command, ThingMagicReaderSharedResources tmsr)
			throws CommandCreationExeption {

		this.tmsr = tmsr;
		this.command = command;

		List<String> tokens = tokenizer(command);

		ListIterator<String> tokenIterator = tokens.listIterator();

		String token = tokenIterator.next();

		if (!token.equals("declare"))
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");

		try {
			token = tokenIterator.next();

			if (!token.matches(WHITE_SPACE))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();
			if (token.matches(A_WORD)) {
				cursorName = token;
				logger.debug("Cursor name is " + cursorName);
			} else {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}
			token = tokenIterator.next();

			if (!token.matches(WHITE_SPACE))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();

			if (!token.equals("cursor"))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();

			if (!token.matches(WHITE_SPACE))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();

			if (!token.equals("for"))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();

			if (!token.matches(WHITE_SPACE))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");

			StringBuffer cursorCommandBuf = new StringBuffer();
			token = tokenIterator.next();

			cursorCommandBuf.append(token);

			while (tokenIterator.hasNext()) {
				cursorCommandBuf.append(tokenIterator.next());
			}

			logger.debug("Command is \"" + cursorCommandBuf.toString() + "\"");

			if (token.equals("select")) {
				cursorCommand = new SelectCommand(cursorCommandBuf.toString(),
						tmsr);
			} else if (token.equals("update")) {
				cursorCommand = new UpdateCommand(cursorCommandBuf.toString(),
						tmsr);
			} else {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}

			/*
			 * The SelectCommand and UpdateCommand both check for the semicolon
			 * at the end, so we don't have to do that here.
			 */
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
			while (token.matches("\\s+")) {
				token = tokenIterator.previous();
			}
			logger.debug("Premature end of token list detected.");
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");

		}

		// if (!(tmsr.getCursorCommandRegistry().size() <= 16)) {
		// /* we can only hold no more than 16 cursors
		// *
		// */
		//			
		// //TODO Correct the messsage.
		// throw new CommandCreationExeption(
		// "Error 0100:     Can not old no more than 16 cursors.");
		// }

		if (tmsr.getCursorCommandRegistry().containsKey(cursorName)) {
			// TODO Correct the message.
			throw new CommandCreationExeption(
					"Error 0100:	Cursor already exists");
		}

	}

	@Override
	public ArrayList<Object> execute() {

		tmsr.getCursorCommandRegistry().put(cursorName, cursorCommand);

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
