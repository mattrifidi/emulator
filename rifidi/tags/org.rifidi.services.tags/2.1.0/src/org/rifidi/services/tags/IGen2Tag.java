package org.rifidi.services.tags;

import javax.naming.AuthenticationException;

import org.rifidi.services.tags.enums.LockStates;
import org.rifidi.services.tags.enums.TagConstants;
import org.rifidi.services.tags.exceptions.InvalidMemoryAccessException;

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
 * This class represents a Genration 2 tag
 * @author Jochen Mader
 *
 */
public interface IGen2Tag extends IGen1Tag{
	/**
	 * Authenticate to the tag
	 * @param password the password to use
	 * @throws AuthenticationException
	 */
	public void sendPassword(byte[] password) throws AuthenticationException;
	
	/**
	 * Read from the tag memory
	 * @param mem identifier for the memroy bank to read from
	 * @param wordPointer the start of the read operation
	 * @param wordCount number of words to read and return
	 * @return the words to be returned
	 * @throws InvalidMemoryAccessException thrown if memory is locked or the readposition or length were invalid
	 */
	public byte[] readMemory(TagConstants mem,int wordPointer, int wordCount) throws InvalidMemoryAccessException, AuthenticationException;
	/**
	 * Write to tag memory
	 * @param mem identifier for the memroy bank to write to
	 * @param wordPointer the start of the write operation
	 * @param data the data to be written
	 * @throws InvalidMemoryAccessException if data doesn't fit into memory or the bank is locked 
	 */
	public void writeMemory(TagConstants mem, int wordPointer, byte[] data) throws InvalidMemoryAccessException;
	/**
	 * lock the specified part of the memory
	 * @param mem identifier of the mem
	 * @param lock lockAction to perform
	 * @throws InvalidMemoryAccessException thrown if given bank is already locked
	 * @throws AuthenticationException thrown if the password is required but wasn't given
	 */
	public void lock(TagConstants mem, LockStates lock) throws InvalidMemoryAccessException, AuthenticationException;

	/**
	 * Kill the tag
	 * @param killpassword the password used to kill the tag
	 * @throws AuthenticationException thrown if the given password doesn't match
	 */
	public void kill(byte[] killpassword) throws AuthenticationException;
	
	/**
	 * Start the tag.
	 * The tag is a statmachine so it needs a little nudge.
	 */
	public void start();
}
