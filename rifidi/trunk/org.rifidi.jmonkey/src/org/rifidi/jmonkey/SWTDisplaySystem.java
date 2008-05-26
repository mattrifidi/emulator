/*
 *  SWTDisplaySystem.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jmonkey;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GLContext;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.OffscreenRenderer;
import com.jme.renderer.Renderer;
import com.jme.renderer.lwjgl.LWJGLOffscreenRenderer;
import com.jme.renderer.lwjgl.LWJGLRenderer;
import com.jme.system.DisplaySystem;
import com.jme.system.lwjgl.LWJGLDisplaySystem;

/**
 * A jmonkey display system for SWT. 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 22, 2008
 * 
 */
public class SWTDisplaySystem extends LWJGLDisplaySystem {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger
			.getLogger(SWTDisplaySystem.class.getName());
	/**
	 * Canvas currently in use.
	 */
	private GLCanvas currentglcanvas;
	/**
	 * Map for mapping a canvas to a renderer.
	 */
	private Map<GLCanvas, Renderer> canvasToRenderer;

	/**
	 * Constructor.
	 */
	public SWTDisplaySystem() {
		super();
		logger.info("SWT Display System created.");
		canvasToRenderer = new HashMap<GLCanvas, Renderer>();
	}

	/**
	 * Create a GLCanvas and it's renderer and rgister them with the
	 * displaysystem.
	 * 
	 * @param width
	 *            wifth of the canvas
	 * @param height
	 *            height of the canvas.
	 * @param parent
	 *            parent of the canvas.
	 * @param data
	 *            data for the canvas
	 * @return
	 */
	public GLCanvas createGLCanvas(int width, int height, Composite parent,
			GLData data) {
		this.width = width;
		this.height = height;
		parent.setLayout(new FillLayout());
		GLCanvas canvas = new GLCanvas(parent, SWT.NO_REDRAW_RESIZE, data);

		canvas.setCurrent();
		try {
			GLContext.useContext(canvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		LWJGLRenderer renderer = new LWJGLRenderer(width, height);
		renderer.setBackgroundColor(ColorRGBA.lightGray);
		renderer.setHeadless(true);
		canvasToRenderer.put(canvas, renderer);
		setCurrentGLCanvas(canvas);

		DisplaySystem.updateStates(getRenderer());
		created = true;
		return canvas;
	}

	/**
	 * Shortcut to create a GLCanvas with reasonable default values.
	 * 
	 * @param width
	 *            width of the canvas
	 * @param height
	 *            height of the canvas
	 * @param parent
	 *            the parent of the canvas
	 * @return
	 */
	public GLCanvas createGLCanvas(int width, int height, Composite parent) {
		this.width = width;
		this.height = height;
		parent.setLayout(new FillLayout());
		GLData data = new GLData();
		data.doubleBuffer = true;
		data.depthSize = 24;
		return createGLCanvas(width, height, parent, data);
	}

	/**
	 * Switch to another canvas. NOTE: That canvas HAS to be created through a
	 * call to createGLCanvas.
	 * 
	 * @param canvas
	 */
	public void setCurrentGLCanvas(GLCanvas canvas) {
		currentglcanvas = canvas;
		((GLCanvas) canvas).setCurrent();
		try {
			GLContext.useContext(canvas);
		} catch (LWJGLException e) {
			logger.warning("Unable to switch to GLCanvas: " + e);
		}
		setRenderer(canvasToRenderer.get(canvas));
		switchContext(canvas);
	}

	/**
	 * Get the glcanvas currently in use.
	 * 
	 * @return
	 */
	public GLCanvas getCurrentGLCanvas() {
		return currentglcanvas;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.system.DisplaySystem#getHeight()
	 */
	@Override
	public int getHeight() {
		return getRenderer().getHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.system.DisplaySystem#getWidth()
	 */
	@Override
	public int getWidth() {
		return getRenderer().getWidth();
	}

	/**
	 * Create a render that allows offscreen rendering.
	 * 
	 * @param width
	 *            width of the renderer
	 * @param height
	 *            height of the renderer
	 * @return
	 */
	public OffscreenRenderer createOffscreenRenderer(int width, int height) {
		if (!isCreated()) {
			return null;
		}

		return new LWJGLOffscreenRenderer(width, height,
				(LWJGLRenderer) getRenderer());

	}
}
