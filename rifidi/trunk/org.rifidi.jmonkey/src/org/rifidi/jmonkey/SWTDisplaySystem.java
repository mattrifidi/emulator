package org.rifidi.jmonkey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.opengl.GLCanvas;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GLContext;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.OffscreenRenderer;
import com.jme.renderer.lwjgl.LWJGLOffscreenRenderer;
import com.jme.renderer.lwjgl.LWJGLRenderer;
import com.jme.system.DisplaySystem;
import com.jme.system.lwjgl.LWJGLDisplaySystem;

public class SWTDisplaySystem extends LWJGLDisplaySystem {
	private Log logger = LogFactory.getLog(SWTDisplaySystem.class);
	private GLCanvas swtglcanvas;

	public SWTDisplaySystem() {
		super();
		logger.info("LWJGL Display System created.");
	}

	public GLCanvas getCurrentGLCanvas() {
		return swtglcanvas;
	}

	public void setCurrentGLCanvas(GLCanvas canvas) {
		swtglcanvas = canvas;
		((GLCanvas) canvas).setCurrent();
		try {
			GLContext.useContext(canvas);
		} catch (LWJGLException e) {
			logger.error("Unable to switch to GLCanvas: "+e);
		}
	}

	public void createCanvasRenderer(int width, int height, ColorRGBA color) {
		setRenderer(new LWJGLRenderer(width, height));
		getRenderer().setBackgroundColor(color);
		getRenderer().setHeadless(true);
		DisplaySystem.updateStates(getRenderer());
		created = true;
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

	public OffscreenRenderer createOffscreenRenderer(int width, int height) {
		if (!isCreated()) {
			return null;
		}

		return new LWJGLOffscreenRenderer(width, height,
				(LWJGLRenderer) getRenderer());

	}
}
