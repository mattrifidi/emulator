/*
 *  CameraState.java
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

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 6, 2008
 * 
 */
public class CameraState {
	private Vector3f location;
	private Vector3f direction;
	private Vector3f lookAtPoint;
	private int zoom;
	private String cameraId;

	/**
	 * @return the cameraId
	 */
	public String getCameraId() {
		return this.cameraId;
	}

	/**
	 * @param cameraId the cameraId to set
	 */
	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}

	/**
	 * @return the location
	 */
	public Vector3f getLocation() {
		return this.location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Vector3f location) {
		this.location = location;
	}

	/**
	 * @return the direction
	 */
	public Vector3f getDirection() {
		return this.direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	/**
	 * @return the zoom
	 */
	public int getZoom() {
		return this.zoom;
	}

	/**
	 * @param zoom
	 *            the zoom to set
	 */
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	/**
	 * @return the lookAtPoint
	 */
	public Vector3f getLookAtPoint() {
		return this.lookAtPoint;
	}

	/**
	 * @param lookAtPoint the lookAtPoint to set
	 */
	public void setLookAtPoint(Vector3f lookAtPoint) {
		this.lookAtPoint = lookAtPoint;
	}

}
