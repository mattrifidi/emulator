/*
 *  EPCReaderModuleOffPowerState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.epc.module;

import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.module.abstract_.AbstractOffPowerState;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class EPCReaderModuleOffPowerState extends AbstractOffPowerState {

	/**
	 * The singleton instance for this class.
	 */
	private static final EPCReaderModuleOffPowerState SINGLETON_INSTANCE = new EPCReaderModuleOffPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static EPCReaderModuleOffPowerState getInstance() {
		return EPCReaderModuleOffPowerState.SINGLETON_INSTANCE;
	}

	/**
	 * Constructor which is private to support singleton instance.
	 */
	private EPCReaderModuleOffPowerState() {
	}

	public void turnOn(PowerControllable pcObject) {
		EPCReaderModule epcMod = (EPCReaderModule) pcObject;

		epcMod.getInteractiveCommunication().turnOn();
		
		epcMod.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(true);
		epcMod.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);
		
		epcMod.changePowerState(EPCReaderModuleOnPowerState.getInstance());
		
		String readername = epcMod.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " on");
	}

}
