/*
 *  CommandSearcher.java
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
 * 
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public interface CommandSearcher {

	/**
	 * This method takes in the formatted output that came out of the formatter, 
	 * the AbstractReaderSharedResources, and the HashMap that lists all of the 
	 * commands for this current state, and then finds the CommandObject in 
	 * commandHashMap based on the information in formattedOutput.  If no 
	 * command can be found, it returns null.  
	 * 
	 * @param formattedOutput
	 * @param asr
	 * @param commandHashMap
	 * @return
	 */
	public CommandObject search(ArrayList<Object> formattedOutput,
			AbstractReaderSharedResources asr,
			HashMap<String, CommandObject> commandHashMap);
}
