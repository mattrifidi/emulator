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
import org.rifidi.services.tags.impl.C1G1Tag;

/**
 * Class to test the Class 1 Generation 1 tag
 * @author Jochen Mader
 *
 */
public class C1G1Test {
	private static C1G1Tag c1g1;
	private static byte[] data={0x01, 0x02, 0x03, 0x04, 0x05};
	
	/**
	 * Initialize everything 
	 *
	 */
	@BeforeClass public static void initialize(){
		c1g1=new C1G1Tag(data);
	}
	
	/**
	 * First write should succeed
	 *
	 */
	@Test public void testWrite(){
		try {
			data=new byte[7];
			data[0]=0x01;
			data[1]=0x02;
			data[2]=0x03;
			data[3]=0x04;
			data[4]=0x05;
			data[5]=0x06;
			data[6]=0x07;
			
			c1g1.writeId(data);
		} catch (InvalidMemoryAccessException e) {
			fail("First write should succeed");
		}
	}
	
	/**
	 * Check the read function
	 *
	 */
	@Test public void testRead(){
		byte[] id=c1g1.readId();
		byte[] databytes=data;
		for(int count=0;count<databytes.length;count++){
			if(!(id[count] == databytes[count])){
				fail("id in tag is wrong changed! ");
			}
		}
	}
	
	/**
	 * Second write should fail
	 *
	 */
	@Test public void testSecondWrite(){
		try {
			BigInteger newdata=new BigInteger("12345777");
			c1g1.writeId(newdata.toByteArray());
			fail("Second write should fail");			
		} catch (InvalidMemoryAccessException e) {
		}
		
		byte[] id=c1g1.readId();
		byte[] databytes=data;
		for(int count=0;count<databytes.length;count++){
			if(!(id[count] == databytes[count])){
				fail("id in tag has changed!");
			}
		}
	}
}
