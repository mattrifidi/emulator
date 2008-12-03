/*
 *  EventsService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.events;

/**
 * Service for recording and displaying events.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 27, 2008
 * 
 */
public interface EventsService {
	/**
	 * Publish the given event.
	 * 
	 * @param worldEvent
	 */
	public void publish(WorldEvent worldEvent);

	/**
	 * Record the given event types.
	 * 
	 * @param eventType
	 */
	public void record(Class<?>... eventType);

	/**
	 * Stop recording.
	 */
	public void stopRecording();

	/**
	 * Clear recorded events.
	 */
	public void clearRecords();

	/**
	 * Check if we are recording.
	 */
	public boolean isRecording();
}
