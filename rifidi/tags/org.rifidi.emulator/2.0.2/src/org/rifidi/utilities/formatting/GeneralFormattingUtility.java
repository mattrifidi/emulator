/*
 *  GeneralFormattingUtility.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.utilities.formatting;

/**
 * @author Matthew Dean
 * 
 */
public class GeneralFormattingUtility {

	/**
	 * This method validates if a port number is valid
	 * 
	 * @param portnumber
	 *            The port number to be checked
	 * @return true if portnumber is a valid port number
	 */
	public static boolean isValidIPPort(int portnumber) {
		if (portnumber >= 0) {
			if (portnumber <= 65535) {
				return true;
			}
		}

		return false;

	}

}
