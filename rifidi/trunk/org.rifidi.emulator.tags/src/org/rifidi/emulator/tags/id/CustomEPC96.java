/**
 * 
 */
package org.rifidi.emulator.tags.id;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.rifidi.common.utilities.ByteAndHexConvertingUtility;

/**
 * This is a basic Class representing a more customizable Tag
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class CustomEPC96 {

	public static String header = "00";

	private static SecureRandom secureRandom = new SecureRandom();

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.id.ID#getRandomTagData(java.lang.String)
	 */
	public static byte[] getRandomTagData(String prefix) {

		if(prefix == null)
		{
			prefix = "";
		}
		BigInteger random;
		// if the header is 0, we must generate a header to be used because
		// otherwise random will be 0 after the first step
		//logger.debug("Prefix is " + prefix);
		BigInteger headerInt = new BigInteger(prefix, 16);
		if (headerInt.equals(BigInteger.ZERO)) {
			BigInteger tempInt = new BigInteger(4, secureRandom);
			tempInt = tempInt.mod(new BigInteger("15")).add(BigInteger.ONE);
			String tempHeader = tempInt.toString(16);
			random = new BigInteger(tempHeader + "00000000000000000000000", 16);
		} else {
			random = new BigInteger(prefix + "00000000000000000000000", 16);
		}
		random = random.or(new BigInteger(4, secureRandom).shiftLeft(84));
		random = random.or(new BigInteger(48, secureRandom).shiftLeft(36));
		random = random.or(new BigInteger(36, secureRandom));
		// prepend 0s

		String tagData = random.toString(16).substring(0, 24);
		char[] headerArray = prefix.toCharArray();
		int index = 0;
		while (index < headerArray.length && headerArray[index] == '0') {
			tagData = 0 + tagData;
			index++;
		}
		tagData = tagData.substring(0, 24);
		return ByteAndHexConvertingUtility.fromHexString(tagData);
	}


	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.id.ID#parseEncoding(java.lang.String)
	 */
	public BigInteger parseEncoding(String hexEncoding)
			throws NumberFormatException {
		if (hexEncoding.length() > 24) {
			throw new NumberFormatException("Tag data too long");
		}
		if (hexEncoding.length() < 24) {
			throw new NumberFormatException("Tag data too short");
		}
		BigInteger temp = new BigInteger(hexEncoding, 16);
		return temp;
	}

}
