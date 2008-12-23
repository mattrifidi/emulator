/*
 *  RifidiTagNotAvailableException.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.exceptions;

/**
 * Thrown if a container tries to take a tag fom the service that isn't
 * available.
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 18, 2008
 * 
 */
public class RifidiTagNotAvailableException extends Exception {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that takes the id of the tag that is already taken.
	 * 
	 * @param arg0
	 */
	public RifidiTagNotAvailableException(String arg0) {
		super(arg0);
	}

}
