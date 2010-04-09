/*
 *  AwidCommandFormatter.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.formatter;

import java.util.ArrayList;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.formatter.CommandFormatter;

/**
 * The formatter for the Awid reader Calsulates the checksum going out, checks
 * the checksum coming in, and does little else.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidCommandFormatter implements CommandFormatter {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(AwidCommandFormatter.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	public ArrayList<Object> decode(byte[] arg) {

		// TODO: Check the checksum.

		ArrayList<Object> retVal = new ArrayList<Object>();
		byte[] newArg;
		if (arg.length >= 3) {
			newArg = new byte[arg.length - 2];
		} else {
			newArg = new byte[arg.length];
		}
		for (int i = 0; i < newArg.length; i++) {
			newArg[i] = arg[i];
		}
		retVal.add(bytesToHex(newArg));

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.formatter.CommandFormatter#encode(java.util
	 * .ArrayList)
	 */
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		// ArrayList that holds the return value
		ArrayList<Object> retVal = new ArrayList<Object>();

		// for each object in the return value
		for (Object obj : arg) {
			String str = (String) obj;
			// if the value is not an ACK Byte
			if (!str.equalsIgnoreCase("FF") && !str.equalsIgnoreCase("00")) {
				// remove any spaces
				String value = str.replace(" ", "");
				try {

					// convert the string to hex
					byte[] byteArray = Hex.decodeHex(value.toCharArray());

					// calculate the CRC
					int crc = crc16(byteArray);

					// create a new byte with two extra slots to hold the CRC
					byte[] returnByte = new byte[byteArray.length + 2];
					for (int i = 0; i < byteArray.length; i++) {
						returnByte[i] = (byte) byteArray[i];
					}

					// Add the CRC bytes
					byte[] crcBytes;
					if (crc > 4095) {
						crcBytes = fromHexString(Integer.toHexString(crc));
					} else {
						crcBytes = fromHexString("0" + Integer.toHexString(crc));
					}
					returnByte[byteArray.length] = crcBytes[0];
					returnByte[byteArray.length + 1] = crcBytes[1];

					// add the byte message
					retVal.add(returnByte);
				} catch (DecoderException ex) {

				}
			} else {
				// if the byte is an ACK byte, just add it
				if (str.equalsIgnoreCase("FF")) {
					retVal.add(new byte[] { (byte) 0xFF });
				} else {
					retVal.add(new byte[] { (byte) 0x00 });
				}
			}
		}
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.formatter.CommandFormatter#getActualCommand
	 * (byte[])
	 */
	public String getActualCommand(byte[] arg) {
		return new String(arg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.formatter.CommandFormatter#promptSuppress()
	 */
	public boolean promptSuppress() {
		return false;
	}

	/*
	 * Convenience method to convert a byte to a hex string. Code taken from a
	 * forum post at:
	 * http://forum.java.sun.com/thread.jspa?threadID=252591&messageID=2272668
	 * by user austinplatt.
	 * 
	 * @param data the byte to convert @return String the converted byte
	 */
	private static String byteToHex(byte data) {
		StringBuffer buf = new StringBuffer();
		buf.append(toHexChar((data >>> 4) & 0x0F));
		buf.append(toHexChar(data & 0x0F));
		return buf.toString();
	}

	/*
	 * Convenience method to convert a byte array to a hex string. Code taken
	 * from a forum post at:
	 * http://forum.java.sun.com/thread.jspa?threadID=252591&messageID=2272668
	 * by user austinplatt.
	 * 
	 * @param data the byte[] to convert @return String the converted byte[]
	 */
	private static String bytesToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			buf.append(byteToHex(data[i]));
			if (i < data.length - 1) {
				buf.append("_");
			}
		}
		return buf.toString();
	}

	/**
	 * Convenience method to convert an int to a hex char. Code taken from a
	 * forum post at:
	 * http://forum.java.sun.com/thread.jspa?threadID=252591&messageID=2272668
	 * by user austinplatt.
	 * 
	 * @param i
	 *            the int to convert
	 * @return char the converted char
	 */
	private static char toHexChar(int i) {
		if ((0 <= i) && (i <= 9))
			return (char) ('0' + i);
		else
			return (char) ('A' + (i - 10));
	}

	/*
	 * generator polynomial
	 */
	private static final int poly = 0x1021;

	/*
	 * scrambler lookup table for fast computation.
	 */
	private static int[] crcTable = new int[256];
	static {
		// initialise scrambler table
		for (int i = 0; i < 256; i++) {
			int fcs = 0;
			int d = i << 8;
			for (int k = 0; k < 8; k++) {
				if (((fcs ^ d) & 0x8000) != 0) {
					fcs = (fcs << 1) ^ poly;
				} else {
					fcs = (fcs << 1);
				}
				d <<= 1;
				fcs &= 0xffff;
			}
			crcTable[i] = fcs;
		}
	}

	/*
	 * Calc CRC with CCITT method. Note, method previously posted was a
	 * proprietary 16-bit CRC.
	 * 
	 * Method taken from http://mindprod.com/jgloss/crc.html
	 * 
	 * @param b byte array to compute CRC on
	 * 
	 * @return 16-bit CRC, unsigned
	 */
	private static int crc16(byte[] b) {
		// loop, calculating CRC for each byte of the string
		int work = 0xffff;
		for (int i = 0; i < b.length; i++) {
			// xor the next data byte with the high byte of what we have so
			// far to
			// look up the scrambler.
			// xor that with the low byte of what we have so far.
			// Mask back to 16 bits.
			work = (crcTable[(b[i] ^ (work >>> 8)) & 0xff] ^ (work << 8)) & 0xffff;
		}
		return work;
	}

	/*
	 * Convert a hex string to a byte array. Permits upper or lower case hex.
	 * 
	 * @param s String must have even number of characters. and be formed only
	 * of digits 0-9 A-F or a-f. No spaces, minus or plus signs. @return
	 * corresponding byte array.
	 */
	private static byte[] fromHexString(String s) {
		int stringLength = s.length();
		logger.debug("s.length = " + stringLength);
		if ((stringLength & 0x1) != 0) {
			throw new IllegalArgumentException(
					"Return value bad.  Check return value.");
		}
		byte[] b = new byte[stringLength / 2];

		for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
			int high = charToNibble(s.charAt(i));
			int low = charToNibble(s.charAt(i + 1));
			b[j] = (byte) ((high << 4) | low);
		}
		return b;
	}

	/*
	 * Alternate implementation of charToNibble using a precalculated array.
	 * Based on code by: Brian Marquis Orion Group Software Engineers
	 * http://www.ogse.com
	 * 
	 * convert a single char to corresponding nibble.
	 * 
	 * @param c char to convert. must be 0-9 a-f A-F, no spaces, plus or minus
	 * signs.
	 * 
	 * @return corresponding integer @throws IllegalArgumentException on invalid
	 * c. @throws ArrayIndexOutOfBoundsException on invalid c
	 */
	private static int charToNibble(char c) {
		int nibble = correspondingNibble[c];
		if (nibble < 0) {
			throw new IllegalArgumentException("Invalid hex character: " + c);
		}
		return nibble;
	}

	private static byte[] correspondingNibble = new byte['f' + 1];

	static {
		// only 0..9 A..F a..f have meaning. rest are errors.
		for (int i = 0; i <= 'f'; i++) {
			correspondingNibble[i] = -1;
		}
		for (int i = '0'; i <= '9'; i++) {
			correspondingNibble[i] = (byte) (i - '0');
		}
		for (int i = 'A'; i <= 'F'; i++) {
			correspondingNibble[i] = (byte) (i - 'A' + 10);
		}
		for (int i = 'a'; i <= 'f'; i++) {
			correspondingNibble[i] = (byte) (i - 'a' + 10);
		}
	}
}
