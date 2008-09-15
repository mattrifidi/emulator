/*
 *  ZoomableLWJGLCamera.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.camera;

import java.util.concurrent.Callable;

import org.rifidi.designer.rcp.game.DesignerGame;

import com.jme.math.Vector3f;
import com.jme.renderer.lwjgl.LWJGLCamera;

/**
 * A decorator class for jme cameras. The idea is to provide zooming capabilites
 * for all classes that implement camera.
 * 
 * @author Jochen Mader - jochen@pramari.com - Sep 15, 2008
 * 
 */
public class ZoomableLWJGLCamera extends LWJGLCamera {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** current zoomlevel */
	private int zoomlevel = 81;
	/** zoom offset */
	private int zoomoffset = -40;
	/** calculation basis for the frustum */
	private int baseFrustumvalue = 50;
	/** LOD of the camera */
	private int lod = 3;
	/** camera values for recreating old perspective */
	private Vector3f[] cameraValues;
	/** implementor of the app */
	private DesignerGame implementor;

	/**
	 * @param implementor
	 * @param width
	 * @param height
	 */
	public ZoomableLWJGLCamera(DesignerGame implementor, int width, int height) {
		super(width, height);
		setFrustum(-100f, 1000.0f, -(baseFrustumvalue + zoomlevel) * 4 / 3,
				(baseFrustumvalue + zoomlevel) * 4 / 3,
				-(baseFrustumvalue + zoomlevel), (baseFrustumvalue + zoomlevel));

		setLocation(new Vector3f(4.3f, 2, 4.6f));
		lookAt(new Vector3f(3.7f, 1, 3), Vector3f.UNIT_Y);
		setParallelProjection(true);
		update();
		this.implementor = implementor;
	}

	/**
	 * Get closer to the scene.
	 */
	public void zoomIn() {
		zoomlevel *= 0.7;
		if (zoomlevel <= 5) {
			zoomlevel = 5;
		}
		adjustLOD();
		implementor.render(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				setFrustum(-200f, 1000.0f,
						-(baseFrustumvalue + zoomlevel + zoomoffset) * 4 / 3,
						(baseFrustumvalue + zoomlevel + zoomoffset) * 4 / 3,
						-(baseFrustumvalue + zoomlevel + zoomoffset),
						(baseFrustumvalue + zoomlevel + zoomoffset));
				update();
				apply();
				return null;
			}

		});
	}

	/**
	 * Move away from the scene.
	 */
	public void zoomOut() {
		zoomlevel *= 1.3;
		if (zoomlevel >= 200) {
			zoomlevel = 200;
		}
		adjustLOD();
		implementor.render(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				setFrustum(-200f, 1000.0f,
						-(baseFrustumvalue + zoomlevel + zoomoffset) * 4 / 3,
						(baseFrustumvalue + zoomlevel + zoomoffset) * 4 / 3,
						-(baseFrustumvalue + zoomlevel + zoomoffset),
						(baseFrustumvalue + zoomlevel + zoomoffset));
				update();
				apply();

				return null;
			}

		});
	}

	/**
	 * Check if lod has changed and apply it.
	 */
	private void adjustLOD() {
		if (zoomlevel + zoomoffset <= -30) {
			if (lod != 0) {
				lod = 0;
			}
		} else if (zoomlevel + zoomoffset <= -10) {
			if (lod != 1) {
				lod = 1;
			}
		} else if (zoomlevel + zoomoffset <= 40) {
			if (lod != 2) {
				lod = 2;
			}
		} else if (zoomlevel + zoomoffset > 40) {
			if (lod != 3) {
				lod = 3;
			}
			if (lod == 3 && cameraValues == null) {
				cameraValues = new Vector3f[3];
				cameraValues[0] = getLeft();
				cameraValues[1] = getUp();
				cameraValues[2] = getDirection();
				setAxes(new Vector3f(-1, 0, 0), new Vector3f(0, 1, -0.5f),
						new Vector3f(0, -1, 0));
				apply();
			}
		}
		if (cameraValues != null && lod != 3) {
			setAxes(cameraValues[0], cameraValues[1], cameraValues[2]);
			apply();
			cameraValues = null;
		}
	}

	/**
	 * @return the lod
	 */
	public int getLod() {
		return this.lod;
	}

}
