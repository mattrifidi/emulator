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
package org.rifidi.designer.rcp.game;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jme.math.FastMath;
import com.jme.math.Matrix4f;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
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
	/** zoomcallable used by both zoomin methods */
	private ZoomCallable zoomCallable;

	/**
	 * @param implementor
	 * @param width
	 * @param height
	 */
	public ZoomableLWJGLCamera(DesignerGame implementor, int width, int height) {
		super(width, height);
		setFrustum(-200f, 1000.0f,
				-(baseFrustumvalue + zoomlevel + zoomoffset) * 4 / 3,
				(baseFrustumvalue + zoomlevel + zoomoffset) * 4 / 3,
				-(baseFrustumvalue + zoomlevel + zoomoffset), (baseFrustumvalue
						+ zoomlevel + zoomoffset));

		setLocation(new Vector3f(4.3f, 2, 4.6f));
		lookAt(new Vector3f(3.7f, 1, 3), Vector3f.UNIT_Y);
		setParallelProjection(true);
		zoomCallable = new ZoomCallable();
		update();
		this.implementor = implementor;
	}

	/**
	 * Get closer to the scene.
	 */
	public void zoomIn(final int x, final int y) {
		if (zoomCallable.working.compareAndSet(false, true)) {
			zoomlevel *= 0.7;
			if (zoomlevel <= 5) {
				zoomlevel = 5;
			}
			zoomCallable.x = x;
			zoomCallable.y = y;
			implementor.render(zoomCallable);
		}
	}

	/**
	 * Move away from the scene.
	 */
	public void zoomOut(int x, int y) {
		if (zoomCallable.working.compareAndSet(false, true)) {
			zoomlevel *= 1.3;
			if (zoomlevel >= 200) {
				zoomlevel = 200;
			}
			zoomCallable.x = x;
			zoomCallable.y = y;
			implementor.render(zoomCallable);
		}
	}

	/**
	 * Check if lod has changed and apply it.
	 */
	public void adjustLOD() {
		if (zoomlevel + zoomoffset <= -30) {
			if (lod != 0) {
				lod = 0;
			}
		} else if (zoomlevel + zoomoffset <= -10) {
			if (lod != 1) {
				lod = 1;
			}
		} else if (zoomlevel + zoomoffset <= 70) {
			if (lod != 2) {
				lod = 2;
			}
		} else if (zoomlevel + zoomoffset > 70) {
			if (lod != 3) {
				lod = 3;
			}
			//death to 2d view
			if (lod == 3 && cameraValues == null) {
				cameraValues = new Vector3f[3];
				cameraValues[0] = getLeft();
				cameraValues[1] = getUp();
				cameraValues[2] = getDirection();
				setAxes(new Vector3f(-1f, 0f, 0f), new Vector3f(0f, 0f, -1f),
						new Vector3f(0f, -1f, 0f));

				update();
				apply();
			}
			//yep, it's dead
		}
		if (cameraValues != null && lod != 3) {
			setAxes(cameraValues[0], cameraValues[1], cameraValues[2]);
			apply();
			cameraValues = null;
		}
	}

	public Vector3f getWorldCoordinates(Vector2f screenPosition, float zPos,
			Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		Matrix4f modelViewProjection = getModelViewMatrix().mult(
				getProjectionMatrix());
		Matrix4f modelViewProjectionInverse = invert(modelViewProjection);
		Quaternion tmp_quat = new Quaternion();
		tmp_quat.set((screenPosition.x / getWidth() - viewPortLeft)
				/ (viewPortRight - viewPortLeft) * 2 - 1, (screenPosition.y
				/ getHeight() - viewPortBottom)
				/ (viewPortTop - viewPortBottom) * 2 - 1, zPos * 2 - 1, 1);
		modelViewProjectionInverse.mult(tmp_quat, tmp_quat);
		tmp_quat.multLocal(1.0f / tmp_quat.w);
		store.x = tmp_quat.x;
		store.y = tmp_quat.y;
		store.z = tmp_quat.z;
		return store;
	}

	public Matrix4f invert(Matrix4f source) {
		Matrix4f store = new Matrix4f();

		float fA0 = source.m00 * source.m11 - source.m01 * source.m10;
		float fA1 = source.m00 * source.m12 - source.m02 * source.m10;
		float fA2 = source.m00 * source.m13 - source.m03 * source.m10;
		float fA3 = source.m01 * source.m12 - source.m02 * source.m11;
		float fA4 = source.m01 * source.m13 - source.m03 * source.m11;
		float fA5 = source.m02 * source.m13 - source.m03 * source.m12;
		float fB0 = source.m20 * source.m31 - source.m21 * source.m30;
		float fB1 = source.m20 * source.m32 - source.m22 * source.m30;
		float fB2 = source.m20 * source.m33 - source.m23 * source.m30;
		float fB3 = source.m21 * source.m32 - source.m22 * source.m31;
		float fB4 = source.m21 * source.m33 - source.m23 * source.m31;
		float fB5 = source.m22 * source.m33 - source.m23 * source.m32;
		float fDet = fA0 * fB5 - fA1 * fB4 + fA2 * fB3 + fA3 * fB2 - fA4 * fB1
				+ fA5 * fB0;

		if (FastMath.abs(fDet) <= 0)
			throw new ArithmeticException("This matrix cannot be inverted");

		store.m00 = +source.m11 * fB5 - source.m12 * fB4 + source.m13 * fB3;
		store.m10 = -source.m10 * fB5 + source.m12 * fB2 - source.m13 * fB1;
		store.m20 = +source.m10 * fB4 - source.m11 * fB2 + source.m13 * fB0;
		store.m30 = -source.m10 * fB3 + source.m11 * fB1 - source.m12 * fB0;
		store.m01 = -source.m01 * fB5 + source.m02 * fB4 - source.m03 * fB3;
		store.m11 = +source.m00 * fB5 - source.m02 * fB2 + source.m03 * fB1;
		store.m21 = -source.m00 * fB4 + source.m01 * fB2 - source.m03 * fB0;
		store.m31 = +source.m00 * fB3 - source.m01 * fB1 + source.m02 * fB0;
		store.m02 = +source.m31 * fA5 - source.m32 * fA4 + source.m33 * fA3;
		store.m12 = -source.m30 * fA5 + source.m32 * fA2 - source.m33 * fA1;
		store.m22 = +source.m30 * fA4 - source.m31 * fA2 + source.m33 * fA0;
		store.m32 = -source.m30 * fA3 + source.m31 * fA1 - source.m32 * fA0;
		store.m03 = -source.m21 * fA5 + source.m22 * fA4 - source.m23 * fA3;
		store.m13 = +source.m20 * fA5 - source.m22 * fA2 + source.m23 * fA1;
		store.m23 = -source.m20 * fA4 + source.m21 * fA2 - source.m23 * fA0;
		store.m33 = +source.m20 * fA3 - source.m21 * fA1 + source.m22 * fA0;

		float fInvDet = 1.0f / fDet;
		store.multLocal(fInvDet);

		return store;
	}

	/**
	 * @return the lod
	 */
	public int getLod() {
		return this.lod;
	}

	private class ZoomCallable implements Callable<Object> {

		public AtomicBoolean working = new AtomicBoolean(false);
		public int x, y;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Object call() throws Exception {
			Vector3f coordsOld = getWorldCoordinates(new Vector2f(x, y), 0);
			Vector3f coordsOld2 = getWorldCoordinates(new Vector2f(x, y), 1);
			Vector3f direction = coordsOld.subtract(coordsOld2)
					.normalizeLocal();
			coordsOld.subtractLocal(direction.mult(coordsOld.y / direction.y));
			coordsOld.setY(0);

			setFrustum(-200f, 1000.0f,
					-(baseFrustumvalue + zoomlevel + zoomoffset) * 4 / 3,
					(baseFrustumvalue + zoomlevel + zoomoffset) * 4 / 3,
					-(baseFrustumvalue + zoomlevel + zoomoffset),
					(baseFrustumvalue + zoomlevel + zoomoffset));
			update();
			apply();
			

			Vector3f coordsNew = getWorldCoordinates(new Vector2f(x, y), 0);
			Vector3f coordsNew2 = getWorldCoordinates(new Vector2f(x, y), 1);
			Vector3f direction2 = coordsNew.subtract(coordsNew2)
					.normalizeLocal();
			coordsNew.subtractLocal(direction.mult(coordsNew.y / direction2.y));
			coordsNew.setY(0);
			getLocation().addLocal(coordsOld.subtract(coordsNew));
			update();
			apply();
			adjustLOD();
			working.compareAndSet(true, false);
			return null;
		}

	}

	/**
	 * @return the zoomlevel
	 */
	public int getZoomlevel() {
		return this.zoomlevel;
	}
}
