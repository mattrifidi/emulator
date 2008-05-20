/*
 *  CameraService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.camera;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * This service keeps track of cameras and notifies subscribed listeners of
 * camera change/update events..
 * 
 * @author Dan West - dan@pramari.com
 */
public interface CameraService {

	/**
	 * Store the position, zoomlevel and direction of the currently active
	 * camera.
	 * 
	 * @param num
	 */
	void recordCameraState(int num);

	/**
	 * Load a stored camera state back.
	 * @param num
	 */
	void loadRecordedCameraState(int num);
	
	/**
	 * Zoom in one step.
	 */
	void zoomIn();
	
	/**
	 * Zoom out one step.
	 */
	void zoomOut();
	
	/**
	 * Set zoom to default level.
	 */
	void resetZoom();
	
	/**
	 * Creates a camera with the default settings.
	 * 
	 * @return a new initialized Jmonkey camera
	 */
	public void createCamera();
	
	/**
	 * Get the main camera used by the 3d view.
	 * @return
	 */
	public Camera getMainCamera();
	
	/**
	 * Center the current camera.
	 */
	public void centerCamera();
	
	/**
	 * 
	 */
	public void positionCamera(Vector3f targetPos);
}
