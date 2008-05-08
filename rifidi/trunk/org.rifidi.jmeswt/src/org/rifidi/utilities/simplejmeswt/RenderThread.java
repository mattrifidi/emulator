/*
 *  RenderThread.java
 *
 *  Project:		RiFidi IDE 2.0 - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.utilities.simplejmeswt;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;
import org.rifidi.jmonkey.SWTDisplaySystem;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.geom.Debugger;

/**
 * This thread is responsible for rendering the scene to the different canvas'.
 * 
 * @author Jochen Mader
 */
public class RenderThread extends Thread {
	private static Log logger = LogFactory.getLog(RenderThread.class);

	private boolean started = false;

	private boolean debug3d = false;

	/**
	 * Flag for thread termination
	 */
	private boolean keepRunning = true;

	/**
	 * Runnable thread for executing the main body of the rendering
	 */
	private RenderRunnable renderRunnable;

	private GLCanvas glCanvas;

	private Display display;

	private ReentrantLock lock;

	/**
	 * Constructor for the renderthread object
	 * 
	 * @param rootNode
	 *            The rootnode for the scene
	 * @param physicsSpace
	 *            The physicsspace for the scene
	 */
	public RenderThread(ReentrantLock lock, Display display, Node rootNode, GLCanvas glCanvas) {
		logger.debug("New RenderThread instantiated.");
		this.glCanvas = glCanvas;
		this.display = display;
		renderRunnable = new RenderRunnable(rootNode);
		this.lock = lock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		logger.debug("RenderThread started");
		try {
			while (keepRunning) {
				started = true;
				if ( !glCanvas.isDisposed() )
					display.asyncExec(renderRunnable);
				try {
					sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			started = false;
		} catch (SWTException e) {
			logger.debug(e);
			e.printStackTrace();
		}
		logger.debug("RenderThread stopped");
	}

	/**
	 * @return the keepRunning
	 */
	public boolean isKeepRunning() {
		return keepRunning;
	}

	/**
	 * @return the started
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * @param keepRunning
	 *            the keepRunning to set
	 */
	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

	/**
	 * The threaded object that executes the main body of the render thread
	 * 
	 * @author jochen
	 * @author dan
	 */
	private class RenderRunnable implements Runnable {
		private SWTDisplaySystem displaySys;

		private Node rootNode;

		public RenderRunnable(Node rootNode) {
			this.rootNode = rootNode;
			displaySys = (SWTDisplaySystem)DisplaySystem.getDisplaySystem();
			checkandresize();
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			lock.lock();
			try {
				// prepare for rendering
				checkandresize();
				rootNode.updateRenderState();
				displaySys.getRenderer().clearBuffers(); // clear screen
				displaySys.getRenderer().draw(rootNode); // draw

				// Execute queued render callables
				GameTaskQueueManager.getManager()
					.getQueue(GameTaskQueue.RENDER).execute();

				// display 3d debugging
				if ( debug3d ) {
					Debugger.drawBounds(rootNode, displaySys.getRenderer());
					Debugger.drawNormals(rootNode, displaySys.getRenderer());
				}

				// finalize the render
				displaySys.getRenderer().displayBackBuffer();
				glCanvas.swapBuffers();
			} catch (SWTException swte) { // exception occurs if wdiget is
				// disposed
				logger.debug(swte);
			} finally {
				lock.unlock();
			}
		}

		/**
		 * resize the camera if needed
		 */
		private void checkandresize() {
			Point canvasSize = glCanvas.getSize();
			Renderer r = displaySys.getRenderer();
			int renderWidth = r.getWidth();
			int renderHeight = r.getHeight();
			if ( (canvasSize.x != renderWidth || canvasSize.y != renderHeight) &&
					canvasSize.x > 0 && canvasSize.y > 0 ) {
				logger.info("Resizing the renderer to: "+canvasSize.x+"x"+canvasSize.y);
				r.reinit(canvasSize.x, canvasSize.y);
			}
		}
	}

	/**
	 * @return the debug3d
	 */
	public boolean isDebug3d() {
		return debug3d;
	}

	/**
	 * @param debug3d the debug3d to set
	 */
	public void setDebug3d(boolean debug3d) {
		this.debug3d = debug3d;
	}
}