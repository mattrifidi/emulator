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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.database.IDBRow;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class MasterFilter implements IFilter {
	private static Log logger = LogFactory.getLog(MasterFilter.class);
	
	IFilter root;

	private boolean ignore;
	
	public MasterFilter(ListIterator<String> tokenIterator, String table, ThingMagicReaderSharedResources tmsr ) throws CommandCreationExeption{
		logger.debug("Creating Master Filter...");
		if (tmsr.getDataBase().getTable(table).size() == 0) {
			logger.debug("Filter disabled... no tags to filter");
			ignore = true;
		}
		root = new SingleFilter(tokenIterator, table, tmsr);
	}
	
	
	@Override
	public List<IDBRow> filter(List<IDBRow> rows) {
		logger.debug("Filtering.... ");
		// TODO this might be a subtle bug... not sure though
		if (ignore) {
			return rows;
		}
		
		return root.filter(rows);
	}

}
