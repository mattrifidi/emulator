package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

public class SelectCommand implements Command {
	private static Log logger = LogFactory.getLog(SelectCommand.class);
	
	
	private String command;
	
	private List<String> columns = new ArrayList<String>();
	private String table;


	private ThingMagicReaderSharedResources tmsr;
	
	
	public SelectCommand(String command, ThingMagicReaderSharedResources tmsr) {
		this.tmsr = tmsr;
		this.command = command;
		List<String> commandBlocks = new ArrayList<String>();
		// TODO Auto-generated constructor stub
	
		logger.debug("Parsing command: " + command );
		
		
		/* how goal here is not to do error checking but to 
		 * parse the incoming command to make it easer to check for errors
		 * latter on as the command is broken up into very predictable sub-blocks
		 * See java.util.regex
		 */
		Pattern pattern = Pattern.compile(
				"\\w+|[\\s,]+|<>|>=|<=|=|>|<|\\(|\\)|[^\\s\\w,<>=\\(\\)]+",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL
			);
		Matcher matcher = pattern.matcher(command);
		
		while (matcher.find())
			commandBlocks.add(matcher.group().toLowerCase());
		
		int index = 0;
		if (commandBlocks.get(index).matches("\\s")){
			index++;
		}
		
		if (!commandBlocks.get(index).equals("select")){
			//TODO throw an exception
		}
		index++;
		
		if (!commandBlocks.get(index).matches("\\s")){
			//TODO throw an exception
		}
		
		index++;
		
		for (; !commandBlocks.get(index).equals("from") ;index++){
			if (commandBlocks.get(index).matches("[,\\s]*")) continue;
			if (commandBlocks.get(index).matches("\\w")){
				columns.add(commandBlocks.get(index));
			} else {
				//TODO throw an exception
			}
		}
		
		if (!commandBlocks.get(index).matches("\\s")){
			//TODO throw an exception
		}
		
		if (commandBlocks.get(index).matches("\\w")){
			table = commandBlocks.get(index);
		} else {
			//TODO throw an exception
		}
		
		if (!commandBlocks.get(index).matches("\\s")){
			//TODO throw an exception
		}
		index++;
		
		if (!commandBlocks.get(index).matches("where")){
			if (!commandBlocks.get(index).matches("\\s")){
				//TODO throw an exception
			}
			index++;
			
			for (; index < commandBlocks.size() ; index++ ){
				if (commandBlocks.get(index).equals("set")) break;
				//TODO parse where clause
				//whereClause.append(args.get(index));
			}
		}
		
		if (!commandBlocks.get(index).matches("set")){
			//TODO implement the set clause
		}
		
		//TODO check if the table and column names exist
	}
	
	

	@Override
	public ArrayList<Object> execute() {
		// TODO Auto-generated method stub
		ArrayList<Object> retVal = new ArrayList<Object>();
		//TODO add filtering
		
		for (IDBRow row : tmsr.getDataBase().getTable(table)){
			StringBuffer buff = new StringBuffer();
			for (int x = 0; x < columns.size(); x++){
				buff.append(row.get(columns.get(x)));
				if (x < columns.size() - 1)
					buff.append("|");
			}
			retVal.add(buff.toString());
		}
		return retVal;
	}

	@Override
	public String toCommandString() {
		// TODO Auto-generated method stub
		return command;
	}

}
