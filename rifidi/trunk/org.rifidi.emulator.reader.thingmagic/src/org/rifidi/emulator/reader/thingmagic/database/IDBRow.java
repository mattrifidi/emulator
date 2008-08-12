package org.rifidi.emulator.reader.thingmagic.database;

import org.rifidi.emulator.reader.thingmagic.database.exceptions.DBReadException;


public interface IDBRow {
	public boolean containsColumn(String column);
	public boolean isWritable(String column);
	public boolean isReadable(String column);
	
	//TODO: Research the behavior of this command further.
	public String put(String key, String value);
	
	public String get(String key) throws DBReadException;
}
