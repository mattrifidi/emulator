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

import java.math.BigInteger;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.rifidi.services.tags.exceptions.InvalidMemoryAccessException;
import org.rifidi.services.tags.impl.C0G1Tag;

/**
 * Test class for Class 0 Generation 1 tags
 * 
 * @author Jochen Mader
 *
 */
public class C0G1Test {
	private static C0G1Tag c0g1;
	private static BigInteger data;
	
	/**
	 * Initialize 
	 *
	 */
	@BeforeClass public static void initialize(){
		data=new BigInteger("1234577");
		c0g1=new C0G1Tag(data);
	}
	
	/**
	 * Write should fail on this tag type
	 *
	 */
	@Test public void testWrite(){
		try {
			c0g1.writeId((new BigInteger("12345")).toByteArray());
			fail("InvalidMemoryAccessException expected!");
		} catch (InvalidMemoryAccessException e) {
		}
	}
	
	/**
	 * Check the read operation
	 *
	 */
	@Test public void testRead(){
		byte[] id=c0g1.readId();
		byte[] databytes=data.toByteArray();
		for(int count=0;count<databytes.length;count++){
			if(!(id[count] == databytes[count])){
				fail("id in tag has changed!");
			}
		}
	}
}
