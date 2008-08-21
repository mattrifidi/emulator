package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.database.impl.DBTagID;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

public class SelectCommand implements Command {
	private static Log logger = LogFactory.getLog(SelectCommand.class);

	private String command;

	private List<String> columns = new ArrayList<String>();
	private String table;

	private ThingMagicReaderSharedResources tmsr;

	public SelectCommand(String command, ThingMagicReaderSharedResources tmsr)
			throws CommandCreationExeption {
		this.tmsr = tmsr;
		this.command = command;
		List<String> tokens = new ArrayList<String>();
		// TODO Auto-generated constructor stub

		logger.debug("Parsing command: " + command);

		Pattern tokenizer = Pattern.compile(
				"\\w+|\\s*<>\\*|\\s*>=\\s*|\\s*<=\\s*|\\s*=\\s*|\\s*,\\s*|\\s?+|"
						+ ">|<|\\(|\\)|'|[^\\s\\w,<>=\\(\\)']+",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher tokenFinder = tokenizer.matcher(command.toLowerCase().trim());

		while (tokenFinder.find()){
			String temp = tokenFinder.group();		
			/* 
			 * no need to add empty strings at tokens.
			 * 
			 */
			//TODO: Figure out why we are getting empty stings as tokens.
			if (temp.equals(""))
				continue;
			tokens.add(temp);
		}

		logger.debug("Command Blockes: " + tokens);

		int index = 0;

		if (!tokens.get(index).equals("select")) {
			throw new CommandCreationExeption(
					"Error 0100:     syntax error at '"
							+ tokens.get(index) + "'");

		}
		index++;

		logger.debug("Huray! we are in the correct command object!");
		// TODO Refine the error handling to mirror more exactly what the
		// thingmagic would return;
		try {
			/*
			 * Look for white spaces
			 */
			if (!tokens.get(index).matches("\\s+")) {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '"
								+ tokens.get(index--) + "'");
			}

			index++;

			logger.debug("Expected whitespace found.");

			// TODO: Handle errors correctly here.
			for (; !tokens.get(index).equals("from"); index++) {
				/*
				 * look for words
				 */
				if (tokens.get(index).matches("\\w+")) {
					columns.add(tokens.get(index));
				} else {
					throw new CommandCreationExeption(
							"Error 0100:     syntax error at '"
									+ tokens.get(index) + "'");
				}
				index++;

				if (tokens.get(index + 1).equals("from")) {
					logger.debug("Found keyword from");
					if (!tokens.get(index).matches("\\s*")) {
						logger.debug("Bad command block found.");
						throw new CommandCreationExeption(
								"Error 0100:     syntax error at 'from'");
					}

					/*
					 * move the index to the "from" command block.
					 */
					index++;
					break;
				}

				/*
				 * look for a comma with any number of white spaces on either
				 * side.
				 */
				if (!tokens.get(index).matches("\\s*,\\s*")) {
					throw new CommandCreationExeption(
							"Error 0100:     syntax error at '"
									+ tokens.get(index++) + "'");
				}

			}
			index++;

			/*
			 * Look for white spaces
			 */
			if (!tokens.get(index).matches("\\s+")) {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '"
								+ tokens.get(index--) + "'");
			}
			index++;

			/*
			 * Look for words
			 */
			if (tokens.get(index).matches("\\w+")) {
				table = tokens.get(index);
			} else {
				throw new CommandCreationExeption(
						"Error 0100:     syntax error at '"
								+ tokens.get(index) + "'");
			}

			index++;
			if (tokens.size() < index) {
				/*
				 * Look for white spaces
				 */
				if (!tokens.get(index).matches("\\s+")) {
					// TODO throw an exception
				} else {
					index++;

					if ((tokens.size() < index)
							&& (tokens.get(index).matches("where"))) {

						/*
						 * Look for white spaces
						 */
						if (!tokens.get(index).matches("\\s+")) {
							// TODO throw an exception
						}
						index++;

						for (; index < tokens.size(); index++) {
							if (tokens.get(index).equals("set"))
								break;
							// TODO parse where clause
							// whereClause.append(args.get(index));
						}
					}

					if ((tokens.size() < index)
							&& (tokens.get(index).matches("set"))) {
						// TODO implement the set clause
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
			/*
			 * look for the last offending command block that is not a series of
			 * whitespaces.
			 */
			for (int x = tokens.size() - 1; x >= 0; x--) {
				if (!tokens.get(x).matches("\\s")) {
					throw new CommandCreationExeption(
							"Error 0100:     syntax error at '"
									+ tokens.get(index) + "'");
				}
			}
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

		/*
		 * Do the select database work.
		 */
		for (int x = 0; x < tmsr.getDataBase().getTable(table).size(); x++) {
			IDBRow row = tmsr.getDataBase().getTable(table).get(x);

			StringBuffer buff = new StringBuffer();
			for (int y = 0; y < columns.size(); y++) {
				buff.append(row.get(columns.get(y)));
				if (y < columns.size() - 1)
					buff.append("|");
			}
			retVal.add(buff.toString());
		}
		return retVal;
	}

	@Override
	public String toCommandString() {
		// TODO Auto-generated method stub
		return command;
	}

}
