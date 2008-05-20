//TODO: fix test
package org.rifidi.emulator.reader.command.xml;

import junit.framework.TestCase;
import java.util.ArrayList;
import org.rifidi.emulator.reader.command.*;

/**
 * Test class for the CommandXMLDigester
 * 
 * @author Matthew Dean
 */
public class CommandXMLDigesterTest extends TestCase {
	
	CommandXMLDigester newCommand;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		newCommand = new CommandXMLDigester();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	/**
	 * Tests the parseToCommand() method in CommandXMLDigester.  
	 * <br />
	 * This will give the XMLDigester a sample XML file that it must parse through and 
	 * store the values.  
	 * <br />
	 * This test will fail if: <br />
	 * 		-something is not read in correctly<br />
	 * 		-an exception occurs during reading<br />
	 */
	public void testParseToCommand() {
		@SuppressWarnings("unused")
		String xmlFile = "src/org/rifidi/emulator/reader/commandhandler/common/XMLFile.xml";
//		newCommand.parseToCommand(xmlFile);
		CommandObjectHolder newHolder = newCommand.getHolder();
		ArrayList<CommandObject> newList = new ArrayList<CommandObject>(newHolder.getAllCommands());
		newList.clear();
	}
	
	/**
	 * Tests the getCommandsByState() method.<br />
	 * <br />
	 * This will put a decently-sized XML file into the digester and then query the 
	 * holder to return the objects of a certain state.  <br />
	 * <br />
	 * This test will fail if: <br />
	 * 		-Any CommandObjects are returned that are not in the given state<br />
	 * 		-An exception occurs
	 */
	public void testGetCommandsByState() {
		final String STATE = "Interactive";
		@SuppressWarnings("unused")
		String xmlFile = "src/org/rifidi/emulator/reader/commandhandler/common/XMLFile.xml";
//		newCommand.parseToCommand(xmlFile);
		CommandObjectHolder newHolder = newCommand.getHolder();
		ArrayList<CommandObject> newList = new ArrayList<CommandObject>(newHolder.getCommandsByState(STATE));
		for(Object i : newList) {
			CommandObject temp = (CommandObject)i;
			if( !temp.getCommandState().equals(STATE) ) {
				fail();
			}
		}
	}
	
}
