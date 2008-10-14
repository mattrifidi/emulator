/**
 * 
 */
package org.rifidi.emulator.reader.llrp.trigger;

import java.util.ArrayList;
import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * This class is like the control Signal, except that it is specifically to be
 * used for Triggers. It has a "state" variable which is either "executing"
 * (true) or "not executing" (false). Triggers (such as Duration Trigger or
 * PeriodicTrigger) will update this value, and either the ROSpecController,
 * AISpec (depending on what controller is using this observable object) will
 * listen for a change in this state and act accordingly.
 * 
 * @author kyle
 * 
 */
public class TriggerObservable extends Observable {

	/**
	 * If the Trigger Observable is being used for ROSPecs, we need to know the
	 * ROSPec ID. If this Trigger Observable is not being used for ROSpecs, keep
	 * the roSpecNum=-1, which is an invalid ROSpec ID
	 */
	private int roSpecNum = -1;

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(TriggerObservable.class);

	/**
	 * The current state of this observable.
	 */
	private boolean state;

	/**
	 * A constructor which takes in an initial value. Use for AISpecs.
	 * 
	 * @param initialValue
	 *            The initial state of the spec
	 */
	public TriggerObservable(boolean initialValue) {
		super();
		this.state = initialValue;
	}

	/**
	 * A construction which takes in an intial value and a rospec ID. Use for
	 * ROSpecs only
	 * 
	 * @param initialValue
	 *            The initial state of the spec
	 * @param roSpecID
	 *            The ID of the ROSpec
	 */
	public TriggerObservable(boolean initialValue, int roSpecID) {
		super();
		this.state = initialValue;
		this.roSpecNum = roSpecID;
	}

	/**
	 * This method sets the state to true and fires a start trigger.
	 * 
	 * @param changer
	 *            The class who called this method
	 */
	public void fireStartTrigger(Class changer) {
		setState(true, changer);
	}

	/**
	 * This method sets the state to false and fires a stop trigger
	 * 
	 * @param changer
	 *            The class who called this method
	 */
	public void fireStopTrigger(Class changer) {
		setState(false, changer);
	}

	/**
	 * This private method actually does the work of setting the state and
	 * notifying the obervers
	 * 
	 * @param newState
	 *            The new state.
	 * @param changer
	 *            The Class who fired this trigger
	 */
	private synchronized void setState(boolean newState, Class changer) {
		logger.debug("Changing state of TriggerObservable to " + newState);
		this.state = newState;
		ArrayList<Object> extraInfo = new ArrayList<Object>();
		extraInfo.add(newState);
		extraInfo.add(changer);

		// check to see if this trigger observable is being used for rospecs. If
		// so, add the rospec number to the extraInfo
		if (roSpecNum != -1) {
			extraInfo.add(roSpecNum);
		}
		this.setChanged();
		this.notifyObservers(extraInfo);
	}

	public boolean getState() {
		return this.state;
	}
}
