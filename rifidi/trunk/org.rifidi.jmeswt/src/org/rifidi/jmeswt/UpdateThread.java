/*
 *  UpdateThread.java
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

import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;

/**
 * This thread is responsible for running the gametaskqueue and updating the
 * input. And whatever else you think might be fitting into this one.
 * 
 * @author Jochen Mader - jochen@pramari.com - May 27, 2008
 * 
 */
public class UpdateThread extends Thread {

	/**
	 * High resolution timer for jME.
	 */
	protected Timer timer;
	/**
	 * Set to false to stop the thread.
	 */
	private boolean keepRunning = true;
	/**
	 * Reference to the game this thread is a part of.
	 */
	private SWTBaseGame game;
	/**
	 * Simply an easy way to get at timer.getTimePerFrame(). Also saves time so
	 * you don't call it more than once per frame.
	 */
	protected float tpf;
	/**
	 * The update queue.
	 */
	private GameTaskQueue updateQueue;
	/**
	 * Semaphore to prevent update and render threads from working at the same
	 * time.
	 */
	private Semaphore semaphore;
	/**
	 * Time between two updates.
	 */
	private int resolution;

	/**
	 * Constructor.
	 * 
	 * @param game
	 *            the game this thread is a part of
	 * @param semaphore
	 *            semaphore for preventing collisions with the render thread
	 * @param resolution
	 *            amount of millisecs between two updates
	 */
	public UpdateThread(SWTBaseGame game, Semaphore semaphore, int resolution) {
		super(game.getName() + ".updateThread");
		this.game = game;
		/** Get a high resolution timer for FPS updates. */
		timer = Timer.getTimer();
		updateQueue = GameTaskQueueManager.getManager().getQueue(
				game.getName() + ".update");
		this.semaphore = semaphore;
		this.resolution = resolution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (keepRunning) {
			try {
				semaphore.acquire();
			} catch (InterruptedException e1) {
				Thread.currentThread().interrupt();
			}
			try {
				/** Recalculate the framerate. */
				timer.update();
				/** Update tpf to time per frame according to the Timer. */
				tpf = timer.getTimePerFrame();
				updateQueue.execute();
				game.update(tpf);
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

}
