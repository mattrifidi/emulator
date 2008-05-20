//TODO: fix test

/*
 *  ReflectiveCommandAdapterTest.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.control.adapter;

import junit.framework.TestCase;

//import org.rifidi.emulator.reader.command.exception.alien.AlienExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandXMLDigester;
//import org.rifidi.emulator.reader.formatter.alien.AlienCommandFormatter;

/**
 * Test class for ReflectiveCommandAdapter. <br />
 * This test currently fails, howerver this is not the CommandAdapters fault. It
 * is a problem spawning from the XML file/CommandFormatter. Given an XML file
 * and a CommandFormatter that sync up, this test would succeed. After the first
 * reader is written, I can modify this test so it will pass.
 * 
 * @author Matthew Dean
 */
public class ReflectiveCommandAdapterTest extends TestCase {

	
//	AlienCommandFormatter newFormat;

	ReflectiveCommandAdapter newAdapter;

	CommandXMLDigester dig;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
//		newFormat = new AlienCommandFormatter();
		@SuppressWarnings("unused")
		String xmlFile = "src/org/rifidi/emulator/reader/commandhandler/alien/AlienALR9800.xml";
		dig = new CommandXMLDigester();
//		dig.parseToCommand(xmlFile);
//		newAdapter = new ReflectiveCommandAdapter("Interactive", newFormat,
//				dig, new AlienExceptionHandler(), null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tests the ExecuteCommand() method in ReflectiveCommandAdapter() <br />
	 * This method will fail if:<br />
	 * -An exception is thrown<br />
	 * <br />
	 * Currently this method fails, but it is a problem with the XML
	 * File/CommandFormatter. Given working versions of those methods, the
	 * method would pass.
	 */
	public void testExecuteCommand() {
		byte[] command = new String("set PersistTime=30").getBytes();
		newAdapter.executeCommand(command);
		
		command = new String("getWelcome").getBytes();
		newAdapter.executeCommand(command);
		
	}

	public void testExecuteCommand2() {
		byte[] command = new String("     Info  ").getBytes();
		newAdapter.executeCommand(command);
	}

}
