package org.rifidi.emulator.reader.thingmagic.database;

import java.util.List;

public interface IDBTable {
	public IDBColumn<String> getColumn(String column);
	public int rowCount();
	public List<String> getColumnNameList();
}
