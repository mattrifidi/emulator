/*
 *  IInitializer.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.initializer;

import org.rifidi.initializer.exceptions.InitializationException;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 14, 2008
 * 
 */
public interface IInitializer {
	/**
	 * Initialize the given object.
	 * 
	 * @param initializee
	 */
	public void init(Object initializee) throws InitializationException;
}
