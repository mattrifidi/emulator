/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic.formatter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.commandhandler.RQLEncodedCommands;
import org.rifidi.emulator.reader.thingmagic.database.IFilter;
import org.rifidi.emulator.reader.thingmagic.database.MuiltipleFilter;
import org.rifidi.emulator.reader.thingmagic.database.enums.ETable;
import org.rifidi.emulator.reader.thingmagic.database.enums.Etag_id;
import org.rifidi.emulator.reader.thingmagic.database.enums.GenericTableRow;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * @author jmaine
 * This class parses the command after it has been error checked.
 */
public class ParsedCommandObject {

	private static Log logger = LogFactory.getLog(ParsedCommandObject.class);
	
	List<GenericTableRow<?>> rows;
	ETable table;
	
	IFilter<RifidiTag> filter;
	Map<String, String> set;
	
	String originalCommand;
	
	/**
	 * This constructor parses the command and sends it on as a coherent object.
	 * @param args The list of pre-split arguments that were previously error checked.
	 * @param originalCommand The original command.
	 */
	/* we parse the command to meaningful values here */
	public ParsedCommandObject(List<String> args, String originalCommand){
		filter = null;
		this.originalCommand = originalCommand;
		rows = new ArrayList<GenericTableRow<?>>();
		set = new TreeMap<String, String>();
		
		StringBuffer whereClause = new StringBuffer("");
		
		int index = 0;
		
		if (args.contains("FROM")){
			
			/* we have to know the table before hand
			 * so we can now what rows it has.
			 */
			table = ETable.valueOf( args.get(args.indexOf("FROM") + 2));
			
			/* then we grab the rows the client want to look at
			 * 
			 */
			for (; !args.get(index).equals("FROM") ;index++){
				if (args.get(index).matches("[,\\s]*")) continue;
				rows.add(table.getTableRowsValueOf(args.get(index)));
			}
		}
		
		index = args.indexOf("WHERE");
		if (index != -1 ){
			index++; // move past where
			
			/* we don't parse this here... pass the problem onto somewhere else...
			 * 
			 */
			for (; index < args.size() ; index++ ){
				if (args.get(index).equals("SET")) break;
				whereClause.append(args.get(index));
			}
			filter = new MuiltipleFilter<RifidiTag>(
								(Comparator[]) table.getTableRows(),
								whereClause.toString()
						 );
		}
		
		
		/* this sets optional variables that the reader may use.
		 */
		index = args.indexOf("SET");
		if (index != -1 ){
			String key;
			String value;
			index+=2; 
			
			for (; index < args.size();index++){
				if (args.get(index).matches("[\\s]*") ) index++;
				
				key = args.get(index++);
				
				if (args.get(index).matches("[\\s]*") ) index++;
				
				if (!args.get(index).matches("=") ) throw new IllegalArgumentException();
				index++;
				
				if (args.get(index).matches("[\\s]*") ) index++;
				
				value = args.get(index++);
				
				logger.debug(key + "=" + value);
				
				set.put(key, value);
				
			}
			
		}
		
		/* dereference if empty
		 */
		//rows = (rows.isEmpty() ) ? null: rows;		
		
	}

	
	public List<GenericTableRow<?>> getRows(){
		/* be kind and give the caller only one thing to compare to
		 * for all these methods
		 */
		return rows;
	}
	
	public ETable getETable(){
		return table;
	}
	
	public IFilter<RifidiTag> getFilter(){
		return filter;
	}
	
	public Map<String, String> getSets(){
		return set;
	}
	
	public String toString(){
		return originalCommand;
	}
}
