/*
 *  CommandStateService.java
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
 * This service is used to enable/disable commands based on the state of the
 * world.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 18, 2008
 * 
 */
public interface CommandStateService {
	/**
	 * Checks if the given command is enabled for the current worldstate.
	 * 
	 * @param commandName
	 * @return
	 */
	public boolean isEnabled(String commandName);
}
