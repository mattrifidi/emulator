/*
 *  DuplicateReaderException.java
 *
 *  Created:	Mar 2, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.wizards.reader.exceptions;

/**
 * 
 * Thrown if someone tries to add a reader to the registry that already exists.
 * 
 * @author Prasith Govin - prasith@pramari.com
 * 
 */
public class DuplicateReaderException extends Exception {

	/**
	 * Constructor
	 *
	 */
	public DuplicateReaderException() {
		super();
	}

	/**
	 * Constructor that takes a message as its argument
	 * @param message
	 */
	public DuplicateReaderException(String message) {
		super(message);
	}

	/**
	 * Constructor that takes a throwable as its argument
	 * @param throwable
	 */
	public DuplicateReaderException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

}
