/*
 *  TagEventConsumer.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Hardware
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi.org Steering Committee
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sharedrc.tagmemory.tagevent;

/**
 * Classes implementing this interface want to be informed about changes to the taglist
 * @author Jochen Mader
 *
 */
public interface TagEventConsumer {
	/**
	 * A tag got added
	 *
	 */
	public void add();
	/**
	 * A tag got removed
	 *
	 */
	public void remove();
}
