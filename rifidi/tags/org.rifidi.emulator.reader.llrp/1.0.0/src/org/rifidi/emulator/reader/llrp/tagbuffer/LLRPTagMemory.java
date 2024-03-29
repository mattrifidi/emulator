/*
 *  LLRPTagMemory.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.tagbuffer;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sharedrc.tagmemory.RifidiTagMap;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.emulator.tags.impl.RifidiTag;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

/**
 * This LLRPTagMemory Class originally implemented observer and observed the Radio,
 * which would notify it when a tag was added. However, becuase we are now using
 * polling in the AISpec to read from the radio, it should not observe the radio
 * anymore, and instead should just ask it for new tags.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author kyle
 */
public class LLRPTagMemory implements TagMemory {

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(LLRPTagMemory.class);

	/**
	 * Map of the current buffer of tags, which includes tags in tagMap, and
	 * also those which have been removed but have not been reported yet.
	 */
	private RifidiTagMap tagBuffer;

	private boolean suspended = false;

	/**
	 * The collection of tags that were in the latest scan that have not been seen before
	 */
	public Collection<RifidiTag> newTagsInLatestScan;

	/**
	 * Private constructor
	 */
	public LLRPTagMemory() {
		this.tagBuffer = new RifidiTagMap();
		this.newTagsInLatestScan = new ArrayList<RifidiTag>();
	}

	/**
	 * Adds a tag to the buffer.
	 * 
	 * @param newTag
	 *            The tag to add to the buffer.
	 */
	private void addTag(RifidiTag newTag) {
		this.tagBuffer.addTag(newTag);
	}

	/**
	 * updates tags to reflect latest scan by the radio
	 * 
	 * @param tagsToAdd
	 */
	public void updateMemory(Collection<RifidiTag> tagsInLatestScan) {
		if (!suspended) {
			this.newTagsInLatestScan.clear();
			for (RifidiTag t : tagsInLatestScan) {
				//add new tags -- first check if the tag has been seen before.  If not, add it to the new
				if (!this.tagBuffer.contains(t.getTag().readId())) {
					logger.debug("Adding new tag: "
							+ ByteAndHexConvertingUtility.toHexString(t
									.getTag().readId()));
					this.newTagsInLatestScan.add(t);
				}
			}
			tagBuffer.clear();
			tagBuffer.addTags(tagsInLatestScan);
		}
	}

	/**
	 * Tells the buffer it should start reading tags.
	 */
	public void suspend() {
		suspended = true;
	}

	/**
	 * Tells the buffer to stop reading tags.
	 */
	public void resume() {
		suspended = false;
	}

	/**
	 * Returns a tag report for all tags on all antennas.
	 * 
	 * @return
	 */
	public Collection<RifidiTag> getTagReport() {
		return this.tagBuffer.getTagSet();
	}

	/**
	 * Clears the tag memory
	 */
	public void clear() {
		this.tagBuffer.clear();
		this.newTagsInLatestScan.clear();
	}

	public void removeTag(byte[] id) {
		this.tagBuffer.removeTag(id);

	}
}
