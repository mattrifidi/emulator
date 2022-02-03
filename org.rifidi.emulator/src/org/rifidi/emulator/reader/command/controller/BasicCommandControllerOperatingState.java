/*
 *  @(#)BasicCommandControllerOperatingState.java
 *
 *  Created:	Oct 24, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller;

import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;

/**
 * A basic command controller which does nothing but shuttle data to/from its
 * CommandAdapter.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class BasicCommandControllerOperatingState extends
		AbstractCommandControllerOperatingState {

	/**
	 * Basic constructor. Simply calls the super constructor.
	 * 
	 * @param adapter
	 */
	public BasicCommandControllerOperatingState(CommandAdapter adapter) {
		super(adapter);

	}

}
