package org.rifidi.designer.services.core.camera.exceptions;

/**
 * This exception indicates that an attempt was made to register a reader with
 * an id that already exists.
 * 
 * @author Dan West - dan@pramari.com
 */
@SuppressWarnings("serial")
public class DuplicateCameraIdException extends Exception {

	/**
	 * Constructor for exception.
	 * 
	 * @param id
	 *            the id that already exists
	 */
	public DuplicateCameraIdException(final String id) {
		super("A camera with the id \'" + id + "\' is already registered!");
	}
}
