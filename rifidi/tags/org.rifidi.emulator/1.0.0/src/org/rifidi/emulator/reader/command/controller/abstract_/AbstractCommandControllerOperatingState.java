/*
 *  @(#)AbstractCommandControllerOperatingState.java
 *
 *  Created:	Oct 13, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.abstract_;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.reader.command.controller.CommandController;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;

/**
 * A basic command controller operating state which has a command adapter
 * associated with it to process commands.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractCommandControllerOperatingState{

	/*
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AbstractCommandControllerOperatingState.class);
	
	/**
	 * The command adapter which this operating state uses.
	 */
	private CommandAdapter commandAdapter;

	/**
	 * A constructor for an AbstractCommandControllerOperatingState which takes
	 * in an adapter to use for processing commands.
	 * 
	 * @param adapter
	 *            The adapter to use to aid in the invocation of commands.
	 */
	public AbstractCommandControllerOperatingState(CommandAdapter adapter) {
		this.commandAdapter = adapter;

	}

	/**
	 * Returns the commandAdapter.
	 * 
	 * @return Returns the commandAdapter.
	 */
	protected CommandAdapter getCommandAdapter() {
		return this.commandAdapter;

	}

	/**
	 * Sends the passed command to the command adapter for processing.
	 * 
	 */
	public ArrayList<Object> processCommand(byte[] command, CommandController controller) {
		/* Send the command to the adapter for processing */
		
		return this.commandAdapter.executeCommand(command);
		
	}
	
	/**
	 * Sometimes the Operating state needs to initialize as soon as a connection is made.
	 */
	public void initialize(AbstractCommandController controller, Communication comm){

	}

}
