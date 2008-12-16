/*
 *  TagRegistryServiceImpl.java
 *
 *  Project: Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright: Pramari LLC and the Rifidi Project
 *  License: Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.registry.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.rifidi.services.tags.factory.TagCreationPattern;
import org.rifidi.services.tags.factory.TagFactory;
import org.rifidi.services.tags.impl.RifidiTag;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.services.tags.utils.RifidiTagMap;

/**
 * Service for handling all tags created in a Rifidi application.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@prmari.com
 * @author Jochen Mader - jochen@pramari.com
 */
public class TagRegistryImpl implements ITagRegistry {
	/** Tags by patterns. */
	private RifidiTagMap tagMap;
	/** Start value for tag ids. */
	private long uniqueIDSeed;
	/** Support for listening changes to the taglist. */
	private PropertyChangeSupport propertyChangeSupport;

	/**
	 * Constructor.
	 */
	public TagRegistryImpl() {
		tagMap = new RifidiTagMap();
		propertyChangeSupport = new PropertyChangeSupport(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.tags.service.TagRegistryService#createTags(org.rifidi
	 * .emulator.tags.factory.TagCreationPattern)
	 */
	@Override
	public ArrayList<RifidiTag> createTags(TagCreationPattern tagCreationPattern) {
		List<RifidiTag> oldList = getTags();
		ArrayList<RifidiTag> newTags = TagFactory
				.generateTags(tagCreationPattern);
		for (RifidiTag t : newTags) {
			t.setTagEntitiyID(this.uniqueIDSeed);
			uniqueIDSeed++;
			tagMap.addTag(t);
		}
		propertyChangeSupport.firePropertyChange("tags", oldList, getTags());
		return newTags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#getTags()
	 */
	@Override
	public List<RifidiTag> getTags() {
		return tagMap.getTagList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#initialize()
	 */
	@Override
	public void initialize() {
		List<RifidiTag> oldList = getTags();
		uniqueIDSeed = 1;
		tagMap = new RifidiTagMap();
		propertyChangeSupport.firePropertyChange("tags", oldList, getTags());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.tags.service.TagRegistryService#initialize(java.util
	 * .List)
	 */
	@Override
	public void initialize(List<RifidiTag> tags) {
		uniqueIDSeed = 1;
		List<RifidiTag> oldList = getTags();
		tagMap = new RifidiTagMap();
		for (RifidiTag t : tags) {
			// make sure uniqueIDseed is the highest IDseed seen in the list of
			// tags
			if (uniqueIDSeed < t.getTagEntitiyID()) {
				uniqueIDSeed = t.getTagEntitiyID();
			}
			this.tagMap.addTag(t);
		}
		propertyChangeSupport.firePropertyChange("tags", oldList, getTags());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.tags.service.TagRegistryService#remove(org.rifidi
	 * .services.tags.impl.RifidiTag)
	 */
	@Override
	public void remove(RifidiTag tag) {
		List<RifidiTag> oldList = getTags();
		tagMap.removeTag(tag.getTagEntitiyID());
		propertyChangeSupport.firePropertyChange("tags", oldList, getTags());
		tag.setDeleted(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.tags.service.TagRegistryService#remove(java.util.
	 * List)
	 */
	@Override
	public void remove(List<RifidiTag> tags) {
		List<RifidiTag> oldList = getTags();

		for (RifidiTag t : tags) {
			this.tagMap.removeTag(t.getTagEntitiyID());
			t.setDeleted(true);
		}
		propertyChangeSupport.firePropertyChange("tags", oldList, getTags());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#remove()
	 */
	@Override
	public void remove() {
		List<RifidiTag> oldList = getTags();
		for (RifidiTag tag : getTags()) {
			tag.setDeleted(true);
		}
		this.tagMap.clear();

		propertyChangeSupport.firePropertyChange("tags", oldList, getTags());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#getTag(Long)
	 */
	@Override
	public RifidiTag getTag(Long tagEntityID) {
		return tagMap.getTag(tagEntityID);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.registry.ITagRegistry#addPropertyChangeListener
	 * (java.beans.PropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener("tags", listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.registry.ITagRegistry#removePropertyChangeListener
	 * (java.beans.PropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener("tags", listener);
	}

}
