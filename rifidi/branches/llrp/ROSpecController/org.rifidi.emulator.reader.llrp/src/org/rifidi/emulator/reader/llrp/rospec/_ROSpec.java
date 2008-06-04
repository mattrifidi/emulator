/*
 *  ROSpec.java
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
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.report.ROReportFormat;
import org.rifidi.emulator.reader.llrp.trigger.Trigger;

import edu.uark.csce.llrp.ROSpec;

/**
 * This class represents an LLRP Reader Operation Specification. A ROSpec is a
 * controller that executes several AISpecs.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class _ROSpec {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(_ROSpec.class);

	/**
	 * A unique identification for the ROSpec.
	 */
	private int id;

	/**
	 * The priority of this ROSpec.
	 */
	private int priority;

	/**
	 * The specs that this ROSpec will execute.
	 */
	private ArrayList<ExecutableSpec> specsToExecute;

	/**
	 * The current state of this ROSpec.
	 */
	private ROSpecState currentState;

	/**
	 * The startTrigger that this ROSpec has.
	 */
	private Trigger startTrigger;

	/**
	 * The stopTrigger that this rospec has.
	 */
	private Trigger stopTrigger;

	/**
	 * A collection of variables that describe what information should be in a
	 * ROReport. If set to null, use global roReportFormat
	 */
	private ROReportFormat roReportFormat = null;

	private Integer currentSpecIndex = null;

	/**
	 * This class represents an LLRP Reader Operation Specification. A ROSpec is
	 * a controller that executes several AISpecs.
	 * 
	 * @param id
	 *            A unique identification for the ROSpec.
	 * @param priority
	 *            The priority of this ROSpec.
	 * @param startTrigger
	 *            The startTrigger that this ROSpec has.
	 * @param stopTrigger
	 *            The stopTrigger that this rospec has.
	 */
	public _ROSpec(ROSpec rospec) throws IllegalArgumentException {
	}

	public void toActiveState() {
		this.currentState = ROSpecState.ACTIVE;
		this.stopTrigger.enable();
		currentSpecIndex = -1;
		executeNextSpec();

	}

	public void toInactiveState() {
		this.currentState = ROSpecState.INACTIVE;
		this.startTrigger.enable();
		this.stopTrigger.disable();
	}

	public void toDisabledState() {
		this.currentState = ROSpecState.DISABLED;
		this.startTrigger.disable();
		this.stopTrigger.disable();
		specsToExecute.get(currentSpecIndex).stop();
	}

	public void delete() {
		this.currentState = null;
		this.startTrigger.disable();
		this.stopTrigger.disable();
	}

	public void executeNextSpec() {
		currentSpecIndex++;
		if (this.specsToExecute.size() < currentSpecIndex) {
			specsToExecute.get(currentSpecIndex).start();
		} else {
			this.getStopTrigger().fireTrigger();
		}
	}

	public void stopCurrentSpec(int SpecIndex) {
		if (this.currentSpecIndex == SpecIndex) {
			specsToExecute.get(currentSpecIndex).stop();
			executeNextSpec();
		}

	}

	public ROSpec toROSpec() {
		return null;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the specsToExecute
	 */
	public Collection<ExecutableSpec> getSpecsToExecute() {
		return specsToExecute;
	}

	/**
	 * @param specsToExecute
	 *            the specsToExecute to set
	 */
	public void setSpecsToExecute(ArrayList<ExecutableSpec> specsToExecute) {
		this.specsToExecute = specsToExecute;
	}

	/**
	 * @return the currentState
	 */
	public ROSpecState getCurrentState() {
		return currentState;
	}

	/**
	 * @param currentState
	 *            the currentState to set
	 */
	public void setCurrentState(ROSpecState currentState) {
		this.currentState = currentState;
	}

	/**
	 * @return the startTrigger
	 */
	public Trigger getStartTrigger() {
		return startTrigger;
	}

	/**
	 * @param startTrigger
	 *            the startTrigger to set
	 */
	public void setStartTrigger(Trigger startTrigger) {
		this.startTrigger = startTrigger;
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
	 * @return the roReportFormat
	 */
	public ROReportFormat getRoReportFormat() {
		return roReportFormat;
	}

	/**
	 * @param roReportFormat
	 *            the roReportFormat to set
	 */
	public void setRoReportFormat(ROReportFormat roReportFormat) {
		this.roReportFormat = roReportFormat;
	}
}
