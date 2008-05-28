/*
 *  DurationTrigger.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.trigger;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.utilities.Timer;

/**
 * This class wraps the timer class to provide a trigger. It works by listening
 * to a timer. When the timer notifies this object it fires a trigger.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author kyle - kyle@pramari.com
 */
public class DurationTrigger implements Observer, TimerTrigger {

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(DurationTrigger.class);

	TriggerObservable specSignal;

	int timeToWait;

	Timer timer;

	boolean fireTrigger;

	boolean suspended = false;

	public DurationTrigger(int timeToWait) {
		this.timeToWait = timeToWait;

	}

	/**
	 * This mehtod sets the control signal to change when a trigger fires;
	 * 
	 * @param specSignal
	 */
	public void setTriggerObservable(TriggerObservable specState) {
		this.specSignal = specState;
	}

	/**
	 * Start the timer
	 */
	public void startTimer() {

		fireTrigger = true;
		timer = new Timer(timeToWait);
		timer.addObserver(this);
		new Thread(timer).start();
		if (suspended) {
			// handle the case that the suspend happens before the timer was
			// started
			timer.suspend();
		}

	}

	/**
	 * Don't fire the trigger when the timer stops
	 */
	public void stopTimer() {
		fireTrigger = false;
		this.timer.deleteObserver(this);
		this.timer = null;
	}

	/**
	 * When the timer that this class is listening for notifies, fire a stop
	 * trigger
	 */
	public void update(Observable arg0, Object arg1) {
		if (fireTrigger) {
			this.specSignal.fireStopTrigger(this.getClass());
		}
		fireTrigger = false;
	}

	public void cleanUp() {
		if (timer != null) {
			timer.deleteObserver(this);
			timer.stop();
			timer = null;
		}

	}

	public void resume() {
		suspended = false;
		if (this.timer != null) {
			this.timer.resume();
		}

	}

	public void suspend() {
		suspended = true;
		if (this.timer != null) {
			this.timer.suspend();
		}

	}

}
