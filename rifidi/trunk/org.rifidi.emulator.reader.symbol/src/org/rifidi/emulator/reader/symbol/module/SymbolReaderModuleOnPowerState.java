/*
 *  SymbolReaderModuleOnPowerState.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.symbol.module;

import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.module.abstract_.AbstractOnPowerState;

/**
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class SymbolReaderModuleOnPowerState extends AbstractOnPowerState {

	/**
	 * Private instance
	 */
	private static final SymbolReaderModuleOnPowerState instance = new SymbolReaderModuleOnPowerState();

	/**
	 * Gets a singleton instance of this class.
	 * 
	 * @return
	 */
	public static SymbolReaderModuleOnPowerState getInstance() {
		return instance;
	}

	/**
	 * Private Constructor
	 */
	private SymbolReaderModuleOnPowerState() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
	 */
	public void suspend(PowerControllable pcObject) {
		SymbolReaderModule rm = (SymbolReaderModule) pcObject;
		rm.getHttpComm().suspend();
		rm.getByteComm().suspend();
		rm.getInteractiveBitController().suspend();
		rm.getInteractiveHttpController().suspend();
		
		SymbolReaderSharedResources ssr = rm.getSharedResources();
		
		ssr.getTagMemory().suspend();

		rm
				.changePowerState(SymbolReaderModuleSuspendedPowerState
						.getInstance());
		
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " suspended");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable,
	 *      java.lang.Class)
	 */
	public void turnOff(PowerControllable pcObject) {
		SymbolReaderModule rm = (SymbolReaderModule) pcObject;
		rm.getInteractiveBitController().turnOff();
		rm.getInteractiveHttpController().turnOff();

		rm.getSharedResources().getInteractiveBytePowerSignal()
				.setControlVariableValue(false);
		rm.getSharedResources().getInteractiveHttpPowerSignal()
				.setControlVariableValue(false);
		rm.getSharedResources().getInteractiveByteConnectionSignal()
				.setControlVariableValue(false);
		rm.getSharedResources().getInteractiveHttpConnectionSignal()
				.setControlVariableValue(false);
		
		//SymbolReaderSharedResources ssr = rm.getSharedResources();

		rm.changePowerState(SymbolReaderModuleOffPowerState.getInstance());
		
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");
	}

}
