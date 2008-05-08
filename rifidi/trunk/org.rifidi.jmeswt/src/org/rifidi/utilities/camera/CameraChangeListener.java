package org.rifidi.utilities.camera;

/**
 * Classes who wish to be notified of camera change events should implement this
 * interface and subscribe to the camera service.
 * 
 * @author Dan West - dan@pramari.com
 */
public interface CameraChangeListener {

	/**
	 * Notifies the implementing class that the active camera has changed.
	 * 
	 * @param cam
	 *            the new active camera
	 */
	void notifyCameraChange(ZoomableCamera cam);
}
