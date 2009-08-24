/*
 *  @(#)AlienReaderModuleOnPowerState.java
 *
 *  Created:	Oct 27, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.alien.module;

import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.module.abstract_.AbstractOnPowerState;

/**
 * The on state for an Alien reader.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AlienReaderModuleOnPowerState extends AbstractOnPowerState {

	/**
	 * The singleton instance for this class.
	 */
	private static final AlienReaderModuleOnPowerState SINGLETON_INSTANCE = new AlienReaderModuleOnPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static AlienReaderModuleOnPowerState getInstance() {
		return AlienReaderModuleOnPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private AlienReaderModuleOnPowerState() {

	}

	/**
	 * Suspends all Alien Reader components.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	public void suspend(PowerControllable pcObject) {
		/* Cast to an AlienReaderModule object. */
		AlienReaderModule alienModule = (AlienReaderModule) pcObject;

		alienModule.getSharedResources().getHearbeatController().suspend();

		alienModule.getSharedResources().getTagMemory().suspend();
		alienModule.getInteractiveCommunication().suspend();
		alienModule.getInteractiveCommandController().suspend();

		/* Switch to the suspended state */
		alienModule.changePowerState(AlienReaderModuleSuspendedPowerState
				.getInstance());

		String readername = alienModule.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(
				readername + " suspended");

	}

	/**
	 * Turns off all Alien Reader components, as well as all control signals.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOff(PowerControllable pcObject) {
		/* Cast to an AlienReaderModule object. */
		AlienReaderModule alienModule = (AlienReaderModule) pcObject;

		alienModule.getSharedResources().getHearbeatController()
				.stopHeartbeat();

		alienModule.getSharedResources().getAutoStateController()
				.stopAutoMode();

		alienModule.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(false);
		alienModule.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);

		/* Switch to the off state */
		alienModule.changePowerState(AlienReaderModuleOffPowerState
				.getInstance());

		String readername = alienModule.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");

	}

}
