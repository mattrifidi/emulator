/*
 *  TestCRC.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.formatter;

/**
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class TestCRC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		boolean[] boolList = new boolean[9];
		int[] crcList = new int[9];
		for (int i = 0; i < boolList.length; i++) {
			boolList[i] = false;
			crcList[i] = 0x0000;
		}

		boolean sentinal = true;

		int data[] = { 0x17, 0x00, 0x00, 0x46, 0x69, 0x72, 0x6D, 0x77, 0x61, 0x72, 0x65, 0x20, 0x56, 0x65, 0x72, 0x20, 0x33, 0x2E, 0x30, 0x38, 0x4D };

		int crc = crcList[0];
		
		for (int x = 0; sentinal; x++) {

			if (x % 2 != 0) {
				boolList[0] = true;
				crcList[0] = 0xFFFF;
			} else {
				boolList[0] = false;
				crcList[0] = 0x0000;
			}
			
			if ((x/2) % 2 != 0) {
				boolList[1] = true;
				crcList[1] = 0xFFFF;
			} else {
				boolList[1] = false;
				crcList[1] = 0x0000;
			}
			
			if ((x/4) % 2 != 0) {
				boolList[2] = true;
				crcList[2] = 0xFFFF;
			} else {
				boolList[2] = false;
				crcList[2] = 0x0000;
			}
			
			if ((x/8) % 2 != 0) {
				boolList[3] = true;
				crcList[3] = 0xFFFF;
			} else {
				boolList[3] = false;
				crcList[3] = 0x0000;
			}
			
			if ((x/16) % 2 != 0) {
				boolList[4] = true;
				crcList[4] = 0xFFFF;
			} else {
				boolList[4] = false;
				crcList[4] = 0x0000;
			}
			
			if ((x/32) % 2 != 0) {
				boolList[5] = true;
				crcList[5] = 0xFFFF;
			} else {
				boolList[5] = false;
				crcList[5] = 0x0000;
			}
			
			if ((x/64) % 2 != 0) {
				boolList[6] = true;
				crcList[6] = 0xFFFF;
			} else {
				boolList[6] = false;
				crcList[6] = 0x0000;
			}
			
			if ((x/128) % 2 != 0) {
				boolList[7] = true;
				crcList[7] = 0xFF;
			} else {
				boolList[7] = false;
				crcList[7] = 0x00;
			}
			
			if ((x/256) % 2 != 0) {
				boolList[8] = true;
				crcList[8] = 0xFF;
			} else {
				boolList[8] = false;
				crcList[8] = 0x00;
			}
			
			System.out.print("CRCLIST FOR " + x + " IS: ");
			for( int i=0;i<boolList.length;i++ ) {
				System.out.print(" " + boolList[i]);
			}
			System.out.println();
			
			for (int i = 0; i < data.length; i++) {
				crc = (crc >> 8) | ((crc << 8) & crcList[1]);
				crc = crc ^ data[i];
				crc = crc ^ ((crc & crcList[7]) >> 4);
				crc = crc ^ ((((crc << 8) & crcList[2]) << 4) & crcList[3]);
				crc = crc
						^ (((((crc & crcList[8]) << 4) & crcList[4]) << 1) & crcList[5]);
			}

			crc = ~crc & crcList[6];
			System.out.println(Integer.toHexString(crc));

			if (Integer.toHexString(crc).equalsIgnoreCase("9f05")) {
				System.out
						.println("\n\n RESULT FOUND\n\n RESULT = " + boolList);
				sentinal = false;
			}
			boolean isFalse = false;
			for (int i = 0; i < boolList.length; i++) {
				if (!boolList[i]) {
					isFalse = true;
				}
			}
			if (!isFalse) {
				System.out.println("NO RESULT");
				sentinal = false;
			}
		}
	}

}
