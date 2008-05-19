/*
 *  SymbolTagMemory.java
 *
 *  Created:	Aug 16, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.symbol.tagbuffer;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sharedrc.tagmemory.RifidiTagMap;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * This is a SymbolTagMemory class that provides a tag memory for symbol. It is taken
 * from the LLRP reader.
 * 
 * Symbol memory is fairly simple. There are two states that tags can be in:
 * visible and invisible. Visible tags are tags that the antenna can see right
 * now. Invisible tags are tags that the reader has seen but cannot see now. The
 * lists are cleared when a purge is called.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author kyle
 */
public class SymbolTagMemory implements TagMemory {

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(SymbolTagMemory.class);

	/**
	 * Map of the current buffer of tags, which includes tags in tagMap, and
	 * also those which have been removed but have not been reported yet.
	 */
	private RifidiTagMap tagBufferVisible;

	private RifidiTagMap tagBufferInvisible;

	private boolean suspended = false;


	public SymbolTagMemory() {
		this.tagBufferVisible = new RifidiTagMap();
		this.tagBufferInvisible = new RifidiTagMap();
	}

	/**
	 * Add a tag to the visible hash map and remove it from the invisible hash
	 * map if it is there
	 * 
	 * @param newTag
	 *            The tag to add to the buffer.
	 */
	private void addTagVisible(RifidiTag newTag) {
		this.tagBufferVisible.addTag(newTag);
		this.tagBufferInvisible.removeTag(newTag.getTagEntitiyID());
	}

	/**
	 * Adds a collection of tags to the visible hash map
	 * 
	 * @param tagsToAdd
	 */
	public void addCollectionTagsVisible(Collection<RifidiTag> tagsToAdd) {
		for (RifidiTag r : tagsToAdd) {
			this.addTagVisible(r);
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

	public ArrayList<RifidiTag> getInvisibleTags() {
		return this.tagBufferInvisible.getTagList();
	}
	/**
	 * Purge
	 */
	public void clear() {
		this.tagBufferVisible.clear();
		this.tagBufferInvisible.clear();		
	}

	/**
	 * Get visible tags
	 */
	public Collection<RifidiTag> getTagReport() {
		return this.tagBufferVisible.getTagList();
	}

	public void updateMemory(Collection<RifidiTag> tagsSeen) {
		if (!suspended) {
			//add new tags to visible
			tagBufferVisible.addTags(tagsSeen);
			
			//move old tags to invisible tags
			Collection<RifidiTag>diff = tagBufferVisible.generateSetDiff(tagsSeen);
			tagBufferInvisible.addTags(diff);
			for(RifidiTag t : tagsSeen){
				tagBufferVisible.removeTag(t.getTagEntitiyID());
			}
			
		}
	}
}
