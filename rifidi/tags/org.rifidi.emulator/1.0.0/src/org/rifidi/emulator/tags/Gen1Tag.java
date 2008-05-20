package org.rifidi.emulator.tags;

import java.io.Serializable;

import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.exceptions.InvalidMemoryAccessException;

/*
 *  Gen1Tag.java
 *
 *  Created:	Sep 21, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

/**
 * Use this interface to implement a Generation 1 tag.
 * @author Jochen Mader
 *
 */
public interface Gen1Tag extends Serializable{
	/**
	 * Returns the generation of the tag
	 * @return the tag generation identifier
	 */
	public TagGen getTagGeneration();
	/**
	 * Read the id of the tag
	 * @return the id
	 */
	public byte[] readId();
	/**
	 * Write a new id to the tag
	 * @return the id
	 */
	public void writeId(byte[] id) throws InvalidMemoryAccessException;

	/**
	 * Accessor for tag ID (needed by jaxb)
	 */
	public byte[] getId();
	/**
	 * Setter for tagID (needed by jaxb)
	 */
	public void setId( byte[] id ) throws InvalidMemoryAccessException;
}
