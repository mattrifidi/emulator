/*
 *  CommandFormatter.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.formatter;

import java.util.ArrayList;

/**
 * This interface defines the methods needed for encoding and decoding the
 * messages sent to and from a reader. <br />
 * Since different readers exchange information is different formats, a
 * translating class must be written.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public interface CommandFormatter {
	/**
	 * Decode a string in a way the Reader can understand.
	 * 
	 * @param arg
	 *            The string to encode
	 * @return The encoded String
	 */
	public ArrayList<Object> decode(byte[] arg);

	/**
	 * Encode a string in such a way that the Reader can understand.
	 * 
	 * @param arg
	 *            The string to decode
	 * @return The decoded String
	 */
	public ArrayList<Object> encode(ArrayList<Object> arg);

	/**
	 * Tells whether or not the prompt will be supressed at the next command.
	 * Needed for readers such as the Alien AL9800, which has such an option.
	 * 
	 * @return True if the prompt is to be supressed at the next prompt, false
	 *         otherwise.
	 */
	public boolean promptSuppress();

	/**
	 * Returns the actual command typed in by the user, such as "set
	 * PersistTime".
	 * 
	 * @return The actual command typed in by the user.
	 */
	public String getActualCommand(byte[] arg);
}
