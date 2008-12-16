/*
 *  AISpec.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.aispec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.accessspec._AccessSpec;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.llrp.properties.EventNotificationTable;
import org.rifidi.emulator.reader.llrp.report.LLRPReportController;
import org.rifidi.emulator.reader.llrp.report.LLRPReportControllerFactory;
import org.rifidi.emulator.reader.llrp.report.ROReportFormat;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecController;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecControllerFactory;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecExecuter;
import org.rifidi.emulator.reader.llrp.tagbuffer.LLRPTagMemory;
import org.rifidi.emulator.reader.llrp.trigger.DurationTrigger;
import org.rifidi.emulator.reader.llrp.trigger.GPIWithTimeoutTrigger;
import org.rifidi.emulator.reader.llrp.trigger.TagObservationTrigger;
import org.rifidi.emulator.reader.llrp.trigger.TimerTrigger;
import org.rifidi.emulator.reader.llrp.trigger.Trigger;
import org.rifidi.emulator.reader.llrp.trigger.TriggerObservable;
import org.rifidi.tags.impl.RifidiTag;

import edu.uark.csce.llrp.AISpecEvent;
import edu.uark.csce.llrp.C1G2SingulationDetails;
import edu.uark.csce.llrp.ReaderEventNotificationData;
import edu.uark.csce.llrp.TagReportData;

/**
 * Antenna Inventory spec, looks for tags on the specified antennas. It observes
 * the spec signal and stops when it is set to false.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle Neumeier
 */
public class _AISpec implements Observer {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(_AISpec.class);

	/**
	 * The unique identification number of the AISpec
	 */
	private int specIndex;

	/**
	 * The trigger that will stop this AISpec when it is executing.
	 */
	private Trigger stopTrigger;

	/**
	 * The IDs of the antennas the AISpec will be looking at.
	 */
	private int[] antennaIDs;

	/**
	 * The set of antennas
	 */
	private Set<Integer> antennas;

	/**
	 * List of Inventory Parameters.
	 */
	private Collection<InventoryParameterSpec> inventoryParameterList;

	/**
	 * List of custom parameters.
	 */
	private Collection<CustomParameter> customParameterList;

	/**
	 * 
	 */
	private LLRPReaderSharedResources llrpsr;

	/**
	 * The control signal that this AISpec uses for stop trigger
	 */
	private TriggerObservable specSignal;

	/**
	 * The rospec ID that this AISpec is associated with
	 */
	private int roSpecID;

	/**
	 * boolean that tells the main loop when to quit
	 */
	private boolean readMoreTags;

	/**
	 * The format that the the report should have
	 */
	private ROReportFormat format;

	private boolean suspended = false;

	private long lastSuspendTime = 0;

	private long totalSuspendTime = 0;

	private long lastAISPecExecution = 0;

	/**
	 * Constructor for AISpec.
	 * 
	 * @param name
	 * @param stopTrig
	 * @param antennaIDs
	 */
	public _AISpec(int specIndex, Trigger stopTrig, int[] antennaIDs,
			int roSpecID, LLRPReaderSharedResources llrpsr,
			ROReportFormat format) {
		this.specIndex = specIndex;
		this.stopTrigger = stopTrig;
		this.antennaIDs = antennaIDs;
		this.llrpsr = llrpsr;
		this.roSpecID = roSpecID;
		this.specIndex = specIndex;
		this.specSignal = new TriggerObservable(false);
		this.format = format;
		specSignal.addObserver(this);
		readMoreTags = true;

		this.antennas = new HashSet<Integer>();
		for (int i : antennaIDs) {
			this.antennas.add(i);
		}
	}

	/**
	 * Looks for tags in its given antennas. This method attempts to follow the
	 * state diagram of AISpec operation (figure 5 in the LLRP Spec).
	 * 
	 * @param tagMap
	 */
	public void execute() {

		readMoreTags = true;

		// Make sure that the spec signal is set to true
		this.specSignal.fireStartTrigger(this.getClass());

		// Set up the tag buffer
		LLRPTagMemory tagMem = (LLRPTagMemory) llrpsr.getTagMemory();

		// set up the stop trigger for this AISpec
		stopTrigger.setTriggerObservable(specSignal);

		if (this.stopTrigger instanceof TagObservationTrigger) {
			((TagObservationTrigger) this.stopTrigger).resetTagsSeen();
			((TagObservationTrigger) this.stopTrigger).resetLastSeenTime();
		}

		if (this.stopTrigger instanceof TimerTrigger) {
			((TimerTrigger) stopTrigger).startTimer();
		}

		long startAISpecTime = System.currentTimeMillis();
		totalSuspendTime = 0;
		lastSuspendTime = 0;

		if (lastAISPecExecution == 0) {
			logger.debug("Starting AISpec for first time");
		} else {
			logger.debug("Starting AISpec.  Real time since last "
					+ "AISpec execution: "
					+ (System.currentTimeMillis() - lastAISPecExecution));
		}
		lastAISPecExecution = System.currentTimeMillis();

		while (readMoreTags) {

			while (suspended) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// scan the radio for tags and add to tag buffer
			llrpsr.getRadio().scan(this.antennas, tagMem);

			// Add all new tags to TagReportDataEntries and perform access
			// operation on tag if we need to. This is equivalent to
			// singulation.
			for (RifidiTag t : tagMem.getTagReport()) {

				t.incrementReadCount();
				
				TagReportData trd = LLRPReportController.formatTagReport(
						getReportFormat(), t, roSpecID, specIndex, llrpsr);

				// TODO: this method is incorrect because it only adds the tag
				// if the EPCID has not already been seen. That is there are no
				// duplicates.
				llrpsr.getTagReportDataEntries().add(trd);

				_AccessSpec accessSpec = llrpsr.accessSpecs
						.getFirstAccessSpecthatMatches(t);

				if (accessSpec != null) {
					if (accessSpec.getRoSpecID() == 0
							|| accessSpec.getRoSpecID() == this.roSpecID) {

						accessSpec.performOperations(t, trd);

					}
				}

				// if we need to send out a report now, do it
				if (this.getReportFormat().reportTrigger == 1
						|| this.getReportFormat().reportTrigger == 2) {
					if (this.getReportFormat().N > 0) {
						if (llrpsr.getTagReportDataEntries()
								.getNumDataEntries() == getReportFormat().N) {

							LLRPReportControllerFactory
									.getInstance()
									.getReportController(
											this.llrpsr.getReaderName())
									.sendAllReports(llrpsr, getReportFormat().N);

							logger.debug("Sent a report because"
									+ " of N tag trigger");

						}
					}
				}
			}
			

			// if we are using Tag Observation Trigger, update it with new tags
			if (this.stopTrigger instanceof TagObservationTrigger) {
				TagObservationTrigger trig = (TagObservationTrigger) stopTrigger;
				trig.updateTagTrigger(tagMem.getTagReport().size());
			}
			
			tagMem.clear();

			// wait so that this while loop is not so expensive
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		long totalTime = System.currentTimeMillis() - startAISpecTime;
		logger.debug("Stopping AISpec.  Total time was " + totalTime
				+ ". ExecutionTime was " + (totalTime - this.totalSuspendTime));
		if (this.stopTrigger instanceof TimerTrigger) {
			((TimerTrigger) stopTrigger).stopTimer();
		}

		// Send report if we need to do that now
		int trig = getReportFormat().reportTrigger;
		if (trig == 1
				&& llrpsr.getTagReportDataEntries().getNumDataEntries() > 0) {
			LLRPReportControllerFactory.getInstance().getReportController(
					this.llrpsr.getReaderName()).sendAllReports(llrpsr, 0);
		}

		// Send the AISpecEventParameter
		EventNotificationTable table = llrpsr.getProperties().eventNotificaionTable;
		if (table
				.getEventNotificaiton(EventNotificationTable.AI_SPEC_EVENT_TYPE)) {
			ReaderEventNotificationData rend = new ReaderEventNotificationData();
			AISpecEvent event = new AISpecEvent();
			event.setEventType((byte) 0);
			event.setROSpecID(roSpecID);
			event.setSpecIndex((short) specIndex);
			if (table
					.getEventNotificaiton(EventNotificationTable.AI_SPEC_EVENT_WITH_SINGULATION_DETAILS_TYPE)) {
				// TODO: check to see if this reader supports this message & and
				// add proper values
				C1G2SingulationDetails apsd = new C1G2SingulationDetails();
				apsd.setNumCollisionSlots((short) 0);
				apsd.setNumEmptySlots((short) 0);
			}
			rend.setAISpecEventParam(event);
			LLRPReportController controller = LLRPReportControllerFactory
					.getInstance().getReportController(llrpsr.getReaderName());
			controller.sendEvent(rend);
		}

	}

	/**
	 * @return the antennaIDs
	 */
	public int[] getAntennaIDs() {
		return antennaIDs;
	}

	/**
	 * @param antennaIDs
	 *            the antennaIDs to set
	 */
	public void setAntennaIDs(int[] antennaIDs) {
		this.antennaIDs = antennaIDs;
	}

	/**
	 * @return the inventoryParameterList
	 */
	public Collection<InventoryParameterSpec> getInventoryParameterList() {
		return inventoryParameterList;
	}

	/**
	 * @param inventoryParameterList
	 *            the inventoryParameterList to set
	 */
	public void setInventoryParameterList(
			Collection<InventoryParameterSpec> inventoryParameterList) {
		this.inventoryParameterList = inventoryParameterList;
	}

	/**
	 * @return the num
	 */
	public int getSpecIndex() {
		return specIndex;
	}

	/**
	 * @return the stopTrigger
	 */
	public Trigger getStopTrigger() {
		return stopTrigger;
	}

	/**
	 * @param stopTrigger
	 *            the stopTrigger to set
	 */
	public void setStopTrigger(Trigger stopTrigger) {
		this.stopTrigger = stopTrigger;
	}

	/**
	 * @return the customParameterList
	 */
	public Collection<CustomParameter> getCustomParameterList() {
		return customParameterList;
	}

	/**
	 * @param customParameterList
	 *            the customParameterList to set
	 */
	public void setCustomParameterList(
			Collection<CustomParameter> customParameterList) {
		this.customParameterList = customParameterList;
	}

	/**
	 * @return the specSignal
	 */
	public TriggerObservable getSpecState() {
		return specSignal;
	}

	/**
	 * This is method contains the logic for what happens when the AISpec's
	 * specState changes. AISpec should only observe a TriggerObservable object
	 */
	@SuppressWarnings("unchecked")
	public void update(Observable arg0, Object arg1) {
		ArrayList<Object> extraInfo;
		boolean newState;
		Class callingClass;

		try {
			extraInfo = (ArrayList<Object>) arg1;
			newState = (Boolean) extraInfo.get(0);
			callingClass = (Class) extraInfo.get(1);

			if (newState == false) {
				if (callingClass.equals(DurationTrigger.class)) {
					logger.debug("AIStop Trigger fired by Duration Trigger");
				} else if (callingClass.equals(TagObservationTrigger.class)) {
					logger
							.debug("AIStop Trigger fired by Tag Observation Trigger");
				} else if (callingClass.equals(GPIWithTimeoutTrigger.class)) {
					logger.debug("AIStop Trigger fired by GPI Trigger");
				} else if (callingClass.equals(ROSpecExecuter.class)) {
					logger.debug("AIStop Trigger fired by end of ROSpec");
				} else {
					logger.debug("Unidentified class stopped AISpec: "
							+ callingClass);
				}

				readMoreTags = false;

				if (!callingClass.equals(ROSpecExecuter.class)) {
					ROSpecController rsc = ROSpecControllerFactory
							.getInstance().getReportController(
									llrpsr.getReaderName());
					rsc.stopROSpec(roSpecID);
				}
			}
		} catch (Exception e) {
			logger.debug("There was an error when trying to update "
					+ "AISpec.  Check to make sure the TriggerObservable's "
					+ "extra informaiton was formed correctly");
		}
	}

	private ROReportFormat getReportFormat() {
		if (this.format == null) {
			return llrpsr.getProperties().roReportFormat_Global;
		} else
			return this.format;
	}

	public void suspend() {
		this.suspended = true;
		if (this.stopTrigger != null) {
			this.stopTrigger.suspend();
		}
		lastSuspendTime = System.currentTimeMillis();
	}

	public void resume() {
		this.suspended = false;
		if (this.stopTrigger != null) {
			this.stopTrigger.resume();
		}
		totalSuspendTime += System.currentTimeMillis() - lastSuspendTime;
	}

}
