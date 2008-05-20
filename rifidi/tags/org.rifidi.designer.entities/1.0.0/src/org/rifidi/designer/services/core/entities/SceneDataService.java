/*
 *  SceneDataService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.entities;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.SceneData.Direction;

import com.jme.scene.Node;

/**
 * This service is responsible for loading and saving of scenedata. It is also
 * responsible for informing listeners about newly loaded scene.
 * 
 * 
 * @author Jochen Mader Jan 24, 2008
 * @tags
 * 
 */
public interface SceneDataService {

	/**
	 * Load a scene from the given IFile.
	 * 
	 * @param file
	 */
	void loadScene(IFile file);

	/**
	 * Save the currently loaded scene to the given file.
	 * 
	 * @param file
	 *            the file to save it to
	 */
	void saveScene(IFile file);

	/**
	 * Save the currently loaded scene to the file it was loaded from.
	 * 
	 * @param file
	 *            the file to save it to
	 */
	void saveScene();

	/**
	 * Registers a listener to inform objects about changes to scenedata
	 * to the last loaded scenedata.
	 * 
	 * @param viewer
	 */
	void addSceneDataChangedListener(SceneDataChangedListener listener);

	/**
	 * Remove a registered viewer from the list of viewers.
	 * @param listener
	 */
	void removeSceneDataChangedListener(SceneDataChangedListener listener);
	
	/**
	 * Get the RootNode of the currently loaded scene.
	 * @return
	 */
	Node getRootNode();
	
	/**
	 * Get the RoomNode of the currently loaded scene.
	 * @return
	 */
	Node getRoomNode();
	
	/**
	 * Get the width of the currently loaded scenedata.
	 * @return
	 */
	Integer getWidth();
	
	/**
	 * Get the roomwalls.
	 * @return
	 */
	Map<Direction, Node> getWalls();
	
	/**
	 * Get the currently loaded SceneData.
	 * 
	 * @return currently loaded SceneData or null
	 */
	SceneData getCurrentSceneData();

	/**
	 * Set the name of the currently loaded scene.
	 * @param name
	 */
	void setName(String name);
	
	/**
	 * Get the name of the currently loaded scene.
	 * @return
	 */
	String getName();
}