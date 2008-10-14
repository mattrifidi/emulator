/*
 *  @(#)DataBuffer.java
 *
 *  Created:	Oct 3, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.common;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A class which allows for the control of data flow using a buffer. <br>
 * 
 * This buffer uses a Queue as the implementation with a fixed size. If this
 * size is met, any attempts to add data to the buffer will block. Likewise, if
 * the buffer is empty, any attempts to take from the buffer will block. <br>
 * 
 * This behavior is changed by two variables: the interrupt and suspension
 * variables. If the suspension flag is set, any reads/writes to the buffer will
 * block. An interrupt will cause all future and currently blocking read/write
 * attempts to immediately return, returning null or not adding the data,
 * respectively.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * @param <T>
 *            The type of data which this buffer holds.
 * 
 */
public class DataBuffer<T> {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(DataBuffer.class);

	/**
	 * The interrupt flag for the buffer. If this is true, then buffer is said
	 * to be operating in "interrupted mode" and will immediately return any add
	 * or take method calls.
	 */
	private boolean interrupted;

	/**
	 * The suspend flag for the buffer. If this is true, then buffer is said to
	 * be operating in "suspended mode" and will suspend any add or take method
	 * calls as long as not operating in interrupted mode.
	 */
	private boolean suspended;

	/**
	 * Sometimes, we want to just suspend the add to buffer calls.
	 */
	private boolean suspendAdds;

	/**
	 * The queue that this buffer uses to get the job done.
	 */
	private Queue<T> theQueue;

	/**
	 * A basic constructor for a data buffer. It takes in a maximum size
	 * variable and a first-in, first-out selection variable.
	 * 
	 * @param maxBufferSize
	 *            The maximum number of elements the buffer can hold before
	 *            being full.
	 */
	public DataBuffer(int maxBufferSize) {
		/* Make a new FIFO queue */
		this.theQueue = new ArrayBlockingQueue<T>(maxBufferSize, true);

		/* Place the buffer in normal operating mode. */
		this.suspended = false;
		this.suspendAdds = false;
		this.interrupted = false;

	}

	/**
	 * Adds the passed data to the buffer. This will block until there is room
	 * in the buffer for the data to be placed. <br>
	 * 
	 * This method is affected by the current state. If suspended, the method
	 * will block until either the suspention is lifted or an interrupt is
	 * triggered. That is to say, an interrupt takes the highest priority in
	 * this message.
	 * 
	 * @param data
	 *            The data to add to the buffer.
	 * 
	 * @throws DataBufferInterruptedException
	 *             If the add is interrupted.
	 */
	public void addToBuffer(T data) throws DataBufferInterruptedException {
		boolean offerSuccessful = false;

		/* Perform the add synchronzed on the queue. */
		synchronized (this.theQueue) {

			/* Force at least one condition check */
			while ((!offerSuccessful || this.suspended || this.suspendAdds)
					&& !this.interrupted) {

				/* Check if we're in loop due to a suspend */
				if (this.suspended || this.suspendAdds) {
					/* Suspended - wait until notified. */
					try {
						this.theQueue.wait();
					} catch (InterruptedException e) {
						/* Do nothing */
					}

				} else {
					/* Not suspended -- try to offer */
					offerSuccessful = this.theQueue.offer(data);

					/* Check to see if we succeeded */
					if (!offerSuccessful) {
						/* Force a wait for successful offer or interruption. */
						try {
							this.theQueue.wait();
						} catch (InterruptedException e) {
							/* Do nothing */
						}

					}

				}

			}

			/* Notify anything waiting on the send buffer */
			this.theQueue.notifyAll();

		}

		/* Perform last check -- if interrupted, throw an exception. */
		if (!offerSuccessful) {
			throw new DataBufferInterruptedException(
					"Buffer write interrupted.");

		}

	}

	/**
	 * Clears the buffer of all elements.
	 */
	public void clearBuffer() {
		/* Clear the buffers */
		synchronized (this.theQueue) {
			this.theQueue.clear();
			this.theQueue.notifyAll();

		}

	}

	/**
	 * Sets interrupted to the passed parameter, interrupted.
	 * 
	 * @param interrupted
	 *            The value to set the interrupted flag to.
	 */
	public void setInterrupted(boolean interrupted) {
		/* Set the interrupted flag to the passed value */
		synchronized (this.theQueue) {
			this.interrupted = interrupted;
			this.theQueue.notifyAll();

		}

	}

	/**
	 * Sets suspended to the passed parameter, suspended.
	 * 
	 * @param suspended
	 *            The value to set the suspended flag to.
	 */
	public void setSuspended(boolean suspended) {
		/* Set the suspended flag to the passed value */
		synchronized (this.theQueue) {
			this.suspended = suspended;
			this.theQueue.notifyAll();
			logger.debug("Databuffer suspended: " + suspended);
		}

	}

	/**
	 * This method sets the buffer into a mode where it is can only be read
	 * from, but not written to. Sometimes, we need the buffer to suspend only
	 * add operations, but not take operations.
	 * 
	 * @param suspendAdds
	 */
	public void setSuspendAdds(boolean suspendAdds) {
		synchronized (this.theQueue) {
			this.suspendAdds = suspendAdds;
			this.theQueue.notifyAll();
			logger.debug("DATABUFFER: is suspending adds");
		}
	}

	/**
	 * Grabs the first element from the receive buffer. This method will block
	 * until data is received, or interrupted. <br>
	 * 
	 * This method is affected by the current state. If suspended, the method
	 * will block until either the suspention is lifted or an interrupt is
	 * triggered. That is to say, an interrupt takes the highest priority in
	 * this message.
	 * 
	 * @return The next piece of data from the buffer.
	 * @throws DataBufferInterruptedException
	 *             If the take is interrupted.
	 */
	public T takeNextFromBuffer() throws DataBufferInterruptedException {
		/* The data to return */
		T retData = null;

		/* Perform the take synchronzed on the queue. */
		synchronized (this.theQueue) {
			/* Force at least one condition check */
			while (((retData == null) || this.suspended) && !this.interrupted) {

				/* Check if we're in loop due to a suspend */
				if (this.suspended) {
					/* Suspended - wait until notified. */
					try {
						this.theQueue.wait();
					} catch (InterruptedException e) {
						/* Do nothing */
					}

				} else {
					/* Not suspended -- try a poll. */
					retData = this.theQueue.poll();

					/* Check to see if we got anything. */
					if (retData == null) {
						/* Force a wait for data or interruption. */
						try {
							this.theQueue.wait();
						} catch (InterruptedException e) {
							/* Do nothing */
						}

					}

				}

			}

			/* Notify anything waiting on the buffer */
			this.theQueue.notifyAll();

		}

		/* Perform last check -- if interrupted, throw an exception. */
		if (retData == null) {
			throw new DataBufferInterruptedException("Buffer read interrupted.");

		}

		/* Return the data */
		return retData;

	}

	/**
	 * Returns the current interruption state. <br>
	 * 
	 * This method is provided for testing purposes, and should not be used for
	 * things other than unit testing.
	 * 
	 * @return True if interrupted, false otherwise.
	 */
	public final boolean isInterrupted() {
		boolean retVal = false;

		/* Grab the current value of interrupted */
		synchronized (this.theQueue) {
			retVal = this.interrupted;
		}

		return retVal;

	}

	/**
	 * Returns the current suspension state. <br>
	 * 
	 * This method is provided for testing purposes, and should not be used for
	 * things other than unit testing.
	 * 
	 * @return True if suspended, false otherwise.
	 */
	public final boolean isSuspended() {
		boolean retVal = false;

		/* Grab the current value of suspended */
		synchronized (this.theQueue) {
			retVal = this.suspended;
		}

		return retVal;

	}

	/**
	 * Somtimes we want the buffer to suspend only adding operations, but not
	 * taking opertions.
	 * 
	 * @return True if the buffer is suspending adds, false otherwise
	 */
	public final boolean isSusupendingAdds() {
		boolean retVal = false;

		synchronized (this.theQueue) {
			retVal = this.suspendAdds;
		}
		return retVal;
	}

	/**
	 * Returns the current number of elements in the buffer. <br>
	 * 
	 * This method is provided for testing purposes, and should not be used for
	 * things other than unit testing.
	 * 
	 * @return The current number of elements in the buffer.
	 */
	public final int getCurrentBufferSize() {
		/* The value to return */
		int retVal = 0;

		/* Grab the current size of the queue */
		synchronized (this.theQueue) {
			retVal = this.theQueue.size();
		}

		return retVal;

	}

}
