/*
 *  DBTagDataRow.java
 *
 *  Created:	August 13, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.database.impl.row;

import org.rifidi.emulator.reader.thingmagic.database.IDBRow;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
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
	public String get(String key) {
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

	@Override
	public int compareToValue(String key, String testValue) {
		// TODO Auto-generated method stub
		return 0;
	}

}
