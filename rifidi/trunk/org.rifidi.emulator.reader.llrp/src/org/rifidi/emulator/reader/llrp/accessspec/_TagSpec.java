/*
 *  _TagSpec.java
 *
 *  Created:	Oct 9, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.llrp.accessspec;

import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * @author kyle
 * 
 */
public interface _TagSpec {

	/**
	 * Returns true if this tag matches this filter
	 * 
	 * @param tag
	 *            The Tag to test. 
	 * @return
	 */
	public boolean matchTag(RifidiTag tag);

}
