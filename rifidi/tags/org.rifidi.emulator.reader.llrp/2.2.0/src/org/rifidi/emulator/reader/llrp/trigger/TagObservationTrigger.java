package org.rifidi.emulator.reader.llrp.trigger;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.utilities.Timer;

/**
 * This trigger fires in one of three scenarios: 1)Upon seeing N tag
 * observations 2)Upon seeing no new tags for t ms 3)Timeout The last trigger
 * type: N attempts does not work, and will just wait for a time out.
 * 
 * The way that the timeout works is that this trigger observes a timer. When
 * the timer is done, it notifies this object, which then fires a trigger to the
 * intersted party (e.g. AISpec).
 * 
 * @author kyle
 * 
 */
public class TagObservationTrigger implements TimerTrigger, Observer {

	/**
	 * Possible Values: 0 - Upon seeing N tag observations, or timeout 1 - Upon
	 * seeing no more new tag observations for t ms, or timeout 2 - N attempts
	 * to see all tags in the FOV, or timeout
	 */
	byte triggetType;

	/**
	 * This field SHALL be ignored whenTriggerType != 0.
	 */
	short numOfTags;

	/**
	 * This field SHALL be ignored whenTriggerType != 2.
	 */
	short numOfAttempts;

	/**
	 * Idle time between tag responses in milliseconds
	 */
	short t;

	/**
	 * Trigger timeout value in milliseconds. If set to zero, it indicates that
	 * there is no timeout.
	 */
	int timeout;

	/**
	 * The signal to set to false when done
	 */
	TriggerObservable specState;

	/**
	 * Last time a tag was seen
	 */
	private long lastTagSeenTime;

	/**
	 * Number of unique tags from the previous read attempt
	 */
	private int previousNumUniqueTags;

	/**
	 * Timeout thread
	 */
	private Timer timer_thread;

	private boolean fireTimer;

	private boolean suspended = false;

	/**
	 * The total time this trigger has been suspended.  Do not modify outside of a synchronized block
	 */
	private long suspendedTime = 0;

	/**
	 * The last timer that this trigger was suspended
	 */
	private long lastSuspendTime = 0;
	
	private static Log logger = LogFactory.getLog(TagObservationTrigger.class);

	/**
	 * Public constructor for TagObservationTrigger (see TagObservationTrigger
	 * in the LLRP spec for explanation of fields)
	 * 
	 * @param triggerType
	 * @param numOfTags
	 * @param numOfAttempts
	 * @param t
	 * @param timeout
	 */
	public TagObservationTrigger(byte triggerType, short numOfTags,
			short numOfAttempts, short t, int timeout) {
		this.triggetType = triggerType;
		this.numOfTags = numOfTags;
		this.numOfAttempts = numOfAttempts;
		this.t = t;
		this.timeout = timeout;
		this.lastTagSeenTime = System.currentTimeMillis();

	}

	public void startTimer() {
		timer_thread = new Timer(timeout);
		timer_thread.addObserver(this);

		fireTimer = true;
		if (timeout > 0) {
			new Thread(timer_thread).start();
		}

		if (suspended) {
			timer_thread.suspend();
		}
	}

	public void stopTimer() {
		fireTimer = false;
		timer_thread.deleteObserver(this);
		timer_thread = null;
	}

	/**
	 * This mehtod sets the control signal to change when a trigger fires;
	 * 
	 * @param specSignal
	 */
	public void setTriggerObservable(TriggerObservable state) {
		this.specState = state;
	}

	/**
	 * This method should be called after an AISpec reads from the antennas
	 * 
	 * @param numUniqueTagsSeen
	 *            The total number of unique tags seen so far
	 */
	public void updateTagTrigger(int numUniqueTagsSeen) {
		if (triggetType == 0) {
			if (numUniqueTagsSeen >= this.numOfTags) {
				logger.debug("Firing a stop trigger because we have seen " + numUniqueTagsSeen + " tags");
				specState.fireStopTrigger(this.getClass());

			}
		} else if (triggetType == 1) {
			if (previousNumUniqueTags == numUniqueTagsSeen) {
				long msSinceLastUniqueTag = System.currentTimeMillis()
						- (lastTagSeenTime - suspendedTime);
				if (msSinceLastUniqueTag >= t) {
					logger.debug("Firing a stop trigger because it has been over " + msSinceLastUniqueTag + " ms since last new tag read");
					specState.fireStopTrigger(this.getClass());
				}
			} else {
				lastTagSeenTime = System.currentTimeMillis();
				previousNumUniqueTags = numUniqueTagsSeen;
				synchronized (this) {
					suspendedTime = 0;
				}
			}

		}
	}

	public void resetTagsSeen() {
		previousNumUniqueTags = 0;
	}

	public void resetLastSeenTime() {
		this.lastTagSeenTime = System.currentTimeMillis();
		this.lastSuspendTime = 0;
		synchronized (this) {
			this.suspendedTime = 0;
		}
	}

	/**
	 * @return the numOfAttempts
	 */
	public short getNumOfAttempts() {
		return numOfAttempts;
	}

	/**
	 * @return the numOfTags
	 */
	public short getNumOfTags() {
		return numOfTags;
	}

	/**
	 * @return the t
	 */
	public short getT() {
		return t;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @return the triggetType
	 */
	public byte getTriggetType() {
		return triggetType;
	}

	public void update(Observable arg0, Object arg1) {

		/*
		 * fire trigger due to timeout. Don't fire if state is already false,
		 * because the N Tag trigger might have already changed it
		 */
		if (specState.getState() && fireTimer) {
			specState.fireStopTrigger(this.getClass());
		}

	}

	public void cleanUp() {
		if (this.timer_thread != null) {
			this.timer_thread.deleteObserver(this);
			timer_thread = null;
		}

	}

	public void resume() {
		suspended = false;
		if (timer_thread != null) {
			this.timer_thread.resume();
		}
		synchronized (this) {
			this.suspendedTime += System.currentTimeMillis()
					- this.lastSuspendTime;
		}

	}

	public void suspend() {
		suspended = true;
		if (timer_thread != null) {
			this.timer_thread.suspend();
		}
		this.lastSuspendTime = System.currentTimeMillis();

	}

}
