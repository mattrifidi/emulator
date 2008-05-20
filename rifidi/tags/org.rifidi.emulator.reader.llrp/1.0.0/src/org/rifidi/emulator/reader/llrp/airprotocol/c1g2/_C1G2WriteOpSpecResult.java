/*
 *  _C1G2WriteOpSpecResult.java
 *
 *  Created:	Oct 11, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

import org.rifidi.emulator.reader.llrp.accessspec._OpSpecResult;

import edu.uark.csce.llrp.C1G2OpSpecResult;
import edu.uark.csce.llrp.C1G2WriteOpSpecResult;

/**
 * @author kyle
 *
 */
public class _C1G2WriteOpSpecResult implements _OpSpecResult {

	int opspecID;
	
	short numWordsWritten;
	
	int result;
	
	/**
	 * 
	 * @param opSpecID The OPSecID of the opspec that ran this operation
	 * @param numWordsWritten The number of words written to the memory.
	 * @param result The result of this operation.  0 is success
	 */
	public _C1G2WriteOpSpecResult(int opSpecID, short numWordsWritten, int result){
		this.opspecID = opSpecID;
		this.numWordsWritten = numWordsWritten;
		this.result = result;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.accessspec._OpSpecResult#getResult()
	 */
	public int getResult() {
		return result;
	}

	public C1G2OpSpecResult getLLRPTKResult() {
		C1G2WriteOpSpecResult wosr = new C1G2WriteOpSpecResult();
		wosr.setNumWordsWritten(numWordsWritten);
		wosr.setOpSpecID((short)opspecID);
		wosr.setResult((byte)result);
		return wosr;
	}

}
