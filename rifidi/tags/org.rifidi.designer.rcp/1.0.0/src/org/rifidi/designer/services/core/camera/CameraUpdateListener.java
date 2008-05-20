/*
 *  CameraUpdateListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.camera;

import org.rifidi.utilities.camera.ZoomableCamera;

/**
 * Classes that wish to be notified of camera update events should implement
 * this interface and subscribe to the camera service.
 * 
 * @author Dan West - dan@pramari.com
 */
public interface CameraUpdateListener {

	/**
	 * Notifies the listener that the active camera has been updated.
	 * 
	 * @param camera
	 *            the active camera
	 */
	void notifyCameraUpdate(ZoomableCamera camera);
}
