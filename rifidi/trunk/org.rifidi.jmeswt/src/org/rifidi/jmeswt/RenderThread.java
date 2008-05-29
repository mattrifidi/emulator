/*
 *  RenderThread.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jmeswt;

import java.util.concurrent.Semaphore;

import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;

import com.jme.util.GameTaskQueue;

/**
 * Thread for rendering the scene
 * 
 * @author Jochen Mader - jochen@pramari.com - May 27, 2008
 * 
 */
public class RenderThread extends Thread {
	/**
	 * Should we continue running?
	 */
	private boolean keepRunning = true;
	/**
	 * We want to reuse the runnable.
	 */
	private RenderRunnable renderRunnable;
	/**
	 * Semaphore to prevent update and render threads from working at the same
	 * time.
	 */
	private Semaphore semaphore;
	/**
	 * Time between two renderattempts.
	 */
	private int resolution;
	/**
	 * The game this thread is a part of.
	 */
	private SWTBaseGame game;
	/**
	 * The render queue.
	 */
	private GameTaskQueue renderQueue;
	/**
	 * SWT display for the runnables.
	 */
	private Display display;
	/**
	 * Canvas to render to.
	 */
	private GLCanvas canvas;

	/**
	 * Constructor.
	 */
	public RenderThread(SWTBaseGame game, Semaphore semaphore, int resolution) {
		super(game.getName() + ".renderThread");
		this.semaphore = semaphore;
		this.renderRunnable = new RenderRunnable();
		this.resolution = resolution;
		this.game = game;
		this.renderQueue = game.getRenderQueue();
		this.canvas = game.getGlCanvas();
		this.display = canvas.getDisplay();
		game.getDisplaySys().setCurrentGLCanvas(canvas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (!game.getGlCanvas().isDisposed() && keepRunning) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}

			try {
				if (canvas != null) {
					display.syncExec(new RenderRunnable());
				}
			} finally {
				semaphore.release();
			}
			try {
				Thread.sleep(resolution);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * @return the keepRunning
	 */
	public boolean isKeepRunning() {
		return this.keepRunning;
	}

	/**
	 * @param keepRunning
	 *            the keepRunning to set
	 */
	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

	/**
	 * Reusable runnable for rednering.
	 * 
	 * 
	 * @author Jochen Mader - jochen@pramari.com - May 27, 2008
	 * 
	 */
	private class RenderRunnable implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			renderQueue.execute();
			game.getDisplaySys().getRenderer().clearBuffers();
			game.getDisplaySys().getRenderer().clearStatistics();
			game.getDisplaySys().getRenderer().draw(game.getRootNode());
			game.render(-1f);
		}
	}
}
