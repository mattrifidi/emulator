/*
 *  RawCommandSearcher.java
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
 * This is a version of the commandSearcher which will simply search for the
 * String outright in the commandHashMap. If no String is found, it will return
 * null.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class RawCommandSearcher implements CommandSearcher {

	/**
	 * Message logger
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory
			.getLog(RawCommandSearcher.class);
	
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
		commandName=commandName.toLowerCase();
		
		if (commandHashMap.containsKey(commandName)) {
			retVal = commandHashMap.get(commandName);

			formattedOutput.remove(0);

			for (Object i : formattedOutput) {
				retVal.addArgument(i);
			}
		}

		return retVal;
	}

}
