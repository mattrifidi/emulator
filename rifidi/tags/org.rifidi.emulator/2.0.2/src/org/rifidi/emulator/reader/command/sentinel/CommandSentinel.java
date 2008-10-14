/*
 *  CommandSentinel.java
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
 * Interface for CommandSentinel Objects.  The Sentinel is a class
 * that will intercept an incoming command before it gets to the adapter 
 * and break it up into several commands that will be called in a certain
 * sequence.  This is an effective way to stagger output of a function if 
 * a reader requires some output happen before the rest (such as the echoing
 * of a command happening before a time-consuming tag read takes place).  
 *
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public interface CommandSentinel {
	/**
	 * Does the Sentinel object contain the command requested?
	 * 
	 * @param arg	
	 * 				The command that is requested
	 * @return
	 * 				True if the sentinel contains the command, 
	 * 				false if it does not
	 */
	public boolean containsCommand(byte[] arg);
	
	/**
	 * Returns the list of input commands mapped to an arrayList of the output commands.  
	 * 
	 * @return
	 */
	public ArrayList<byte[]> getCommandList(byte[] arg);
}
