/*
 *  MasterFilter.java
 *
 *  Created:	October 3, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.conditional;

import java.util.List;
import java.util.ListIterator;

import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class MasterFilter implements IFilter {

	IFilter root;
	public MasterFilter(ListIterator<String> tokenIterator, String table, ThingMagicReaderSharedResources tmsr ) throws CommandCreationExeption{
		
		root = new SingleFilter(tokenIterator, table, tmsr);
	}
	
	
	@Override
	public List<IDBRow> filter(List<IDBRow> rows) {
		// TODO Auto-generated method stub
		return root.filter(rows);
	}

}
