/*
 *  GPI.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;


/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 27, 2008
 * 
 */
public interface GPI {

	/**
	 * The given pin is set to low.
	 * 
	 * @param portNum
	 */
	void setLow(int portNum);

	/**
	 * The given pin is set to high.
	 * 
	 * @param portNum
	 */
	void setHigh(int portNum);
	
	/**
	 * Enable/Disable GPI for the entity.
	 * @param enablement
	 */
	void enableGPI(boolean enablement);
}
