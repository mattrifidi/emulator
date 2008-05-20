/*
 *  CableChangeListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.cabling;

/**
 * Listener for cable changes.
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 17, 2008
 * 
 */
public interface CableChangeListener {
	/**
	 * A cable has been added/deleted.
	 */
	void cableChanged();
}
