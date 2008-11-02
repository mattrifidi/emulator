/*
 *  CommandCreationExeption.java
 *
 *  Created:	August 12, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class CommandCreationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4075195828731702092L;

	public CommandCreationException() {
		// TODO Auto-generated constructor stub
	}

	public CommandCreationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CommandCreationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public CommandCreationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
