/**
 * 
 */
package org.rifidi.emulator.reader.alien.rssi;

/**
 * @author Matthew Dean - matt@pramari.com
 *
 */
public class RSSIException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -616832555186376914L;

	/**
	 * 
	 */
	public RSSIException() {
	}

	/**
	 * @param arg0
	 */
	public RSSIException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public RSSIException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public RSSIException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
