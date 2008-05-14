/*
 *  InitializationException.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.initializer.exceptions;

/**
 * Exception thrown if something went wrong while initializing an object.
 * 
 * @author Jochen Mader - jochen@pramari.com - May 14, 2008
 * 
 */
public class InitializationException extends Exception {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public InitializationException() {
	}

	/**
	 * @param arg0
	 */
	public InitializationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public InitializationException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public InitializationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
