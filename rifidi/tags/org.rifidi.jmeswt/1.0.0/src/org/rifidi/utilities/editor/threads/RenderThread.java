package org.rifidi.utilities.editor.threads;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.rifidi.jmonkey.SWTDisplaySystem;

import com.jme.renderer.Renderer;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.Timer;

public class RenderThread extends Thread {
	private static Log logger = LogFactory.getLog(RenderThread.class);
	private DisplaySystem displaySys;
	private GameTaskQueue queue;
	private ReentrantLock lock;
	private GLCanvas canvas;
	private Display display;
	private boolean active;
	private boolean alive;

	private int targetTPF = 30;

	public RenderThread( ReentrantLock lock, GLCanvas glcanvas, GameTaskQueue queue, Display display  ) {
		displaySys = DisplaySystem.getDisplaySystem();
		this.display = display;
		this.canvas = glcanvas;
		this.queue = queue;
		this.lock = lock;
		targetTPF = 30;
		active = false;
		alive = true;

		// Name the thread
		setName("RenderThread");
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while ( alive ) {
			// Store the current time
			long curTime = Timer.getTimer().getTime();

			// Sync-Exec the rendering
			if ( active ) {
				display.syncExec( new Runnable() {
					/* (non-Javadoc)
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						if ( !canvas.isDisposed() ) {
							lock.lock();

							try {
//								checkandresize();
								displaySys.getRenderer().clearBuffers(); // clear screen

//								Node rootNode = new Node("root");
//								rootNode.updateRenderState();
//								displaySys.getRenderer().draw(rootNode); // draw

								// Execute queued render callables
								queue.execute();

								// finalize the render
								displaySys.getRenderer().displayBackBuffer();
								canvas.swapBuffers();
							} catch ( SWTException swte ) {
								swte.printStackTrace();
							} finally {
								lock.unlock();
							}
						}
					}
				});
			}

			// Figure out how long to sleep for
			long sleepTime = 50;
			if ( active ) {
				sleepTime = targetTPF - (Timer.getTimer().getTime() - curTime);
				if ( sleepTime < 0 )
					sleepTime = 10;
			}

			try {	// Sleep
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * resize the camera if needed
	 */
	private void checkandresize() {
		Point canvasSize = canvas.getSize();
		Renderer r = displaySys.getRenderer();
		int renderWidth = r.getWidth();
		int renderHeight = r.getHeight();
		if ( (canvasSize.x != renderWidth || canvasSize.y != renderHeight) &&
				canvasSize.x > 0 && canvasSize.y > 0 ) {
			logger.info("Resizing the renderer to: "+canvasSize.x+"x"+canvasSize.y);
			r.reinit(canvasSize.x, canvasSize.y);
		}
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public void terminate() {
		alive = false;
	}
}