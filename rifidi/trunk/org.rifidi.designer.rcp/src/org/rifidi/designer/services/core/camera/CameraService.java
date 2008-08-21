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
 * This service keeps track of cameras and adjust LOD. CameraService is NOT
 * supposed to be threadsafe as it should only be used inside the eclipse thread.
 * 
 * @author Jochen Mader - jochen@pramari.com
 */
public interface CameraService {

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
	 * 
	 * @return
	 */
	public Camera getMainCamera();

	/**
	 * Center the current camera on the scene.
	 */
	public void centerCamera();

	/**
	 * Move the camera to the given position.
	 */
	public void positionCamera(Vector3f targetPos);

	/**
	 * This method alters the current LOD. This is used by the minimap and
	 * called on each minimap rendering. This only temporary changes the LOD. To
	 * prevent weird results call resetLOD after you are done.
	 */
	public void setLOD(int lod);

	/**
	 * Reset the LOD to the original value.
	 */
	public void resetLOD();
}
