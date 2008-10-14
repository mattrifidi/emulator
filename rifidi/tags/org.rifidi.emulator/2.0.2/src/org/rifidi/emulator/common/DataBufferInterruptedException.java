/*
 *  @(#)DataBufferInterruptedException.java
 *
 *  Created:	Oct 18, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.common;

/**
 * Thrown when a read from or write to the buffer is performed, but interrupted
 * before the operation could successfully complete.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class DataBufferInterruptedException extends Exception {

	/**
	 * The serial version for this class.
	 */
	private static final long serialVersionUID = -6230827654639439954L;

	/**
	 * The basic constructor, creates an exception with a null message.
	 */
	public DataBufferInterruptedException() {
		super();
	}

	/**
	 * Constructs a new DataBufferInterruptedException which has a detail
	 * message.
	 * 
	 * @param message
	 *            The detail message.
	 */
	public DataBufferInterruptedException(String message) {
		super(message);
	}

	/**
	 * Constructs a new DataBufferInterruptedException which as a detail message
	 * and a cause for the exception.
	 * 
	 * @param message
	 *            The detail message.
	 * @param cause
	 *            The cause for the exception.
	 */
	public DataBufferInterruptedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new DataBufferInterruptedException which has a cause for the
	 * exception and a message which indicates the cause.
	 * 
	 * @param cause
	 *            The cause for the exception.
	 */
	public DataBufferInterruptedException(Throwable cause) {
		super(cause);
	}

}
