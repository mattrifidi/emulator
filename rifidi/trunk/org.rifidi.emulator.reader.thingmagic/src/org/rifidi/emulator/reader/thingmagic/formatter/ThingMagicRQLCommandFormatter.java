package org.rifidi.emulator.reader.thingmagic.formatter;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.emulator.reader.thingmagic.commandhandler.RQLEncodedCommands;
import org.rifidi.emulator.reader.thingmagic.commandobjects.DeclareCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.FetchCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SelectCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SetCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.UpdateCommand;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ThingMagicRQLCommandFormatter implements CommandFormatter {
	private static Log logger = LogFactory.getLog(ThingMagicRQLCommandFormatter.class);

	public ThingMagicRQLCommandFormatter(){
		
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	// The CommmandFactory
	@Override
	public ArrayList<Object> decode(byte[] arg) {
		String command = new String(arg);
		ArrayList<Object> retVal = new ArrayList<Object>();
		
		command = command.trim();
		
		//TODO check if there is any white space after the command name.
		
		if (command.toLowerCase().startsWith("select")){
			retVal.add("execute");
			retVal.add(new SelectCommand());
			return retVal;
		}
		
		if (command.toLowerCase().startsWith("update")){
			retVal.add("execute");
			retVal.add(new UpdateCommand());
			return retVal;
		}
		
		if (command.toLowerCase().startsWith("declare")){
			retVal.add("execute");
			retVal.add(new DeclareCommand());
			return retVal;
		}
		
		if (command.toLowerCase().startsWith("fetch")){
			retVal.add("execute");
			retVal.add(new FetchCommand());
			return retVal;
		}
		
		if (command.toLowerCase().startsWith("set")){
			retVal.add("execute");
			retVal.add(new SetCommand());
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
		
		
		/* gather all the information back in on big string */
		for (Object obj: arg){
			
			List<Object> list = (List<Object>) obj;
			String gather = ""; 
			for (int x = 0; x < list.size(); x++){
				gather += list.get(x);
				gather += (x != list.size() -1 ) ? "|": "\n";
			}
			retVal.add(gather);
		}
		
		// TODO do we need to put another return here if args is empty?
		
		
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
