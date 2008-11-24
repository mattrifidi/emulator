/*
 *  @(#)AlienReaderModuleSuspendedPowerState.java
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
import org.rifidi.emulator.reader.module.abstract_.AbstractSuspendedPowerState;

/**
 * The suspended state for an Alien reader.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AlienReaderModuleSuspendedPowerState extends
		AbstractSuspendedPowerState {

	/**
	 * The singleton instance for this class.
	 */
	private static final AlienReaderModuleSuspendedPowerState SINGLETON_INSTANCE = new AlienReaderModuleSuspendedPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static AlienReaderModuleSuspendedPowerState getInstance() {
		return AlienReaderModuleSuspendedPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private AlienReaderModuleSuspendedPowerState() {

	}

	/**
	 * Resumes all Alien Reader components.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	public void resume(PowerControllable pcObject) {
		/* Cast to an AlienReaderModule object. */
		AlienReaderModule alienModule = (AlienReaderModule) pcObject;
		
		alienModule.getSharedResources().getHearbeatController().resume();
		
		alienModule.getSharedResources().getTagMemory().resume();
		alienModule.getInteractiveCommunication().resume();
		alienModule.getInteractiveCommandController().resume();

		/* Switch to the on state */
		alienModule.changePowerState(AlienReaderModuleOnPowerState
				.getInstance());
		
		String readername = alienModule.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " resumed");

	}

	/**
	 * Turns off all Alien Reader components, as well as all control signals.
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
	 */
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		/* Cast to an AlienReaderModule object. */
		AlienReaderModule alienModule = (AlienReaderModule) pcObject;


		alienModule.getSharedResources().getHearbeatController().stopHeartbeat();
		alienModule.getInteractiveCommunication().turnOff(this.getClass());
		alienModule.getInteractiveCommandController().turnOff(this.getClass());

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
