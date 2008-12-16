/*
 *  RawCommandFormatter.java
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
 * This is a generic formatter which does not modify the incoming and outgoing strings at all.  
 * 
 * If encoding and decoding strings are not required, you can use this class.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class RawCommandFormatter implements CommandFormatter {
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#encode(java.lang.String)
	 */
	public ArrayList<Object> decode(byte[] arg) {
		ArrayList<Object> tempArrayList = new ArrayList<Object>();
		tempArrayList.add(arg);
		return tempArrayList;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(java.lang.String)
	 */
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		//String returnVal = (String)arg.get(0);
		return arg;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#getActualCommand(byte[])
	 */
	public String getActualCommand( byte[] arg ) {
		return new String(arg);
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#promptSuppress()
	 */
	public boolean promptSuppress() {
		return false;
	}
}
