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

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.rifidi.tags.factory.TagCreationPattern;
import org.rifidi.tags.impl.RifidiTag;

/**
 * Tag registries are used to keep track of created/deleted tags.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@prmari.com
 */
@Deprecated
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
	public ArrayList<RifidiTag> createTags(TagCreationPattern tagCreationPattern);

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
	 * Get a single tag using the tagEntityID
	 * 
	 * @param tagEntityID
	 * @return
	 */
	public RifidiTag getTag(Long tagEntityID);

	/**
	 * Add a listener to listen for changes to the set of tags controlled by the
	 * registry.
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Remove a listener form the registry.
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener);
}
