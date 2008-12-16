/*
 *  CursorCommandRegistry.java
 *
 *  Created:	August 7, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.commandregistry;

import java.util.HashMap;

import org.rifidi.emulator.reader.thingmagic.commandobjects.Command;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SelectCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.UpdateCommand;

/**
 * @author Jerry Maine - jerry@pramari.com
 * 
 * This class serves as a registery of commands that can be used by the
 * AutoMode controller or the fetch command.
 *
 */
public class CursorCommandRegistry {
	HashMap<String, Command> registry = new HashMap<String, Command>();
	/**
	 * 
	 */
	private static final long serialVersionUID = -6478837395873055762L;

	public Command put(String key, Command value) throws IllegalStateException, ClassCastException {
		if ((value instanceof UpdateCommand)
				|| (value instanceof SelectCommand)) {
//			if (registry.size() <= 16) {
				return registry.put(key, value);
//			} else {
//				throw new IllegalStateException();
//			}
		} else {
			throw new ClassCastException();
		}
	}

	public Command get(String key) {
		return registry.get(key);
	}
	
	public int size(){
		return registry.size();
	}
	
	public void remove(String key){
		registry.remove(key);
	}
	
	public boolean containsKey(String key){
		return registry.containsKey(key);
	}
	
	public void clear(){
		registry.clear();
	}
}
