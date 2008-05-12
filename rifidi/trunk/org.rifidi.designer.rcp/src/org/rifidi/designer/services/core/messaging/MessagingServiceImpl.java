package org.rifidi.designer.services.core.messaging;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.utilities.messaging.MessageListener;
import org.rifidi.utilities.messaging.MessagingSystem;
import org.rifidi.utilities.messaging.exceptions.CategoryAlreadyExistsException;
import org.rifidi.utilities.messaging.exceptions.ListenerAlreadySubscribedException;
import org.rifidi.utilities.messaging.exceptions.NoSuchCategoryException;
import org.rifidi.utilities.text.TextOverlayGroup;

/**
 * This implementation of the messaging service interface listens for messages
 * posted to the messagingsystem and posts them to text overlays in response.
 * 
 * @author Dan West - dan@pramari.com
 */
public class MessagingServiceImpl implements MessagingService, MessageListener {
	/**
	 * The logger.
	 */
	private static Log logger = LogFactory.getLog(MessagingServiceImpl.class);
	/**
	 * TextOverlayGroups hashed by the message categories that post messages to
	 * them.
	 */
	private HashMap<String, ArrayList<TextOverlayGroup>> subscribedGroups;

	/**
	 * Default constructor.
	 */
	public MessagingServiceImpl() {
		logger.debug("MessagingService created");
		subscribedGroups = new HashMap<String, ArrayList<TextOverlayGroup>>();

		try {
			MessagingSystem.create();
			MessagingSystem.getInstance().addCategory("readerEvents");
			MessagingSystem.getInstance().subscribe("readerEvents", this);
		} catch (ListenerAlreadySubscribedException e) {
			logger.error("ListenerAlreadySubscribedException "+e);
		} catch (CategoryAlreadyExistsException e) {
			logger.error("CategoryAlreadyExistsException "+e);
		} catch (NoSuchCategoryException e) {
			logger.error("NoSuchCategoryException "+e);
		}
	}

	/* (non-Javadoc)
	 * @see org.rifidi.utilities.messaging.MessageListener#messageNotification(java.lang.String, java.lang.String)
	 */
	public void messageNotification(String category, String message) {
		if (subscribedGroups.containsKey(category)) {
			for (TextOverlayGroup group : subscribedGroups.get(category)) {
				logger.debug("message: \'" + message + "\' arrived");
				group.postMessage(message, 2.5f);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.rifidi.services.registry.core.messaging.MessagingService#subscribeOverlayGroup(java.lang.String, org.rifidi.utilities.text.TextOverlayGroup)
	 */
	public void subscribeOverlayGroup(String messageCategory, TextOverlayGroup group) {
		if (!subscribedGroups.containsKey(messageCategory)) {
			subscribedGroups.put(messageCategory,new ArrayList<TextOverlayGroup>());
		}
		subscribedGroups.get(messageCategory).add(group);
	}
}
