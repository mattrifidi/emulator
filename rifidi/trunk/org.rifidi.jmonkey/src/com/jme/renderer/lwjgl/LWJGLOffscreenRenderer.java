/*
 * Copyright (c) 2003-2007 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jme.renderer.lwjgl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.OffscreenRenderer;
import com.jme.scene.Spatial;
import com.jme.util.geom.BufferUtils;

/**
 * This class is used by LWJGL to render a scene to a RenderBuffer (FrameBuffer
 * Objects must be supported) and return an IntBuffer containing pixel data,
 * which can then be used to create an image (for instance an AWT BufferedImage,
 * or an SWT ImageData). Users should <b>not </b> create this class directly.
 * Instead, allow DisplaySystem to create it for you.
 * 
 * @author Joshua Slack, Mark Powell, Olivier Sambourg
 * @version $Id: LWJGLTextureRenderer.java,v 1.38 2007/04/17 20:41:45 rherlitz
 *          Exp $
 * @see com.jme.system.DisplaySystem#createOffscreenRenderer
 */
public class LWJGLOffscreenRenderer implements OffscreenRenderer {

	private static Log logger=LogFactory.getLog(LWJGLOffscreenRenderer.class);
	
	private LWJGLCamera camera;

	private ColorRGBA backgroundColor = new ColorRGBA(1, 1, 1, 1);

	private int active, fboID, colorRBID, depthRBID, width, height;
	private IntBuffer data;
	private boolean isSupported = true;

	private LWJGLRenderer parentRenderer;

	public LWJGLOffscreenRenderer(int width, int height,
			LWJGLRenderer parentRenderer) {

		isSupported = GLContext.getCapabilities().GL_EXT_framebuffer_object;
		if (!isSupported) {
			logger.warn("FBO not supported.");
			// XXX: Fall back to Pbuffer?
			return;
		} else {
			logger.info("FBO support detected.");
		}

		/* Create target image IntBuffer */
		data = ByteBuffer.allocateDirect(width * height * 4).order(
				ByteOrder.LITTLE_ENDIAN).asIntBuffer();

		/* Buffer used for IDs */
		IntBuffer buffer = BufferUtils.createIntBuffer(1);

		/* Init framebuffer object */
		EXTFramebufferObject.glGenFramebuffersEXT(buffer);
		fboID = buffer.get(0);
		if (fboID <= 0) {
			logger.fatal(
					"Invalid FBO id returned! " + fboID);
			isSupported = false;
			// XXX: Fall back to Pbuffer?
			return;
		}
		EXTFramebufferObject.glBindFramebufferEXT(
				EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboID);

		/* Init depth renderbuffer */
		EXTFramebufferObject.glGenRenderbuffersEXT(buffer); // generate id
		depthRBID = buffer.get(0);
		EXTFramebufferObject.glBindRenderbufferEXT(
				EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRBID);
		EXTFramebufferObject.glRenderbufferStorageEXT(
				EXTFramebufferObject.GL_RENDERBUFFER_EXT,
				GL14.GL_DEPTH_COMPONENT32, width, height);
		/* Attache depth renderbuffer to framebuffer */
		EXTFramebufferObject.glFramebufferRenderbufferEXT(
				EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
				EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT,
				EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRBID);

		/* Init color renderbuffer */
		EXTFramebufferObject.glGenRenderbuffersEXT(buffer); // generate id
		colorRBID = buffer.get(0);
		EXTFramebufferObject.glBindRenderbufferEXT(
				EXTFramebufferObject.GL_RENDERBUFFER_EXT, colorRBID);
		EXTFramebufferObject.glRenderbufferStorageEXT(
				EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL11.GL_RGB, width,
				height);
		/* Attach color renderbuffer to framebuffer */
		EXTFramebufferObject.glFramebufferRenderbufferEXT(
				EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
				EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,
				EXTFramebufferObject.GL_RENDERBUFFER_EXT, colorRBID);

		this.width = width;
		this.height = height;

		this.parentRenderer = parentRenderer;
		initCamera();
	}

	/**
	 * 
	 * isSupported obtains the capability of the graphics card. If the graphics
	 * card does not have Framebuffer Object support, false is returned,
	 * otherwise, true is returned. LWJGLOffscreenRenderer will not process any
	 * scene elements if pbuffer is not supported.
	 * 
	 * @return if this graphics card supports pbuffers or not.
	 */
	public boolean isSupported() {
		return isSupported;
	}

	/**
	 * getCamera retrieves the camera this renderer is using.
	 * 
	 * @return the camera this renderer is using.
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * setCamera sets the camera this renderer should use.
	 * 
	 * @param camera
	 *            the camera this renderer should use.
	 */
	public void setCamera(Camera camera) {

		this.camera = (LWJGLCamera) camera;
	}

	/**
	 * setBackgroundColor sets the OpenGL clear color to the color specified.
	 * 
	 * @see com.jme.renderer.OffscreenRenderer#setBackgroundColor(com.jme.renderer.ColorRGBA)
	 * @param c
	 *            the color to set the background color to.
	 */
	public void setBackgroundColor(ColorRGBA c) {
		// if color is null set background to white.
		if (c == null) {
			backgroundColor.a = 1.0f;
			backgroundColor.b = 1.0f;
			backgroundColor.g = 1.0f;
			backgroundColor.r = 1.0f;
		} else {
			backgroundColor = c;
		}
	}

	/**
	 * getBackgroundColor retrieves the clear color of the current OpenGL
	 * context.
	 * 
	 * @see com.jme.renderer.Renderer#getBackgroundColor()
	 * @return the current clear color.
	 */
	public ColorRGBA getBackgroundColor() {
		return backgroundColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.renderer.OffscreenRenderer#render(com.jme.scene.Spatial)
	 */
	public void render(Spatial toDraw) {
		render(toDraw, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.renderer.OffscreenRenderer#render(com.jme.scene.Spatial,
	 *      boolean)
	 */
	public void render(Spatial toDraw, boolean doClear) {
		if (!isSupported) {
			return;
		}

		try {

			activate();

			// Check FBO complete
			checkFBOComplete();

			switchCameraIn(doClear);

			// Override parent's last frustum test to avoid accidental incorrect
			// cull
			if (toDraw.getParent() != null)
				toDraw.getParent().setLastFrustumIntersection(
						Camera.INTERSECTS_FRUSTUM);
			doDraw(toDraw);

			/* Read pixels from renderbuffer */
			GL11.glReadBuffer(EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT);
			GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA,
					GL11.GL_UNSIGNED_BYTE, data);

			/* Restore scene */
			switchCameraOut();
			deactivate();

		} catch (Exception e) {
			logger.error(this.getClass().toString()+" "+
					"render(Spatial) "+e);
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.renderer.OffscreenRenderer#getImageData()
	 */
	public IntBuffer getImageData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.renderer.OffscreenRenderer#render(java.util.ArrayList)
	 */
	public void render(ArrayList<? extends Spatial> toDraw) {
		render(toDraw, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.renderer.OffscreenRenderer#render(java.util.ArrayList,
	 *      boolean)
	 */
	public void render(ArrayList<? extends Spatial> toDraw, boolean doClear) {
		if (!isSupported) {
			return;
		}

		try {

			// setup and render directly to a 2d texture.
			activate();

			EXTFramebufferObject.glFramebufferRenderbufferEXT(
					EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
					EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT,
					EXTFramebufferObject.GL_RENDERBUFFER_EXT, 0);

			// Check FBO complete
			checkFBOComplete();

			switchCameraIn(doClear);

			for (int x = 0, max = toDraw.size(); x < max; x++) {
				Spatial spat = toDraw.get(x);
				// Override parent's last frustum test to avoid accidental
				// incorrect cull
				if (spat.getParent() != null)
					spat.getParent().setLastFrustumIntersection(
							Camera.INTERSECTS_FRUSTUM);

				doDraw(spat);
			}

			/* Read pixels from renderbuffer */
			GL11.glReadBuffer(EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT);
			GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA,
					GL11.GL_UNSIGNED_BYTE, data);

			switchCameraOut();
			deactivate();

		} catch (Exception e) {
			logger.error(this.getClass().toString()+" "+
					"render(Spatial, Texture)"+e);
			e.printStackTrace();
		}
	}

	private void checkFBOComplete() {
		int framebuffer = EXTFramebufferObject
				.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
		switch (framebuffer) {
		case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
			break;
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ fboID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ fboID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ fboID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ fboID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ fboID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception");
		case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
			throw new RuntimeException(
					"FrameBuffer: "
							+ fboID
							+ ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception");
		default:
			throw new RuntimeException(
					"Unexpected reply from glCheckFramebufferStatusEXT: "
							+ framebuffer);
		}
	}

	private Camera oldCamera;
	private int oldWidth, oldHeight;

	public void switchCameraIn(boolean doClear) {
		// grab non-rtt settings
		oldCamera = parentRenderer.getCamera();
		oldWidth = parentRenderer.getWidth();
		oldHeight = parentRenderer.getHeight();
		parentRenderer.setCamera(getCamera());

		// swap to rtt settings
		parentRenderer.getQueue().swapBuckets();
		parentRenderer.reinit(width, height);

		// clear the scene
		if (doClear) {
			parentRenderer.clearBuffers();
		}

		getCamera().update();
		getCamera().apply();
	}

	public void switchCameraOut() {
		parentRenderer.setCamera(oldCamera);
		parentRenderer.reinit(oldWidth, oldHeight);

		// back to the non rtt settings
		parentRenderer.getQueue().swapBuckets();
		oldCamera.update();
		oldCamera.apply();
	}

	private void doDraw(Spatial spat) {
		// do scene render
		spat.onDraw(parentRenderer);
		parentRenderer.renderQueue();
	}

	public void activate() {
		if (!isSupported) {
			return;
		}
		if (active == 0) {
			GL11.glClearColor(backgroundColor.r, backgroundColor.g,
					backgroundColor.b, backgroundColor.a);
			EXTFramebufferObject.glBindFramebufferEXT(
					EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboID);
		}
		active++;
	}

	public void deactivate() {
		if (!isSupported) {
			return;
		}
		if (active == 1) {
			GL11.glClearColor(parentRenderer.getBackgroundColor().r,
					parentRenderer.getBackgroundColor().g, parentRenderer
							.getBackgroundColor().b, parentRenderer
							.getBackgroundColor().a);
			EXTFramebufferObject.glBindFramebufferEXT(
					EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		}
		active--;
	}

	private void initCamera() {
		if (!isSupported) {
			return;
		}
		logger.info("init OffscreenRenderer camera");
		camera = new LWJGLCamera(width, height, this, true);
		camera.setFrustum(1.0f, 1000.0f, -0.50f, 0.50f, 0.50f, -0.50f);
		Vector3f loc = new Vector3f(0.0f, 0.0f, 0.0f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
		camera.setFrame(loc, left, up, dir);
		camera.setDataOnly(false);
	}

	public void updateCamera() {
	}

	public void cleanup() {
		if (!isSupported) {
			return;
		}

		if (fboID > 0) {
			IntBuffer id = BufferUtils.createIntBuffer(1);
			id.put(fboID);
			EXTFramebufferObject.glDeleteFramebuffersEXT(id);
		}
	}

	public LWJGLRenderer getParentRenderer() {
		return parentRenderer;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
