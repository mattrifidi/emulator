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
import org.rifidi.emulator.reader.llrp.aispec._AISpec;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.llrp.report.ROReportFormat;
import org.rifidi.emulator.reader.llrp.trigger.Trigger;

/**
 * This class represents an LLRP Reader Operation Specification. A ROSpec is a
 * controller that executes several AISpecs.
 * 
 * @author Matthew Dean - matt@pramari.com
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
	private Collection<_AISpec> specsToExecute;

	/**
	 * The current state of this ROSpec.
	 */
	private int currentState;

	/**
	 * The startTrigger that this ROSpec has.
	 */
	private Trigger startTrigger;

	/**
	 * The stopTrigger that this rospec has.
	 */
	private Trigger stopTrigger;

	/**
	 * The executer of this ROSpec.
	 */
	private ROSpecExecuter exec;

	/**
	 * A collection of variables that describe what information should be in a
	 * ROReport. If set to null, use global roReportFormat
	 */
	private ROReportFormat roReportFormat = null;

	/**
	 * The name of the reader.
	 */
	private String readerName;

	private LLRPReaderSharedResources llrpsr;
	


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
	public _ROSpec(int id, int priority, Trigger startTrigger,
			Trigger stopTrigger, String readerName,
			LLRPReaderSharedResources llrpsr) {
		this(id, priority, startTrigger, stopTrigger, null, readerName, llrpsr);
	}

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
	 * @param roReportFormat
	 *            Specifies info contained in ROReport
	 */
	public _ROSpec(int id, int priority, Trigger startTrigger,
			Trigger stopTrigger, ROReportFormat roReportFormat,
			String readerName, LLRPReaderSharedResources llrpsr) {
		this.id = id;
		this.priority = priority;
		this.startTrigger = startTrigger;
		this.stopTrigger = stopTrigger;
		this.exec = new ROSpecExecuter(new ArrayList<_AISpec>(), this, llrpsr);
		this.roReportFormat = roReportFormat;
		this.readerName = readerName;
		this.llrpsr = llrpsr;
	}

	/**
	 * @return the currentState
	 */
	public int getCurrentState() {
		return currentState;
	}

	/**
	 * @param currentState
	 *            the currentState to set
	 */
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
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
	public Collection<_AISpec> getSpecsToExecute() {
		return specsToExecute;
	}

	/**
	 * @param specsToExecute
	 *            the specsToExecute to set
	 */
	public void setSpecsToExecute(Collection<_AISpec> specsToExecute) {
		this.specsToExecute = specsToExecute;
		this.exec.setSpecsToExecute((ArrayList<_AISpec>) this.specsToExecute);
	}

	/**
	 * Adds an AISpec from specsToExecute.
	 * 
	 * @param newSpec
	 */
	public void addSpecToExecute(_AISpec newSpec) {
		this.specsToExecute.add(newSpec);
		this.exec.setSpecsToExecute((ArrayList<_AISpec>) this.specsToExecute);
	}

	/**
	 * Remove an AISpec from the list of specs to execute.
	 * 
	 * @param specToRemove
	 */
	public void removeSpecsToExecute(_AISpec specToRemove) {
		this.specsToExecute.remove(specToRemove);
		this.exec.setSpecsToExecute((ArrayList<_AISpec>) this.specsToExecute);
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
	 * Start the ROSpec executer thread.
	 * 
	 */
	public void execute() {
		/* Create a new thread and kick it off */
		Thread tempThread = new Thread(exec, "ROSpec Executer");
		tempThread.start();
	}

	/**
	 * Stop the ROSpec executor thread
	 * 
	 */
	public void stop() {
		exec.stop();
	}

	protected void cleanUp() {
		if (this.startTrigger != null) {
			this.startTrigger.cleanUp();
			this.startTrigger = null;
		}

		if (this.stopTrigger != null) {
			this.stopTrigger.cleanUp();
			this.stopTrigger = null;
		}
		for (_AISpec ais : this.specsToExecute) {
			ais.getStopTrigger().cleanUp();
		}
	}
	
	public void suspend(){
		if (this.startTrigger != null) {
			this.startTrigger.suspend();
		}

		if (this.stopTrigger != null) {
			this.stopTrigger.suspend();
		}
		for(_AISpec ais : this.specsToExecute){
			ais.suspend();
		}
	}
	
	public void resume(){
		if (this.startTrigger != null) {
			this.startTrigger.resume();
		}

		if (this.stopTrigger != null) {
			this.stopTrigger.resume();
		}
		for(_AISpec ais : this.specsToExecute){
			ais.resume();
		}
	}

	/**
	 * @return the roReportFormat
	 */
	public ROReportFormat getRoReportFormat() {
		return roReportFormat;
	}

	/**
	 * @return the readerName
	 */
	public String getReaderName() {
		return readerName;
	}
}
