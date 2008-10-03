/*
 *  IDBTable.java
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

import java.util.Map;


/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public interface IDBTable {
	
	public IDBRow get(int index);
	
	public int size();
	
	public void preTableAccess(Map<String, String> params);
	
}
