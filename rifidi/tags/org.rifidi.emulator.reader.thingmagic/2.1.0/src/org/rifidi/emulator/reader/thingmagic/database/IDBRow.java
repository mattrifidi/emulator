/*
 *  IDBRow.java
 *
 *  Created:	August 7, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.database;


/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public interface IDBRow {
	public boolean containsColumn(String column);
	public boolean isWritable(String column);
	public boolean isReadable(String column);
	
	//TODO: Research the behavior of this command further.
	public String put(String key, String value);
	
	public String get(String column);
	
	public int compareToValue(String column, String testValue);
}
