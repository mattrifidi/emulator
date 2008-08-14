package org.rifidi.emulator.reader.thingmagic.database;



public interface IDBRow {
	public boolean containsColumn(String column);
	public boolean isWritable(String column);
	public boolean isReadable(String column);
	
	//TODO: Research the behavior of this command further.
	public String put(String key, String value);
	
	public String get(String key);
}
