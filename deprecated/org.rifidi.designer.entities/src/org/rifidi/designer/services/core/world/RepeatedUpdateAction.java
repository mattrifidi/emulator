/*
 *  RepeatedUpdateAction.java
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
 * Classes implementing this interface can be submitted to the UpdateThread to
 * be executed on each run.
 * 
 * NOTE: Updates done within here are even performed when the application is set
 * to paused!
 * 
 * @author Jochen Mader Jan 24, 2008
 * @tags
 * 
 */
public interface RepeatedUpdateAction {

	/**
	 * Called on every run of the update thread.
	 * 
	 * @param timePassed
	 *            time passed since last rum of the update thread
	 */
	void doUpdate(float timePassed);
}
