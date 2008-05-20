package org.rifidi.emulator.reader.thingmagic.formatter;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.emulator.reader.thingmagic.commandhandler.RQLEncodedCommands;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ThingMagicRQLCommandFormatter implements CommandFormatter {
	private static Log logger = LogFactory.getLog(ThingMagicRQLCommandFormatter.class);

	public ThingMagicRQLCommandFormatter(){
		
		
	}
	
	@Override
	public ArrayList<Object> decode(byte[] arg) {
		ArrayList<String> store = new ArrayList<String>();
		String argString = new String(arg);
		ArrayList<Object> retVal = new ArrayList<Object>();
		
		logger.debug(argString);
		
		/* how goal here is not to do error checking but to 
		 * parse the incoming command to make it easer to check for errors
		 * latter on as the command is broken up into very predictable sub-blocks
		 * See java.util.regex
		 */
		Pattern pattern = Pattern.compile(
				"\\w+|[\\s,]+|<>|>=|<=|=|>|<|\\(|\\)|[^\\s\\w,<>=\\(\\)]+",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL
			);
		Matcher matcher = pattern.matcher(argString);
		
		if (matcher.find())
			retVal.add(matcher.group());
		
		while (matcher.find())
			store.add(matcher.group());
		
		retVal.add(new ParsedCommandObject(store, argString));
		
		
		logger.debug(retVal);
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
