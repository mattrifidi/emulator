/*
 *  SymbolReaderModuleSuspendedPowerState.java
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
import org.rifidi.emulator.reader.module.abstract_.AbstractSuspendedPowerState;
import org.rifidi.emulator.reader.symbol.tagbuffer.SymbolTagMemory;

/**
 * Represents a suspended state of the symbol reader.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class SymbolReaderModuleSuspendedPowerState extends
		AbstractSuspendedPowerState {

	/**
	 * Private instance
	 */
	private static final SymbolReaderModuleSuspendedPowerState instance = new SymbolReaderModuleSuspendedPowerState();

	/**
	 * Gets a singleton instance of this class.
	 * 
	 * @return
	 */
	public static SymbolReaderModuleSuspendedPowerState getInstance() {
		return instance;
	}

	/**
	 * Private Constructor
	 */
	private SymbolReaderModuleSuspendedPowerState() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	public void resume(PowerControllable pcObject) {
		SymbolReaderModule rm = (SymbolReaderModule) pcObject;

		rm.getByteComm().resume();
		rm.getHttpComm().resume();

		rm.getInteractiveBitController().resume();
		rm.getInteractiveHttpController().resume();
		
		SymbolReaderSharedResources ssr = rm.getSharedResources();
		
		ssr.getTagMemory().resume();

		rm.changePowerState(SymbolReaderModuleOnPowerState.getInstance());
		
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " resumed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		SymbolReaderModule rm = (SymbolReaderModule) pcObject;
		rm.getInteractiveBitController().turnOff(this.getClass());
		rm.getInteractiveHttpController().turnOff(this.getClass());

		rm.getByteComm().turnOff(this.getClass());
		rm.getByteComm().turnOff(this.getClass());

		rm.getSharedResources().getInteractiveByteConnectionSignal()
				.setControlVariableValue(false);
		rm.getSharedResources().getInteractiveBytePowerSignal()
				.setControlVariableValue(false);
		rm.getSharedResources().getInteractiveHttpConnectionSignal()
				.setControlVariableValue(false);
		rm.getSharedResources().getInteractiveHttpPowerSignal()
				.setControlVariableValue(false);

		rm.changePowerState(SymbolReaderModuleOffPowerState.getInstance());
		
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");
	}

}
