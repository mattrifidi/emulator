/**
 * 
 */
package org.rifidi.emulator.reader.llrp.commandhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * @author kyle
 *
 */
public class LLRPErrors {
	
	/*
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(LLRPErrors.class);
	
	public CommandObject errorMessage(CommandObject arg, AbstractReaderSharedResources asr){
		logger.debug("Recieved an ERROR_MESSAGE");
		return arg;
	}

}
