/**
 * 
 */
package org.rifidi.emulator.reader.llrp.util;

import org.rifidi.common.utilities.ByteAndHexConvertingUtility;

/**
 * @author kyle
 * 
 */
public class LLRPUtilities {

	public static boolean checkMessageVersion(byte[] rawMsg, int version){
		byte b = rawMsg[0];
		int v = (b & 28) >> 2;
		if(v == version)
			return true;
		else return false;
	}
	
	public static int calculateMessageNumber(byte[] rawMsg) {
		int highOrderbits = (rawMsg[0] & 0x03)<<8;
		int lowOrderBits = ByteAndHexConvertingUtility.unsignedByteToInt(rawMsg[1]);
		return highOrderbits + lowOrderBits;
	}

	public static int calculateLLRPMessageLength(byte[] bytes)
			throws IllegalArgumentException {
		long msgLength = 0;
		int num1 = 0;
		int num2 = 0;
		int num3 = 0;
		int num4 = 0;

		num1 = ((ByteAndHexConvertingUtility.unsignedByteToInt(bytes[2])));
		num1 = num1 << 32;
		if (num1 > 127) {
			throw new RuntimeException(
					"Cannot construct a message greater than "
							+ "2147483647 bytes (2^31 - 1), due to the fact that there are "
							+ "no unsigned ints in java");
		}

		num2 = ((ByteAndHexConvertingUtility.unsignedByteToInt(bytes[3])));
		num2 = num2 << 16;

		num3 = ((ByteAndHexConvertingUtility.unsignedByteToInt(bytes[4])));
		num3 = num3 << 8;

		num4 = (ByteAndHexConvertingUtility.unsignedByteToInt(bytes[5]));

		msgLength = num1 + num2 + num3 + num4;

		if (msgLength < 0) {
			throw new IllegalArgumentException(
					"LLRP message length is less than 0");
		} else {
			return (int) msgLength;
		}
	}

}
