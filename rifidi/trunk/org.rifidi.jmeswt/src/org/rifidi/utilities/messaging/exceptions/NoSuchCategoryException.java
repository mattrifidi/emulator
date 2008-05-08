package org.rifidi.utilities.messaging.exceptions;

/**
 * This exception indicates that an attempt to access the specified category
 * occurred, but could not be completed because the category does not exist in
 * the messaging system.
 * 
 * @author Dan West - dan@pramari.com
 */
@SuppressWarnings("serial")
public class NoSuchCategoryException extends Exception {

	/**
	 * Constructor for the exception
	 * 
	 * @param category
	 *            the category that was trying to be accessed
	 */
	public NoSuchCategoryException(String category) {
		super("No such category \'" + category + "\' exists in MessagingSystem");
	}
}