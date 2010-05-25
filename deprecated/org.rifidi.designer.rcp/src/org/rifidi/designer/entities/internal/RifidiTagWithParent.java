/*
 *  RifidiTagWithParent.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.internal;

import org.rifidi.services.tags.model.IRifidiTagContainer;
import org.rifidi.tags.impl.RifidiTag;

/**
 * Wrapper object to marry a tag to its parent.
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 23, 2008
 * 
 */
public class RifidiTagWithParent {
	public RifidiTag tag;
	public IRifidiTagContainer parent;

}
