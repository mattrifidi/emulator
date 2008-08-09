/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic.commandhandler;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.thingmagic.commandobjects.Command;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author jmaine
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