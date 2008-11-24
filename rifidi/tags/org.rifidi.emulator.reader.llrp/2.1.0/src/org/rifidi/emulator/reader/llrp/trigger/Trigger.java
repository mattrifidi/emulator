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
 * All triggers need to have a spec state that they can change to true (running)
 * or false(not running)
 * 
 * @author Kyle Neumeier
 */
public interface Trigger {

	/**
	 * This mehtod sets the control signal to change when a trigger fires;
	 * 
	 * @param specSignal
	 */
	public void setTriggerObservable(TriggerObservable triggerObservable);
	
	/**
	 * This method should be called after this trigger's associated rospec or AISpec is deleted
	 */
	public void cleanUp();
	
	public void suspend();
	
	public void resume();
}
