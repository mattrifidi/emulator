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
public class RenderingThread extends Thread {
	private static Log logger = LogFactory.getLog(RenderingThread.class);

	private boolean debug3d = false;

	/**
	 * Flag for thread termination
	 */
	private boolean keepRunning = true;

	/**
	 * Flag for indicating rendering is paused
	 */
	private boolean paused = false;

	/**
	 * Runnable thread for executing the main body of the rendering
	 */
	private RenderRunnable renderRunnable;

	private Display display;

	private GameTaskQueue queue;
	private ReentrantLock lock;
	private GLCanvas glCanvas;

	/**
	 * Constructor for the renderthread object
	 * 
	 * @param rootNode
	 *            The rootnode for the scene
	 * @param physicsSpace
	 *            The physicsspace for the scene
	 */
	public RenderingThread(ReentrantLock lock, GameTaskQueue queue, Node rootNode, GLCanvas glCanvas, Display display) {
		this.glCanvas = glCanvas;
		this.display = display;
		this.queue = queue;
		this.lock = lock;

		renderRunnable = new RenderRunnable(rootNode);
		logger.debug("New RenderThread instantiated.");
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
				if ( !paused && !glCanvas.isDisposed() )
					display.asyncExec(renderRunnable);
				try {
					sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
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
				queue.execute();

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

	/**
	 * Sets the paused state of the rendering
	 * @param paused the new paused state
	 */
	public void setPaused( boolean paused ) {
		this.paused = paused;
	}
}