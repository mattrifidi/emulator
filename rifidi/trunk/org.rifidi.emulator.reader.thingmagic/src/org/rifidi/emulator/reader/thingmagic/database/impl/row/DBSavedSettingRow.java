package org.rifidi.emulator.reader.thingmagic.database.impl.row;

import java.util.HashSet;
import java.util.Set;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.exceptions.DBReadException;


public class DBSavedSettingRow implements IDBRow {
	
	
	private static Set<String> columns = new HashSet<String>(); 
	
	private static boolean initialized = false;
	
	public DBSavedSettingRow () {
		initialize();
	}
	
	private static void initialize(){
		if (!initialized){
			
			
			initialized = true;
		}
	}

	@Override
	public boolean containsColumn(String column) {
		return columns.contains(column);
	}

	@Override
	public boolean isReadable(String column) {
		/*
		 * All columns are readable 
		 */
		return columns.contains(column);
	}

	@Override
	public boolean isWritable(String column) {
		/*
		 * All columns are writable
		 */
		return columns.contains(column);
	}
	
	@Override
	public String get(String key) throws DBReadException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String put(String key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

}
