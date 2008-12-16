/*
 *  TagEvent.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Hardware
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi.org Steering Committee
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sharedrc.tagmemory.tagevent;

import org.rifidi.tags.impl.RifidiTag;

/**
 * Class that constitutes an event.
 * 
 * @author Jochen Mader
 * 
 */
public class TagEvent {
	/**
	 * The tag on which the event happened
	 */
	private RifidiTag tag;

	/**
	 * true: Tag got added false: Tag got removed
	 */
	private boolean added = false;

	/**
	 * Constructor
	 * 
	 * @param tag
	 *            the tag the event happened on
	 * @param added
	 *            tru: it got added false: it got removed
	 */
	public TagEvent(RifidiTag tag, boolean added) {
		this.tag = tag;
		this.added = added;
	}

	/**
	 * @return the added
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * @param added
	 *            the added to set
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * @return the tag
	 */
	public RifidiTag getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(RifidiTag tag) {
		this.tag = tag;
	}
}
