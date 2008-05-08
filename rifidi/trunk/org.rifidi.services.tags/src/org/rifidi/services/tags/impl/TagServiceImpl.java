/*
 *  TagServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rifidi.emulator.tags.impl.RifidiTag;
import org.rifidi.services.tags.TagService;
import org.rifidi.services.tags.TagSource;
import org.rifidi.services.tags.sources.DoD96TagSource;
import org.rifidi.services.tags.sources.GID96TagSource;
import org.rifidi.services.tags.sources.SGTIN96TagSource;
import org.rifidi.services.tags.sources.SSCC96TagSource;

/**
 * @author Jochen Mader - jochen@pramari.com
 *
 */
public class TagServiceImpl implements TagService {

	private Map<String,TagSource> tagSources;
	
	/**
	 * Constructor.
	 */
	public TagServiceImpl(){
		tagSources=new HashMap<String, TagSource>();
		tagSources.put("DoD96",new DoD96TagSource());
		tagSources.put("GID96",new GID96TagSource());
		tagSources.put("SGTIN96",new SGTIN96TagSource());
		tagSources.put("SSCC96",new SSCC96TagSource());
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.services.tags.TagService#getRifidiTag(java.lang.String)
	 */
	@Override
	public RifidiTag getRifidiTag(String sourceName) {
		return tagSources.get(sourceName).getRifidiTag();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.services.tags.TagService#getRifidiTags(java.lang.String, int)
	 */
	@Override
	public List<RifidiTag> getRifidiTags(String sourceName, int number) {
		return tagSources.get(sourceName).getRifidiTags(number);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.services.tags.TagService#getTagSourceNames()
	 */
	@Override
	public List<String> getTagSourceNames() {
		return new ArrayList<String>(tagSources.keySet());
	}

	/* (non-Javadoc)
	 * @see org.rifidi.services.tags.TagService#returnRifidiTag(java.lang.String, org.rifidi.emulator.tags.impl.RifidiTag)
	 */
	@Override
	public void returnRifidiTag(String sourceName, RifidiTag rifidiTag) {
		tagSources.get(sourceName).returnRifidiTag(rifidiTag);
	}

}
