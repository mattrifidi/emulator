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
public class CommandCreationExeption extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4075195828731702092L;

	public CommandCreationExeption() {
		// TODO Auto-generated constructor stub
	}

	public CommandCreationExeption(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CommandCreationExeption(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public CommandCreationExeption(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
