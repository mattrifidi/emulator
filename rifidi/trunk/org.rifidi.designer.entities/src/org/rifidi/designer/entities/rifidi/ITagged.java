/*
 *  ITagged.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.rifidi;

import org.rifidi.tags.impl.RifidiTag;

/**
 * FIXME: Class comment.  
 * 
 * @author Jochen Mader - jochen@pramari.com - Nov 26, 2008
 * 
 */
public interface ITagged {
	public void setRifidiTag(RifidiTag tag);
	public RifidiTag getRifidiTag();
}
