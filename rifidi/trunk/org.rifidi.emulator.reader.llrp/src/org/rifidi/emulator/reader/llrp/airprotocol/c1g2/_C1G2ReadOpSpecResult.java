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
		this.readData = new short[readData.length / 2];
		if (readData != null) {
			
			//need to convert byte array into a short array
			int j = 0;
			for (int i = 0; i < readData.length; i = i + 2) {
				Byte b1 = new Byte(readData[i]);
				Byte b2 = new Byte(readData[i + 1]);
				this.readData[j] = unsignedByteToShort(b1, b2);
				j++;
			}
		} else {
			this.readData = new short[0];
		}
	}

	/**
	 * Helper method to convert two bytes to a short
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	private short unsignedByteToShort(byte b1, byte b2) {
		short s = (short) ((unsignedByteToShort(b1) << 8) + (unsignedByteToShort(b2)));
		return s;
	}

	/**
	 * helper method to convert a byte to a short
	 * 
	 * @param b1
	 * @return
	 */
	private static short unsignedByteToShort(byte b1) {
		return (short) (b1 & 0xFF);
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
