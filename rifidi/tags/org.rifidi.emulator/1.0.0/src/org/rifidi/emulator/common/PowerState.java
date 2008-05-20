/*
 *  @(#)PowerState.java
 *
 *  Created:	Sep 18, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.common;

import org.rifidi.emulator.extra.ExtraInformation;


/**
 * A power state defines the behavior of each turnOff, turnOn, and suspend
 * method. An implementation intended to be paired with a PowerControllable
 * object, but may exist on its own for other purposes.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public interface PowerState {

	/**
	 * Suspends the passed PowerControllable.
	 * 
	 * @param pcObject
	 *            The PowerControllable which invoked this method.
	 * 
	 */
	public void suspend(PowerControllable pcObject);

	/**
	 * Resumes the passed PowerControllable from suspention.
	 * 
	 * @param pcObject
	 *            The PowerControllable which invoked this method.
	 * 
	 */
	public void resume(PowerControllable pcObject);

	/**
	 * Turns off the specified device.
	 * 
	 * @param pcObject
	 *            The PowerControllable which invoked this method.
	 * 
	 */
	public void turnOff(PowerControllable pcObject, Class callingClass);

	/**
	 * Turns on the specified device.
	 * 
	 * @param pcObject
	 *            The PowerControllable which invoked this method.
	 * 
	 */
	public void turnOn(PowerControllable pcObject);
	
	/**
	 * Turns on the specified device with a command to execute
	 * 
	 * @param pcObject
	 *            The PowerControllable which invoked this method.
	 * 
	 */
	public void turnOn(PowerControllable pcObject, ExtraInformation extraInfo);

}
