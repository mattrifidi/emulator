/*
 *  C1G2Operations.java
 *
 *  Project: Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright: Pramari LLC and the Rifidi Project
 *  License: Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sharedrc.radio;

import javax.naming.AuthenticationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.rmi.client.ClientCallbackInterface;
import org.rifidi.tags.enums.LockStates;
import org.rifidi.tags.enums.TagConstants;
import org.rifidi.tags.exceptions.InvalidMemoryAccessException;
import org.rifidi.tags.impl.C1G2Tag;

/**
 * 
 * This class contains some utility methods to perform advanced operations on
 * C1G2 Tags. It handles callbacks as needed.
 * 
 * @author kyle
 * 
 */
public class C1G2Operations {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(C1G2Operations.class);

	private static Log eventLogger = LogFactory.getLog("EventLogger");

	public static byte[] C1G2ReadTagMem(C1G2Tag tag, int memoryBank,
			int wordPtr, int wordCount, byte accessPassword[])
			throws InvalidMemoryAccessException, AuthenticationException {
		TagConstants mb = null;

		if (memoryBank == 0) {
			mb = TagConstants.MemoryReserved;
		} else if (memoryBank == 1) {
			mb = TagConstants.MemoryEPC;
		} else if (memoryBank == 2) {
			mb = TagConstants.MemoryTID;
		} else if (memoryBank == 3) {
			mb = TagConstants.MemoryUser;
		} else {
			logger.error("Incorrect Memory Bank: " + memoryBank);
		}

		byte[] bytes = null;

		tag.sendPassword(accessPassword);

		bytes = tag.readMemory(mb, wordPtr, wordCount);
		eventLogger.info("[TAG EVENT]: " + wordCount * 2 + " bytes read from "
				+ mb);

		return bytes;
	}

	public static void C1G2WriteTagMem(C1G2Tag tag, Long entityID, int memoryBank,
			int wordPtr, byte[] writeData, byte[] accessPassword,
			ClientCallbackInterface callback, Antenna antenna)
			throws AuthenticationException, InvalidMemoryAccessException {

		TagConstants mb = null;

		if (memoryBank == 0) {
			mb = TagConstants.MemoryReserved;
		} else if (memoryBank == 1) {
			mb = TagConstants.MemoryEPC;
		} else if (memoryBank == 2) {
			mb = TagConstants.MemoryTID;
		} else if (memoryBank == 3) {
			mb = TagConstants.MemoryUser;
		} else {
			logger.error("Incorrect Memory Bank: " + memoryBank);
		}

		boolean success = true;
		@SuppressWarnings("unused")
		byte[] old_id;
		try {
			// save id if we need it for callback manager
			old_id = tag.readId();
			tag.sendPassword(accessPassword);

			tag.writeMemory(mb, wordPtr, writeData);
			eventLogger.info("[TAG EVENT]: Wrote " + writeData.length
					+ " bytes to " + mb);

		} catch (AuthenticationException ex) {
			success = false;
			throw ex;

		} catch (InvalidMemoryAccessException ex) {
			success = false;
			throw ex;
		}
		if (success) {
			// if we should do a callback
			if (mb == TagConstants.MemoryEPC) {
				antenna.rehash();
				if (callback != null) {
					callback.tagIDChanged(entityID, tag);
				}
			}
		}

	}

	public static void C1G2WriteID(C1G2Tag tag,  Long tagEntityID, byte[] newID,
			byte[] accessPassword, ClientCallbackInterface callback, Antenna ant)
			throws AuthenticationException, InvalidMemoryAccessException {

		C1G2WriteTagMem(tag, tagEntityID, 1, 2, newID, accessPassword, callback, ant);

	}

	public static void C1G2KillTag(C1G2Tag c1g2tag, byte[] killPassword)
			throws AuthenticationException {

		c1g2tag.kill(killPassword);
		eventLogger.info("[TAG EVENT]: Tag with ID "
				+ ByteAndHexConvertingUtility.toHexString(c1g2tag.getId())
				+ " killed");

	}

	public static void C1G2LockTag(C1G2Tag tag, int memoryBank,
			byte[] password, int lockState) throws AuthenticationException,
			InvalidMemoryAccessException {
		TagConstants mb = null;

		if (memoryBank == 0) {
			mb = TagConstants.PasswordKill;
		} else if (memoryBank == 1) {
			mb = TagConstants.PasswordAccess;
		} else if (memoryBank == 2) {
			mb = TagConstants.MemoryEPC;
		} else if (memoryBank == 3) {
			mb = TagConstants.MemoryTID;
		} else if (memoryBank == 4) {
			mb = TagConstants.MemoryUser;
		} else {
			logger.error("Incorrect Memory Bank: " + memoryBank);
		}

		LockStates ls = null;
		if (lockState == 0) {
			ls = LockStates.locked;
		} else if (lockState == 1) {
			ls = LockStates.permalocked;
		} else if (lockState == 2) {
			ls = LockStates.permaunlocked;
		} else if (lockState == 3) {
			ls = LockStates.unlocked;
		} else {
			logger.error("Incorrect Lock state: " + lockState);
		}

		tag.sendPassword(password);
		tag.lock(mb, ls);
		eventLogger.info("[TAG EVENT]: Tag with ID "
				+ ByteAndHexConvertingUtility.toHexString(tag.readId())
				+ " state changed to " + ls + " state");

	}

	public static byte[] getAccessPass(C1G2Tag tag) {
		try {
			return tag.readMemory(TagConstants.MemoryReserved, 0, 2);
		} catch (InvalidMemoryAccessException e) {
			e.printStackTrace();
			return null;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] getKillPass(C1G2Tag tag) {
		try {
			return tag.readMemory(TagConstants.MemoryReserved, 2, 2);
		} catch (InvalidMemoryAccessException e) {
			e.printStackTrace();
			return null;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return null;
		}
	}

}
