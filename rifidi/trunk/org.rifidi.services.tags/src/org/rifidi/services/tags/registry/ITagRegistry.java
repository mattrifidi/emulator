/*
 *  TagRegistryService.java
 *
 *  Project: Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright: Pramari LLC and the Rifidi Project
 *  License: Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.registry;

import java.util.List;

import org.rifidi.services.tags.factory.TagCreationPattern;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@prmari.com
 * 
 */
public interface ITagRegistry {

	/**
	 * This method initializes the tag registry. It should be called before the
	 * registry is used.
	 */
	public void initialize();

	/**
	 * This method initializes the service with a list of tags and stores the
	 * tags in the newly created registry
	 * 
	 * @param tags
	 */
	public void initialize(List<RifidiTag> tags);

	/**
	 * This method creates a list of tags according to the tagCreationPattern
	 * and stores the tags in the registry
	 * 
	 * @param tagCreationPattern
	 * @return The list of tags that were created
	 */
	public List<RifidiTag> createTags(TagCreationPattern tagCreationPattern);

	/**
	 * This method removes the given tag from the registry
	 * 
	 * @param tag
	 *            the tag to remove
	 */
	public void remove(RifidiTag tag);

	/**
	 * This method removes the given tags from the registry
	 * 
	 * @param tags
	 *            the tags to remove
	 */
	public void remove(List<RifidiTag> tags);

	/**
	 * This method removes all tags from the registry
	 */
	public void remove();

	/**
	 * This method returns a list containing all the tags in the regsitry
	 * 
	 * @return
	 */
	public List<RifidiTag> getTags();

	/**
	 * This method adds a listener to the registry. the listener will be
	 * notified of tag add, tag remove and tag change events.
	 * 
	 * @param listener
	 */
	public void addListener(ITagRegistryListener listener);

	/**
	 * This method removes a listener.
	 * 
	 * @param listener
	 */
	public void removeListener(ITagRegistryListener listener);

}
