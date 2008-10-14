/*
 *  @(#)CommandControllerException.java
 *
 *  Created:	Oct 25, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller;

/**
 * This is an exception which indicates something went wrong while using a
 * CommandController. This could be an when an illegal operation occurs, such as
 * trying to process a command when the controller is off.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class CommandControllerException extends Exception {

	/**
	 * The serial version ID for this class.
	 */
	private static final long serialVersionUID = 7639628657185622050L;

	/**
	 * The basic constructor, creates an exception with a null message.
	 */
	public CommandControllerException() {
		super();
	}

	/**
	 * Constructs a new CommandControllerException which has a detail message.
	 * 
	 * @param message
	 *            The detail message.
	 */
	public CommandControllerException(String message) {
		super(message);
	}

	/**
	 * Constructs a new CommandControllerException which as a detail message and
	 * a cause for the exception.
	 * 
	 * @param message
	 *            The detail message.
	 * @param cause
	 *            The cause for the exception.
	 */
	public CommandControllerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new CommandControllerException which has a cause for the
	 * exception and a message which indicates the cause.
	 * 
	 * @param cause
	 *            The cause for the exception.
	 */
	public CommandControllerException(Throwable cause) {
		super(cause);
	}

}
