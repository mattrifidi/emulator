package org.rifidi.emulator.xml;

import java.util.ArrayList;
import java.util.Collection;

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.CommandObjectHashMap;

import junit.framework.TestCase;


public class CommandObjectHashTest extends TestCase {

	CommandObjectHashMap newMap;
	CommandObject newObj;
	CommandObject obj2;
	CommandObject obj3;
	
	protected void setUp() throws Exception {
		newMap = new CommandObjectHashMap();
		
		newObj = new CommandObject();
		newObj.setArgumentType("String");
		ArrayList<Object> newList = new ArrayList<Object>();
		newList.add((Object)(new String("Alien")));
		newObj.setArguments(newList);
		newObj.setHandlerClass( "com.pramari.sandbox.test.CommandHandlerInvokerTest" );
		newObj.setHandlerPackage("com.pramari.sandbox.test");
		newObj.setHandlerMethod("methodTest");
		//newObj.setCurrentValue("Alien 1.4");
		newObj.setDefaultValue("Alien 1.4");
		newObj.setDisplayName("zomgMethod");
		ArrayList<String> tempList = new ArrayList<String>();
		tempList.add("InteractiveMethod");
		newObj.setName(tempList);
		newObj.setCommandState("Interactive");
		
		
		obj2 = new CommandObject();
		obj2.setArgumentType("String");
		ArrayList<Object> newList2 = new ArrayList<Object>();
		newList2.add((Object)(new String("Alien")));
		obj2.setArguments(newList2);
		obj2.setHandlerClass( "com.pramari.sandbox.test.CommandHandlerInvokerTest" );
		obj2.setHandlerPackage("com.pramari.sandbox.test");
		obj2.setHandlerMethod("methodTest");
		//obj2.setCurrentValue("Alien 1.5");
		obj2.setDefaultValue("Alien 1.5");
		obj2.setDisplayName("zomgMethod");
		ArrayList<String> tempList2 = new ArrayList<String>();
		tempList2.add("AutonomousMethod");
		obj2.setName(tempList2);
		obj2.setCommandState("Autonomous");
		
		obj3 = new CommandObject();
		obj3.setArgumentType("String");
		ArrayList<Object> newList3 = new ArrayList<Object>();
		newList3.add((Object)(new String("Alien")));
		obj3.setArguments(newList3);
		obj3.setHandlerClass( "com.pramari.sandbox.test.CommandHandlerInvokerTest" );
		obj3.setHandlerPackage("com.pramari.sandbox.test");
		obj3.setHandlerMethod("methodTest");
		//obj3.setCurrentValue("Alien 1.6");
		obj3.setDefaultValue("Alien 1.6");
		obj3.setDisplayName("zomgMethod");
		ArrayList<String> tempList3 = new ArrayList<String>();
		tempList3.add("LoginMethod");
		obj3.setName(tempList3);
		obj3.setCommandState("Login");
		
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testAdd() {
		newMap.addCommandObject( newObj );
		newMap.addCommandObject( obj2 );
		newMap.addCommandObject( obj3 );
	}
	
	public void testGetByState() {
		newMap.addCommandObject( newObj );
		newMap.addCommandObject( obj2 );
		newMap.addCommandObject( obj3 );
		
		ArrayList<CommandObject> loginList = (ArrayList<CommandObject>)newMap.getCommandsByState("Login");
		System.out.println( loginList.get(0).toString() );
	}
	
	public void testGetAll() {
		newMap.addCommandObject( newObj );
		newMap.addCommandObject( obj2 );
		newMap.addCommandObject( obj3 );
		
		Collection<CommandObject> newList = newMap.getAllCommands();
		ArrayList<CommandObject> allList = new ArrayList<CommandObject>(newList);
		boolean x=false, y=false, z=false;
		for(int i = 0; i < newList.size(); i++) {
			CommandObject c = (CommandObject)allList.get(i);
			if(c.equals(newObj)) {
				x=true;
			}
			if(c.equals(obj2)) {
				y=true;
			}
			if(c.equals(obj3)) {
				z=true;
			}
		}
		if( !x ||  !y || !z ) {
			fail();
		}
	}

}
