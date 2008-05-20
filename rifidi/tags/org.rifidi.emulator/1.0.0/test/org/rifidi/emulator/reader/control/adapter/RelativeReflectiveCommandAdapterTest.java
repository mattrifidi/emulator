//TODO: fix test
/*
 *  RelativeReflectiveCommandAdapterTest.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.control.adapter;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tests the RelativeReflectiveCommandAdapter.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class RelativeReflectiveCommandAdapterTest extends TestCase {

	ReflectiveCommandAdapter newAdapter;

	private static Log logger = LogFactory
			.getLog(RelativeReflectiveCommandAdapterTest.class);
	
	byte[] command;

	/**
	 * Method called at the start
	 */
	public void setUp() {
		logger.debug("setting up");
//		AwidReaderSharedResources awidSharedResources = new AwidReaderSharedResources(
//				null, null, null, new ControlSignal<Boolean>(
//						false), new ControlSignal<Boolean>(false),
//				new ControlSignal<Boolean>(false), new ControlSignal<Boolean>(
//						true), "alien_reader_1");

//		CommandXMLDigester digester = new CommandXMLDigester();
//		digester.parseToCommand("src/org/rifidi/emulator/reader/"
//				+ "commandhandler/awid/AwidMPR.xml");
//
//		newAdapter = new RelativeReflectiveCommandAdapter("Interactive",
//				new AwidCommandFormatter(), digester,
//				new AwidExceptionHandler(), awidSharedResources,
//				new AwidValueInitializer());
	}

	/**
	 * Method called at the end.
	 */
	public void tearDown() {

	}

	/**
	 * 
	 * 
	 *
	 */
	@SuppressWarnings("unchecked")
	public void testExecuteCommand() {
		command = new byte[] {0x05,0x00,0x00,(byte)0xDA,0x53};
		ArrayList execute = newAdapter.executeCommand(command);
		System.out.println(execute);
	}
}
