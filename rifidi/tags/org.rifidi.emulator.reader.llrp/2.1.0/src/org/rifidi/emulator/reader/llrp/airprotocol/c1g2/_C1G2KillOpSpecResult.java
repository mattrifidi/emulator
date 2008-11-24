/*
 *  _C1G2KillOpSpecResult.java
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

import edu.uark.csce.llrp.C1G2KillOpSpecResult;
import edu.uark.csce.llrp.C1G2OpSpecResult;


/**
 * @author kyle
 *
 */
public class _C1G2KillOpSpecResult implements _OpSpecResult {

	int opSpecID;
	
	int result;
	
	public _C1G2KillOpSpecResult(int opSpecID, int result){
		this.opSpecID = opSpecID;
		this.result = result;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.accessspec._OpSpecResult#getResult()
	 */
	public int getResult() {
		return result;
	}

	public C1G2OpSpecResult getLLRPTKResult() {
		C1G2KillOpSpecResult killResult = new C1G2KillOpSpecResult();
		killResult.setOpSpecID((short)opSpecID);
		killResult.setResult((byte)result);
		return killResult;
	}


}
