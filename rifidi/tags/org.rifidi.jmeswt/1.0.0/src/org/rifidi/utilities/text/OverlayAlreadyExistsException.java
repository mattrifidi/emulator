package org.rifidi.utilities.text;

/**
 * Creates a text overlay for displaying on-screen text for a given canvas.
 * 
 * @author Dan West - dan@pramari.com
 */
@SuppressWarnings("serial")
public class OverlayAlreadyExistsException extends Exception {

	/**
	 * Constructor for the exception
	 */
	public OverlayAlreadyExistsException() {
		super("An overlay has already been created for the given canvas!");
	}
}