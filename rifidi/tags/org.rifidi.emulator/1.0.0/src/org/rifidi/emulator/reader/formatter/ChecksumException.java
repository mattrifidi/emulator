/*
 *  ChecksumException.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.formatter;

/**
 * Exception for a violation of the checksum.  
 *
 * @author Matthew Dean
 * @since  <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class ChecksumException extends Exception {

	private static final long serialVersionUID = 7639628657145622052L;
	
	/**
	 * 
	 */
	public ChecksumException() {
	}

	/**
	 * @param arg0
	 */
	public ChecksumException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public ChecksumException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ChecksumException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
