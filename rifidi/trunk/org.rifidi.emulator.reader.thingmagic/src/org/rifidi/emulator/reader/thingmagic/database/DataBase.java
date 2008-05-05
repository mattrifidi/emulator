package org.rifidi.emulator.reader.thingmagic.database;

import java.util.*;

/* Use TreeMap instead of Hashtable 
 * because of the potential headachs related to using Hashtables.
 * Warning: This implementation is not synchronized.
 */
/**
 * This class implements the overall database.
 */
public class DataBase<T, K, V> 
  extends TreeMap<T, DataBaseTable<K, V>> implements SortedMap<T, DataBaseTable<K, V>>{
	
	//private Map<T, DataBaseTable<K, V>> db;
	
	
	
	private static final long serialVersionUID = 8959138923126909359L;

	
	public DataBase() {
		super();
	}
	
	/**
	 * Create a table with the default implementation 
	 * @param tableName Table name
	 * @return The DataBaseTable that was created
	 */
	public DataBaseTable<K, V> createTable(T tableName) {	
		return put(tableName, new DataBaseDefaultTable<K, V>() );
	}
	
	/**
	 * Add a table to the database
	 * @param tableName Table name
	 * @param table Table Implementation.
	 * @return table
	 */
	public DataBaseTable<K, V> createTable(T tableName, DataBaseTable<K, V> table ){
		return put(tableName, table );
	}
	
	/**
	 * Remove a table from the database
	 * @param table The name of the table to remove from the database.
	 * @return
	 */
	public DataBaseTable<K, V> deleteTable(Object table) {
		return remove(table);
	} 

	/**
	 * Get the table class by using the key
	 * @param table The key
	 * @return The table class
	 */
	public DataBaseTable<K, V> getTable(Object table) {
		return get(table);
	}
	
	/**
	 * Selects which rows and columns to return.
	 * @param keyList Which columns to be returned.
	 * @param where The conditions to be met for those being returned.
	 * @return The list of rows returned.
	 */
	public List<List<Object>> select(T from, List<K> keyList, IFilter<?> where, int timeout){
		
		return get(from).searchTable(keyList, where, timeout);
	}
	
}
