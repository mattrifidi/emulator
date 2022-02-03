/*
 *  CommandAdapter.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.control.adapter;

import java.util.ArrayList;

/**
 * CommandAdapter
 * 
 * Provides an interface for implementations of different CommandAdapters
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public interface CommandAdapter {
	/**
	 * Executes a command given by the string CMD
	 * 
	 * @param command
	 *            The command and value to execute as a byte array
	 * @return The return message of this command
	 */
	public ArrayList<Object> executeCommand(byte[] command);
}
