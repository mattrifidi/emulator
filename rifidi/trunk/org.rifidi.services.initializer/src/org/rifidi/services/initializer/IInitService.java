/*
 *  IInitService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.initializer;

import org.rifidi.services.initializer.exceptions.InitializationException;

/**
 * Service interface. The service defined in here is used to initialize the
 * given objects. Look at the org.rifidi.services.initializer extension point.
 * 
 * @author Jochen Mader - jochen@pramari.com - May 14, 2008
 * 
 */
public interface IInitService {
	/**
	 * Try to initialize the given object.
	 * 
	 * @param initializee
	 * @throws InitializationException
	 */
	public void init(Object initializee) throws InitializationException;
}
