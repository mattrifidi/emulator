/*
 *  EPCReaderModuleSuspendedPowerState.java
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
import org.rifidi.emulator.reader.module.abstract_.AbstractSuspendedPowerState;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class EPCReaderModuleSuspendedPowerState extends
		AbstractSuspendedPowerState {

	/**
	 * The singleton instance for this class.
	 */
	private static final EPCReaderModuleSuspendedPowerState SINGLETON_INSTANCE = new EPCReaderModuleSuspendedPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static EPCReaderModuleSuspendedPowerState getInstance() {
		return EPCReaderModuleSuspendedPowerState.SINGLETON_INSTANCE;
	}

	/**
	 * Constructor which is private to support singleton instance.
	 */
	private EPCReaderModuleSuspendedPowerState() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common
	 * .PowerControllable)
	 */
	public void resume(PowerControllable pcObject) {
		EPCReaderModule epcMod = (EPCReaderModule) pcObject;

		epcMod.getSharedResources().getTagMemory().resume();
		epcMod.getInteractiveCommunication().resume();
		epcMod.getInteractiveCommandController().resume();

		epcMod.changePowerState(EPCReaderModuleOnPowerState.getInstance());

		String readername = epcMod.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername)
				.info(readername + " resumed");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common
	 * .PowerControllable)
	 */
	public void turnOff(PowerControllable pcObject) {
		EPCReaderModule epcMod = (EPCReaderModule) pcObject;

		epcMod.getInteractiveCommunication().turnOff();
		epcMod.getInteractiveCommandController().turnOff();

		epcMod.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(false);
		epcMod.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);

		epcMod.changePowerState(EPCReaderModuleOffPowerState.getInstance());

		String readername = epcMod.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");
	}

}
