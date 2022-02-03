/*
 *  SiritReader.java
 *
 *  Created:	05.08.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.commandhandler;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * This is the class for all commands of sirit's "reader" namespace
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritReader {

	/** logger instance for this class. */
	private static Log logger = LogFactory.getLog(SiritReader.class);
	
	
	public CommandObject reader_isAlive(CommandObject arg,
			AbstractReaderSharedResources asr) {

		logger.debug("SiritReader - reader_isAlive()");
	
		/* The return value for this command */
		ArrayList<Object> returnArray = new ArrayList<Object>();
		
		String returnVal = "ok";
		/* proper termination of return string */
		returnVal += SiritCommon.ENDOFREPLY;
		
		returnArray.add(returnVal);
		
		/* Set the return value */
		arg.setReturnValue(returnArray);

		return arg;
	}
}
