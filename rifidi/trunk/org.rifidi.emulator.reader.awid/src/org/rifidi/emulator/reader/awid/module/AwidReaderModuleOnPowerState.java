/*
 *  AwidReaderModuleOnPowerState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.module;

import org.rifidi.emulator.common.PowerControllable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.rifidi.emulator.reader.module.abstract_.AbstractOnPowerState;

/**
 * Represents the reader in an "on" state. The reader can be turned off or
 * suspended from this state.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidReaderModuleOnPowerState extends AbstractOnPowerState {

	/**
	 * The log4j logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger =
		 LogFactory.getLog(AwidReaderModuleOnPowerState.class);

	/**
	 * The singleton instance for this class.
	 */
	private static final AwidReaderModuleOnPowerState SINGLETON_INSTANCE = new AwidReaderModuleOnPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static AwidReaderModuleOnPowerState getInstance() {
		return AwidReaderModuleOnPowerState.SINGLETON_INSTANCE;
	}

	/**
	 * Private contructor
	 */
	private AwidReaderModuleOnPowerState() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	public void suspend(PowerControllable pcObject) {
		AwidReaderModule awidModule = (AwidReaderModule) pcObject;

		awidModule.getSharedResources().getTagMemory().suspend();
		awidModule.getInteractiveCommunication().suspend();
		awidModule.getInteractiveCommandController().suspend();

		awidModule.changePowerState(AwidReaderModuleSuspendedPowerState
				.getInstance());
		
		String readername = awidModule.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " suspend");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOff(PowerControllable pcObject) {
		AwidReaderModule awidModule = (AwidReaderModule) pcObject;

		awidModule.getInteractiveCommunication().turnOff();
		awidModule.getInteractiveCommandController().turnOff();

		awidModule.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);
		awidModule.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(false);

		awidModule.changePowerState(AwidReaderModuleOffPowerState
				.getInstance());
		
		String readername = awidModule.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");
	}

}
