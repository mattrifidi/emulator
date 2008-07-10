/*
 *  ROSpecExecuter.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.rospec;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.aispec._AISpec;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.llrp.properties.EventNotificationTable;
import org.rifidi.emulator.reader.llrp.report.LLRPReportController;
import org.rifidi.emulator.reader.llrp.report.LLRPReportControllerFactory;
import org.rifidi.emulator.reader.llrp.report.ROReportFormat;
import org.rifidi.emulator.reader.llrp.trigger.ImmediateTrigger;
import org.rifidi.emulator.reader.llrp.trigger.TimerTrigger;
import org.rifidi.emulator.reader.llrp.trigger.TriggerObservable;

import edu.uark.csce.llrp.ROSpecEvent;
import edu.uark.csce.llrp.ReaderEventNotificationData;

/**
 * This is the executer for the ROSpec. It is the thread that will execute the
 * ROSpec.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class ROSpecExecuter implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(ROSpecExecuter.class);

	/**
	 * The AISpecs that will be executed in this class.
	 */
	private ArrayList<_AISpec> specsToExecute;

	/**
	 * The rospec to execute
	 */
	private _ROSpec roSpec;

	private LLRPReaderSharedResources llrpsr;

	/**
	 * The control signal that the current aispec uses for stop trigger
	 */
	private TriggerObservable aiSpecState;

	/**
	 * When this is set to false, execute no more AISpecs or RFSpecs
	 */
	private boolean keepExecuting;

	/**
	 * This is a class that will execute and AISpecs that the ROSpec must
	 * execute.
	 * 
	 * @param specsToExecute
	 * @param tagInventory
	 */
	public ROSpecExecuter(ArrayList<_AISpec> specsToExecute, _ROSpec instance,
			LLRPReaderSharedResources llrpsr) {
		this.llrpsr = llrpsr;
		this.specsToExecute = specsToExecute;
		this.roSpec = instance;
	}

	/**
	 * Runs a ROSpec, then generates a report after the ROSpec has finished.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.debug("Starting the autonomous executer");

		keepExecuting = true;

		/*
		 * This sleep causes a context switch so that another process can run
		 * first. The problem is that if a client sends a START_ROSPEC message,
		 * sometimes the START_ROSPEC_RESPONSE is recieved after the reader
		 * event notification signaling the beginning of a rospec. This short
		 * sleep code, while it doesn't gauentee a fix for the problem, helps
		 * reduce it
		 */
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		/* send ROSpec Start Event if enabled */
		EventNotificationTable table = llrpsr.getProperties().eventNotificaionTable;
		if (table
				.getEventNotificaiton(EventNotificationTable.RO_SPEC_EVENT_TYPE)) {
			ReaderEventNotificationData rend = new ReaderEventNotificationData();
			ROSpecEvent event = new ROSpecEvent();
			event.setEventType((byte) 0);
			event.setROSpecID(roSpec.getId());
			rend.setROSpecEventParam(event);

			LLRPReportController controller = LLRPReportControllerFactory
					.getInstance().getReportController(llrpsr.getReaderName());
			controller.sendEvent(rend);
		}

		if (roSpec.getStopTrigger() instanceof TimerTrigger) {
			TimerTrigger trig = (TimerTrigger) roSpec.getStopTrigger();
			trig.startTimer();
		}

		this.execute();
		ROReportFormat format = roSpec.getRoReportFormat();
		if (format == null) {
			format = llrpsr.getProperties().roReportFormat_Global;
		}
		int trig = format.reportTrigger;
		if (trig == 2 && llrpsr.getTagReportDataEntries().getNumDataEntries() > 0) {
			LLRPReportControllerFactory.getInstance().getReportController(
					this.roSpec.getReaderName()).sendAllReports(llrpsr, 0);
		}
		/* Send ROSpec End Event if enabled */
		if (table
				.getEventNotificaiton(EventNotificationTable.RO_SPEC_EVENT_TYPE)) {
			ReaderEventNotificationData rend = new ReaderEventNotificationData();
			ROSpecEvent event = new ROSpecEvent();
			event.setEventType((byte) 1);
			event.setROSpecID(roSpec.getId());
			rend.setROSpecEventParam(event);

			LLRPReportController controller = LLRPReportControllerFactory
					.getInstance().getReportController(llrpsr.getReaderName());
			controller.sendEvent(rend);
		}
		
		logger.debug("finished executing ROSpec");
		
		//TODO: if immediate trigger, should we restart the rospec?
		if(roSpec.getStartTrigger() instanceof ImmediateTrigger){
			//((ImmediateTrigger)roSpec.getStartTrigger()).restartRoSpec();
		}

	}

	/**
	 * Execute each AISpec in order. First grab the spec state so that an AISpec
	 * trigger can be fired if this ROSPec gets a stopTrigger before the AISpec
	 * does.
	 * 
	 */
	private void execute() {

		for (_AISpec ai : this.specsToExecute) {
			this.aiSpecState = ai.getSpecState();
			if (keepExecuting) {
				ai.execute();
			}
		}
		logger.debug("finished executing all AISpecs");

	}

	/**
	 * Stop execution of AISpecs and tell the currently executing AISpec to
	 * stop.
	 * 
	 */
	public void stop() {
		keepExecuting = false;
		aiSpecState.fireStopTrigger(this.getClass());
	}

	public void setSpecsToExecute(ArrayList<_AISpec> specsToExecute) {
		this.specsToExecute = specsToExecute;
		logger.debug("setting the specsToExecute, size is now: "
				+ this.specsToExecute.size());

	}

}
