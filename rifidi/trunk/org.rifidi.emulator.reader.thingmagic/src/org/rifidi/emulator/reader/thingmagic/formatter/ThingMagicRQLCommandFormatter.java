package org.rifidi.emulator.reader.thingmagic.formatter;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.emulator.reader.thingmagic.commandobjects.DeclareCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.FetchCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SelectCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SetCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.UpdateCommand;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;


public class ThingMagicRQLCommandFormatter implements CommandFormatter {
	private static Log logger = LogFactory.getLog(ThingMagicRQLCommandFormatter.class);
	private ThingMagicReaderSharedResources tmsr;

	public ThingMagicRQLCommandFormatter(ThingMagicReaderSharedResources tmsr){
		
		this.tmsr = tmsr;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	// The CommmandFactory
	@Override
	public ArrayList<Object> decode(byte[] arg) {
		String command = new String(arg);
		logger.debug("Command: " + command);
		ArrayList<Object> retVal = new ArrayList<Object>();
		
		String commandTrimmed = command.trim();
		
		//TODO check if there is any white space after the command name.
		//TODO add error checking.
		
		if (commandTrimmed.toLowerCase().startsWith("select")){
			retVal.add("execute");
			retVal.add(new SelectCommand(command, tmsr));
			return retVal;
		}
		
		if (commandTrimmed.toLowerCase().startsWith("update")){
			retVal.add("execute");
			retVal.add(new UpdateCommand(command));
			return retVal;
		}
		
		if (commandTrimmed.toLowerCase().startsWith("declare")){
			retVal.add("execute");
			retVal.add(new DeclareCommand(command));
			return retVal;
		}
		
		if (commandTrimmed.toLowerCase().startsWith("fetch")){
			retVal.add("execute");
			retVal.add(new FetchCommand(command));
			return retVal;
		}
		
		if (commandTrimmed.toLowerCase().startsWith("set")){
			retVal.add("execute");
			retVal.add(new SetCommand(command));
			return retVal;
		}
		
		retVal.add("error");
		retVal.add(command);
		
		return retVal;
	}
	
	@Override
	public ArrayList<Object> encode(ArrayList<Object> arg){
		logger.debug("encode() has been called: " + arg);
		ArrayList<Object> retVal = new ArrayList<Object>();
		
		
		for (Object o: arg){
			retVal.add(o + "\n");
		}
		
		
		/* there must be a blank line
		 * at the end.. even if we didn't send something useful back.
		 */
		retVal.add("\n");
		
		return retVal;
	}

	@Override
	public String getActualCommand(byte[] arg) {
		// TODO Auto-generated method stub
		logger.debug("ThingMagicRQLCommandFormatter.getActualCommand() has been called.");
		return new String(arg);
	}

	@Override
	public boolean promptSuppress() {
		// TODO Auto-generated method stub
		logger.debug("ThingMagicRQLCommandFormatter.promptSuppress() has been called.");
		return false;
	}

	
	
}
