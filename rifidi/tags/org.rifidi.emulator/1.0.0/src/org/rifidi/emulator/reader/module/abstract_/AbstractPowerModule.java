/*
 *  @(#)AbstractPowerModule.java
 *
 *  Created:	Sep 18, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.module.abstract_;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.common.PowerState;
import org.rifidi.emulator.extra.ExtraInformation;


/**
 * A AbstractPowerModule is a PowerControllable which holds a PowerState. The
 * current PowerState of the module is what dictates its PowerControllable
 * behavior.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractPowerModule implements PowerControllable,
		Observer {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(AbstractPowerModule.class);
	
	/**
	 * The current power state of the AbstractPowerModule.
	 */
	private PowerState curPowerState;

	/**
	 * A control signal which this AbstractPowerModule is observing for
	 * changes. <br>
	 * This control signal allows for control of this AbstractPowerModule
	 * without needing a direct reference to the AbstractPowerModule itself.
	 * Only on/off control is given, suspension calls need to be made directly.
	 * <br>
	 * If this signal is null, then no such monitoring will take place.
	 */
	private ControlSignal<Boolean> powerControlSignal;
	
	protected AbstractPowerModule(){  }

	/**
	 * Creates a AbstractPowerModule with an initial state being the one passed
	 * as an argument.
	 * 
	 * @param initialPowerState
	 *            The desired initial state of the AbstractPowerModule.
	 * @param powerControlSignal
	 *            The control signal for this AbstractPowerModule to observe.
	 *            If <i>null</i> is passed, this will not observe a signal.
	 */
	public AbstractPowerModule(PowerState initialPowerState,
			ControlSignal<Boolean> powerControlSignal) {
		/* Assign instance variables */
		this.curPowerState = initialPowerState;
		this.powerControlSignal = powerControlSignal;

		/* Register this with the power ControlSignal. */
		if (this.powerControlSignal != null) {
			this.powerControlSignal.addObserver(this);

		}
	}

	/**
	 * Changes the current power state to the passed power state.
	 * 
	 * @param anotherPowerState
	 *            The power state to change to.
	 */
	protected void changePowerState(PowerState anotherPowerState) {
		this.curPowerState = anotherPowerState;

	}

	/**
	 * Returns the powerControlSignal.
	 * 
	 * @return Returns the powerControlSignal.
	 */
	protected ControlSignal<Boolean> getPowerControlSignal() {
		return this.powerControlSignal;
	}

	/**
	 * @see org.rifidi.emulator.common.PowerControllable#getPowerState()
	 */
	public PowerState getPowerState() {
		return this.curPowerState;

	}

	/**
	 * @see org.rifidi.emulator.common.PowerControllable#resume()
	 */
	public void resume() {
		/* Call the current power state's implementation of this method */
		this.curPowerState.resume(this);

	}

	/**
	 * @see org.rifidi.emulator.common.PowerControllable#suspend()
	 */
	public void suspend() {
		/* Call the current power state's implementation of this method */
		this.curPowerState.suspend(this);

	}

	/**
	 * @see org.rifidi.emulator.common.PowerControllable#turnOff()
	 */
	public void turnOff(Class callingClass) {
		
		/* Call the current power state's implementation of this method */
			this.curPowerState.turnOff(this, callingClass);
	}

	/**
	 * @see org.rifidi.emulator.common.PowerControllable#turnOn()
	 */
	public void turnOn() {
		/* Call the current power state's implementation of this method */
		this.curPowerState.turnOn(this);

	}

	/**
	 * This turn on method changes the power state to on.  It is different
	 * because it takes in a command to execute.  This is mostly used
	 * for autonomous and asynchronous modules.
	 * 
	 * @param commandMap
	 */
	public void turnOn(ExtraInformation extraInfo) {
		/* Call the current power state's implementation of this method */
		this.curPowerState.turnOn(this, extraInfo);

	}

	/**
	 * If the update is caused by the powerControlSignal, the
	 * AbstractPowerModule will be turned on if the signal is true or turned
	 * off if the signal is false.
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		/* Only update if this was called by the powerControlSignal */
		if ((this.powerControlSignal != null) && (o == this.powerControlSignal)) {
			/* Turn on if the control signal is true, turn off if false. */
			if (this.powerControlSignal.getControlVariableValue()) {
				if( this.powerControlSignal.getExtraInformation() == null ) {
					this.turnOn();
				} else {
					this.turnOn(this.powerControlSignal.getExtraInformation());
				}
			} else {
				this.turnOff(this.getClass());

			}

		}

	}

}
