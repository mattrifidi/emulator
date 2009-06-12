/*
 *  SiritReaderModuleOffPowerState.java
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
import org.rifidi.emulator.reader.module.abstract_.AbstractOffPowerState;

/**
 * represents the off state of the Sirit reader and contains the transition
 * functions for how to go from off->on
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritReaderModuleOffPowerState extends AbstractOffPowerState {

	/**
	 * Private instance
	 */
	private static final SiritReaderModuleOffPowerState instance = new SiritReaderModuleOffPowerState();

	/**
	 * Gets a singleton instance of this class.
	 * 
	 * @return
	 */
	public static SiritReaderModuleOffPowerState getInstance() {
		return instance;
	}

	/**
	 * Private Constructor
	 */
	private SiritReaderModuleOffPowerState() {
	}

	/*
	 * turn the reader and all of its components on
	 * 
	 * @see
	 * org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common
	 * .PowerControllable)
	 */
	public void turnOn(PowerControllable pcObject) {
		SiritReaderModule rm = (SiritReaderModule) pcObject;
		// turn on the communication, then turn on the controllers
		rm.getTCPComm().turnOn();
		rm.getInteractiveCommandController().turnOn();

		// set the power signal for the communication to true
		rm.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(true);
		// make sure the connection signal is set to false
		rm.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);

		// put the reader into the 'on' state
		rm.changePowerState(SiritReaderModuleOnPowerState.getInstance());

		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " on");
	}

}
