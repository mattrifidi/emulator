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
package org.rifidi.emulator.tags.id;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class represents a DoD-96 Tag. 8 bits for header (value for this tagtype
 * is 2F) 4 bits for filter 48 bits for government manged id 36 bits for serial
 * example: 2F-1-1111-2222
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class DoD96 {

	public static final String header = "2F";
	private static SecureRandom secureRandom = new SecureRandom();

	protected static byte[] getRandomTagData(String prefix) {
		BigInteger random = new BigInteger("2F0000000000000000000000", 16);
		random = random.or(new BigInteger(4, secureRandom).shiftLeft(84));
		random = random.or(new BigInteger(48, secureRandom).shiftLeft(36));
		random = random.or(new BigInteger(36, secureRandom));
		return random.toByteArray();
	}

	protected BigInteger parseEncoding(String encoding)
			throws NumberFormatException {
		if (!encoding.substring(0, 2).equals("2F")) {
			throw new NumberFormatException("Not a DoD-96 Tag header.");
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
