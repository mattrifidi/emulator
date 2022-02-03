/*
 *  TagEventPublisher.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Hardware
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi.org Steering Committee
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sharedrc.tagmemory.tagevent;

import java.util.List;

/**
 * A class implementing this interface is able to inform other classes implementing the
 * TagEventConsumer interface about changes to tags.
 * 
 * @see TagEventConsumer
 * 
 * @author Jochen Mader
 *
 */
public interface TagEventPublisher {
	/**
	 * Subscribe a new consumer to get tag events
	 * @param consumer the consumer to subscribe
	 */
	public void subscribe(TagEventConsumer consumer);
	/**
	 * Remove a consumer from the consumer list
	 * @param consumer the consumer to remove
	 */
	public void unsubscribe(TagEventConsumer consumer);
	
	/**
	 * Returns a reference to the list of TagEvents
	 * @return list of TagEvents
	 */
	public List<TagEvent> getTagEvents();
}
