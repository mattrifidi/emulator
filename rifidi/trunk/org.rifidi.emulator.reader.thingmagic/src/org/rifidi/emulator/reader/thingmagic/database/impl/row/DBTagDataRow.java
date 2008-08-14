package org.rifidi.emulator.reader.thingmagic.database.impl.row;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.exceptions.DBReadException;

//TODO figure out how this one should work... :puzzled:
public class DBTagDataRow implements IDBRow {

	private DBTagIDRow tag;

	public DBTagDataRow(DBTagIDRow tag) {
		this.tag = tag;
	}

	@Override
	public boolean containsColumn(String column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String get(String key) throws DBReadException {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public boolean isReadable(String column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWritable(String column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String put(String key, String value) {
		// TODO Auto-generated method stub
		return "";
	}

}
