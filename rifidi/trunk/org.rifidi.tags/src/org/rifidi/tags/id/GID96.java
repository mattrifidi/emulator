/*
 *  GID96.java
 *
 *  Created:	Mar 6, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.tags.id;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class represents a GID-96 Tag. 8 bits for header (value for this tagtype
 * is 35) 28 bits for general manager id 24 bits for object class 36 bits for
 * serial example: 2F-2222-1111-2222
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class GID96 {

	public static final String header = "35";

	private static SecureRandom secureRandom = new SecureRandom();

	protected static byte[] getRandomTagData(String prefix) {
		BigInteger random = new BigInteger("350000000000000000000000", 16);
		random = random.or(new BigInteger(28, secureRandom).shiftLeft(60));
		random = random.or(new BigInteger(24, secureRandom).shiftLeft(36));
		random = random.or(new BigInteger(36, secureRandom));
		return (random.toByteArray());
	}

	protected BigInteger parseEncoding(String encoding)
			throws NumberFormatException {
		if (!encoding.substring(0, 2).equals("35")) {
			throw new NumberFormatException("Not a GID-96 Tag header.");
		}
		if (encoding.length() > 24) {
			throw new NumberFormatException("Tag data too long");
		}
		if (encoding.length() < 24) {
			throw new NumberFormatException("Tag data too short");
		}
		BigInteger temp = new BigInteger(encoding, 16);
		temp.and(new BigInteger("00FFFFFFF000000000000000", 16)
				.shiftRight(15 * 4));
		temp.and(new BigInteger("000000000FFFFFF000000000", 16)
				.shiftRight(9 * 4));
		temp.and(new BigInteger("000000000000000FFFFFFFFF", 16));
		return temp;
	}
}
