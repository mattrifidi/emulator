/*
 *  @(#)AbstractOnPowerState.java
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
 * An abstract implementation of an ON PowerState. Implements methods which
 * would be constant across all power states which are ON.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractOnPowerState implements PowerState {

	/**
	 * Effectively does nothing - not a valid power state transition from the on
	 * state.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	public void resume(PowerControllable pcObject) {
		/* Invalid transition - do nothing */

	}

	/**
	 * Effectively does nothing - not a valid power state transition from the on
	 * state.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOn(PowerControllable pcObject) {
		/* Invalid transition - do nothing */

	}

	/**
	 * Effectively does nothing - not a valid power state transition from the on
	 * state.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOn(PowerControllable pcObject, ExtraInformation extraInfo) {
		/* Invalid transition - do nothing */

	}

}
