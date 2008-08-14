package org.rifidi.emulator.reader.thingmagic.commandregistry;

import java.util.HashMap;

import org.rifidi.emulator.reader.thingmagic.commandobjects.Command;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SelectCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.UpdateCommand;



/*
 * We extend instead of contains to cut down the amount of scaffolding code, 
 * and to move on to other things.  We can revisit this latter if need be.
 *
 */
//TODO contain hashmap not extends.
public class CommandRegistry extends HashMap<String, Command>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6478837395873055762L;

	@Override
	public Command put(String key, Command value){
		if ( (value instanceof UpdateCommand) || (value instanceof SelectCommand)){
			return super.put(key, value);
		} else {
			throw new ClassCastException();
		}
	}
}
