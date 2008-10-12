/*
 *  SingleFilter.java
 *
 *  Created:	September 25, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.conditional;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.commandobjects.Command;
import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.database.DataBase;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 * 
 */
public class SingleFilter implements IFilter {
	private static Log logger = LogFactory.getLog(SingleFilter.class);

	private String attribute;
	private ECompareOperator compareOperator;

	private String testValue;

	private boolean unknown;

	public SingleFilter(ListIterator<String> tokenIterator, String table,
			ThingMagicReaderSharedResources tmsr)
			throws CommandCreationExeption {
		logger.debug("Creating Single filter...");

		DataBase db = tmsr.getDataBase();

		String token = tokenIterator.next();

		if (token.matches(Command.WHITE_SPACE)) {
			token = tokenIterator.next();
		}

		if (!token.matches(Command.A_WORD)) {
			throw new CommandCreationExeption("Error 0100:	syntax error at '"
					+ token + "'");
		}

		attribute = token;

		// TODO See if this behavior is correct.
		if (!db.getTable(table).get(0).containsColumn(attribute)) {
			unknown = true;
		}
		token = tokenIterator.next();

		if (token.matches(Command.EQUALS_WITH_WS)) {
			compareOperator = ECompareOperator.EQUAL;
		} else if (token.matches(Command.GREATER_THAN_EQUALS_W_WS)) {
			compareOperator = ECompareOperator.GREATER_THAN_EQUAL;
		} else if (token.matches(Command.LESS_THAN_EQUALS_W_WS)) {
			compareOperator = ECompareOperator.LESS_THAN_EQUAL;
		} else if (token.matches(Command.GREATER_THAN_WITH_WS)) {
			compareOperator = ECompareOperator.GREATER_THAN;
		} else if (token.matches(Command.LESS_THAN_WITH_WS)) {
			compareOperator = ECompareOperator.LESS_THAN;
		} else if (token.matches(Command.NOT_EQUALS_W_WS)) {
			compareOperator = ECompareOperator.NOT_EQUAL;
		} else {
			throw new CommandCreationExeption("Error 0100:	syntax error at '"
					+ token + "'");
		}

		token = tokenIterator.next();

		if (token.equals("'")) {
			//TODO is this true with other tables??
			if (!attribute.equalsIgnoreCase("protocol_id")) {
				throw new CommandCreationExeption(
						"Error 0100:	Only protocol_id can be used with a string in a WHERE clause");
			}
			/*
			 * gather the quoted string... including the quotes
			 */
			StringBuffer valueBuffer = new StringBuffer();
			valueBuffer.append(token);
			token = tokenIterator.next();
			while (!token.equals("'")) {
				valueBuffer.append(token);
				token = tokenIterator.next();
			}
			valueBuffer.append(token);
			testValue = valueBuffer.toString();
		} else {
			//TODO Figure out a better way of testing if it is a valid number.
			/*
			 * Test if it is a valid number... if not throw an CommandCreationExeption 
			 */
			try {
				Integer.valueOf(token);
			} catch (NumberFormatException e){
				throw new CommandCreationExeption("Error 0100:	syntax error at '"
						+ token + "'");
			}
			testValue = token;
		}
	}

	@Override
	public List<IDBRow> filter(List<IDBRow> rows) {
		// TODO Auto-generated method stub

		List<IDBRow> retVal = new ArrayList<IDBRow>();

		// TODO See if this behavior is correct.
		if (unknown){
			return retVal;
		}
		
		for (IDBRow row : rows) {
			if (compareOperator == ECompareOperator.EQUAL) {
				if (row.compareToValue(attribute, testValue) == 0)
					retVal.add(row);
			}

			if (compareOperator == ECompareOperator.NOT_EQUAL) {
				if (row.compareToValue(attribute, testValue) != 0)
					retVal.add(row);
			}

			if (compareOperator == ECompareOperator.GREATER_THAN_EQUAL) {
				if (row.compareToValue(attribute, testValue) >= 0)
					retVal.add(row);
			}

			if (compareOperator == ECompareOperator.LESS_THAN_EQUAL) {
				if (row.compareToValue(attribute, testValue) <= 0)
					retVal.add(row);
			}

			if (compareOperator == ECompareOperator.GREATER_THAN) {
				if (row.compareToValue(attribute, testValue) > 0)
					retVal.add(row);
			}

			if (compareOperator == ECompareOperator.LESS_THAN) {
				if (row.compareToValue(attribute, testValue) < 0)
					retVal.add(row);
			}
		}
		return retVal;
	}

}
