/*
 *  LLRPException.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.exception;

/**
 * This the generic exception class for any LLRP Module Exceptions.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class LLRPException extends Exception {

	/**
	 * Auto-generated serialVersionUID.
	 */
	private static final long serialVersionUID = -4275326872253278481L;

	/**
	 * 
	 */
	public LLRPException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public LLRPException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public LLRPException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LLRPException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
