/*
 *  InteractiveCommandSentinel.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.command.sentinel;

import java.util.ArrayList;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class InteractiveCommandSentinelTest extends TestCase {

	private static final Log logger = LogFactory
			.getLog(InteractiveCommandSentinelTest.class);
	
	InteractiveCommandSentinel sentinel;

	public void setUp() {
	}

	public void tearDown() {

	}

	/**
	 * Test the parsing of the CommandSentinel.
	 */
	@SuppressWarnings("unchecked")
	public void testParse() {

		sentinel = new InteractiveCommandSentinel(
				"reader_xml_lib/Alien_ALR9800/AlienALR9800Sentinel.xml");

		Map<String, ArrayList<byte[]>> arrayMap = sentinel.getCommandMap();
		logger.debug("keySet = " + arrayMap.keySet());
		logger.debug("values are");
		for (ArrayList i : arrayMap.values()) {
			boolean a = true, b = true;
			for (Object x : i) {
				if (new String((byte[]) x).equals("get TagList_Pre")) {
					a = false;
					logger.debug("getTagList_Pre ");
				} else if (new String((byte[]) x).equals("get TagList")) {
					b = false;
					logger.debug("getTagList ");
				} else {
					logger.debug("neither: " + new String((byte[]) x)+", ");
				}
			}
			logger.debug("past the for loop");
			if (a || b) {
				fail();
			}
		}

		logger.debug("size for gettaglist is:"
				+ sentinel.getCommandList("gettaglist".getBytes()).size());
	}
}
