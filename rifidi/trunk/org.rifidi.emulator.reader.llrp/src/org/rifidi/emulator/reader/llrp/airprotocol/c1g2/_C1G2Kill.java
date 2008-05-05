/*
 *  _C1G2Kill.java
 *
 *  Created:	Oct 10, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

import javax.naming.AuthenticationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.llrp.accessspec._OpSpec;
import org.rifidi.emulator.reader.llrp.accessspec._OpSpecResult;
import org.rifidi.emulator.reader.sharedrc.radio.C1G2Operations;
import org.rifidi.emulator.tags.impl.C1G2Tag;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * 
 * This class performs the Kill access operation on C1G2 tags
 * 
 * @author kyle
 *
 */
public class _C1G2Kill implements _OpSpec {

	private short OpSpecID;
	
	byte[] killPassword;
	
	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(_C1G2Kill.class);
	
	public _C1G2Kill(short OpSpecID, int killPassword){
		this.OpSpecID = OpSpecID;
		this.killPassword = ByteAndHexConvertingUtility.intToByteArray(
				killPassword, 4);

	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.accessspec._OpSpec#performOperation(org.rifidi.tags.Gen1Tag)
	 */
	public _OpSpecResult performOperation(RifidiTag tag) {
		
		C1G2Tag c1g2tag = (C1G2Tag) tag.getTag();
		
		int result = 0;
		
		try {
			logger.debug("Attempting to kill tag");
			C1G2Operations.C1G2KillTag(c1g2tag, killPassword);
		} catch (AuthenticationException e) {
			result = 1;
			logger.debug("Write error: " + e.getMessage());
		}
		
		return new _C1G2KillOpSpecResult(this.OpSpecID, result);
	}

}
