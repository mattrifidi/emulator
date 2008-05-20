/*
 *  @(#)TestHeartbeatCommandAdapter.java
 *
 *  Created:	Oct 27, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.control.adapter;

import java.util.ArrayList;

/**
 * 
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TestHeartbeatCommandAdapter implements CommandAdapter {

	
	public TestHeartbeatCommandAdapter() {}
	
	/**
	 * @see org.rifidi.emulator.reader.control.adapter.CommandAdapter#executeCommand(byte[])
	 */
	public ArrayList<Object> executeCommand(byte[] command) {
		/* The string to eventually return. */
		String retString = new String(command);

		if (retString.equalsIgnoreCase("heartbeat")) {
			retString = "Test Heartbeat\n";
		}
		
		ArrayList<Object> returnArray = new ArrayList<Object>();
		returnArray.add(retString);
		
		//return retString.getBytes();
		return null;
	}
	
}
