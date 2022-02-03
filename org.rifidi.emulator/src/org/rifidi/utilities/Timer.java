/*
 *  TimeController.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.utilities;

import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class waits for x amout of time, then notifies observes. It has a
 * suspend() and a resume() methods for pausing and un-pausing the timer.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class Timer extends Observable implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(Timer.class);

	/**
	 * Amount of time left to wait. For example, if this timer is supposed to
	 * wait 10 seconds, and after it waits 10, it realizes that the timer was
	 * paused for 3 seconds, this variable will be set to 3 seconds
	 */
	private long timeLeftToWait;

	/**
	 * Weather or not this timer is suspended or not
	 */
	private boolean suspended = false;

	/**
	 * The amount of time that the timer was paused
	 */
	private long pausedTime = 0;

	/**
	 * If the timer wakes up and realizes it is in a suspend state, this is the
	 * amount of time between when it wakes up and when the timer is resumed
	 */
	private long extraPauseTime = 0;

	/**
	 * This is the time that the timer was suspended last. It is used to figure
	 * out how long the timer was suspended
	 */
	private long lastSuspendTime = 0;

	/**
	 * An optional name for the timer.
	 */
	private String name;

	private boolean stopped;

	/**
	 * Constructs a timeController, which will fire the given trigger
	 * 
	 * @param timeToWait
	 *            The time, in milliseconds.
	 */
	public Timer(long timeToWait) {
		this.timeLeftToWait = timeToWait;
	}

	/**
	 * Constructs a timeController, which will fire the given trigger
	 * 
	 * @param timeToWait
	 *            The time, in milliseconds.
	 */
	public Timer(long timeToWait, String name) {
		this.timeLeftToWait = timeToWait;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		boolean done = false;
		stopped = false;
		while (!done) {
			sleepSome(timeLeftToWait);
			extraPauseTime = 0;
			while (this.suspended) {
				sleepSome(500);
				extraPauseTime += 500;
			}
			timeLeftToWait = timeLeftToWait
					- (timeLeftToWait - (pausedTime - extraPauseTime));
			if (timeLeftToWait <= 0) {
				done = true;
			} else {
				synchronized (this) {
					pausedTime = 0;
				}
			}
		}

		if (!stopped) {
			this.setChanged();
			if (this.name != null) {
				this.notifyObservers(name);
			} else {
				this.notifyObservers();
			}
		}
	}

	private void sleepSome(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void suspend() {
		logger.debug("Timer suspended");
		suspended = true;
		lastSuspendTime = System.currentTimeMillis();
	}

	public void resume() {
		logger.debug("Timer resumed");
		suspended = false;
		synchronized (this) {
			pausedTime += System.currentTimeMillis() - lastSuspendTime;
		}
	}

	public void stop() {
		this.stopped=true;
	}
}
