package org.rifidi.utilities.messaging.exceptions;

/**
 * This exception indicates that the category has already been created and an
 * attempt to create it again has occurred.
 * 
 * @author Dan West - dan@pramari.com
 */
@SuppressWarnings("serial")
public class CategoryAlreadyExistsException extends Exception {

	/**
	 * Constructor for the exception
	 * 
	 * @param category
	 *            the category that was attempted to be created a second time
	 */
	public CategoryAlreadyExistsException(String category) {
		super("Error creating category \'" + category
				+ "\' - it's already been created!");
	}
}