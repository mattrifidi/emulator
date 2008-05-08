/*
 *  MessagingService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.designer.services.core.messaging;

import org.rifidi.utilities.text.TextOverlayGroup;

/**
 * This service subscribes overlay groups to incoming messages from the
 * messaging system.
 * 
 * @author Dan West - dan@pramari.com
 */
public interface MessagingService {

	/**
	 * Implementing Messaging services should implement this method to accept
	 * overlay groups to post messages to.
	 * 
	 * @param messageCategory
	 *            the category whose messages to accept
	 * @param group
	 *            the textoverlaygroup to post the messages to
	 */
	void subscribeOverlayGroup(String messageCategory,
			TextOverlayGroup group);
}