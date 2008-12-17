/*
 *  TagDataHelper.java
 *
 *  Created:	Sep 25, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Helper class to create random, valid tag ids and to verify existing ids
 * @author Jochen Mader
 *
 */
public class TagDataHelper {
	private static SecureRandom secureRandom = new SecureRandom();
	
	/* 8 bits for header (value for this tagtype
	 * is 2F) 
	 * 4 bits for filter 
	 * 48 bits for government manged id 
	 * 36 bits for serial
	 * example: 2F-1-1111-2222
	 */
	
	/**
	 * Create a random DoD96 id
	 */
	public static BigInteger getRandomDoD96() {		
		BigInteger random=new BigInteger("2F0000000000000000000000",16);
		random=random.or(new BigInteger(4,secureRandom).shiftLeft(84));
		random=random.or(new BigInteger(48,secureRandom).shiftLeft(36));
		random=random.or(new BigInteger(36,secureRandom));
		return random;
	}
	
	public static BigInteger parseDoD96(String encoding) throws NumberFormatException {
		if(!encoding.substring(0,2).equals("2F")){
			throw new NumberFormatException("Not a DoD-96 Tag header.");
		}
		if(encoding.length()>24){
			throw new NumberFormatException("Tag data too long");
		}
		if(encoding.length()<24){
			throw new NumberFormatException("Tag data too short");
		}
		BigInteger temp=new BigInteger(encoding,16);
		return temp;
	}
	
	/*
	 * This class represents a GID-96 Tag. 
	 * 8 bits for header (value for this tagtype
	 * is 35) 
	 * 28 bits for general manager id 
	 * 24 bits for object class
	 * 36 bits for serial
	 * example: 2F-2222-1111-2222
	 */
	
	/**
	 * Create a random GID96 id
	 */
	public static BigInteger getRandomGID96() {
		BigInteger random=new BigInteger("350000000000000000000000",16);
		random=random.or(new BigInteger(28,secureRandom).shiftLeft(60));
		random=random.or(new BigInteger(24,secureRandom).shiftLeft(36));
		random=random.or(new BigInteger(36,secureRandom));
		return random;
	}
	
	public static BigInteger parseGID96(String encoding) throws NumberFormatException {
		if(!encoding.substring(0,2).equals("35")){
			throw new NumberFormatException("Not a GID-96 Tag header.");
		}
		if(encoding.length()>24){
			throw new NumberFormatException("Tag data too long");
		}
		if(encoding.length()<24){
			throw new NumberFormatException("Tag data too short");
		}
		BigInteger temp=new BigInteger(encoding,16);
		temp.and(new BigInteger("00FFFFFFF000000000000000",16).shiftRight(15*4));
		temp.and(new BigInteger("000000000FFFFFF000000000",16).shiftRight(9*4));
		temp.and(new BigInteger("000000000000000FFFFFFFFF",16));
		return temp;
	}

	/*
	 * This class represents a SGTIN-96 tag.
	 * 8 bits header (value for this tagtype is 30)
	 * 3 bits filter
	 * 3 bits partition
	 * 20-40 bits company prefix
	 * 24-4 bits item reference
	 * 38 bits serial 
	 * 
	 * the partition denotes the number of bits available in company prefix and in
	 * item reference
	 */
	
	/**
	 * Create a random SGTIN96 id
	 */
	public static BigInteger getRandomSGTIN96() {
		BigInteger random=new BigInteger("300000000000000000000000",16);
		random=random.or(new BigInteger(3,secureRandom).shiftLeft(85));
		BigInteger part=new BigInteger(3,secureRandom);
		if(part.intValue()>6){
			part=new BigInteger("6");
		}
		random=random.or(part.shiftLeft(82));	
		switch(part.intValue()){
			case 0: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,12)-1)),10).shiftLeft(42));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,1))),10).shiftLeft(38));
					break;
			case 1: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,11)-1)),10).shiftLeft(45));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,2))),10).shiftLeft(38));
					break;
			case 2: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,10)-1)),10).shiftLeft(48));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,3))),10).shiftLeft(38));
					break;
			case 3: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,9)-1)),10).shiftLeft(52));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,4))),10).shiftLeft(38));
					break;
			case 4: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,8)-1)),10).shiftLeft(55));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,5))),10).shiftLeft(38));
					break;
			case 5: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,7)-1)),10).shiftLeft(58));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,6))),10).shiftLeft(38));
					break;
			case 6: 
					random=random.or(new BigInteger(Long.toString((long)Math.sqrt(Math.pow(secureRandom.nextLong(),2))%((long)Math.pow(10,6)-1)),10).shiftLeft(62));
					random=random.or(new BigInteger(Integer.toString(secureRandom.nextInt((int)Math.pow(10,7))),10).shiftLeft(38));
					break;					
		}
		random=random.or(new BigInteger(38,secureRandom));

		return random;
	}
	
	public static BigInteger parseSGTIN96(String encoding) throws NumberFormatException {
		if(!encoding.substring(0,2).equals("30")){
			throw new NumberFormatException("Not a SGTIN-96 Tag header.");
		}
		if(encoding.length()>24){
			throw new NumberFormatException("Tag data too long");
		}
		if(encoding.length()<24){
			throw new NumberFormatException("Tag data too short");
		}
		BigInteger temp=new BigInteger(encoding,16);
		return temp;
	}

	/*
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
	 */
	
	/**
	 * Create a random SSCC96 id
	 */
	public static BigInteger getRandomSSCC96() {
		BigInteger random=new BigInteger("310000000000000000000000",16);
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
		return random;
	}
	
	public static BigInteger parseSSCC96(String encoding) throws NumberFormatException {
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
