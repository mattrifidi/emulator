/*
 *  FrustumCheckCamera.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jme.quadtree;

import com.jme.math.Matrix4f;
import com.jme.renderer.AbstractCamera;

/**
 * @author Jochen Mader Oct 25, 2007
 *
 */
public class FrustumCheckCamera extends AbstractCamera {

	private int width;
	private int height;
	
	public FrustumCheckCamera(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see com.jme.renderer.AbstractCamera#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/* (non-Javadoc)
	 * @see com.jme.renderer.AbstractCamera#getModelViewMatrix()
	 */
	@Override
	public Matrix4f getModelViewMatrix() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.jme.renderer.AbstractCamera#getProjectionMatrix()
	 */
	@Override
	public Matrix4f getProjectionMatrix() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.jme.renderer.AbstractCamera#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/* (non-Javadoc)
	 * @see com.jme.renderer.Camera#apply()
	 */
	public void apply() {
	}

	/* (non-Javadoc)
	 * @see com.jme.renderer.Camera#onViewPortChange()
	 */
	public void onViewPortChange() {
	}

	/* (non-Javadoc)
	 * @see com.jme.renderer.Camera#resize(int, int)
	 */
	public void resize(int width, int height) {
	}

}
