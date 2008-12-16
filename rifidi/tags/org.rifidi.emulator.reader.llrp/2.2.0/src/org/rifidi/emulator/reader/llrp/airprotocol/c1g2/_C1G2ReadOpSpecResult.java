/*
 *  _C1G2ReadOpSpecResult.java
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
import edu.uark.csce.llrp.C1G2ReadOpSpecResult;

/**
 * @author kyle
 * 
 */
public class _C1G2ReadOpSpecResult implements _OpSpecResult {

	short[] readData;

	int opSpecID;

	int result;

	public _C1G2ReadOpSpecResult(byte[] readData, int opSpecID, int result) {
		this.opSpecID = opSpecID;
		this.result = result;
		if (readData != null) {
			this.readData = new short[readData.length];
			for (int i = 0; i < readData.length; i++) {
				this.readData[i] = (short) readData[i];
			}
		}else{
			this.readData = new short[0];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.llrp.accessspec._OpSpecResult#getResult()
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @return the opSpecID
	 */
	public int getOpSpecID() {
		return opSpecID;
	}

	/**
	 * @return the readData
	 */
	public short[] getReadData() {
		return readData;
	}

	public C1G2OpSpecResult getLLRPTKResult() {
		C1G2ReadOpSpecResult rosr = new C1G2ReadOpSpecResult();
		rosr.setOpSpecID((short) opSpecID);
		rosr.setResult((byte) result);
		for (int i = 0; i < readData.length; i++) {
			rosr.addReadDataElement(readData[i]);
		}
		return rosr;
	}

}
