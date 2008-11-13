/*
 *  GPIOTest.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.gpio;

import java.util.List;

/**
 * Defines the methoids required for a General Purpose Input Entity.
 * 
 * @author Jochen Mader - jochen@pramari.com - Nov 4, 2008
 * 
 */
public interface GPIO {
	/**
	 * Get all available GPOPorts.
	 * @return
	 */
	public List<GPOPort> getGPOPorts();
	/**
	 * Get all available GPIPorts.
	 * @return
	 */
	public List<GPIPort> getGPIPorts();
}
