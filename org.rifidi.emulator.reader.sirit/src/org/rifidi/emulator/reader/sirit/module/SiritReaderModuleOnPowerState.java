/*
 *  SiritReaderModuleOnPowerState.java
 *
 *  Created:	10.06.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.module;

import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.module.abstract_.AbstractOnPowerState;

/**
 * represents the on state of the Sirit reader and contains the transition
 * functions for how to go from on->off and on->suspend
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritReaderModuleOnPowerState extends AbstractOnPowerState {

	/**
	 * Private instance
	 */
	private static final SiritReaderModuleOnPowerState instance = new SiritReaderModuleOnPowerState();

	/**
	 * Gets a singleton instance of this class.
	 * 
	 * @return
	 */
	public static SiritReaderModuleOnPowerState getInstance() {
		return instance;
	}

	/**
	 * Private Constructor
	 */
	private SiritReaderModuleOnPowerState() {
	}

	/*
	 * move the reader into the suspended state (= suspending the communication
	 * that the reader is using)
	 * 
	 * @see
	 * org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common
	 * .PowerControllable)
	 */
	public void suspend(PowerControllable pcObject) {
		SiritReaderModule rm = (SiritReaderModule) pcObject;

		// suspend communication and controller
		rm.getTCPComm().suspend();
		rm.getInteractiveCommandController().suspend();

		// suspend tag memory
		rm.getSharedResources().getTagMemory().suspend();

		// move the reader into the SuspendedState
		rm.changePowerState(SiritReaderModuleSuspendedPowerState.getInstance());

		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(
				readername + " suspended");
	}

	/*
	 * turn the reader and all of its components off
	 * 
	 * @see
	 * org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common
	 * .PowerControllable, java.lang.Class)
	 */
	public void turnOff(PowerControllable pcObject) {
		SiritReaderModule rm = (SiritReaderModule) pcObject;

		// turn off communication object and controller
		rm.getInteractiveCommandController().turnOff();
		// turns off the communication by turning off the communication
		// object's power and connection signal
		rm.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(false);
		rm.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);

		// move the reader into the 'off' state
		rm.changePowerState(SiritReaderModuleOffPowerState.getInstance());

		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");
	}
}