package org.rifidi.utilities.jmecontext.threads;

import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.opengl.GLCanvas;

import com.jme.scene.Node;
import com.jme.util.GameTaskQueue;

public class UpdateThread extends Thread {
	private GameTaskQueue queue;
	private ReentrantLock lock;
	private GLCanvas canvas;
	private boolean alive;

	public UpdateThread( ReentrantLock lock, GLCanvas canvas, GameTaskQueue queue ) {
		this.canvas = canvas;
		this.queue = queue;
		this.lock = lock;
		alive = true;

		// Name the thread
		setName("UpdateThread");
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		alive = true;
		while ( alive ) {
			lock.lock();

			queue.execute();

			lock.unlock();

			try {	// Give other threads some time
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void terminate() {
		alive = false;
	}
}