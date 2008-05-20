package org.rifidi.utilities;

import java.util.concurrent.Callable;

import com.jme.util.GameTaskQueue;

/**
 * Allows the callable 'action' to be called during every step of the render
 * queue identified by q
 * 
 * @author Dan West - 'Phoenix' - dan@pramari.com
 */
public class ReoccurringCallable {
	private Callable<Object> action;
	private GameTaskQueue queue;

	/**
	 * Initialize the persistable callable object
	 * 
	 * @param q
	 *            identifier for the queue to add this action to
	 * @param action
	 *            the action to call every update/render
	 */
	public ReoccurringCallable( GameTaskQueue q, Callable<Object> action ) {
		this.action = action;
		this.queue = q;

		q.enqueue(get());
	}

	/**
	 * @return a callable that executes the desired action and queues it up for
	 *         next time around
	 */
	public Callable<Object> get() {
		return new Callable<Object>() {
			public Object call() throws Exception {
				action.call();
				queue.enqueue(get());
				return null;
			}
		};
	}
}
