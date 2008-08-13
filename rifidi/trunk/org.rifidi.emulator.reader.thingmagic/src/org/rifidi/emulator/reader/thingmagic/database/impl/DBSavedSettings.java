package org.rifidi.emulator.reader.thingmagic.database.impl;

import java.util.AbstractList;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.database.impl.row.DBSavedSettingRow;

public class DBSavedSettings extends AbstractList<IDBRow> implements IDBTable {

	private DBSavedSettingRow savedSettings = new DBSavedSettingRow();
	
	@Override
	public IDBRow get(int arg0) {
		if (arg0 != 0)
			throw new IndexOutOfBoundsException();
		return savedSettings;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 1;
	}

	
}
