/*
 *  C1G2Tag.java
 *
 *  Created:	Sep 25, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.tags.impl;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.NotImplementedException;
import org.rifidi.emulator.tags.Gen2Tag;
import org.rifidi.emulator.tags.enums.LockStates;
import org.rifidi.emulator.tags.enums.TagConstants;
import org.rifidi.emulator.tags.enums.TagErrors;
import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.exceptions.InvalidMemoryAccessException;

/**
 * Class 1 Generation 2 tag
 * 
 * @author Jochen Mader
 * 
 */
@XmlRootElement
public class C1G2Tag implements Gen2Tag {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum states {
		Open, Secured, Killed
	};



	private Map<TagConstants, byte[]> mem;

	/*
	 * Map to keep track of the lock states of the different memory banks
	 */
	private Map<TagConstants, LockStates> lockStates;

	/*
	 * current state of the tag
	 */
	private states currentState;

	/**
	 * Constructor
	 * 
	 * @param userMemSize
	 *            size of user memory
	 * @param epcMemSize
	 *            soze of epc memory
	 * @param TIDMemSize
	 *            size of TID memory
	 */
	public C1G2Tag() {
		super();
		mem = new HashMap<TagConstants, byte[]>();
		lockStates = new HashMap<TagConstants, LockStates>();

		byte[] reserved = new byte[8];
		for (int i = 0; i < 4; i++) {
			reserved[i] = 0;
			reserved[i + 4] = 0;
		}
		this.mem.put(TagConstants.MemoryReserved, reserved);
		this.lockStates.put(TagConstants.MemoryReserved, LockStates.permalocked);
		this.lockStates.put(TagConstants.PasswordAccess, LockStates.unlocked);
		this.lockStates.put(TagConstants.PasswordKill, LockStates.unlocked);

		byte[] epcID = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00 };
		byte[] mem_EPC = new byte[epcID.length + 4]; // Store CRC and PC
		// (4Bytes)
		System.arraycopy(epcID, 0, mem_EPC, 4, epcID.length);
		this.mem.put(TagConstants.MemoryEPC, mem_EPC);
		this.lockStates.put(TagConstants.MemoryEPC, LockStates.unlocked);

		byte[] mem_TID = new byte[4];
		for (int i = 0; i < mem_TID.length; i++) {
			mem_TID[i] = 0;
		}
		mem.put(TagConstants.MemoryTID, mem_TID);
		this.lockStates.put(TagConstants.MemoryTID, LockStates.unlocked);

		byte[] mem_user = new byte[8];
		for (int i = 0; i < mem_user.length; i++) {
			mem_user[i] = 0;
		}
		mem.put(TagConstants.MemoryUser, mem_user);
		lockStates.put(TagConstants.MemoryUser, LockStates.unlocked);

		setCurrentState(states.Open);
	}

	/**
	 * This is the constructor of a c1g2
	 * 
	 * @param epcID
	 * @param accessPass
	 *            4 bytes
	 * @param killPass
	 *            4 bytes
	 */
	public C1G2Tag(byte[] epcID, byte[] accessPass, byte[] killPass) {
		this();

		if (accessPass == null || accessPass.length != 4) {
			throw new IllegalArgumentException("Access Password is invalid");
		}

		if (accessPass == killPass || killPass.length != 4) {
			throw new IllegalArgumentException("Kill Password is invalid");
		}

		byte[] reserved = new byte[8];
		for (int i = 0; i < 4; i++) {
			reserved[i] = accessPass[i];
			reserved[i + 4] = killPass[i];
		}
		this.mem.put(TagConstants.MemoryReserved, reserved);

		byte[] mem_EPC = new byte[epcID.length + 4]; // Store CRC and PC
		// (4Bytes)
		System.arraycopy(epcID, 0, mem_EPC, 4, epcID.length);
		this.mem.put(TagConstants.MemoryEPC, mem_EPC);

	}
	

	/**
	 * @return the currentState
	 */
	public states getCurrentState() {
		return currentState;
	}

	/**
	 * @param currentState
	 *            the currentState to set
	 */
	public void setCurrentState(states currentState) {
		this.currentState = currentState;
	}

	/**
	 * @return the lockStates
	 */
	public Map<TagConstants, LockStates> getLockStates() {
		return lockStates;
	}

	/**
	 * @param lockStates
	 *            the lockStates to set
	 */
	public void setLockStates(Map<TagConstants, LockStates> lockStates) {
		this.lockStates = lockStates;
	}

	/**
	 * @return the mem
	 */
	public Map<TagConstants, byte[]> getMem() {
		return mem;
	}

	/**
	 * @param mem
	 *            the mem to set
	 */
	public void setMem(Map<TagConstants, byte[]> mem) {
		this.mem = mem;
	}

	// -----------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.tags.Gen2Tag#sendPassword(byte[])
	 */
	public void sendPassword(byte[] password) throws AuthenticationException {
		if (currentState.equals(states.Killed))
			return;
		for (int count = 0; count < password.length; count++) {
			// compare password
			if (!(mem.get(TagConstants.MemoryReserved)[count] == password[count])) {
				// on failure throw exception
				throw new AuthenticationException("Wrong access password");
			}
		}
		currentState = states.Secured;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.tags.Gen2Tag#getTagGeneration()
	 */
	public TagGen getTagGeneration() {
		return TagGen.GEN2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.tags.Gen2Tag#getId()
	 */
	public byte[] readId() {
		if (currentState.equals(states.Killed))
			return null;
		byte[] ret = new byte[mem.get(TagConstants.MemoryEPC).length - 4];
		System
				.arraycopy(mem.get(TagConstants.MemoryEPC), 4, ret, 0,
						ret.length);
		doTransition();
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.tags.Gen2Tag#writeId(byte[])
	 */
	public void writeId(byte[] id) {
		throw new NotImplementedException(
				"WriteId is not supported for Gen2 tags, use writeMemory");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.tags.Gen2Tag#kill(byte[])
	 */
	public void kill(byte[] killpassword) throws AuthenticationException {
		if (currentState.equals(states.Killed))
			return;
		for (int count = 0; count < killpassword.length; count++) {
			// compare password
			if (!(mem.get(TagConstants.MemoryReserved)[count + 4] == killpassword[count])) {
				// on failure throw exception
				doTransition();
				throw new AuthenticationException("Wrong kill password");
			}
		}
		currentState = states.Killed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.tags.Gen2Tag#lock(org.rifidi.tags.enums.TagConstants)
	 */
	public void lock(TagConstants mem, LockStates lockAction) throws InvalidMemoryAccessException,
			AuthenticationException {
		if (currentState.equals(states.Killed))
			return;
		if(mem == TagConstants.MemoryReserved){
			doTransition();
			throw new InvalidMemoryAccessException(TagErrors.InvalidMemoryBank);
		}
		if (currentState.equals(states.Secured)) {
			// don't lock twice
			if (lockStates.get(mem).equals(lockAction)) {
				doTransition();
				throw new InvalidMemoryAccessException(
						TagErrors.TargetMemoryIsLocked);
			}
			// trying to lock a permalocked memory fails
			if (lockStates.get(mem).equals(LockStates.permalocked)) {
				doTransition();
				throw new InvalidMemoryAccessException(
						TagErrors.TargetMemoryIsPermalocked);
			}
			// trying to change a permaunlocked memory fails
			if (lockStates.get(mem).equals(LockStates.permaunlocked)) {
				doTransition();
				throw new InvalidMemoryAccessException(
						TagErrors.TargetMemoryIsPermaunlocked);
			}
			lockStates.put(mem, LockStates.unlocked);
		} else {
			doTransition();
			throw new AuthenticationException(
					"Authentication needed for this operation");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.tags.Gen2Tag#readMemory(org.rifidi.tags.enums.TagConstants,
	 *      int, int)
	 */
	public byte[] readMemory(TagConstants mem, int wordPointer, int wordCount)
			throws InvalidMemoryAccessException, AuthenticationException {
		if (currentState.equals(states.Killed))
			return null;

		if (wordPointer * 2 > this.mem.get(mem).length) {
			doTransition();
			throw new InvalidMemoryAccessException(
					TagErrors.InvalidReadingPosition);
		}
		// are we trying to read more than is available
		if (wordPointer * 2 + wordCount * 2 > this.mem.get(mem).length) {
			doTransition();
			throw new InvalidMemoryAccessException(TagErrors.InvalidReadLength);
		}
		byte[] ret = new byte[wordCount * 2];
		System.arraycopy(this.mem.get(mem), wordPointer * 2, ret, 0,
				wordCount * 2);
		doTransition();
		return ret;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.tags.Gen2Tag#writeMemory(org.rifidi.tags.enums.TagConstants,
	 *      int, byte[])
	 */
	public void writeMemory(TagConstants mem, int wordPointer, byte[] data)
			throws InvalidMemoryAccessException {
		if (currentState.equals(states.Killed))
			return;
		
		//if locked and not in secured state
		if (lockStates.get(mem).equals(LockStates.locked)
				&& currentState == states.Secured) {
			doTransition();
			throw new InvalidMemoryAccessException(TagErrors.TargetMemoryIsLocked);
		}
		//if permalocked
		if(lockStates.get(mem).equals(LockStates.permalocked)){
			doTransition();
			throw new InvalidMemoryAccessException(TagErrors.TargetMemoryIsPermalocked);
		}
		
		if(mem==TagConstants.MemoryReserved){
			//ifWriting access pass
			//if(wordPointer)
			
			//if writing kill pass
			
			//if writing both
		}
		
		// are we writing outisde of the memory
		if (wordPointer * 2 > this.mem.get(mem).length) {
			doTransition();
			throw new InvalidMemoryAccessException(
					TagErrors.InvalidWritingPosition);
		}
		// are we trying to write more than available?
		if (wordPointer * 2 + data.length > this.mem.get(mem).length) {
			doTransition();
			throw new InvalidMemoryAccessException(TagErrors.InvalidWriteLength);
		}
		System.arraycopy(data, 0, this.mem.get(mem), wordPointer * 2,
				data.length);
		doTransition();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.tags.Gen2Tag#start()
	 */
	public void start() {
		if (currentState.equals(states.Killed))
			return;
		doTransition();
	}

	/**
	 * This method is responsible to switch between open and secured THis method
	 * has to be callled after every gen 2 command.
	 * 
	 */
	private void doTransition() {
		if (mem.get(TagConstants.MemoryReserved) == null) {
			currentState = states.Secured;
		} else {
			currentState = states.Open;
		}
	}

	@SuppressWarnings("unused")
	private byte[] getPassword(TagConstants tc) {
		int offset = 0;
		if (tc == TagConstants.PasswordAccess) {

		} else if (tc == TagConstants.PasswordKill) {
			offset = 4;
		} else {
			return null;
		}

		byte[] pass = new byte[4];
		byte[] reserved = mem.get(TagConstants.MemoryReserved);
		System.arraycopy(reserved, offset, pass, 0, 4);
		return pass;
	}


	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.Gen1Tag#getId()
	 */
	public byte[] getId() {
		return mem.get(TagConstants.MemoryEPC);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.Gen1Tag#setId(byte[])
	 */
	public void setId(byte[] id) throws InvalidMemoryAccessException {
		if ( id != null )
			throw new InvalidMemoryAccessException(TagErrors.TagIsReadOnly);
		else
			mem.put(TagConstants.MemoryEPC, id);
	}
	
	public byte[] getAccessPass(){
		byte retval[] = new byte[4];
		for(int i=0; i<4; i++){
			retval[i]=this.mem.get(TagConstants.MemoryReserved)[i];
		}
		return retval;
	}
	
	public byte[] getLockPass(){
		byte retval[] = new byte[4];
		for(int i=4; i<8; i++){
			retval[i]=this.mem.get(TagConstants.MemoryReserved)[i];
		}
		return retval;
	}
}