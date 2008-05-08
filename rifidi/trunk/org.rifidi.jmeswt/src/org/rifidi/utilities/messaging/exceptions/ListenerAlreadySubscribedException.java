package org.rifidi.utilities.messaging.exceptions;

/**
 * This exception indicates that the listener subscription process has been
 * aborted because the listener was already subscribed to the same category.
 * 
 * @author Dan West - dan@pramari.com
 */
@SuppressWarnings("serial")
public class ListenerAlreadySubscribedException extends Exception {

	/**
	 * Constructor for the exception
	 * 
	 * @param category
	 *            the category in which the exception occurred
	 */
	public ListenerAlreadySubscribedException(String category) {
		super(
				"Subscription process aborted! Listener already subscribed to category \'"
						+ category + "\'");
	}
}