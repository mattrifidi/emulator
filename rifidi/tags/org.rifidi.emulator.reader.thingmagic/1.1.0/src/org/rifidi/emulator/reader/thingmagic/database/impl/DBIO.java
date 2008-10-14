/*
 *  DBIO.java
 *
 *  Created:	August 12, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.database.impl;

import java.util.Map;

import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.database.IDBTable;
import org.rifidi.emulator.reader.thingmagic.database.impl.row.DBIORow;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
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

	@Override
	public void preTableAccess(Map<String, String> params) {
		// TODO Auto-generated method stub

	}

}
