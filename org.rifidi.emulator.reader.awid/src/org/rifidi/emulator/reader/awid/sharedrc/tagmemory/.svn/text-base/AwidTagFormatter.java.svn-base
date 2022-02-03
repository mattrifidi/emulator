/*
 *  AwidTagFormatter.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.sharedrc.tagmemory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.tags.impl.RifidiTag;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidTagFormatter {

	/**
	 * The logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(AwidTagFormatter.class);
	
	
	/**
	 * Formats the tag in the desired way.  A sample tag will look like this:
	 * 
	 * 15 20 00 30 00 30 00 21 41 60 C0 04 00 00 0A 0F A8 A8 FE
	 * 
	 * 
	 * 
	 * @param genTag
	 * @param regex
	 * @return
	 */
	public String formatTag(RifidiTag genTag, String regex) {
		String retVal = "";
		
		retVal = byteToTagString(genTag.getTag().readId());
				
		return retVal;
	}
	
	
	private String byteToTagString(byte[] arg) {
		String retVal = "";
		for(byte a : arg) {
			if( a>16 ) {
				retVal += ( Integer.toHexString(a) + " ");
			} else {
				retVal += ( "0" + Integer.toHexString(a) + " ");
			}
		}
		
		//get rid of the trailing space
		retVal = retVal.substring(0, retVal.length()-1);
		
		logger.debug("Tag = " + retVal);
		
		return retVal;
	}
}
