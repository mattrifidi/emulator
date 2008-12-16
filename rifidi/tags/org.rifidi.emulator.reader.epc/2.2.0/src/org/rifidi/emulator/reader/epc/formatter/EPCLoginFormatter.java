/*
 *  EPCLoginFormatter.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.epc.formatter;

import java.util.ArrayList;

import org.rifidi.emulator.reader.formatter.CommandFormatter;

/**
 *
 *
 * @author Matthew Dean - matt@pramari.com
 *
 */
public class EPCLoginFormatter implements CommandFormatter {

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	public ArrayList<Object> decode(byte[] arg) {
		ArrayList<Object> retVal = new ArrayList<Object>();
		String retString = new String(arg);
		String[] spaceString = retString.split(" ");
		for(String i:spaceString) {
			retVal.add(i);
		}
		return retVal;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#encode(java.util.ArrayList)
	 */
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		return arg;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#getActualCommand(byte[])
	 */
	public String getActualCommand(byte[] arg) {
		return new String(arg);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#promptSuppress()
	 */
	public boolean promptSuppress() {
		return false;
	}

}
