/*
 *  Trigger.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

import org.rifidi.designer.services.core.entities.FinderService;

/**
 * A trigger can be triggered to do something by another object.
 * 
 * @author Jochen Mader Oct 19, 2007
 * 
 */
public interface Trigger {
	/**
	 * Called to execute the trigger function.
	 * 
	 * @param source the source of the trigger event
	 */
	void trigger(Object source);

}
