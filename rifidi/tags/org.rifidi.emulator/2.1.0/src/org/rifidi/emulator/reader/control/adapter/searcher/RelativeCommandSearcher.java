/*
 *  RelativeCommandSearcher.java
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

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * This command searcher will first search for a
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class RelativeCommandSearcher implements CommandSearcher {

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

		String currentCommandName = "";
		boolean keepRunning = true;
		CommandObject inputCommand = null;
		String commandName = (String) formattedOutput.get(0);
		commandName = commandName.toLowerCase();

		String[] tempStringTokens = commandName.split("_");
		int tempStringTokenNumber = 0;
		while (tempStringTokenNumber < tempStringTokens.length && keepRunning) {
			if (tempStringTokenNumber != 0) {
				currentCommandName += "_";
			}

			currentCommandName += tempStringTokens[tempStringTokenNumber];

			inputCommand = (CommandObject) commandHashMap
					.get(currentCommandName);
			// inputCommand.reset();
			++tempStringTokenNumber;
			if (inputCommand != null) {
				keepRunning = false;
				for (int x = tempStringTokenNumber; x < tempStringTokens.length; x++) {
					inputCommand.getArguments().add(tempStringTokens[x]);
				}
			}
		}
		return inputCommand;
	}
}
