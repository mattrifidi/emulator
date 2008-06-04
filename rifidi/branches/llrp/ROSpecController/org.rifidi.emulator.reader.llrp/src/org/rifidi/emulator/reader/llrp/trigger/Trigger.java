/*
 *  Trigger.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.trigger;

/**
 * 
 * All triggers should implement these methods
 * 
 * @author Kyle Neumeier
 */
public interface Trigger {

	/**
	 * This method is called when the trigger needs to do something, such as
	 * start a ROSpec.
	 */
	public void fireTrigger();

	/**
	 * This method sets up the trigger to operate, for example to start a timer
	 */
	public void enable();

	/**
	 * This method disables the trigger, for example to turn off timers
	 */
	public void disable();

	/**
	 * This method should be called after this trigger's associated rospec or
	 * AISpec is deleted
	 */
	public void cleanUp();

	/**
	 * Suspend the trigger
	 */
	public void suspend();

	/**
	 * Resume a suspended timer
	 */
	public void resume();

}
