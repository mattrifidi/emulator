/*
 *  RawCommandSentinel.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.command.sentinel;

import java.util.ArrayList;

/**
 * Raw sentinel - always returns false for containsCommand.  This is used 
 * if no commands need to be intercepted by the sentinel.  It is the default
 * sentinel of the InteractiveCommandController, and is changed only if 
 * specifically set.  
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class RawCommandSentinel implements CommandSentinel {
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.sentinel.CommandSentinel#containsCommand(byte[])
	 */
	public boolean containsCommand(byte[] arg) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.sentinel.CommandSentinel#getCommandList(byte[])
	 */
	public ArrayList<byte[]> getCommandList(byte[] arg) {
		return null;
	}
}
