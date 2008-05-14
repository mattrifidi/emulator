/*
 *  EntityWizardIface.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library;

import org.rifidi.designer.entities.Entity;

/**
 * Every entity wizard is required to implement this interface additionally to
 * the jface wizard stuff.
 * 
 * @author Jochen Mader Oct 4, 2007
 * 
 */
public interface EntityWizardIface {
	/**
	 * Get the newly created entity. 
	 * Returns null on creation failure.
	 * 
	 * @return get the new entity
	 */
	Entity getEntity();
}
