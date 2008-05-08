/*
 *  TagSource.java
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
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public interface TagSource {
	/**
	 * Get a number of tags from the repository.
	 * 
	 * @param number
	 * @return
	 */
	List<RifidiTag> getRifidiTags(int number);

	/**
	 * Shorthand method for getting a single method.
	 */
	RifidiTag getRifidiTag();
	
	/**
	 * Return a tag to the pool of availablke tags.
	 * @param rifidiTag
	 */
	void returnRifidiTag(RifidiTag rifidiTag);
	
}
