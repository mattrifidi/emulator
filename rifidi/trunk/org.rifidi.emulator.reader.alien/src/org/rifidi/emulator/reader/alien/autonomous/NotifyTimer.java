/*
 *  NotifyTimer.java
 *
 *  Created:	Jan 11, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *  Author:    Kyle Neumeier - kyle@pramari.com
 */
package org.rifidi.emulator.reader.alien.autonomous;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.rifidi.emulator.reader.alien.commandhandler.AlienTag;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.utilities.Timer;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class NotifyTimer implements Observer {

	private AlienReaderSharedResources asr;
	private Timer timer;
	private NotifyController controller;

	public NotifyTimer(AlienReaderSharedResources asr,
			NotifyController controller) {
		this.asr = asr;
		this.controller = controller;
	}

	public void startNotifyTimer() {
		String s = this.asr.getPropertyMap().get("notifytime")
				.getPropertyStringValue();
		long time = Integer.parseInt(s) * 1000;

		stopNotifyTimer();
		if (time > 0) {

			timer = new Timer(time);
			timer.addObserver(this);
			Thread t = new Thread(timer, "notify timer");
			t.start();
		}
	}

	public void stopNotifyTimer() {
		if (timer != null) {
			timer.stop();
			timer = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1) {
		scanTagListAndSend();
		startNotifyTimer();

	}

	private void scanTagListAndSend() {

		// send the message
		String automode = asr.getPropertyMap().get("automode")
				.getPropertyStringValue();
		if (automode.equalsIgnoreCase("on")) {

			this.controller.sendMessage(AlienTag.getTagList(asr),
					EvaluationTriggerCondition.TrueFalse,
					TagEventTriggerCondition.Change, true);
		} else {
			this.controller.sendMessage(new ArrayList<RifidiTag>(),
					EvaluationTriggerCondition.TrueFalse,
					TagEventTriggerCondition.Change, true);
		}
	}

}
