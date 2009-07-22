/*
 *  SiritTagMemory.java
 *
 *  Created:	09.06.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.tagbuffer;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.services.tags.utils.RifidiTagMap;
import org.rifidi.tags.impl.RifidiTag;

/**
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritTagMemory implements TagMemory {

	/** logger instance for this class. */
	private static Log logger = LogFactory.getLog(SiritTagMemory.class);

	/** the tag database, represented by a RifidiTagMap */
	private RifidiTagMap tagDatabase;

	/** flag that holds the suspend state of the memory */
	private boolean suspended = false;

	public SiritTagMemory() {
		this.tagDatabase = new RifidiTagMap();
	}

	/** Tells the memory to stop reading tags */
	public void suspend() {
		logger.debug("Disabling Sirit's TagMemory");
		suspended = true;
	}

	/** Tells the memory that reading tags is allowed again */
	public void resume() {
		logger.debug("Enabling Sirit's TagMemory");
		suspended = false;
	}

	/** Purges the tag database */
	public void clear() {
		this.tagDatabase.clear();
	}

	/**
	 * Returns the tag database
	 * 
	 * @return list of the tags currently in the reader's antennas' fields
	 */
	public ArrayList<RifidiTag> getTagReport() {
		return this.tagDatabase.getTagList();
	}

	/**
	 * Adds the most recent seen tags to the database. This method is called
	 * after a scan.
	 */
	public void updateMemory(Collection<RifidiTag> tagsToAdd) {
		logger.debug("SiritTagMemory - updateMemory() with " + tagsToAdd.size()
				+ " Tags");
		if (!suspended) {
			for (RifidiTag t : tagsToAdd) {
				this.tagDatabase.addTag(t);
			}
		}

		logger.debug("sirit's taglist inventory:");
		for (RifidiTag tag : this.tagDatabase.getTagList()) {
			logger.debug("\t TagID: " + tag.getTag().getId() + "\t First: "
					+ tag.getDiscoveryDate() + "\t Last: "
					+ tag.getLastSeenDate() + "\t Antenna: "
					+ tag.getAntennaLastSeen() + "\t Count: "
					+ tag.getReadCount());
		}
	}
}
