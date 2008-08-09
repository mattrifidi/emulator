package org.rifidi.emulator.reader.thingmagic.database;

import java.util.HashMap;
import java.util.Map;

public class DataBase {
	public Map<String, IDBTable> tables = new HashMap<String, IDBTable>();
	
	public void addTable(String name, IDBTable table){
		tables.put(name, table);
	}
	public IDBTable getTable(String name){
		return tables.get(name);
	}

}
