package org.rifidi.emulator.reader.thingmagic.database;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author jmaine
 *
 * @param <K>
 * @param <V>
 */
public class DataBaseDefaultTable <K, V> implements DataBaseTable<K, V>{
	private K idKey; /* the values in the rows of this table /must/ be unique*/
	private Map<K, Map<K, V>> table;
	
	public DataBaseDefaultTable() {
		
		table = new Hashtable<K, Map<K, V>>();
	}
	
	public DataBaseDefaultTable(K idKey){
		this();
		this.idKey = idKey;
	}
	
	/**
	 * Removes a row from the database table.
	 * @param value The value of the idKey of the row to delete.
	 * @return true if successful.
	 */
	public boolean removeRow(V value){
		
		return false;
	}
	
	public K getIDKey(){
		return this.idKey;
	}
	
	public List<List<Object>> searchTable(List<K> keyList, IFilter where, int timeout){
		return null;
	}	
}
