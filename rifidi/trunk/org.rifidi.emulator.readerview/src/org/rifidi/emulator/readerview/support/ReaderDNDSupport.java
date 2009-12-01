/**
 * 
 */
package org.rifidi.emulator.readerview.support;

/**
 * This class provides helper methods for parsing a texttransfer associated with
 * dragging and dropping an antenna
 * 
 * example of a text tranfer string: reader|id:dock door|ant:2
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ReaderDNDSupport {

	/**
	 * Determines if the text transfer string is an antenna
	 * 
	 * @param textTransfer
	 * @return
	 */
	public static boolean isReaderDND(String textTransfer) {
		try {
			return textTransfer.startsWith("reader");
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("textTranfer cannot be null");
		}
	}

	/**
	 * Given a text transfer string, return the reader ID
	 * 
	 * @param textTransfer
	 * @return
	 */
	public static String getReaderID(String textTransfer) {
		try {
			return textTransfer.split("\\|")[1].split(":")[1];
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Text Tranfer string is invalid: " + textTransfer);
		}
	}

	/**
	 * Given a text tranfer string, return the antenna ID
	 * 
	 * @param textTransfer
	 * @return
	 */
	public static Integer getAntennaID(String textTransfer) {
		try {
			String s = textTransfer.split("\\|")[2].split(":")[1];
			return Integer.parseInt(s);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Text Tranfer String is invalid: " + textTransfer);
		}
	}

	/**
	 * Create a new text tranfer string
	 * 
	 * @param readerID
	 * @param antID
	 * @return
	 */
	public static String getTextTranferString(String readerID, Integer antID) {
		if (readerID == null || antID == null) {
			throw new IllegalArgumentException(
					"readerID and antID cannot be null");
		}
		return "reader|id:" + readerID + "|ant:" + antID;
	}

}
