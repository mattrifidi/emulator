/*
 *  SceneDataChangedListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.scenedata;

import org.rifidi.designer.entities.SceneData;

/**
 * Listeners implementing this interface will be informed
 * about load events for SceneData.
 * 
 * @see SceneData
 * @author Jochen Mader Jan 24, 2008
 * @tags
 * 
 */
public interface SceneDataChangedListener {

	/**
	 * Initialize the world, create threads ...
	 * @param sceneData
	 */
	void sceneDataChanged(SceneData sceneData);
	
	/**
	 * Destroy all ressources bound for the given scenedata.
	 * This method should block until everything is done
	 * @param sceneData
	 */
	void destroySceneData(SceneData sceneData);
}
