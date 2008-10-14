/**
 * 
 */
package org.rifidi.ui.ide.views.antennaview.exception;

/**
 * @author jochen
 * 
 */
public class RifidiIndexDoesNotMatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5039916815549972021L;

	/**
	 * 
	 */
	public RifidiIndexDoesNotMatchException() {
		super("The number of GPI or GPO ports given in the "
				+ "naming scheme does not match the "
				+ "actual number of GPI or GPO ports");
	}

	public RifidiIndexDoesNotMatchException(String e) {
		super(e);
	}
}
