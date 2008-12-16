/*
 *  Activator.java
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
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationException;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 * 
 */
public class UpdateCommand extends Command {
	private static Log logger = LogFactory.getLog(UpdateCommand.class);

	private String command;
	private String table;

	Map<String, String> keyValuePairs = new HashMap<String, String>();

	private ThingMagicReaderSharedResources tmsr;

	public UpdateCommand(String command, ThingMagicReaderSharedResources tmsr)
			throws CommandCreationException {
		// TODO Auto-generated constructor stub
		this.command = command;
		this.tmsr = tmsr;

		List<String> tokens = tokenizer(command);

		logger.debug(tokens);

		ListIterator<String> tokenIterator = tokens.listIterator();
		String token = tokenIterator.next();

		if (!token.equals("update"))
			throw new CommandCreationException(
					"Error 0100:     syntax error at '" + token + "'");

		try {
			token = tokenIterator.next();

			if (!token.matches(WHITE_SPACE))
				throw new CommandCreationException(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();

			table = token;
			if (!table.matches(A_WORD))
				throw new CommandCreationException(
						"Error 0100:     syntax error at '" + table + "'");

			token = tokenIterator.next();

			if (!token.matches(WHITE_SPACE))
				throw new CommandCreationException(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();

			if (!token.equals("set"))
				throw new CommandCreationException(
						"Error 0100:     syntax error at '" + token + "'");

			token = tokenIterator.next();

			parseKeyValuePairs(tokenIterator);

			// check if the command correctly ends in a semicolon
			if (tokenIterator.hasNext()) {
				token = tokenIterator.next();

				if (token.matches(WHITE_SPACE)) {
					token = tokenIterator.next();
				}

				if (!token.equals(";")) {
					throw new CommandCreationException(
							"Error 0100:     syntax error at '" + token + "'");
				}
			} else {
				throw new CommandCreationException(
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
			throw new CommandCreationException(
					"Error 0100:     syntax error at '" + token + "'");

		}

		IDBTable tableImpl = tmsr.getDataBase().getTable(table);
		if (tableImpl == null) {
			throw new CommandCreationException(
					"Error 0100:     syntax error at '" + table + "'");
		}

		for (int x = 0; x < tableImpl.size(); x++) {
			IDBRow row = tableImpl.get(x);
			for (String column : keyValuePairs.keySet()) {
				if (!row.containsColumn(column)) {
					throw new CommandCreationException(
							"Error 0100:     Unknown " + column);
				}

				// if (!row.isReadable(column)) {
				// throw new CommandCreationExeption(
				// "Error 0100:     Could not read from '" + column
				// + "' in '" + table + "'");
				// }
			}
		}
	}

	/*
	 * private helper method to help break up the logical layout of the parser.
	 */
	private void parseKeyValuePairs(ListIterator<String> tokenIterator)
			throws CommandCreationException, NoSuchElementException {
		// TODO Auto-generated method stub
		String token;
		do {
			token = tokenIterator.next();

			if (!token.matches(A_WORD))
				throw new CommandCreationException(
						"Error 0100:     syntax error at '" + token + "'");

			String key = token;

			token = tokenIterator.next();

			if (!token.matches(EQUALS_WITH_WS))
				throw new CommandCreationException(
						"Error 0100:     syntax error at '" + token + "'");

			StringBuffer valueBuffer = new StringBuffer();
			token = tokenIterator.next();

			if (!token.equals("'")) {
				token = tokenIterator.previous().trim();
				throw new CommandCreationException(
						"Error 0100:     syntax error at '" + token + "'");

			}

			token = tokenIterator.next();
			while (!token.equals("'")) {
				valueBuffer.append(token);
				token = tokenIterator.next();
			}
			logger.debug("key: " + key + " value: '" + valueBuffer.toString()
					+ "'");
			keyValuePairs.put(key, valueBuffer.toString());

			/*
			 * if we have no more tokens. it is valid.. and we should break
			 * here.
			 */
			if (!tokenIterator.hasNext())
				break;

			token = tokenIterator.next();
		} while (token.matches(COMMA_WITH_WS));
	}

	@Override
	public ArrayList<Object> execute() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ArrayList<Object> retVal = new ArrayList<Object>();
		// TODO add filtering
		List<String> columns = new ArrayList<String>(keyValuePairs.keySet());

		logger.debug("Getting table: " + table);
		logger.debug("Getting column data: " + columns);

		tmsr.getDataBase().getTable(table).preTableAccess(null);

		/*
		 * Do the udpate database work.
		 */
		// TODO add filter capability to this method.
		for (int x = 0; x < tmsr.getDataBase().getTable(table).size(); x++) {
			IDBRow row = tmsr.getDataBase().getTable(table).get(x);

			StringBuffer buff = new StringBuffer();
			for (int y = 0; y < keyValuePairs.size(); y++) {
				buff.append(row.put(columns.get(y), keyValuePairs.get(columns
						.get(y))));
				if (y < columns.size() - 1)
					buff.append("|");
			}
			retVal.add(buff.toString());
		}

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
