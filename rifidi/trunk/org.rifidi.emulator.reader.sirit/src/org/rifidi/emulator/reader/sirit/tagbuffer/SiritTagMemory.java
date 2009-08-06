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
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.emulator.reader.sirit.module.SiritReaderSharedResources;
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
	private RifidiTagMap tagDatabase = new RifidiTagMap();

	/**
	 * flag that holds the active state of the reader and thus of the tag memory
	 */
	private boolean active = true;

	/** the shared resources for this reader */
	private SiritReaderSharedResources srsr;


	/** Tells the memory to stop reading tags */
	public void suspend() {
		logger.debug("Disabling Sirit's TagMemory");
		active = false;
	}

	/** Tells the memory that reading tags is allowed again */
	public void resume() {
		logger.debug("Enabling Sirit's TagMemory");
		active = true;
	}

	/** Purges the tag database */
	public void clear() {
		this.tagDatabase.clear();
	}

	/**
	 * Returns the tag database
	 * 
	 * @return list of the tags
	 */
	public ArrayList<RifidiTag> getTagReport() {
		return this.tagDatabase.getTagList();
	}

	/**
	 * This method is called after a scan. It handles the most recent seen tags
	 * by adding them to the database. Tags that were already in the database
	 * are quasi updated. Tags are only added/updated when operating mode is
	 * "active".
	 */
	public void updateMemory(Collection<RifidiTag> tagsToAdd) {
		logger.debug("SiritTagMemory - updateMemory()");

		/* check operating mode */
		if (this.active) {
			logger.debug("SiritTagMemory - adding tags to tag database");

			/* update read count and (re-)add tags */
			for (RifidiTag tag : tagsToAdd) {
				tag.incrementReadCount();
				this.tagDatabase.addTag(tag);
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
}
