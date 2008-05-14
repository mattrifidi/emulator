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
import org.rifidi.designer.entities.RMIManager;

/**
 * Every entity wizard is required to implement this interface additionally to
 * the jface wizard stuff.
 * This interface is for entites that use the rifidi emulator.
 * 
 * @author Jochen Mader Oct 4, 2007
 * 
 */
public interface EntityWizardRifidiIface {
	/**
	 * Get the newly created entity. 
	 * Returns null on creation failure.
	 * 
	 * @return get the entity
	 */
	Entity getEntity();
	
	/**
	 * RMIManager for creating/managing readers.
	 * @param rmiManager
	 */
	void setRMIManager(RMIManager rmiManager);
}
