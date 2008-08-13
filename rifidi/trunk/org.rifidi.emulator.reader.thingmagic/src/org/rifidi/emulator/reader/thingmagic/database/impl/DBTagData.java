package org.rifidi.emulator.reader.thingmagic.database.impl;

import java.util.AbstractList;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.database.impl.row.DBTagDataRow;
import org.rifidi.emulator.reader.thingmagic.database.impl.tagbuffer.TagRowData;
import org.rifidi.emulator.reader.thingmagic.database.impl.tagbuffer.ThingMagicTagTableMemory;

public class DBTagData extends AbstractList<IDBRow> implements IDBTable {

	private ThingMagicTagTableMemory tagMemory;

	public DBTagData(ThingMagicTagTableMemory tagMemory) {
		this.tagMemory = tagMemory;
	}
	
	@Override
	public IDBRow get(int index) {
		// TODO Auto-generated method stub
		return new DBTagDataRow((TagRowData) tagMemory.get(index));
	}

	@Override
	public int size() {

		//TODO Disable this until we figure out how TagDataRow should work... :puzzled:
		//return tagMemory.size();
		return 0;
	}

}
