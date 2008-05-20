/*
 *  EPCReaderModuleOnPowerState.java
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
import org.rifidi.emulator.reader.module.abstract_.AbstractOnPowerState;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class EPCReaderModuleOnPowerState extends AbstractOnPowerState {

	/**
	 * The singleton instance for this class.
	 */
	private static final EPCReaderModuleOnPowerState SINGLETON_INSTANCE = new EPCReaderModuleOnPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static EPCReaderModuleOnPowerState getInstance() {
		return EPCReaderModuleOnPowerState.SINGLETON_INSTANCE;
	}

	/**
	 * Constructor which is private to support singleton instance.
	 */
	private EPCReaderModuleOnPowerState() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	public void suspend(PowerControllable pcObject) {
		EPCReaderModule epcMod = (EPCReaderModule) pcObject;

		epcMod.getSharedResources().getTagMemory().suspend();
		epcMod.getInteractiveCommunication().suspend();
		epcMod.getInteractiveCommandController().suspend();

		epcMod.changePowerState(EPCReaderModuleSuspendedPowerState
				.getInstance());
		
		String readername = epcMod.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " suspended");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		EPCReaderModule epcMod = (EPCReaderModule) pcObject;

		epcMod.getInteractiveCommunication().turnOff(this.getClass());
		epcMod.getInteractiveCommandController().turnOff(this.getClass());

		epcMod.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(false);
		epcMod.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);

		epcMod.changePowerState(EPCReaderModuleOffPowerState.getInstance());
		
		String readername = epcMod.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");
	}

}
