/*
 *  SSCC96.java
 *
 *  Created:	Mar 1, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.id;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class represents a SSCC-96 tag.
 * 8 bits header(value for this tagtype is 31)
 * 3 bits filter
 * 3 bits partition
 * 20-40 bits company prefix
 * 38-18 bits serial reference
 * 24 bits unallocated (has to be filled with 0s to be conform with spec) 
 * 
 * the partition denotes the number of bits available in company prefix and in
 * serial reference
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class SSCC96 {

	public static final String header = "31";
	
	private static SecureRandom secureRandom = new SecureRandom();

	protected static byte[] getRandomTagData(String prefix) {
		BigInteger random=new BigInteger("310000000000000000000000",16);
//		BigInteger filter=new BigInteger(2,secureRandom);
//		if(filter.intValue()>2){
//			filter=new BigInteger("2");
//		}
		BigInteger filter=new BigInteger("0");
		random=random.or(filter.shiftLeft(85));
		BigInteger part=new BigInteger(3,secureRandom);
		if(part.intValue()>6){
			part=new BigInteger("6");
		}
		random=random.or(part.shiftLeft(82));   
		switch(part.intValue()){
			case 0: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,12)-1)),10).shiftLeft(42));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,5))),10).shiftLeft(24));
					break;
			case 1: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,11)-1)),10).shiftLeft(45));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,6))),10).shiftLeft(24));
					break;
			case 2: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,10)-1)),10).shiftLeft(48));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,7))),10).shiftLeft(24));
					break;
			case 3: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,9)-1)),10).shiftLeft(52));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,8))),10).shiftLeft(24));
					break;
			case 4: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,8)-1)),10).shiftLeft(55));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,9))),10).shiftLeft(24));
					break;
			case 5: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,7)-1)),10).shiftLeft(58));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,10))),10).shiftLeft(24));
					break;
			case 6: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,6)-1)),10).shiftLeft(62));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,11))),10).shiftLeft(24));
					break;					
		}
		return random.toByteArray();
	}
	
	public BigInteger parseEncoding(String encoding) throws NumberFormatException {
		if(!encoding.substring(0,2).equals("31")){
			throw new NumberFormatException("Not a GID-96 Tag header.");
		}
		if(encoding.length()>24){
			throw new NumberFormatException("Tag data too long");
		}
		if(encoding.length()<24){
			throw new NumberFormatException("Tag data too short");
		}
		if(!encoding.endsWith("000000")){
			throw new NumberFormatException("Last 24 bits of SSCC-96 have to be 0s.");
		}
		BigInteger temp=new BigInteger(encoding,16);
		return temp;
	}

}
