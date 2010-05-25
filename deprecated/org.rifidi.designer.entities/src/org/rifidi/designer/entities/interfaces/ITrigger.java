/*
 *  ITrigger.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

/**
 * A trigger can be triggered by another object to performe an action.
 * 
 * @author Jochen Mader Oct 19, 2007
 * 
 */
public interface ITrigger {
	/**
	 * Called to execute the trigger function.
	 * 
	 * @param source the source of the trigger event
	 */
	void trigger(Object source);

}
