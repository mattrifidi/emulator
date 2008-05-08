/*
 *  SceneControl.java
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
 * This interface is for entites to be controlled by the overall scene state
 * (start/stop/pause).
 * 
 * @author Jochen Mader Oct 14, 2007
 */
public interface SceneControl {
	/**
	 * Start the entity.
	 */
	void start();

	/**
	 * Pause an entity. On a subsequent start the entity should resume on the
	 * point where it was paused.
	 */
	void pause();

	/**
	 * Stop the entity, should also reset internal state.
	 */
	void reset();
}