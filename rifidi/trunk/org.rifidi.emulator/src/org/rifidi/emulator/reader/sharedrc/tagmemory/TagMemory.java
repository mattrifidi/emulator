/*
 *  TagMemory.java
 *
 *  Created:	Oct 15, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.sharedrc.tagmemory;

import java.util.Collection;

import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * 
 * This is an interface describing what functions a TagMemory should have. It is
 * used with the new radio. Each reader should implement its own tag memory. At
 * first, we tried to implement a generic tag memory, but it turns out that most
 * readers seem to have quirky differnces in the way they store tags.
 * 
 * @author kyle
 * 
 */
public interface TagMemory{

	/**
	 * This method is how the radio will add tags to the tag memory after it
	 * scans tags on the anntennas
	 * 
	 * @param tagsToAdd
	 */
	public void updateMemory(Collection<RifidiTag> tagsToAdd);

	/**
	 * This method is for getting tags from the memory
	 * 
	 * @return
	 */
	public Collection<RifidiTag> getTagReport();

	/**
	 * Allow tags to be added or removed from the tag memory
	 */
	public void resume();

	/**
	 * Prevents tags from being added or removed from the memory
	 */
	public void suspend();

	/**
	 * Should clear all tag data structures in the memory
	 */
	public void clear();

}
