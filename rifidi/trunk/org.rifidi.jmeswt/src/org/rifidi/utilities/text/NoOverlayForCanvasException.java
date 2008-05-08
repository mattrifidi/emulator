package org.rifidi.utilities.text;

/**
 * This exception indicates that an attempt to get an overlay manager for a
 * canvas failed because there is no registered overlay for the canvas.
 * 
 * @author Dan West - dan@pramari.com
 */
@SuppressWarnings("serial")
public class NoOverlayForCanvasException extends Exception {

	/**
	 * Constructor for the exception
	 */
	public NoOverlayForCanvasException() {
		super("No overlay registered with the specified canvas!");
	}
}