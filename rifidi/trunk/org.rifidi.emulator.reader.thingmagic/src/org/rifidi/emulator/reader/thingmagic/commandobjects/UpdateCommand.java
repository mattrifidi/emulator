package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

public class UpdateCommand implements Command {

	private String command;
	private String table;

	Map<String, String> keyValuePairs = new HashMap<String, String>();
	
	public UpdateCommand(String command, ThingMagicReaderSharedResources tmsr) throws CommandCreationExeption {
		// TODO Auto-generated constructor stub
		this.command = command;
		
		List<String> commandBlocks = new ArrayList<String>();
		/*
		 * This regex helps break up the command into easily parsable 
		 * commandBlocks. This makes it much easier to check for syntax errors
		 * because the commandBlocks are highly predictable.
		 */
		Pattern pattern = Pattern.compile(
				"\\w+|[\\s,]+|<>|>=|<=|=|>|<|\\(|\\)|'|[^\\s\\w,<>=\\(\\)']+",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(command.toLowerCase());

		while (matcher.find())
			commandBlocks.add(matcher.group());
		
		int index = 0;
		
		if (commandBlocks.get(index).matches("\\s+"))
			index++;
		
		if (!commandBlocks.get(index).equals("update"))
			throw new CommandCreationExeption("Error 0100:     syntax error at '" + commandBlocks.get(index) + "'");
		
		index++;
		
		try {
			if (!commandBlocks.get(index).matches("\\s+"))
				throw new CommandCreationExeption("Error 0100:     syntax error at '" + commandBlocks.get(index) + "'");
			
			index++;
			
			table = commandBlocks.get(index);
			if (!table.matches("\\w+"))
				throw new CommandCreationExeption("Error 0100:     syntax error at '" + commandBlocks.get(index) + "'");
			
			index++;
			
			if (!commandBlocks.get(index).matches("\\s+"))
				throw new CommandCreationExeption("Error 0100:     syntax error at '" + commandBlocks.get(index) + "'");
			
			index++;
			
			if (!commandBlocks.get(index).equals("set"))
				throw new CommandCreationExeption("Error 0100:     syntax error at '" + commandBlocks.get(index) + "'");
			
			index++;
			
			parseKeyValuePairs(commandBlocks, index);
		} catch (IndexOutOfBoundsException e) {
			/*
			 * look for the last offending command block that is not a series of whitespaces.
			 * 
			 */
			for(int x = commandBlocks.size()-1; x >= 0; x--) {
				if (!commandBlocks.get(x).matches("\\s")){
					throw new CommandCreationExeption("Error 0100:     syntax error at '" + commandBlocks.get(index) + "'");
				}
			}
		}
	}

	/*
	 * private helper method to help break up the logical layout of the parser.
	 */
	private void parseKeyValuePairs(List<String> commandBlocks, int index) throws CommandCreationExeption {
		// TODO Auto-generated method stub
		for (; (commandBlocks.size() < index) || (!commandBlocks.get(index).equals("where")) ; index++){
			
		}
	}

	@Override
	public ArrayList<Object> execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toCommandString() {
		// TODO Auto-generated method stub
		return command;
	}

}
