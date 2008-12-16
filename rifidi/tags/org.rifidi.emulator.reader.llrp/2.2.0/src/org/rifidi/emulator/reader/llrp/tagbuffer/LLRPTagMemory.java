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
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.services.tags.utils.RifidiTagMap;

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
	 * Private constructor
	 */
	public LLRPTagMemory() {
		this.tagBuffer = new RifidiTagMap();
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
			for (RifidiTag t : tagsInLatestScan) {
				tagBuffer.addTags(tagsInLatestScan);
			}			
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
	}
}
