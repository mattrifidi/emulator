/*
 *  AwidCommandFormatterTest.java
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

import java.util.ArrayList;

//import junit.framework.TestCase;

import org.rifidi.emulator.reader.awid.formatter.AwidAutonomousCommandFormatter;
import org.rifidi.emulator.reader.awid.formatter.AwidCommandFormatter;

/**
 * Test class for the AwidCommandFormatterFormatter and
 * AwidAutonomousCommandFormatter.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
//public class AwidCommandFormatterTest extends TestCase {
public class AwidCommandFormatterTest{
	AwidCommandFormatter newFormatter;

	AwidAutonomousCommandFormatter newAutoFormatter;

	/**
	 * Method called at the start
	 */
	public void setUp() {
		newFormatter = new AwidCommandFormatter();

		newAutoFormatter = new AwidAutonomousCommandFormatter();
	}

	/**
	 * Method called at the end.
	 */
	public void tearDown() {

	}

	/**
	 * Tests the autonomous encode statement.
	 */
	@SuppressWarnings("unchecked")
	public void testAutonomousEncode() {
		String testString = "12 16 11 00 00 00 00 00 00 61 05 43 18 15 86 01";

		ArrayList<Object> arg = new ArrayList<Object>();
		arg.add(testString);
		ArrayList retVal = newAutoFormatter.encode(arg);

		System.out.print("retVal for autonomous encode = ");
		for (Object i : retVal) {
			String x = (String) i;
			byte[] y = x.getBytes();
			for (byte a : y) {
				System.out.print(Integer.toHexString(a) + " ");
			}
		}
		System.out.println();
	}

	/**
	 * Tests the CRC for the system.  
	 */
	@SuppressWarnings("unchecked")
	public void testCRC() {
		String testString = "12 16 11 00 00 00 00 00 00 61 05 43 18 15 86 01";

		ArrayList<Object> arg = new ArrayList<Object>();
		arg.add(testString);
		ArrayList retVal = newAutoFormatter.encode(arg);

		System.out.print("retVal for autonomous encode = ");

		Object i = retVal.get(0);
		String x = (String) i;
		byte[] y = x.getBytes();
		
		if(y[y.length-2] != 12 || y[y.length-1] != 54 ) {
//			fail();
		}

		System.out.println();
	}

	/**
	 * Tests the autonomous decode method.
	 */
	@SuppressWarnings("unchecked")
	public void testAutonomousDecode() {
		byte[] testString = "05_11_07".getBytes();
		ArrayList retVal = newAutoFormatter.decode(testString);

		System.out.println("autonomous decode = " + retVal.get(0));
	}

	/**
	 * Tests the regular decode method.
	 */
	@SuppressWarnings("unchecked")
	public void testRegularDecode() {
		byte[] testString = "05_11_07".getBytes();
		ArrayList retVal = newAutoFormatter.decode(testString);

		System.out.println("autonomous decode = " + retVal.get(0));
	}

	/**
	 * Tests the regular encode method.
	 */
	@SuppressWarnings("unchecked")
	public void testRegularEncode() {
		String testString = "17 00 00 46 69 72 6D 77 61 72 "
				+ "65 20 56 65 72 20 33 2E 30 38 4D";

		ArrayList<Object> arg = new ArrayList<Object>();
		arg.add(testString);
		ArrayList retVal = newAutoFormatter.encode(arg);

		System.out.print("retVal for regular encode = ");
		for (Object i : retVal) {
			String x = (String) i;
			byte[] y = x.getBytes();
			for (byte a : y) {
				System.out.print(Integer.toHexString(a) + " ");
			}
		}
		System.out.println();
	}

}
