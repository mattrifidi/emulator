/**
 * 
 */
package org.rifidi.emulator.reader.llrp.trigger.ROSpecStop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecController;
import org.rifidi.emulator.reader.llrp.rospec.execeptions.ROSpecControllerException;
import org.rifidi.emulator.reader.llrp.trigger.DurationTrigger;

/**
 * Time-based Duration stop trigger for ROSpecs.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public final class ROSpecStopDurationTrigger extends DurationTrigger {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(ROSpecStopDurationTrigger.class);
	/**
	 * The ROSpecController used to stop the ROSpec
	 */
	private ROSpecController controller;

	/**
	 * The ROSpecID this stop trigger belongs to
	 */
	private int ROSpecID;

	/**
	 * Time based Duration Stop Trigger for ROSpecs
	 * 
	 * @param timeToWait
	 *            The time before the trigger expires
	 * @param ROSpecID
	 *            The ROSpec ID of the ROSpec this trigger is associated with
	 * @param roSpecController
	 *            The ROSpecController to use to stop the ROSpec
	 */
	public ROSpecStopDurationTrigger(int timeToWait, int ROSpecID,
			ROSpecController roSpecController) {
		super(timeToWait);
		this.controller = roSpecController;
		this.ROSpecID = ROSpecID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.llrp.trigger.Trigger#fireTrigger()
	 */
	@Override
	public void fireTrigger() {

		try {
			controller.stopROSpec(ROSpecID);
		} catch (ROSpecControllerException e) {
			logger.debug(e.getMessage(), e);
		}

	}

}
