/**
 * 
 */
package org.rifidi.emulator.reader.awid.formatter;

import java.util.ArrayList;

import org.rifidi.emulator.reader.formatter.CommandFormatter;

/**
 * Formatter specifically for the AWID welcome message.  
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class AwidWelcomeCommandFormatter implements CommandFormatter {

	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	@Override
	public ArrayList<Object> decode(byte[] arg) {
		ArrayList<Object> tempArrayList = new ArrayList<Object>();
		tempArrayList.add(new String(arg));
		return tempArrayList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#encode(java.util.ArrayList)
	 */
	@Override
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		// ArrayList that holds the return value
		ArrayList<Object> retVal = new ArrayList<Object>();
		String[] strArray = ((String)(arg.get(0))).split(" ");
		String value = "";
		for (int i = 0; i < strArray.length; i++) {
			value += strArray[i];
		}
		byte[] byteArray = fromHexString(value);
		
		retVal.add(new String(byteArray));
		
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#getActualCommand(byte[])
	 */
	@Override
	public String getActualCommand(byte[] arg) {
		return new String(arg);
	}

	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#promptSuppress()
	 */
	@Override
	public boolean promptSuppress() {
		return false;
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
