package org.rifidi.emulator.reader.thingmagic.database.impl;

import java.util.AbstractList;
import java.util.Map;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.database.impl.row.DBTagDataRow;
import org.rifidi.emulator.reader.thingmagic.database.impl.row.DBTagIDRow;

public class DBTagData extends AbstractList<IDBRow> implements IDBTable {

	private DBTagID tagMemory;

	public DBTagData(DBTagID tagMemory) {
		this.tagMemory = tagMemory;
	}

	@Override
	public IDBRow get(int index) {
		// TODO Auto-generated method stub
		return new DBTagDataRow((DBTagIDRow) tagMemory.get(index));
	}

	@Override
	public int size() {

		// TODO Disable this until we figure out how TagDataRow should work...
		// :puzzled:
		// return tagMemory.size();
		return 0;
	}

	@Override
	public void preTableAccess(Map<String, String> params) {
		// TODO Auto-generated method stub

	}

}
