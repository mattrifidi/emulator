/*
 *  C0G1Test.java
 *
 *  Created:	Sep 25, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.tags;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.rifidi.emulator.tags.enums.LockStates;
import org.rifidi.emulator.tags.enums.TagConstants;
import org.rifidi.emulator.tags.exceptions.InvalidMemoryAccessException;
import org.rifidi.emulator.tags.impl.C1G2Tag;
import org.rifidi.emulator.tags.impl.C1G2Tag.states;
import org.rifidi.emulator.tags.utils.TagDataHelper;

/**
 * Class to test the Generation 2 Class 1 tag
 * @author Jochen Mader
 *
 */
public class C1G2Test {
	private static C1G2Tag c1g2;
	private static BigInteger data;
	private static byte[] accessPass;
	private static byte[] killPass;
	
	@BeforeClass public static void initialize(){
		data=TagDataHelper.getRandomDoD96();
		c1g2=new C1G2Tag();
		c1g2.setCurrentState(states.Open);
		accessPass=new byte[]{1,2,3,4};
		killPass=new byte[]{5,6,7,8};
		Map<TagConstants,byte[]> mem=new HashMap<TagConstants, byte[]>();
		/*
		 * Map to keep track of the lock states of the different memory banks
		 */
		Map<TagConstants, LockStates> lockStates=new HashMap<TagConstants, LockStates>();
		//assemble reserved memory
		byte[] memRes=new byte[8];
		System.arraycopy(accessPass, 0, memRes, 0, 4);
		System.arraycopy(killPass, 0, memRes, 4, 4);
		mem.put(TagConstants.MemoryReserved,memRes);
		mem.put(TagConstants.MemoryTID, new byte[]{(byte)226,(byte)255});
		mem.put(TagConstants.MemoryUser, new byte[1024]);
		byte[] epcmem=new byte[16];
		System.arraycopy(data.toByteArray(), 0, epcmem, 4, 12);
		mem.put(TagConstants.MemoryEPC, epcmem);
		
		//set lock states of the memory
		lockStates.put(TagConstants.MemoryReserved, LockStates.unlocked);
		lockStates.put(TagConstants.MemoryTID, LockStates.unlocked);
		lockStates.put(TagConstants.MemoryUser, LockStates.unlocked);
		lockStates.put(TagConstants.MemoryEPC, LockStates.unlocked);
		
		c1g2.setMem(mem);
		c1g2.setLockStates(lockStates);
		c1g2.start();
	}
	
	@Test(expected = Exception.class) public void testWrite(){
		c1g2.writeId(new byte[]{1,2,3,4,5});
	}
	
	/**
	 * Check the id
	 *
	 */
	@Test public void testRead(){
		byte[] id=c1g2.readId();
		byte[] databytes=data.toByteArray();
		for(int count=0;count<databytes.length;count++){
			if(!(id[count] == databytes[count])){
				fail("id in tag has changed!");
			}
		}
	}
	
	/**
	 * Do a write to the tagmem
	 *
	 */
	@Test public void testRealWrite(){
		data=TagDataHelper.getRandomGID96();
		//write a new password
		try {
			c1g2.writeMemory(TagConstants.MemoryEPC, 2, data.toByteArray());
		} catch (InvalidMemoryAccessException e) {
			fail("InvalidMemoryAccessException "+e.getError());
		}
		try {
			byte[] id=c1g2.readMemory(TagConstants.MemoryEPC, 2, 6);
			byte[] databytes=data.toByteArray();
			for(int count=0;count<id.length;count++){
				if(id[count]!=databytes[count]){
					fail("Written data different from read data!");
				}
			}
		} catch (InvalidMemoryAccessException e) {
			fail("InvalidMemoryAccessException "+e.getError());
		} catch (AuthenticationException e) {
			fail("AuthenticationException "+e.getMessage());
		}
	}
	
	/**
	 * Authenticate and then write to the tag
	 *
	 */
	@Test public void testAuthentication(){
		
		try {
			c1g2.sendPassword(accessPass);
		} catch (AuthenticationException e) {
			fail("Wrong password");
		}
		Assert.assertTrue(c1g2.getCurrentState().equals(C1G2Tag.states.Secured));
		try {
			c1g2.writeMemory(TagConstants.MemoryReserved, 2, killPass);
		} catch (InvalidMemoryAccessException e) {
			fail("Right password supplied but modification disallowed: "+e.getError());
		}
		Assert.assertTrue(c1g2.getCurrentState().equals(C1G2Tag.states.Open));
	}

	/**
	 * Test the kill command of the tag.
	 *
	 */
	@Test public void testLock(){
		//try without authenticating
		try {
			c1g2.lock(TagConstants.MemoryReserved, LockStates.locked);
		} catch (AuthenticationException e) {
			//supposed to happen
		} catch (InvalidMemoryAccessException e) {
			fail("Memory is locked!");
		}

		//try with authenticating
		try {
			c1g2.sendPassword(accessPass);
		} catch (AuthenticationException e) {
			fail("Wrong password");
		}
		
		try {
			c1g2.lock(TagConstants.MemoryReserved, LockStates.locked);
		} catch (AuthenticationException e) {
			fail("Wrong password");
		} catch (InvalidMemoryAccessException e) {
			fail("Memory is locked!");
		}
	}
	
	/**
	 * Test the kill command of the tag.
	 *
	 */
	@Test public void testKill(){
		try {
			c1g2.kill(killPass);
		} catch (AuthenticationException e) {
			fail("Wrong password");
		}
		//check if tag is really dead
		Assert.assertTrue(c1g2.getCurrentState().equals(C1G2Tag.states.Killed));
		Assert.assertTrue(c1g2.readId()==null);
	}	
}
