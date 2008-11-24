/*
 *  ImmediateTrigger.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This represents a start trigger that will fire immediately.
 * 
 * It may need to restart a rospec when it is done
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class ImmediateTrigger implements Trigger {

	private boolean suspended = false;

	private boolean shouldRestartAfterResume = false;

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(ImmediateTrigger.class);

	TriggerObservable rospecState;

	public void setTriggerObservable(TriggerObservable specState) {
		this.rospecState = specState;

	}

	public void restartRoSpec() {
		if (suspended) {
			shouldRestartAfterResume = true;
		} else {
			logger.debug("RestartRoSpec due to Immediate Trigger");
			rospecState.fireStartTrigger(this.getClass());
		}
	}

	public void cleanUp() {

	}

	public void resume() {
		this.suspended = false;
		if (this.shouldRestartAfterResume) {
			restartRoSpec();
			shouldRestartAfterResume = false;
		}

	}

	public void suspend() {
		this.suspended = true;

	}

}
