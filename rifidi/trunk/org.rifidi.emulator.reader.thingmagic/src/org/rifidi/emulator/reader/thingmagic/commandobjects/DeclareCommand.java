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

public class DeclareCommand implements Command {
	private static Log logger = LogFactory.getLog(DeclareCommand.class);

	private String cursorName;
	
	private Command cursorCommand;

	private ThingMagicReaderSharedResources tmsr;
	
	String command;

	public DeclareCommand(String command, ThingMagicReaderSharedResources tmsr)
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

		if (!token.equals("update"))
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");

		try {
			token = tokenIterator.next();

			if (!token.matches("\\s+"))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();
			if (token.matches("\\w+")) {
				cursorName = token;
			} else {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}
			token = tokenIterator.next();

			if (!token.matches("\\s+"))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();
			
			if (!token.equals("cursor"))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			
			token = tokenIterator.next();
			
			if (!token.matches("\\s+"))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();

			if (!token.equals("for"))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			
			token = tokenIterator.next();
			
			if (!token.matches("\\s+"))
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			
			
			StringBuffer cursorCommandBuf = new StringBuffer();
			token = tokenIterator.next();
			
			cursorCommandBuf.append(token);
			
			while (tokenIterator.hasNext()){
				cursorCommandBuf.append(tokenIterator.next());
			}
			
			if (token.equals("select")) {
				cursorCommand = new SelectCommand(cursorCommandBuf.toString(), tmsr);
			} else if (token.equals("update")) {
				cursorCommand = new UpdateCommand(cursorCommandBuf.toString(), tmsr);
			} else {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
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
			while (token.matches("\\s+")) {
				token = tokenIterator.previous();
			}
			logger.debug("Premature end of token list detected.");
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");

		}
		
//		if (!(tmsr.getCursorCommandRegistry().size() <= 16)) {
//			/* we can only hold no more than 16 cursors
//			 * 
//			 */
//			
//			//TODO Correct the message.
//			throw new CommandCreationExeption(
//					"Error 0100:     Can not old no more than 16 cursors.");
//		}
		
		if (tmsr.getCursorCommandRegistry().containsKey(cursorName)){
			//TODO Correct the message.
			throw new CommandCreationExeption(
					"Error 0100:	Cursor already exists");
		}
		
	}

	@Override
	public ArrayList<Object> execute() {
		// TODO Auto-generated method stub
		tmsr.getCursorCommandRegistry().put(cursorName, cursorCommand);
		
		ArrayList<Object> retVal = new ArrayList<Object>();
		retVal.add("\n");
		return retVal;
	}

	@Override
	public String toCommandString() {
		// TODO Auto-generated method stub
		return command;
	}

}
