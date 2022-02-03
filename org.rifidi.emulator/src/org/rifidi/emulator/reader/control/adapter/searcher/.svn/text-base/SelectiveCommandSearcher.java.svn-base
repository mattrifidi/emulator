/*
 *  SelectiveCommandSearcher.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.control.adapter.searcher;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * This command searcher will first search for the command outright in the
 * commandHashMap. If it cannot find the command, it will try to split the
 * command string at a period '.'. If the command it found, it will return the
 * command. If it is still not found, it will return null.
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class SelectiveCommandSearcher implements CommandSearcher {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory
			.getLog(SelectiveCommandSearcher.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.control.adapter.searcher.CommandSearcher#search(java.util.ArrayList,
	 *      org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources,
	 *      java.util.HashMap)
	 */
	public CommandObject search(ArrayList<Object> formattedOutput,
			AbstractReaderSharedResources asr,
			HashMap<String, CommandObject> commandHashMap) {
		CommandObject retVal = null;
		String commandName = (String) formattedOutput.get(0);
		commandName = commandName.toLowerCase();

		retVal = commandHashMap.get(commandName);

		if (retVal == null) {

			logger.debug("command is null, splitting at the period and "
					+ "searching again");

			String addString = commandName.split("\\.")[0];

			retVal = (CommandObject) commandHashMap.get(addString);

			if (retVal == null) {
				return null;
			}
		}

		formattedOutput.remove(0);

		for (Object i : formattedOutput) {
			retVal.addArgument(i);
		}

		return retVal;
	}

}
