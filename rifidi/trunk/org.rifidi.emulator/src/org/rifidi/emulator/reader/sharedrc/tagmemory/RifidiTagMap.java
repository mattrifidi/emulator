/*
 *  RifidiTagMap.java
 *
 *  Created:	Oct 15, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.sharedrc.tagmemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * This data structure provides a convienient way to store RifidiTags. Its
 * underlying data structure is a HashMap, so it only stores one tag per ID, and
 * updates the ID if another tag is added with that ID.
 * 
 * @author kyle
 * 
 */
public class RifidiTagMap {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(RifidiTagMap.class);

	/**
	 * The hashmap of tags
	 */
	private ConcurrentHashMap<Integer, RifidiTag> tags;

	public RifidiTagMap() {
		tags = new ConcurrentHashMap<Integer, RifidiTag>();
	}

	/**
	 * Add a collection of tags to the hashmap
	 * 
	 * @param tagsToAdd
	 *            A collection of tags to remove
	 */
	public void addTags(Collection<RifidiTag> tagsToAdd) {
		for (RifidiTag t : tagsToAdd) {
			tags.put(t.hashCode(), t);
		}
	}
	
	/**
	 * Add a single tag the hashmap
	 * 
	 * @param tagsToAdd
	 *            A collection of tags to remove
	 */
	public boolean addTag(RifidiTag tagToAdd) {
		RifidiTag t = tags.put(tagToAdd.hashCode(), tagToAdd);
		if(t==null){
			return false;
		}else return true;
	}


	/**
	 * Remove a tag from the hashmap
	 * 
	 * @param tagID
	 *            The ID of the tag to remove
	 * @return
	 */
	public boolean removeTag(Long tagID) {
		if (!tags.containsKey(tagID.hashCode())) {

			logger.debug("Tag that is trying to be removed (ID: "+tagID
					+ ") does not exist in the array, whose size is "
					+ tags.size());
		}

		if (tags.remove(tagID.hashCode()) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns true if a tag with the tag entity ID is found in the hashmap
	 * 
	 * @param tagID
	 * @return
	 */
	public boolean contains(Long tagID) {
		return this.tags.containsKey(tagID.hashCode());
	}

	/**
	 * Returns true if the hashmap is empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return tags.isEmpty();
	}

	/**
	 * Clears the hashmap
	 * 
	 */
	public void clear() {
		tags.clear();
	}

	/**
	 * Returns a new HashSet containing the tags in this set.
	 * 
	 * @return
	 */
	public HashSet<RifidiTag> getTagSet() {
		HashSet<RifidiTag> tagsToReturn = new HashSet<RifidiTag>(this.tags
				.size());
		tagsToReturn.addAll(tags.values());
		return tagsToReturn;
	}

	/**
	 * Returns a new ArrayList containing the tags in this set.
	 * 
	 * @return
	 */
	public ArrayList<RifidiTag> getTagList() {
		ArrayList<RifidiTag> tagsToReturn = new ArrayList<RifidiTag>(this.tags
				.size());
		tagsToReturn.addAll(tags.values());
		return tagsToReturn;
	}

	/**
	 * Generates a set of tags that are contained in this structure but not in
	 * the incoming tag set. In more formal terms, if this set is S1 and the
	 * incoming set is S2, this method returns a new set that contains the
	 * elements represented by (S1 MINUS (S1 INTERSECT S2))
	 * 
	 * @return
	 */
	public HashSet<RifidiTag> generateSetDiff(Collection<RifidiTag> tagSet) {
		HashSet<RifidiTag> returnTags = new HashSet<RifidiTag>();
		HashSet<RifidiTag> currentTags = getTagSet();

		for (RifidiTag t : currentTags) {
			if (!tagSet.contains(t)) {
				returnTags.add(t);
			}
		}

		return returnTags;
	}

	/**
	 * Gets a tag from the tagMap
	 * 
	 * @param id
	 * @return
	 */
	public RifidiTag getTag(byte[] id) {
		return this.tags.get(Arrays.hashCode(id));
	}

}
