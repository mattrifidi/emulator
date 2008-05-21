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

import java.util.List;

import org.rifidi.services.tags.factory.TagCreationPattern;
import org.rifidi.services.tags.factory.TagFactory;
import org.rifidi.services.tags.impl.RifidiTag;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.services.tags.registry.ITagRegistryListener;
import org.rifidi.services.tags.utils.RifidiTagMap;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@prmari.com
 * 
 */
public class TagRegistryImpl implements ITagRegistry {

	private RifidiTagMap tagMap;

	private long uniqueIDSeed;

	/**
	 * 
	 */
	public TagRegistryImpl() {
		tagMap=new RifidiTagMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#addListener(org.rifidi.emulator.tags.service.TagRegistryListener)
	 */
	@Override
	public void addListener(ITagRegistryListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#createTags(org.rifidi.emulator.tags.factory.TagCreationPattern)
	 */
	@Override
	public List<RifidiTag> createTags(TagCreationPattern tagCreationPattern) {
		List<RifidiTag> newTags = TagFactory.generateTags(tagCreationPattern);
		for(RifidiTag t : newTags){
			t.setTagEntitiyID(this.uniqueIDSeed);
			uniqueIDSeed++;
			this.tagMap.addTag(t);
		}
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
		uniqueIDSeed = 1;
		tagMap = new RifidiTagMap();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#initialize(java.util.List)
	 */
	@Override
	public void initialize(List<RifidiTag> tags) {
		initialize();
		for (RifidiTag t : tags) {
			if (uniqueIDSeed < t.getTagEntitiyID()) {
				uniqueIDSeed = t.getTagEntitiyID();
			}
			this.tagMap.addTag(t);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#remove(org.rifidi.services.tags.impl.RifidiTag)
	 */
	@Override
	public void remove(RifidiTag tag) {
		tagMap.removeTag(tag.getTagEntitiyID());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#remove(java.util.List)
	 */
	@Override
	public void remove(List<RifidiTag> tags) {

		for (RifidiTag t : tags) {
			this.tagMap.removeTag(t.getTagEntitiyID());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#remove()
	 */
	@Override
	public void remove() {
		this.tagMap.clear();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#removeListener(org.rifidi.emulator.tags.service.TagRegistryListener)
	 */
	@Override
	public void removeListener(ITagRegistryListener listener) {
		// TODO Auto-generated method stub

	}

}
