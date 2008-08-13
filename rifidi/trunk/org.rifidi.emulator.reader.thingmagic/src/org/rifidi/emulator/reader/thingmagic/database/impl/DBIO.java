package org.rifidi.emulator.reader.thingmagic.database.impl;

import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.database.impl.row.DBIORow;

public class DBIO implements IDBTable {

	public DBIORow io;
	
	public DBIO(GPIOController gpioController) {
		io = new DBIORow(gpioController);
	}

	@Override
	public IDBRow get(int index) {
		if (index != 0)
			throw new IndexOutOfBoundsException();
		return io;
	}

	@Override
	public int size() {
		return 1;
	}
	
}
