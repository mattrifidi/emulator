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
package org.rifidi.emulator.tags.service;

import java.util.List;

import org.rifidi.emulator.tags.factory.TagCreationPattern;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@prmari.com
 *
 */
public interface TagRegistryService {
	
	public void initialize();
	
	public void initialize(List<RifidiTag> tags);
	
	public List<RifidiTag> createTags(TagCreationPattern tagCreationPattern);
	
	public void remove(RifidiTag tag);
	
	public void remove(List<RifidiTag> tags);
	
	public void remove();
	
	public List<RifidiTag> getTags();
	
	public void addListener(TagRegistryListener listener);
	
	public void removeListener(TagRegistryListener listener);
	
	

}
