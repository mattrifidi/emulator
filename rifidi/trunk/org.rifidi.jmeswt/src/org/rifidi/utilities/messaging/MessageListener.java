package org.rifidi.utilities.messaging;

/**
 * Classes listening for messages posted to the messaging system should
 * implement this interface and subscribe to the messaging system
 * 
 * @author Dan West - dan@pramari.com
 */
public interface MessageListener {

	/**
	 * This is called when a message is posted to a category to which this
	 * listener is subscribed
	 * 
	 * @param category
	 *            the category the message was posted to
	 * @param message
	 *            the message which triggered this listener
	 */
	public void messageNotification(String category, String message);
}