/*
 *  SiritReaderModuleSuspendedPowerState.java
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
import org.rifidi.emulator.reader.module.abstract_.AbstractSuspendedPowerState;

/**
 * represents the suspended state of the Sirit reader and contains the
 * transition functions for how to go from suspended->on and suspended->off
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritReaderModuleSuspendedPowerState extends
		AbstractSuspendedPowerState {

	/**
	 * Private instance
	 */
	private static final SiritReaderModuleSuspendedPowerState instance = new SiritReaderModuleSuspendedPowerState();

	/**
	 * Gets a singleton instance of this class.
	 * 
	 * @return
	 */
	public static SiritReaderModuleSuspendedPowerState getInstance() {
		return instance;
	}

	/**
	 * Private Constructor
	 */
	private SiritReaderModuleSuspendedPowerState() {
	}

	/*
	 * turn the reader and all of its components back on after having been
	 * suspended
	 * 
	 * @see
	 * org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common
	 * .PowerControllable)
	 */
	public void resume(PowerControllable pcObject) {
		SiritReaderModule rm = (SiritReaderModule) pcObject;

		// resume the communication and the controllers
		rm.getTCPComm().resume();
		rm.getInteractiveCommandController().resume();

		// resume tag memory
		rm.getSharedResources().getTagMemory().resume();

		// change the power state to the OnPowerSate
		rm.changePowerState(SiritReaderModuleOnPowerState.getInstance());

		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername)
				.info(readername + " resumed");
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