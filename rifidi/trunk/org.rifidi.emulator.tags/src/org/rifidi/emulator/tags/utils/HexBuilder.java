/*
 *  HexBuilder.java
 *
 *  Created:	Mar 3, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.tags.utils;

import java.math.BigInteger;

/**
 * A little helperclass used to assemble the hexencoded tag header.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class HexBuilder {
	private BigInteger bigInt;

	private BigInteger dummy;

	/**
	 * Constructo used to creat a hexbuilder that is able to hold a number with
	 * the given number of bits.
	 * 
	 * @param size
	 *            number of bits
	 */
	public HexBuilder(int size) {
		bigInt = new BigInteger("0");
		// we want to keep the leading 0s
		bigInt = (new BigInteger("F", 16)).shiftLeft(96);
	}

	/**
	 * give a hexencoded number, sift it to the left by the given number and or
	 * it to our header.
	 * 
	 * @param value
	 *            value of our number as a hexencoded string
	 * @param shift
	 *            value for the leftshift
	 */
	public void put(String value, int shift) {
		dummy = new BigInteger(value, 16);
		dummy = dummy.shiftLeft(shift);
		bigInt = bigInt.or(dummy);
	}

	public String toString() {
		return bigInt.toString(16).substring(1);
	}
}
