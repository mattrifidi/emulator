package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;

public interface Command {
	public ArrayList<Object> execute();
	
	/**
	 * 
	 * @return the original unmodified command sent to
	 *  the constructor of the command object
	 */
	public String toCommandString();
}
