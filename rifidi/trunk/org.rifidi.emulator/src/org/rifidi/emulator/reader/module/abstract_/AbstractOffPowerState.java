/*
 *  @(#)AbstractOffPowerState.java
 *
 *  Created:	Oct 2, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.module.abstract_;

import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.common.PowerState;
import org.rifidi.emulator.extra.ExtraInformation;

/**
 * An abstract implementation of an OFF PowerState. Implements methods which
 * would be constant across all power states which are OFF.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractOffPowerState implements PowerState {

	/**
	 * Effectively does nothing - not a valid power state transition from the
	 * off state.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	public void resume(PowerControllable pcObject) {
		/* Invalid transition - do nothing */

	}

	/**
	 * Effectively does nothing - not a valid power state transition from the
	 * off state.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	public void suspend(PowerControllable pcObject) {
		/* Invalid transition - do nothing */

	}

	/**
	 * Effectively does nothing - not a valid power state transition from the
	 * off state.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOff(PowerControllable pcObject) {
		/* Invalid transition - do nothing */

	}

	/**
	 * Though this is a valid state to be in.  This is only used for special cases
	 * such as asynchronous modules so it is not required.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOn(PowerControllable pcObject, ExtraInformation extraInfo) {
		/* Invalid transition - do nothing */

	}
	
	
}
