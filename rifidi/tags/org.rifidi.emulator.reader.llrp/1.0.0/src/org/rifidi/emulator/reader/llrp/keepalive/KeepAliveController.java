/*
 *  KeepAliveController.java
 *
 *  Created:	Oct 3, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *  Author:   kyle
 */
package org.rifidi.emulator.reader.llrp.keepalive;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.report.LLRPReportController;
import org.rifidi.emulator.reader.llrp.report.LLRPReportControllerFactory;
import org.rifidi.emulator.reader.llrp.trigger.PeriodicTrigger;
import org.rifidi.emulator.reader.llrp.trigger.TriggerObservable;

/**
 * This class is a controller for sending out KeepAlive events.
 * 
 * @author kyle
 * 
 */
public class KeepAliveController implements Observer {

	private TriggerObservable triggerObservable;

	private PeriodicTrigger trigger;

	private LLRPReportController reportController;

	private String readerName;

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(KeepAliveController.class);

	public KeepAliveController(String readerName) {
		triggerObservable = new TriggerObservable(false);
		triggerObservable.addObserver(this);
		this.readerName = readerName;

	}

	public void startKeepAlives(int period) {
		trigger = new PeriodicTrigger(period, period);
		trigger.setTriggerObservable(this.triggerObservable);
		trigger.startTimer();
	}

	public void stopKeepAlives() {
		if (trigger != null) {
			trigger.stopTimer();
			trigger = null;
		}
	}

	private void sendKeepAlive() {
		if (reportController == null) {
			reportController = LLRPReportControllerFactory.getInstance()
					.getReportController(readerName);
		}
		reportController.sendKeepAlive();
		triggerObservable.fireStopTrigger(this.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1) {
		ArrayList<Object> extraInfo;
		boolean state;
		Class callingClass;

		try {
			extraInfo = (ArrayList<Object>) arg1;
			state = (Boolean) extraInfo.get(0);
			callingClass = (Class) extraInfo.get(1);

			if (state == true) {
				if (callingClass.equals(PeriodicTrigger.class)) {
					logger.debug("Keepalive Trigger "
							+ "fired by Periodic Trigger");
				}

				sendKeepAlive();
			} else {
				logger.debug("keepalive observable state reset");
			}

		} catch (Exception e) {
			logger.debug("There was an error when trying to update "
					+ "KeepAlive.  Check to make sure the TriggerObservable's "
					+ "extra informaiton was formed correctly");

		}

	}

	public void suspend() {
		logger.debug("Keep alives are suspended");
		if (trigger != null) {
			trigger.suspend();
		}
	}

	public void resume() {
		logger.debug("Keep alives are suspended");
		if (trigger != null) {
			trigger.resume();
		}
	}
}
