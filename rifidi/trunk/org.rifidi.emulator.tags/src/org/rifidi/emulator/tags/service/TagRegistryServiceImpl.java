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
package org.rifidi.emulator.tags.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.rifidi.emulator.tags.factory.TagCreationPattern;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@prmari.com
 *
 */
public class TagRegistryServiceImpl implements TagRegistryService {

	private HashMap<Long, RifidiTag> tags;
	
	private long uniqueIDSeed;
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#addListener(org.rifidi.emulator.tags.service.TagRegistryListener)
	 */
	@Override
	public void addListener(TagRegistryListener listener) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#createTags(org.rifidi.emulator.tags.factory.TagCreationPattern)
	 */
	@Override
	public List<RifidiTag> createTags(TagCreationPattern tagCreationPattern) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#getTags()
	 */
	@Override
	public List<RifidiTag> getTags() {
		return new ArrayList(this.tags.values());
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#initialize()
	 */
	@Override
	public void initialize() {
		uniqueIDSeed = 1;

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#initialize(java.util.List)
	 */
	@Override
	public void initialize(List<RifidiTag> tags) {
		initialize();
		for(RifidiTag t : tags){
			if(uniqueIDSeed<t.getTagEntitiyID()){
				uniqueIDSeed = t.getTagEntitiyID();
			}
			this.tags.put(t.getTagEntitiyID(), t);
		}

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#remove(org.rifidi.emulator.tags.impl.RifidiTag)
	 */
	@Override
	public void remove(RifidiTag tag) {
		tags.remove(tag.getTagEntitiyID());

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#remove(java.util.List)
	 */
	@Override
	public void remove(List<RifidiTag> tags) {
		
		for(RifidiTag t : tags){
			this.tags.remove(t.getTagEntitiyID());
		}

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#remove()
	 */
	@Override
	public void remove() {
		this.tags.clear();

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.service.TagRegistryService#removeListener(org.rifidi.emulator.tags.service.TagRegistryListener)
	 */
	@Override
	public void removeListener(TagRegistryListener listener) {
		// TODO Auto-generated method stub

	}

}
