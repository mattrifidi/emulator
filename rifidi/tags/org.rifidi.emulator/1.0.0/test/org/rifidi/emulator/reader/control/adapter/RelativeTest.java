/*
 *  RelativeTest.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.control.adapter;


/**
 *
 *
 * @author Matthew Dean
 * @since  <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class RelativeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testRelative("05_17_82_FF");
	}
	
	public static void testRelative(String commandName) {
		String currentCommandName = "";
		String[] tempStringTokens = commandName.split("_");
		int tempStringTokenNumber = 0;
		while (tempStringTokenNumber < tempStringTokens.length) {
			if (tempStringTokenNumber != 0) {
				currentCommandName += "_";
			}
			
			currentCommandName += tempStringTokens[tempStringTokenNumber];
			//logger.debug("The current command name is: " + currentCommandName);
			System.out.println("The current command name is: " + currentCommandName);
			++tempStringTokenNumber;
		}
	}

}
