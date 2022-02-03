/*
 *  IRifidiTagService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags;

/**
 * 
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 18, 2008
 * 
 */
public interface RifidiTagServiceChangeListener {
	/**
	 * Called whenever the set of tags in the service changes (new one added,
	 * deleted, taken).
	 */
	void tagsChanged();
}
