/*
 *  Switch.java
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
 * Entities that have an internal state need to implement this interface to be
 * controlled.
 * 
 * @author Jochen Mader Oct 8, 2007
 * 
 */
public interface Switch {

	/**
	 * Start the entity.
	 * 
	 */
	void turnOn();

	/**
	 * Returns true if the Switch is on the on state.
	 * 
	 * @return running state
	 */
	boolean isRunning();

	/**
	 * Stop the entity. Stops the execution and resets the entity to start
	 * state.
	 * 
	 */
	void turnOff();
}