package org.rifidi.emulator.reader.thingmagic.conditional;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.rifidi.emulator.reader.thingmagic.commandobjects.Command;
import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.database.DataBase;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

public class SingleFilter implements IFilter {
	
	
	private String attribute;
	private ECompareOperator compareOperator;

	public SingleFilter(ListIterator<String> tokenIterator, ThingMagicReaderSharedResources tmsr, String table) throws CommandCreationExeption{
		DataBase db = tmsr.getDataBase();
		
		
		String token = tokenIterator.next();
		
		if (token.matches(Command.WHITE_SPACE)){
			token = tokenIterator.next();
		}
		
		if (!token.matches(Command.A_WORD)){
			throw new CommandCreationExeption();
		}
		
		attribute = token;
		
		if (!db.getTable(table).get(0).containsColumn(attribute)){
			throw new CommandCreationExeption();
		}
		token = tokenIterator.next();
		
		if (token.matches(Command.EQUALS_WITH_WS)){
			compareOperator = ECompareOperator.EQUAL;
		} else if (token.matches(Command.GREATER_THAN_EQUALS_W_WS)) {
			compareOperator = ECompareOperator.GREATER_THAN_EQUAL;
		} else if (token.matches(Command.LESS_THAN_EQUALS_W_WS)){
			compareOperator = ECompareOperator.LESS_THAN_EQUAL;
		} else if (token.matches(Command.GREATER_THAN_WITH_WS)){
			compareOperator = ECompareOperator.GREATER_THAN;
		} else if (token.matches(Command.LESS_THAN_WITH_WS)){
			compareOperator = ECompareOperator.LESS_THAN;
		} else if (token.matches(Command.NOT_EQUALS_W_WS)){
			compareOperator = ECompareOperator.NOT_EQUAL;
		} else {
			throw new CommandCreationExeption();
		}
		
		
	}

	@Override
	public List<IDBRow> filter(List<IDBRow> rows) {
		// TODO Auto-generated method stub
		
		List<IDBRow> retVal = new ArrayList<IDBRow>();
		
		if (compareOperator == ECompareOperator.EQUAL){
			
		}
		
		if (compareOperator == ECompareOperator.NOT_EQUAL){
			
		}
		
		if (compareOperator == ECompareOperator.GREATER_THAN_EQUAL){
			
		}
		
		if (compareOperator == ECompareOperator.LESS_THAN_EQUAL){
			
		}
		
		if (compareOperator == ECompareOperator.GREATER_THAN){
			
		}
		
		if (compareOperator == ECompareOperator.LESS_THAN){
			
		}
		return retVal;
	}

}
