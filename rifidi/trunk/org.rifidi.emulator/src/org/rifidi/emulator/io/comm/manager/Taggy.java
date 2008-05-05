/*
 *  Taggy.java
 *
 *  Created:	Mar 14, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.manager;

/**
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class Taggy {
	public Boolean addTag(String antennaName, String tag, String generation){
		AntennaRegistry.getInstance().addTag(antennaName, tag, generation);
		return true;
	}
	
	public Boolean removeTag(String antennaName, String tag){
		AntennaRegistry.getInstance().removeTag(antennaName, tag);
		return true;
	}
	
	public Boolean purgeTags(String antennaName){
		AntennaRegistry.getInstance().purgeTags(antennaName);
		return true;
	}
}
