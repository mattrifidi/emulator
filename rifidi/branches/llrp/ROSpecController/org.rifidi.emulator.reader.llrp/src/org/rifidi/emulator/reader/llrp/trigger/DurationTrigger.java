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
 * @author kyle - kyle@pramari.com
 */
public abstract class DurationTrigger implements Trigger, Observer {
	
	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(DurationTrigger.class);

	private int timeToWait;

	private Timer timer;
	
	private boolean disabled = false;
	
	public DurationTrigger(int timeToWait) {
		this.timeToWait = timeToWait;
	}

	/**
	 * Start the timer
	 */
	public void enable() {
		disabled = false;
		Timer timer = new Timer(timeToWait);
		timer.addObserver(this);
		Thread t = new Thread(timer, "Duration Timer");
		t.start();
	}

	/**
	 * Don't fire the trigger when the timer stops
	 */
	public void disable() {
		disabled = true;
	}


	public void cleanUp() {

	}

	public void resume() {

	}

	public void suspend() {
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(!disabled){
			logger.debug("Duration Trigger fired");
			fireTrigger();
		}
		
	}

}
