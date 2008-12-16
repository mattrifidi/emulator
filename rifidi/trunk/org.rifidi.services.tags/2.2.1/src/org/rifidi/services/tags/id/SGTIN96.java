/*
 *  DoD96.java
 *
 *  Created:	Mar 1, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.id;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class represents a SGTIN-96 tag. 8 bits header (value for this tagtype
 * is 30) 3 bits filter 3 bits partition 20-40 bits company prefix 24-4 bits
 * item reference 38 bits serial
 * 
 * the partition denotes the number of bits available in company prefix and in
 * item reference
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class SGTIN96 {

	public static final String header = "30";

	private static SecureRandom secureRandom = new SecureRandom();

	protected static byte[] getRandomTagData(String prefix) {
		BigInteger random = new BigInteger("300000000000000000000000", 16);
		random = random.or(new BigInteger(3, secureRandom).shiftLeft(85));
		BigInteger part = new BigInteger(3, secureRandom);
		if (part.intValue() > 6) {
			part = new BigInteger("6");
		}
		random = random.or(part.shiftLeft(82));
		switch (part.intValue()) {
		case 0:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 12) - 1)), 10).shiftLeft(42));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 1))), 10).shiftLeft(38));
			break;
		case 1:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 11) - 1)), 10).shiftLeft(45));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 2))), 10).shiftLeft(38));
			break;
		case 2:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 10) - 1)), 10).shiftLeft(48));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 3))), 10).shiftLeft(38));
			break;
		case 3:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 9) - 1)), 10).shiftLeft(52));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 4))), 10).shiftLeft(38));
			break;
		case 4:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 8) - 1)), 10).shiftLeft(55));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 5))), 10).shiftLeft(38));
			break;
		case 5:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 7) - 1)), 10).shiftLeft(58));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 6))), 10).shiftLeft(38));
			break;
		case 6:
			random = random.or(new BigInteger(Long.toString((long) Math
					.sqrt(Math.pow(secureRandom.nextLong(), 2))
					% ((long) Math.pow(10, 6) - 1)), 10).shiftLeft(62));
			random = random.or(new BigInteger(Integer.toString(secureRandom
					.nextInt((int) Math.pow(10, 7))), 10).shiftLeft(38));
			break;
		}
		random = random.or(new BigInteger(38, secureRandom));

		return random.toByteArray();
	}

	protected BigInteger parseEncoding(String encoding)
			throws NumberFormatException {
		if (!encoding.substring(0, 2).equals("30")) {
			throw new NumberFormatException("Not a SGTIN-96 Tag header.");
		}
		if (encoding.length() > 24) {
			throw new NumberFormatException("Tag data too long");
		}
		if (encoding.length() < 24) {
			throw new NumberFormatException("Tag data too short");
		}
		BigInteger temp = new BigInteger(encoding, 16);
		return temp;
	}

}
