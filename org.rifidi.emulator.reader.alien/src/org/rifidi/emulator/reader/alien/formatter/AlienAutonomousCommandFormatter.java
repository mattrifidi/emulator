/*
 *  AlienAutonomousCommandFormatter.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.formatter;

import java.util.ArrayList;

import org.rifidi.emulator.reader.formatter.CommandFormatter;

/**
 * @author matt
 *
 */
public class AlienAutonomousCommandFormatter implements CommandFormatter {

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	public ArrayList<Object> decode(byte[] arg) {
		ArrayList<Object> retVal = new ArrayList<Object>();
		retVal.add(new String(arg));
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
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#promptSuppress()
	 */
	public boolean promptSuppress() {
		return false;
	}

}
