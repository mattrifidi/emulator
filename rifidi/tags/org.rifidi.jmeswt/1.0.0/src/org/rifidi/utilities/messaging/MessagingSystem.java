package org.rifidi.utilities.messaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.rifidi.utilities.messaging.exceptions.CategoryAlreadyExistsException;
import org.rifidi.utilities.messaging.exceptions.ListenerAlreadySubscribedException;
import org.rifidi.utilities.messaging.exceptions.NoSuchCategoryException;

/**
 * This is a singleton implementation of a messaging system. A call to the
 * static create method creates the messaging system, and subsequent calls to
 * addCategory creates the categories that the messaging system uses. Classes
 * implementing the MessageListener interface can subscribe to a particular
 * category and they will be notified when a message is posted to the specified
 * category. Messages are posted to the messaging system using the postMessage
 * method.
 * 
 * @author Dan West - dan@pramari.com
 */
public class MessagingSystem {
	/**
	 * Storage for the categories and their associated listeners
	 */
	private HashMap<String, ArrayList<MessageListener>> categories;

	/**
	 * The instance of this singleton
	 */
	private static MessagingSystem instance;

	/**
	 * Private constructor (singleton implementation)
	 */
	private MessagingSystem() {
		categories = new HashMap<String, ArrayList<MessageListener>>();
	}

	/**
	 * Creates a new instance of the messaging system
	 */
	public static void create() {
		instance = new MessagingSystem();
	}

	/**
	 * @return the singleton instance of the messaging system
	 */
	public static MessagingSystem getInstance() {
		return instance;
	}

	/**
	 * Adds a category with the specified name to the messaging system
	 * 
	 * @param categoryName
	 *            the name of the new category
	 */
	public void addCategory(String categoryName)
			throws CategoryAlreadyExistsException {
		categories.put(categoryName, new ArrayList<MessageListener>());
	}

	/**
	 * Gets the categories from the messaging system
	 * 
	 * @return an unmodifiable list of the categories
	 */
	public Set<String> getCategories() {
		return Collections.unmodifiableSet(categories.keySet());
	}

	/**
	 * Subscribes a MessageListener to be notified whenever a message is posted
	 * to the given category
	 * 
	 * @param category
	 *            the category to subscribe to
	 * @param listener
	 *            the listener to be notified
	 * @throws NoSuchCategoryException
	 *             if the category does not exist in the messagingsystem
	 * @throws ListenerAlreadySubscribedException
	 *             if the specified listener has already been subscribed to the
	 *             given category
	 */
	public void subscribe(String category, MessageListener listener)
			throws NoSuchCategoryException, ListenerAlreadySubscribedException {
		if (!categories.containsKey(category)) {
			throw (new NoSuchCategoryException(category));
		} else {
			if (categories.get(category).contains(listener)) {
				throw (new ListenerAlreadySubscribedException(category));
			} else {
				categories.get(category).add(listener);
			}
		}
	}

	/**
	 * Notifies the messaging system that something of relevance occurred. This
	 * method triggers any listeners subscribed to the category.
	 * 
	 * @param category
	 *            the category this message pertains to
	 * @param message
	 *            the message itself
	 * @throws NoSuchCategoryException
	 *             if the category does not exist in the messaging system
	 */
	public void postMessage(String category, String message)
			throws NoSuchCategoryException {
		if (!categories.containsKey(category)) {
			throw (new NoSuchCategoryException(category));
		} else {
			for (MessageListener ml : categories.get(category)) {
				ml.messageNotification(category, message);
			}
		}
	}
}