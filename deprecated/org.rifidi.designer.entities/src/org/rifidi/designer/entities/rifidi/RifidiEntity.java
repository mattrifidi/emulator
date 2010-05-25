/*
 *  RifidiEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.rifidi;

import org.rifidi.designer.entities.RMIManager;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;

/**
 * For classes that need access to the Rifidi RMIManager.
 * 
 * @author Jochen Mader Nov 30, 2007
 * 
 */
public interface RifidiEntity {
	/**
	 * @param rmimanager
	 *            the RMIManager instance
	 */
	void setRMIManager(RMIManager rmimanager);
	
	/**
	 * Get the reader module associated with this entity.
	 * @return
	 */
	ReaderModuleManagerInterface getReaderInterface();
}
