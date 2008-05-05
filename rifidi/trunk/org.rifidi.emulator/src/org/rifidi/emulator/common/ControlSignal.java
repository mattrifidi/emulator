/*
 *  @(#)ControlSignal.java
 *
 *  Created:	Oct 23, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.common;

import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.extra.ExtraInformation;

/**
 * A ControlSignal is an Observable which contains a single variable to modify
 * and observe.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * @param <T>
 *            The type of variable to store.
 * 
 */
public class ControlSignal<T> extends Observable {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(ControlSignal.class);

	/**
	 * The current value of the control variable.
	 */
	private T controlVariableValue;

	/**
	 * A basic constructor which takes in an initial value to set the control
	 * variable to.
	 * 
	 * @param initialValue
	 *            The initial value to set the control variable at.
	 */
	public ControlSignal(T initialValue) {
		super();
		this.controlVariableValue = initialValue;
	}

	/**
	 * Returns the controlVariableValue.
	 * 
	 * @return Returns the controlVariableValue.
	 */
	public final T getControlVariableValue() {
		return this.controlVariableValue;

	}

	/**
	 * Sets controlVariableValue to the passed parameter. Notifies any observers
	 * of this object that the change has happened.
	 * 
	 * @param controlVariableValue
	 *            The controlVariableValue to set.
	 */
	public final void setControlVariableValue(T controlVariableValue) {
		this.controlVariableValue = controlVariableValue;
		logger.debug("Set control variable value " + controlVariableValue);
		super.setChanged();
		super.notifyObservers();

	}

	/**
	 * Sets controlVariableValue to the passed parameter. Notifies any observers
	 * of this object that the change has happened.
	 * 
	 * @param controlVariableValue
	 *            The controlVariableValue to set.
	 * @param extraInformation
	 *            extraInformaiton to send to the observers
	 */
	public final void setControlVariableValue(T controlVariableValue,
			Object extraInformation) {
		this.controlVariableValue = controlVariableValue;
		logger.debug("Set control variable value" + controlVariableValue);
		super.setChanged();
		super.notifyObservers(extraInformation);

	}

	/**
	 * The map that contains all of the commands to run.
	 */
	private ExtraInformation extraInfo;

	/**
	 * Get the map that has all of the commands to run
	 * 
	 * @return The map that contains all of the commands to run.
	 */
	public ExtraInformation getExtraInformation() {
		return extraInfo;
	}

	/**
	 * Sets the commandMap that has all of the commands to run
	 * 
	 * @param extraInfo
	 *            The extra information to set
	 */
	public void setExtraInformation(ExtraInformation extraInfo) {
		this.extraInfo = extraInfo;
	}

}
