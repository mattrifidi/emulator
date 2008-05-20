/*
 *  UpdateThread.java
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

import com.jme.scene.Node;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;

/**
 * @author Jochen Mader
 * @author Dan West
 */
public class UpdatingThread extends Thread{

	private Log logger = LogFactory.getLog(UpdatingThread.class);

	private boolean keepRunning = true;

	private Timer timer;

	private float lasttick;

	private GameTaskQueue queue;
	private ReentrantLock lock;
	private Node rootNode;

	public UpdatingThread(ReentrantLock lock, GameTaskQueue queue, Node rootNode) {
		logger.debug("New UpdateThread instantiated.");
		this.rootNode = rootNode;
		this.queue = queue;
		this.lock = lock;
		timer = Timer.getTimer();
		timer.reset();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		logger.debug("UpdateThread started.");
		lasttick = timer.getTimeInSeconds();
		float currentTime = 0.0f;
		while (keepRunning) {
			lock.lock();
			timer.update();
			currentTime = timer.getTimeInSeconds();
			float dt = currentTime-lasttick;

			queue.execute();

			// TODO i believe this is occasionally causing threading errors
			// it might make sense for this to be in the render
			//  thread, but that doesn't update as often as this does
			rootNode.updateGeometricState(0, true);
			rootNode.updateWorldData(dt);
			rootNode.updateRenderState();

			lock.unlock();

			lasttick = currentTime;
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// we can ignore this buddy
			}
		}
		logger.debug("UpdateThread stopped.");
	}

	/**
	 * @return the keepRunninung
	 */
	public boolean isKeepRunninung() {
		return keepRunning;
	}

	/**
	 * @param keepRunninung
	 *            the keepRunninung to set
	 */
	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

}
