/*
 *  _C1G2Lock.java
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

import java.util.ArrayList;

import javax.naming.AuthenticationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.accessspec._OpSpec;
import org.rifidi.emulator.reader.llrp.accessspec._OpSpecResult;
import org.rifidi.emulator.tags.exceptions.InvalidMemoryAccessException;
import org.rifidi.emulator.tags.impl.C1G2Tag;
import org.rifidi.emulator.tags.impl.RifidiTag;
import org.rifidi.emulator.tags.utils.C1G2Operations;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

/**
 * 
 * This class performs the Lock access operation on C1G2 tags
 * 
 * @author kyle
 *
 */
public class _C1G2Lock implements _OpSpec {

	private int OpSpecID;
	
	private ArrayList<_C1G2LockPayload>LockPayloadList;
	
	private byte[] accessPassword;
	
	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(_C1G2Lock.class);
	
	
	public _C1G2Lock(int OpSpecID, ArrayList<_C1G2LockPayload>LockPayloadList, int accessPassword){
		this.OpSpecID = OpSpecID;
		this.LockPayloadList = LockPayloadList;
		this.accessPassword = ByteAndHexConvertingUtility.intToByteArray(accessPassword,4);
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.accessspec._OpSpec#performOperation(org.rifidi.tags.Gen1Tag)
	 */
	public _OpSpecResult performOperation(RifidiTag tag) {
		C1G2Tag c1g2tag = (C1G2Tag) tag.getTag();
		int result = 0;
		for(_C1G2LockPayload p : LockPayloadList){
			try {
				logger.debug("Attempting to Lock tag");
				C1G2Operations.C1G2LockTag(c1g2tag,p.getData() , this.accessPassword, p.getPrivilege());
			} catch (AuthenticationException e) {
				logger.debug("c1g2lock error: " + e.getMessage());
				result = 1;
			} catch (InvalidMemoryAccessException e) {
				logger.debug("c1g2lock error: " + e.getError());
				result = 1;
			}
		}
		return new _C1G2LockOpSpecResult(this.OpSpecID, result);
	}

}
