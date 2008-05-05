/*
 *  TagRegistryListener.java
 *
 *  Project: Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright: Pramari LLC and the Rifidi Project
 *  License: Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.tags.service;

import java.util.List;

import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * This is the Interface each Listener must implement to listen for certain
 * Events happening in the registry
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 */
public interface TagRegistryListener {

	/**
	 * This is event is fired every time tags get added or created to the registry
	 * 
	 * @param event the affected RifidiTags
	 */
	public void addEvent(List<RifidiTag> event);

	/**
	 * This is event is fired every time tags get removed from the registry
	 * 
	 * @param event the affected RifidiTags
	 */
	public void removeEvent(List<RifidiTag> event);

	/**
	 * This is event is fired every time tags get modified
	 * 
	 * @param event the affected RifidiTags
	 */
	public void modifyEvent(List<RifidiTag> event);

}
