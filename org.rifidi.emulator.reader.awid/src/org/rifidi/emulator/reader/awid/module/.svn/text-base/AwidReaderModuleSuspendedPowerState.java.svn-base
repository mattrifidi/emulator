/*
 *  AwidReaderModuleSuspendedPowerState.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.module;

import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.module.abstract_.AbstractSuspendedPowerState;

/**
 * This represents a suspended reader state. The reader can be resumed or turned
 * off from this state.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidReaderModuleSuspendedPowerState extends
		AbstractSuspendedPowerState {

	/**
	 * The singleton instance for this class.
	 */
	private static final AwidReaderModuleSuspendedPowerState SINGLETON_INSTANCE = new AwidReaderModuleSuspendedPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static AwidReaderModuleSuspendedPowerState getInstance() {
		return AwidReaderModuleSuspendedPowerState.SINGLETON_INSTANCE;
	}

	/**
	 * Empty private constructor
	 */
	private AwidReaderModuleSuspendedPowerState() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common
	 * .PowerControllable)
	 */
	public void resume(PowerControllable pcObject) {
		AwidReaderModule awidModule = (AwidReaderModule) pcObject;

		awidModule.getSharedResources().getTagMemory().resume();
		awidModule.getInteractiveCommunication().resume();
		awidModule.getInteractiveCommandController().resume();

		awidModule.changePowerState(AwidReaderModuleOnPowerState.getInstance());

		String readername = awidModule.getSharedResources().getReaderName();
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
		AwidReaderModule awidModule = (AwidReaderModule) pcObject;

		awidModule.getInteractiveCommunication().turnOff();
		awidModule.getInteractiveCommandController().turnOff();

		awidModule.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);
		awidModule.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(false);

		awidModule
				.changePowerState(AwidReaderModuleOffPowerState.getInstance());

		String readername = awidModule.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");
	}

}
