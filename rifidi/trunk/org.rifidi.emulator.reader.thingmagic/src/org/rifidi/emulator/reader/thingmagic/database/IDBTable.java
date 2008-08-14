package org.rifidi.emulator.reader.thingmagic.database;

import java.util.Map;



public interface IDBTable {
	
	public IDBRow get(int index);
	
	public int size();
	
	public void preTableAccess(Map<String, String> params);
	
}
