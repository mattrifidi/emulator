/*
 *  _C1G2Read.java
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
import org.rifidi.services.tags.exceptions.InvalidMemoryAccessException;
import org.rifidi.services.tags.impl.C1G2Tag;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * This class performs the Read access operation on C1G2 tags
 * 
 * @author kyle
 *
 */
public class _C1G2Read implements _OpSpec {

	int OpSpecID;
	
	int memoryBank;
	
	short wordPtr;
	
	short wordCount;
	
	byte[] accessPassword;

	
	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(_C1G2Read.class);
	
	public _C1G2Read(int OpSpecID, int memoryBank, short wordPtr, short wordCount, int accessPassword){
		this.OpSpecID = OpSpecID;
		this.memoryBank = memoryBank;
		this.wordPtr = wordPtr;
		this.wordCount = wordCount;
		this.accessPassword = ByteAndHexConvertingUtility.intToByteArray(accessPassword, 4);
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.accessspec._OpSpec#performOperation(org.rifidi.tags.Gen1Tag)
	 */
	public _OpSpecResult performOperation(RifidiTag tag) {
		
		C1G2Tag c1g2tag = (C1G2Tag) tag.getTag();
		int result=0;
				
		byte[] bytes=null;
		
		try {
			logger.debug("Attempting to read " + wordCount + " words");
			bytes = C1G2Operations.C1G2ReadTagMem(c1g2tag, this.memoryBank, wordPtr, wordCount, accessPassword);
		} catch (InvalidMemoryAccessException e) {
			logger.debug("Read Error: " + e.getError());
			result=1;
		} catch (AuthenticationException e) {
			logger.debug("Read Error: " + e.getMessage());
			result=1;
		} 
		if(result!=1){
		logger.debug("C1G2read: " + ByteAndHexConvertingUtility.toHexString(bytes));
		}
		
		return new _C1G2ReadOpSpecResult(bytes, this.OpSpecID, result);
	}

}
