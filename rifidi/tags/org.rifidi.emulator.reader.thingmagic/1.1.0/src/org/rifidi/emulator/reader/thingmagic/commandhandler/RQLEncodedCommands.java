/*
 *  RQLEncodedCommands.java
 *
 *  Created:	May 5, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.commandhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.thingmagic.commandobjects.Command;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class RQLEncodedCommands {
	
	private static Log logger = LogFactory.getLog(RQLEncodedCommands.class);
	
	public CommandObject connect(CommandObject arg,
			AbstractReaderSharedResources asr){
		logger.debug("Dummy connect called in ThingMagic handler method");
		return arg;
	}	

	public CommandObject execute(CommandObject arg,
			AbstractReaderSharedResources asr){
		Command command = (Command) arg.getArguments().get(0);
		logger.debug("Command type: " + command.getClass().getSimpleName());
		logger.debug("Executing: " + command.toCommandString());
		arg.setReturnValue(command.execute());
		
		return arg;
	}
}