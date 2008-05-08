/*
 *  TagService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags;

import java.util.List;

import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * The TagService manages all available TagSources.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public interface TagService {
	/**
	 * Get a list of tag source names.
	 * 
	 * @return
	 */
	List<String> getTagSourceNames();

	/**
	 * Get a number of tags from the repository.
	 * 
	 * @param number
	 * @param sourceName
	 *            name of the TagSource
	 * @return
	 */
	List<RifidiTag> getRifidiTags(String sourceName, int number);

	/**
	 * Shorthand method for getting a single tag from the given TagSource.
	 * 
	 * @param sourceName
	 *            name of the TagSource
	 */
	RifidiTag getRifidiTag(String sourceName);

	/**
	 * Return a tag to the pool of availablke tags.
	 * 
	 * @param sourceName
	 *            name of the TagSource
	 * @param rifidiTag
	 */
	void returnRifidiTag(String sourceName, RifidiTag rifidiTag);

}
