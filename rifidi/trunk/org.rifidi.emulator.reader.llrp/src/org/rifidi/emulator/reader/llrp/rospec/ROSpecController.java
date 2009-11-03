/*
 *  ROSpecController.java
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.trigger.DurationTrigger;
import org.rifidi.emulator.reader.llrp.trigger.GPIWithTimeoutTrigger;
import org.rifidi.emulator.reader.llrp.trigger.ImmediateTrigger;
import org.rifidi.emulator.reader.llrp.trigger.PeriodicTrigger;
import org.rifidi.emulator.reader.llrp.trigger.TimerTrigger;
import org.rifidi.emulator.reader.llrp.trigger.TriggerObservable;

/**
 * This is a controller that manages all of the ROSpecs. It listens for start
 * and stop triggers, and changes the state of rospecs according to the start
 * and stop triggers.
 * 
 * LLRP defines three states for a ROSpec to be in: 1)Disabled. 2)Inactive
 * 3)Running. When a ROSpec is added, it is in the Disabled Spec. An
 * ENABLE_ROSPEC message moves the ROSpec to the inactive state. Then it waits
 * for a start trigger to put it in the execution state. When it is executing, a
 * stop trigger will move it back to the disabled state. A DISABLE_ROSPEC
 * Message moves it back to the Disabled state. Finally a DELETE_ROSPEC message
 * will remove it completely
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle Neumeier
 */
public class ROSpecController implements Observer {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(ROSpecController.class);

	/**
	 * A map of every ROSpec that exists in this reader.
	 */
	private HashMap<Integer, _ROSpec> allROSpecs;

	/**
	 * Queue of disabled rospecs.
	 */
	private HashMap<Integer, _ROSpec> disabledList;

	/**
	 * Queue of active rospects.
	 */
	private ArrayList<_ROSpec> activeList;

	/**
	 * Queue of active rospects.
	 */
	private HashMap<Integer, _ROSpec> inactiveList;

	/**
	 * Hashmap of SpecStates. Each rospec has a spec state that will start and
	 * stop its execution.
	 */
	private HashMap<Integer, TriggerObservable> executionStateList;

	/**
	 * Hashmap of states where the rospec should go after it gets stopped. The
	 * key is the ROSpecID and the value is the state
	 */
	private HashMap<Integer, Integer> afterStopStateList;

	private static final int STATE_DISABLED = 0;

	private static final int STATE_INACTIVE = 1;

	private static final int STATE_ACTIVE = 2;

	private static final int STATE_DELETED = 3;

	/**
	 * Private constructor
	 */
	ROSpecController() {
		this.allROSpecs = new HashMap<Integer, _ROSpec>();
		this.disabledList = new HashMap<Integer, _ROSpec>();
		this.activeList = new ArrayList<_ROSpec>();
		this.inactiveList = new HashMap<Integer, _ROSpec>();
		this.executionStateList = new HashMap<Integer, TriggerObservable>();
		this.afterStopStateList = new HashMap<Integer, Integer>();
	}

	/**
	 * Adds a rospec to the disabled queue. Puts the ROSpec in the disabled
	 * state.
	 * 
	 * @param rospecToAdd
	 *            ROSpec object to add
	 * @return
	 */
	public boolean addROSpec(_ROSpec rospecToAdd) {
		if (allROSpecs.containsKey(rospecToAdd.getId())) {
			return false;
		}
		TriggerObservable ss = new TriggerObservable(false, rospecToAdd.getId());
		ss.addObserver(this);
		this.executionStateList.put(rospecToAdd.getId(), ss);
		this.allROSpecs.put(rospecToAdd.getId(), rospecToAdd);
		this.disabledList.put(rospecToAdd.getId(), rospecToAdd);

		rospecToAdd.setCurrentState(STATE_DISABLED);

		if (rospecToAdd.getStartTrigger() instanceof PeriodicTrigger) {
			PeriodicTrigger pt = (PeriodicTrigger) rospecToAdd
					.getStartTrigger();
			pt.setTriggerObservable(ss);
			// pt.startTimer();
		}
		if (rospecToAdd.getStartTrigger() instanceof ImmediateTrigger) {
			rospecToAdd.getStartTrigger().setTriggerObservable(ss);
		}
		if (rospecToAdd.getStartTrigger() instanceof GPIWithTimeoutTrigger) {
			rospecToAdd.getStartTrigger().setTriggerObservable(ss);
		}

		logger.info("ROSpec Added: " + rospecToAdd.getId());

		return true;
	}

	/**
	 * Moves a ROSpec from the disabled to the inactive queue. Returns false if
	 * the ROSpec does not exist or if the ROSpec is not in the inactive queue.
	 * In other words, this method puts the ROSpec into the enabled state
	 * 
	 * @param rospecToEnable
	 *            ID of ROspec
	 * @return
	 */
	public boolean enableROSpec(int rospecToEnable) {
		if (!disabledList.containsKey(rospecToEnable)) {
			return false;
		}
		_ROSpec spec = this.allROSpecs.get(rospecToEnable);

		this.inactiveList.put(rospecToEnable, allROSpecs.get(rospecToEnable));
		this.disabledList.remove(rospecToEnable);
		spec.setCurrentState(STATE_INACTIVE);

		logger.info("ROSpec enabled: " + rospecToEnable);

		/* If the rospec has an Immediate trigger, start it now */
		if (spec.getStartTrigger() instanceof ImmediateTrigger) {
			return startROSpec(rospecToEnable);

		}

		if (spec.getStartTrigger() instanceof PeriodicTrigger) {
			((PeriodicTrigger) spec.getStartTrigger()).startTimer();
		}

		return true;

	}

	/**
	 * Starts the rospec with the given integer ID. If the ROSpec is in the
	 * inactive state and no other ROSpecs are currently running, it will return
	 * true. If there is a ROSpec running or the current ROSpec is not in the
	 * inactive queue, it will return false.
	 * 
	 * Currently only one ROSpec can run at a time
	 * 
	 * @param rospecToStart
	 *            ID of ROSPec to start
	 * @return
	 */
	public boolean startROSpec(int rospecToStart) {
		if (!inactiveList.containsKey(rospecToStart)
				|| !this.activeList.isEmpty()) {
			return false;
		}
		_ROSpec rs = allROSpecs.get(rospecToStart);
		TriggerObservable ss = executionStateList.get(rospecToStart);

		/*
		 * Check to see if the spec state is true. If it is not, fire the start
		 * trigger, which in turn will notify this object and will immediatly
		 * call this method again. The next time this method is called however,
		 * the state will already be true, and it will actually start the ROSpec
		 */

		if (!ss.getState()) {
			ss.fireStartTrigger(this.getClass());
			return true;
		} else {

			this.activeList.add(allROSpecs.get(rospecToStart));
			this.inactiveList.remove(rospecToStart);
			rs.setCurrentState(STATE_ACTIVE);

			/*
			 * By default, this rospec should go to the inactive state when
			 * stopped
			 */
			this.afterStopStateList.put(rospecToStart, STATE_INACTIVE);

			/* Set up stop triggers */
			if (rs.getStopTrigger() instanceof DurationTrigger) {
				DurationTrigger trig = (DurationTrigger) rs.getStopTrigger();
				trig.setTriggerObservable(ss);
			} else if (rs.getStopTrigger() instanceof GPIWithTimeoutTrigger) {
				GPIWithTimeoutTrigger trig = (GPIWithTimeoutTrigger) rs
						.getStopTrigger();
				trig.setTriggerObservable(ss);
			}

			rs.execute();
			if(logger.isDebugEnabled()){
				logger.debug("ROSpec started: " + rospecToStart);
			}
			return true;
		}
	}

	/**
	 * Stops the given ROSpec. Returns true if the ROSpec was currently running
	 * and is stopped correctly. Returns false if the ROSpec was not running or
	 * has a problem stopping.
	 * 
	 * After a rospec is stopped it returns to the inactive state. Then, this
	 * method checks the afterStopState list to see if it is supposed to make an
	 * additional transition (either to the disabled or deleted state).
	 * 
	 * @param rospecToStop
	 *            ID of ROSpec to stop
	 * @return
	 */

	public boolean stopROSpec(int rospecToStop) {
		if (!allROSpecs.containsKey(rospecToStop)) {
			return false;
		}
		if (!activeList.contains(allROSpecs.get(rospecToStop))) {
			return false;
		}

		_ROSpec stopSpec = activeList.get(0);

		TriggerObservable ss = executionStateList.get(stopSpec.getId());

		/*
		 * Make sure that the control signal is false. If it is true, by setting
		 * it to false, it will notify the observer (which is this class), and
		 * this method will be called again, except this time the else part of
		 * this construct will be run because the control variable will have
		 * been set to false.
		 */
		if (ss.getState()) {
			ss.fireStopTrigger(this.getClass());
			return true;
		} else {
			stopSpec.stop();

			if (stopSpec.getStopTrigger() instanceof TimerTrigger) {
				((TimerTrigger) stopSpec.getStopTrigger()).stopTimer();
			}

			activeList.remove(stopSpec);

			inactiveList.put(stopSpec.getId(), stopSpec);
			stopSpec.setCurrentState(STATE_INACTIVE);

			int nextState = this.afterStopStateList.get(rospecToStop);

			if (logger.isDebugEnabled()) {
				logger.debug("ROSpec stopped: " + stopSpec.getId());
			}

			/*
			 * If the rospec should make an additional transition, as noted by
			 * the nextState variable, do it here
			 */
			if (nextState == STATE_DISABLED) {
				return disableROSpec(stopSpec.getId());
			} else if (nextState == STATE_DELETED) {
				return deleteROSpec(stopSpec.getId());
			}

			return true;
		}
	}

	/**
	 * Disables the ROSpec, moving it to the disabled state. If the rospec is in
	 * the active state, the rosoc must be stopped first. Because stopping
	 * happens asyncronously, the afterStopState hashmap is updated for this
	 * rospec, and the rospec is stopped using the stopROSpec() method. The
	 * stopROSpec() method will then call this method again and the rospec will
	 * be disabled
	 * 
	 * @param rospecToDisable
	 *            ID of ROSpec to disable
	 * @return
	 */
	public boolean disableROSpec(int rospecToDisable) {

		if (!allROSpecs.containsKey(rospecToDisable)) {
			return false;
		}
		_ROSpec rs = this.allROSpecs.get(rospecToDisable);
		/* Make sure RoSpec is either in inactive or active queue */
		if (!inactiveList.containsKey(rospecToDisable)) {
			if (!activeList.contains(rs)) {
				return false;
			}
		}

		/*
		 * If the rospec has an Periodic start trigger, stop it so that it
		 * doesn't fire anymore
		 */
		if (rs.getStartTrigger() instanceof PeriodicTrigger) {
			((PeriodicTrigger) rs.getStartTrigger()).stopTimer();
		}

		/* If inactive list contains rospec */
		if (inactiveList.containsKey(rospecToDisable)) {

			this.inactiveList.remove(rospecToDisable);
			this.disabledList.put(rospecToDisable, allROSpecs
					.get(rospecToDisable));
			rs.setCurrentState(STATE_DISABLED);

			logger.info("ROSpec Disabled: " + rs.getId());
			return true;
		}
		/* Else active list contains rospec. Must stop it and then disable it. */
		else {
			this.afterStopStateList.put(rospecToDisable, STATE_DISABLED);
			return stopROSpec(rospecToDisable);
		}

	}

	/**
	 * Deletes a ROSpec. Returns false if the ROSpec does not exist. If the
	 * ROSpec is in the active state, it must be stopped first. Because stopping
	 * happens asyncronoulsy, the afterStopState hashmap is update for this
	 * rospec. Then the stopROSpec() method is called. After it stops the
	 * ROspec, it will call this method again, which will delete it.
	 * 
	 * @param rospecToDelete
	 *            ID of ROSpec to delete
	 * @return
	 */
	public boolean deleteROSpec(int rospecToDelete) {
		logger.debug("Trying to delete ROSpec of ID: " + rospecToDelete);
		if (!allROSpecs.containsKey(rospecToDelete)) {
			return false;
		}

		_ROSpec rs = allROSpecs.get(rospecToDelete);

		/*
		 * If rospec is active, stop it first, then delete it
		 */
		if (this.activeList.contains(rs)) {
			afterStopStateList.put(rospecToDelete, STATE_DELETED);
			return stopROSpec(rospecToDelete);
		}

		if (this.disabledList.containsKey(rospecToDelete)) {
			this.disabledList.remove(rospecToDelete);
		}
		if (this.inactiveList.containsKey(rospecToDelete)) {
			this.inactiveList.remove(rospecToDelete);
		}
		this.allROSpecs.get(rospecToDelete).cleanUp();
		this.allROSpecs.remove(rospecToDelete);
		this.executionStateList.remove(rospecToDelete);
		this.afterStopStateList.remove(rospecToDelete);

		logger.info("ROSpec deleted: " + rospecToDelete);
		return true;
	}

	public void cleanUp() {
		Iterator<_ROSpec> iter = allROSpecs.values().iterator();
		while (iter.hasNext()) {
			_ROSpec current = iter.next();
			this.deleteROSpec(current.getId());
		}

	}

	/**
	 * This is the logic for what happens when a specState that is being
	 * observed by this object changes. This class should only be notified by
	 * TriggerObservable objects
	 */
	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {

		ArrayList<Object> extraInfo;
		boolean newState;
		Class callingClass;
		int rospecID;

		try {
			extraInfo = (ArrayList<Object>) arg;
			newState = (Boolean) extraInfo.get(0);
			callingClass = (Class) extraInfo.get(1);
			rospecID = (Integer) extraInfo.get(2);

			// If rospec stop trigger
			if (newState == false) {
				this.stopROSpec(rospecID);
				if (this.allROSpecs.get(rospecID).getStartTrigger() instanceof ImmediateTrigger) {
					ImmediateTrigger trig = (ImmediateTrigger) this.allROSpecs
							.get(rospecID).getStartTrigger();
					trig.restartRoSpec();
				}
			}
			// if rospec start trigger
			else {
				this.startROSpec(rospecID);
			}

		} catch (Exception e) {
			logger.debug("There was an error when trying to update "
					+ "ROSPec.  Check to make sure the TriggerObservable's "
					+ "extra informaiton was formed correctly");
		}

	}

	public HashMap<Integer, _ROSpec> getROSpecs() {
		return this.allROSpecs;
	}
}
