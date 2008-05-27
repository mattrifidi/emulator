/*
 *  Helpers.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jmeswt.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.jme.util.GameTaskQueueManager;

/**
 * Collection of helpers.
 * 
 * @author Jochen Mader Jan 20, 2008
 * 
 */
public class Helpers {

	/**
	 * This method blocks until the future has returned.
	 * 
	 * @param callable
	 *            the callable to be executed
	 */
	public static void waitOnCallabel(Callable<Object> callable) {
		Thread thread = new Thread(new TestRunnable(GameTaskQueueManager
				.getManager().update(callable)));
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This runnable is used to wait for a future to return.
	 * 
	 * 
	 * @author Jochen Mader Feb 1, 2008
	 * @tags
	 * 
	 */
	private static class TestRunnable implements Runnable {
		/**
		 * The future to wait on.
		 */
		private Future<Object> future;

		/**
		 * Constructor.
		 * 
		 * @param future
		 *            the future to wait for
		 */
		public TestRunnable(Future<Object> future) {
			this.future = future;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

	}
}
