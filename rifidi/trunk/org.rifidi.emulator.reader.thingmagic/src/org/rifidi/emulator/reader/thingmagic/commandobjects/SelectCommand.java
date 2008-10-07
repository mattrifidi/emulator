/*
 *  SelectCommand.java
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
import org.rifidi.emulator.reader.thingmagic.conditional.MasterFilter;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.database.impl.DBTagID;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 * 
 */
public class SelectCommand extends Command {
	private static Log logger = LogFactory.getLog(SelectCommand.class);

	private String command;

	private List<String> columns = new ArrayList<String>();
	private String table;

	private ThingMagicReaderSharedResources tmsr;

	private MasterFilter filter;

	public SelectCommand(String command, ThingMagicReaderSharedResources tmsr)
			throws CommandCreationExeption {
		this.tmsr = tmsr;
		this.command = command;

		// TODO Auto-generated constructor stub

		logger.debug("Parsing command: " + command);

		List<String> tokens = tokenizer(command);

		ListIterator<String> tokenIterator = tokens.listIterator();

		String token = tokenIterator.next();

		if (!token.equals("select")) {
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");

		}

		logger.debug("Huray! we are in the correct command object!");
		// TODO Refine the error handling to mirror more exactly what the
		// thingmagic would return;
		try {
			token = tokenIterator.next();

			/*
			 * Look for white spaces
			 */
			if (!token.matches(WHITE_SPACE)) {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}

			logger.debug("Expected whitespace found.");
			do {
				token = tokenIterator.next();

				/*
				 * look a word
				 */
				if (token.matches(A_WORD)) {
					columns.add(token);
				} else {
					throw new CommandCreationExeption(
							"Error 0100:     syntax error at '" + token + "'");
				}
				token = tokenIterator.next();
				/*
				 * look for a comma with any number of white spaces on either
				 * side.
				 */
			} while (token.matches(COMMA_WITH_WS));

			if (tokenIterator.next().equals("from")) {

				if (!token.matches(WHITE_SPACE))
					throw new CommandCreationExeption(
							"Error 0100:     syntax error at 'from'");

			} else {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at ','");
			}

			token = tokenIterator.next();
			/*
			 * Look for white spaces
			 */
			if (!token.matches(WHITE_SPACE)) {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}

			token = tokenIterator.next();

			/*
			 * Look for words
			 */
			if (token.matches(A_WORD)) {
				table = token;
			} else {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '" + token + "'");
			}

			if (tokenIterator.hasNext()) {
				token = tokenIterator.next();

				if (token.matches(WHITE_SPACE)) {

					if (tokenIterator.hasNext()) {
						token = tokenIterator.next();

						if (token.equals("where")) {

							token = tokenIterator.next();

							if (!token.matches(WHITE_SPACE)) {
								throw new CommandCreationExeption(
										"Error 0100:     syntax error at '"
												+ token + "'");
							}

							filter = new MasterFilter(tokenIterator, table,
									tmsr);
						}
					}

				}
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
			while (token.matches("\\s+")) {
				token = tokenIterator.previous();
			}
			logger.debug("Premature end of token list detected.");
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + token + "'");
		}

		IDBTable tableImpl = tmsr.getDataBase().getTable(table);
		if (tableImpl == null) {
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '" + table + "'");
		}

		/*
		 * This is an exception to the rule for the thingmagic reader. If we are
		 * reading a tag... make best effort to complete the command in an
		 * orderly and predictable manner.
		 */
		if (tableImpl instanceof DBTagID) {
			return;
		}

		for (int x = 0; x < tableImpl.size(); x++) {
			IDBRow row = tableImpl.get(x);
			for (String column : columns) {
				if (!row.containsColumn(column)) {
					throw new CommandCreationExeption(
							"Error 0100:     Unknown " + column);
				}

				if (!row.isReadable(column)) {
					throw new CommandCreationExeption(
							"Error 0100:     Could not read from '" + column
									+ "' in '" + table + "'");
				}
			}
		}
	}

	@Override
	public ArrayList<Object> execute() {
		// TODO Auto-generated method stub
		ArrayList<Object> retVal = new ArrayList<Object>();
		// TODO add filtering
		logger.debug("Getting table: " + table);
		logger.debug("Getting column data: " + columns);

		tmsr.getDataBase().getTable(table).preTableAccess(null);

		// TODO implement this better.
		List<IDBRow> rows = new ArrayList<IDBRow>();
		for (int x = 0; x < tmsr.getDataBase().getTable(table).size(); x++) {
			rows.add(tmsr.getDataBase().getTable(table).get(x));
		}

		//TODO: ThingMagic reader ignores the filter when it is not the tag_id or tag_data tables
		if (filter != null) {
			rows = filter.filter(rows);
		}
		/*
		 * Do the select database work.
		 */
		for (int x = 0; x < rows.size(); x++) {
			IDBRow row = rows.get(x);

			StringBuffer buff = new StringBuffer();
			for (int y = 0; y < columns.size(); y++) {
				buff.append(row.get(columns.get(y)));
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
