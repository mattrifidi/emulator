/*
 *  @(#)CommunicationException.java
 *
 *  Created:	May 03, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm;

/**
 * This is an exception which indicates something went wrong while using the
 * Communication class. For example, this may be went a connection is
 * interrupted, or cannot be established.
 * 
 * @author John Olender
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class CommunicationException extends Exception {

	/**
	 * The serial verision for this (for object reading/writing).
	 */
	private static final long serialVersionUID = 5235457053023047925L;

	/**
	 * The basic constructor, creates an exception with a null message.
	 */
	public CommunicationException() {
		super();
	}

	/**
	 * Constructs a new CommunicationException which has a detail message.
	 * 
	 * @param message
	 *            The detail message.
	 */
	public CommunicationException(String message) {
		super(message);
	}

	/**
	 * Constructs a new CommunicationException which as a detail message and a
	 * cause for the exception.
	 * 
	 * @param message
	 *            The detail message.
	 * @param cause
	 *            The cause for the exception.
	 */
	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new CommunicationException which has a cause for the
	 * exception and a message which indicates the cause.
	 * 
	 * @param cause
	 *            The cause for the exception.
	 */
	public CommunicationException(Throwable cause) {
		super(cause);
	}

}
