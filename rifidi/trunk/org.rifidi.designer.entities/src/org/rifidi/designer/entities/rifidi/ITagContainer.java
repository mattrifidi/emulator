/*
 *  ITagContainer.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.rifidi;

import java.util.Set;

import org.rifidi.services.tags.impl.RifidiTag;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Nov 26, 2008
 * 
 */
public interface ITagContainer {
	public void addTags(Set<RifidiTag> tags);

	public void removeTags(Set<RifidiTag> tags);

	public void removeTag(RifidiTag tag);
}
