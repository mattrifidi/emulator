/*
 *  ZoomableCamera.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.utilities.camera;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;

/**
 * Wrapper class for the a Jmonkey camera that takes care of zooming, centering,
 * and resizing.
 * 
 * @author Dan West - 'Phoenix' - dan@pramari.com
 */
public class ZoomableCamera {
	/**
	 * The logger.
	 */
	private static Log logger = LogFactory.getLog(ZoomableCamera.class);
	/**
	 * Listeners to be notified of changes to this camera.
	 */
	private List<CameraChangeListener> listeners;
	/**
	 * The Jmonkey camera this wraps.
	 */
	private Camera camera;
	/**
	 * The current zoom level.
	 */
	private int zoom = 0;
	/**
	 * The most recent size this camera was resized to.
	 */
	private Vector2f canvasSize;
	/**
	 * Whether this camera is fixed looking at a point.
	 */
	private boolean lookatMode;
	/**
	 * The point being looked at if this camera is in lookat mode.
	 */
	private Vector3f lookatPoint;
	/**
	 * The maximum horizontal span of the camera (at zoom level 0).
	 */
	private float maxFrustumLR;
	/**
	 * The maximum vertical span of the camera (at zoom level 0).
	 */
	private float maxFrustumTB;

	/**
	 * Creates a new zoomable camera.
	 * 
	 * @param camera
	 *            the Jmonkey camera
	 */
	public ZoomableCamera(final Camera camera) {
		logger.debug("initializing camera:\n" + "\tloc:  "
				+ camera.getLocation() + "\tdir:  " + camera.getDirection()
				+ "\ttblr: " + camera.getFrustumTop() + " "
				+ camera.getFrustumBottom() + " " + camera.getFrustumLeft()
				+ " " + camera.getFrustumRight());

		this.camera = camera;

		setFocusMode(true);
	}

	/**
	 * Sets the zoom level for the camera.
	 * 
	 * @param newzoom
	 *            the new zoom level for the camera
	 */
	public void setZoom(int newzoom) {
		if (newzoom < 0) {
			newzoom = 0;
		}
		if (newzoom > 100) {
			newzoom = 100;
		}
		zoom = newzoom;

		if (canvasSize != null) {

			float aspect = 4f / 3f;
			float xscl = (float) canvasSize.x
					/ (float) DisplaySystem.getDisplaySystem().getRenderer()
							.getWidth();
			float yscl = (float) canvasSize.y
					/ (float) DisplaySystem.getDisplaySystem().getRenderer()
							.getHeight();

			float maxzoom = Math.min(maxFrustumLR / aspect, maxFrustumTB);
			newzoom = (int) ((((float) newzoom) / 100f) * maxzoom);

			float flreff = maxFrustumLR - newzoom * aspect;
			float ftbeff = maxFrustumTB - newzoom;
			camera.setFrustumLeft(-flreff + flreff * (1 - xscl));
			camera.setFrustumRight(flreff + flreff * (1 - xscl));
			camera.setFrustumTop(-ftbeff + ftbeff * (1 - yscl));
			camera.setFrustumBottom(ftbeff + ftbeff * (1 - yscl));
			camera.update();
		}
	}

	/**
	 * Resizes the camera to the new width and height with aspect preservation.
	 * 
	 * @param width
	 *            the new width of the canvas
	 * @param height
	 *            the new height of the canvas
	 */
	public void resize(final int width, final int height) {
		float finalwidth, finalheight;
		float aspect = 4f / 3f;
		float lr, tb;
		float scl;

		canvasSize = new Vector2f(width, height);

		float basefrustumvalue = 80;
		tb = basefrustumvalue;
		lr = basefrustumvalue * aspect;

		// determine the new width and height
		if (width / aspect > height) {
			finalwidth = width;
			finalheight = (float) finalwidth / aspect;
			scl = (float) finalheight / (float) height;
		} else {
			finalheight = height;
			finalwidth = (float) finalheight * aspect;
			scl = (float) finalwidth / (float) width;
		}

		// scale the frustum to the canvas size and apply
		setMaxFrustum(lr * scl, tb * scl);
	}

	/**
	 * @return the current zoom level for this camera
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * Rotates the camera about its viewpoint by the given amount.
	 * 
	 * @param phi
	 *            rotation about the xaxis
	 * @param theta
	 *            rotation about the yaxis
	 */
	public void rotate(final float phi, final float theta) {
		if (lookatMode) {
			float r = lookatPoint.distance(camera.getLocation());

			Quaternion q = new Quaternion();
			q.fromAngles(phi, theta, 0);

			Vector3f dir = camera.getDirection();
			Vector3f newdir = q.toRotationMatrix().mult(dir);
			Vector3f newoff = newdir.normalize().negate().mult(r);

			camera.setLocation(lookatPoint.add(newoff));
			camera.lookAt(camera.getLocation().add(newdir), Vector3f.UNIT_Y);
			camera.update();

		} else {
			Quaternion q = new Quaternion();
			q.fromAngles(phi, theta, 0);
			q.toRotationMatrix(new Matrix3f()).multLocal(camera.getDirection());
			camera.update();
		}
	}

	/**
	 * Centers the camera on the given location, looking in the direction dir.
	 * If either of these parameters are null, the existing value is used.
	 * 
	 * @param loc
	 *            the location to center the camera on
	 * @param dir
	 *            the direction in which to look at the centering point
	 * @param forceapply
	 *            indicates whether or not the camera should actually be applied
	 *            (needed for stuff like recentering then performing ray
	 *            shooting into the scene)
	 */
	public void recenter(Vector3f loc, Vector3f dir) {
		recenter(loc, dir, false);
	}

	/**
	 * Centers the camera on the given location, looking in the direction dir.
	 * If either of these parameters are null, the existing value is used.
	 * 
	 * @param loc
	 *            the location to center the camera on
	 * @param dir
	 *            the direction in which to look at the centering point
	 * @param forceapply
	 *            indicates whether or not the camera should actually be applied
	 *            (needed for stuff like recentering then performing ray
	 *            shooting into the scene)
	 */
	public void recenter(Vector3f loc, Vector3f dir, boolean forceapply) {
		if (loc == null) {
			loc = lookatPoint;
		}
		if (dir == null) {
			dir = getDirection();
		}
		dir.normalizeLocal();
		lookatPoint = loc;

		float y = camera.getLocation().getY() - loc.getY();
		camera.setLocation(loc.subtract(dir.mult(Math.abs(y / dir.y))));
		camera.lookAt(loc, Vector3f.UNIT_Y);
		camera.update();

		// if apply is needed, do it
		if (forceapply) {
			camera.apply();
		}
	}

	/**
	 * Places the camera at the given location, looking in the given direction.
	 * 
	 * @param loc
	 *            the new location of the camera
	 * @param dir
	 *            the new direction the camera faces
	 */
	public void placeCamera(Vector3f loc, Vector3f dir) {
		camera.setLocation(loc);
		camera.lookAt(loc.add(dir), Vector3f.UNIT_Y);
		camera.update();
	}

	/**
	 * @param newmaxlr
	 *            the new maximum left/right value of the frustum
	 * @param newmaxtb
	 *            the new maximum top/bottom value of the frustum
	 */
	private void setMaxFrustum(float newmaxlr, float newmaxtb) {
		maxFrustumLR = newmaxlr;
		maxFrustumTB = newmaxtb;
	}

	/**
	 * @return the max frustum Left/Right value
	 */
	public float getMaxFrustumLR() {
		return maxFrustumLR;
	}

	/**
	 * @return the max frustum Top/Bottom value
	 */
	public float getMaxFrustumTB() {
		return maxFrustumTB;
	}

	/**
	 * Switches between camera rotation and lookat point rotation.
	 * 
	 * @param focus
	 *            the new state of the focus mode
	 */
	public void setFocusMode(final boolean focus) {
		lookatMode = focus;
		if (focus && lookatPoint == null) {
			lookatPoint = camera.getLocation().add(
					camera.getDirection().mult(
							Math.abs(camera.getLocation().y
									/ camera.getDirection().y)));
			logger.debug("focus point is: " + lookatPoint);
		}
	}

	/**
	 * @return the point the camera is currently looking at
	 */
	public Vector3f getLookAtPoint() {
		return lookatPoint;
	}

	/**
	 * Gets the world coordinates for the camera location specified by the given
	 * vector.
	 * 
	 * @param pos
	 *            the location to get the world location of. the vertical or
	 *            horizontal location is specified in the range (0,1)
	 * @return the world coordinates of the given point
	 */
	public Vector3f getWorldCoordinates(final Vector2f pos) {
		return camera.getWorldCoordinates(new Vector2f(pos.x * canvasSize.x,
				pos.y * canvasSize.y), 0);
	}

	/**
	 * Registers the given listener to be notified when this camera changes.
	 * 
	 * @param listener
	 *            the listener to notify on camera changes
	 */
	public void registerCameraChangeListener(final CameraChangeListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<CameraChangeListener>();
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes the listener from those listeners that will be notified on camera
	 * changes.
	 * 
	 * @param listener
	 *            the listener that should no longer be notified
	 */
	public void removeCameraChangeListener(CameraChangeListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	/**
	 * Returns the direction the camera is looking.
	 * 
	 * @return the direction the camera is looking
	 */
	public Vector3f getDirection() {
		return (Vector3f) camera.getDirection().clone();
	}

	/**
	 * Sets this camera as the renderer camera.
	 */
	public void setActiveCamera() {
		DisplaySystem.getDisplaySystem().getRenderer().setCamera(camera);
		camera.update();
	}

	public Vector3f getLocation() {
		return camera.getLocation();
	}
}