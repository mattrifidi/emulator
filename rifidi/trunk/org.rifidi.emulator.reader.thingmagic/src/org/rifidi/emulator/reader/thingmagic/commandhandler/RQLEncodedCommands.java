/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic.commandhandler;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.thingmagic.formatter.ParsedCommandObject;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author jmaine
 *
 */
public class RQLEncodedCommands {
	
	private static Log logger = LogFactory.getLog(RQLEncodedCommands.class);
	
	public CommandObject select(CommandObject arg,
			AbstractReaderSharedResources asr) {
		//TODO add error checking!
		logger.debug("Select called in ThingMagic handler method: ");
			
		ParsedCommandObject args = (ParsedCommandObject) arg.getArguments().get(0);		
		ThingMagicReaderSharedResources tmsr = (ThingMagicReaderSharedResources) asr;
		
		
		/* we use the information in the Parsed Command Object 
		 * sent to us to directly call the data base.
		 */
		arg.setReturnValue(
				(ArrayList<Object>) tmsr.getDataBase().select(
						args.getETable(), args.getRows(), 
						args.getFilter(),
						
						/* see if there is a specifide time out and use it, if not then use the default */
						(( (args.getSets() != null) && (args.getSets().containsKey("TIME_OUT")) ) ?
								Integer.parseInt(args.getSets().get("TIME_OUT")) :
								250
						)
				));
		return arg;
	}
	
	public CommandObject connect(CommandObject arg,
			AbstractReaderSharedResources asr){
		logger.debug("Dummy connect called in ThingMagic handler method");
		return arg;
	}	

}
