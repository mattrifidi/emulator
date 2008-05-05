/*
 *  ExtraInformation.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.extra;

import java.util.HashMap;


/**
 * This class contains extra information about a certain command.  This
 * information includes: the name of the command mapped to how often
 * to run the command, and how long to run 
 * the command (limited by time, limited by cycles, and unlimited).  
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class CommandInformation implements ExtraInformation {

	/**
	 * This constant can be set to either run by a time-limited function 
	 * (in milliseconds), a cycle-limited function (number of times the
	 * method is called) or an unlimited function (a method that waits for
	 * a specific stop command to interrupt it).
	 *
	 * @author Matthew Dean - matt@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 */
	public enum LimitedRunningState {
		/**
		 * Limited by time (in milliseconds)
		 */
		TIME_LIMITED,

		/**
		 * Limited by number of cycles
		 */
		CYCLE_LIMITED,

		/**
		 * Not limited (waiting for an interrupt signal
		 */
		NOT_LIMITED;
	}
	
	/**
	 * The map for each command
	 */
	private HashMap<byte[], Integer> commandMap;

	/**
	 * How many milliseconds each command should be called 
	 * for/how many cycles each command should be called for
	 */
	private int value;
	
	/**
	 * The state that each command is getting called in.  
	 */
	private LimitedRunningState state;

	/**
	 * Sets extra information that will be used to run autonomous commands 
	 * 
	 * @param commandMap	The map for each command mapped to how often each 
	 * 						command will be run.  
	 * @param value			How many milliseconds each command should be called 
	 * 						for/how many cycles each command should be called for
	 * @param state			The state that each command is getting called in.  
	 */
	public CommandInformation(HashMap<byte[], Integer> commandMap, int value,
			LimitedRunningState state) {
		this.commandMap = commandMap;
		this.state = state;
		this.value = value;
	}

	/**
	 * @return the commandMap
	 */
	public HashMap<byte[], Integer> getCommandMap() {
		return commandMap;
	}

	/**
	 * @param commandMap the commandMap to set
	 */
	public void setCommandMap(HashMap<byte[], Integer> commandMap) {
		this.commandMap = commandMap;
	}

	/**
	 * @return the state
	 */
	public LimitedRunningState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(LimitedRunningState state) {
		this.state = state;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
}
