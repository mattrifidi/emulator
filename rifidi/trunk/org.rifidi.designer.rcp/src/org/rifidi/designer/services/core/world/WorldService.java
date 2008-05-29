/*
 *  WorldService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.world;

/**
 * This service controls the world.
 * 
 * @author Jochen Mader Jan 24, 2008
 * @tags
 * 
 */
public interface WorldService {
	/**
	 * Start the world.
	 */
	void run();

	/**
	 * Stop the world.
	 */
	void stop();

	/**
	 * Pause the world.
	 */
	void pause();
}
