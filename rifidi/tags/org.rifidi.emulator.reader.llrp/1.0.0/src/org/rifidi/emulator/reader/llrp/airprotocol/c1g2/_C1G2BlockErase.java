/*
 *  _C1G2BlockErase.java
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

import org.rifidi.emulator.reader.llrp.accessspec._OpSpec;
import org.rifidi.emulator.reader.llrp.accessspec._OpSpecResult;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * This class performs the BlockErase access operation on C1G2 Tags
 * 
 * @author kyle
 *
 */
public class _C1G2BlockErase implements _OpSpec {

	int opSpecID;
	
	int memoryBank;
	
	short wordPtr;
	
	short wordCount;
	
	int accessPassword;
	
	public _C1G2BlockErase(int OpSpecID, int memoryBank, short wordPtr, short wordCount, int accessPassword){
		this.opSpecID = OpSpecID;
		this.memoryBank = memoryBank;
		this.wordPtr = wordPtr;
		this.wordCount = wordCount;
		this.accessPassword = accessPassword;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.accessspec._OpSpec#performOperation(org.rifidi.tags.Gen1Tag)
	 */
	public _OpSpecResult performOperation(RifidiTag tag) {
		// TODO Auto-generated method stub
		return new _C1G2BlockEraseOpSpecResult();
	}

}
