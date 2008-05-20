/*
 *  @(#)PowerControllable.java
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

/**
 * This interface mimics a device which has power controls. This is akin to
 * having a power switch, suspend button, and status lights.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public interface PowerControllable {

	/**
	 * Turns on the device.
	 */
	public void turnOn();

	/**
	 * Turns off the device.
	 */
	public void turnOff(Class callingClass);

	/**
	 * Suspends the device.
	 */
	public void suspend();

	/**
	 * Resumes the device from suspension.
	 */
	public void resume();

	/**
	 * Returns the current PowerState.
	 * 
	 * @return The current PowerState.
	 */
	public PowerState getPowerState();

}
