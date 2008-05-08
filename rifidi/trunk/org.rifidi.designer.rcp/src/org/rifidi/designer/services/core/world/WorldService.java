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

import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;

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
	void start();

	/**
	 * Stop the world.
	 */
	void stop();

	/**
	 * Pause the world.
	 */
	void pause();

	/**
	 * Add a new repeated action.
	 * 
	 * @param action
	 */
	void addRepeatedUpdateActiom(RepeatedUpdateAction action);

	/**
	 * Remove repeated action.
	 * 
	 * @param action
	 */
	void removeRepeatedUpdateActiom(RepeatedUpdateAction action);

	/**
	 * Set the GLCanvas the world should be rendered to.
	 */
	void setGLCanvas(GLCanvas glCanvas);

	/**
	 * @param display the display to set
	 */
	public void setDisplay(Display display);
}
