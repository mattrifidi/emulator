/*
 *  IInternalEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

import org.rifidi.designer.entities.gpio.Cable;

/**
 * This interface marks an internal entity. Internal entities are not shown in
 * the library view.
 * 
 * e.g.
 * @see Cable
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 20, 2008
 * 
 */
public interface IInternalEntity {
	/**
	 * Returns true if the entity should be visible in the entities view.
	 * 
	 * @return
	 */
	boolean isVisible();
}
