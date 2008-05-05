/*
 *  @(#)CommandHandlerInvokerTest.java
 *
 *  Created:	Oct 2, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.commandhandler;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.rifidi.emulator.reader.command.CommandObject;

/**
 * A collection of unit test cases for the CommandHandlerInvoker. <br>
 * 
 * @author Matt Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class CommandHandlerInvokerTest extends TestCase {

	private CommandObject newObj;

	/**
	 * Set up the jUnit test
	 */
	@Override
	protected void setUp() throws Exception {
		newObj = new CommandObject();
		newObj.setArgumentType("String");
		ArrayList<Object> newList = new ArrayList<Object>();
		newList.add((Object) (new String("Alien")));
		newObj.setArguments(newList);
		newObj.setHandlerClass("org.rifidi.emulator.reader.commandhandler.alien.AlienHandler");
		newObj.setHandlerPackage("org.rifidi.emulator.reader.commandhandler.alien");
		newObj.setHandlerMethod("handlerMethod");
		//newObj.setCurrentValue("Alien 1.4");
		newObj.setDefaultValue("Alien 1.4");
		newObj.setDisplayName("zomgMethod");
		ArrayList<String> tempList = new ArrayList<String>();
		tempList.add("AlienReader");
		newObj.setName(tempList);
		super.setUp();
	}

	/**
	 * Tear down the jUnit test
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Calls the CommandHandlerInvoker invokeHandler method with the object
	 * created in setUp().
	 * <br />
	 * The method will suceed if: -Reflection is called correctly -The
	 * currentValue of the CommandObject returned from the method matches the
	 * currentValue of the native CommandObject
	 * <br />
	 * The method will fail if: 
	 * <br />-Reflection is not called correctly 
	 * <br />-The returnValue is not equal to the argument passed in
	 */
	public void testInvokeHandler() {
		CommandObject testObj = CommandHandlerInvoker.invokeHandler(newObj, null, new GenericExceptionHandlerImpl());
		
		assertEquals(testObj.getReturnValue().get(0), newObj.getArguments().get(0));

	}
}
