/*
 *  IRifidiTagContainer.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.model;

import java.util.Collection;

import org.rifidi.tags.impl.RifidiTag;

/**
 * A tag container takes and returns tags to the registry.
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 18, 2008
 * 
 */
public interface IRifidiTagContainer {
	/**
	 * Add tags to the container. 
	 * @return
	 */
	void addTags(Collection<RifidiTag> tags);
	/**
	 * Get the list of tags currently in the container. 
	 * @return
	 */
	Collection<RifidiTag> getTags();
	/**
	 * Remove the given tags from the container. This method should block until  
	 * @param tags
	 */
	void removeTags(Collection<RifidiTag> tags);
}
