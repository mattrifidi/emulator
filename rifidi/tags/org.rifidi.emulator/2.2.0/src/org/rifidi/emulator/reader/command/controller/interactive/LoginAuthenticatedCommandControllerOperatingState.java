/*
 *  @(#)LoginAuthenticatedCommandControllerOperatingState.java
 *
 *  Created:	Oct 26, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.interactive;

import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;

/**
 * The authenticated, state of a login process. When in this mode, a full set of
 * commands applicable to an interactive mode may be used. These commands are
 * defined in the adapter passed into the constructor. This state does not
 * contains paths to any other state, and thus the CommandController which has
 * this state will not leave this state until asynchronously reset (either
 * through control signals or direct interaction).
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class LoginAuthenticatedCommandControllerOperatingState extends
		AbstractCommandControllerOperatingState {

	/**
	 * Creates an authenticated state with the passed adapter for command
	 * handler lookup.
	 * 
	 * @param adapter
	 *            The adapter to use to aid in the invocation of commands.
	 */
	public LoginAuthenticatedCommandControllerOperatingState(
			CommandAdapter adapter) {
		/* Simply invoke the super constructor, no extra state is needed */
		super(adapter);

	}

}
