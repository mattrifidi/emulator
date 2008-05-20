/*
 *  SymbolReaderModuleOffPowerState.java
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
import org.rifidi.emulator.reader.module.abstract_.AbstractOffPowerState;
import org.rifidi.emulator.reader.symbol.tagbuffer.SymbolTagMemory;

/**
 *
 * @author Matthew Dean - matt@pramari.com
 */
public class SymbolReaderModuleOffPowerState extends AbstractOffPowerState {
	
	/**
	 * Private instance
	 */
	private static final SymbolReaderModuleOffPowerState instance = new SymbolReaderModuleOffPowerState();

	/**
	 * Gets a singleton instance of this class.  
	 * 
	 * @return
	 */
	public static SymbolReaderModuleOffPowerState getInstance() {
		return instance;
	}

	/**
	 * Private Constructor
	 */
	private SymbolReaderModuleOffPowerState() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
	 */
	public void turnOn(PowerControllable pcObject) {
		SymbolReaderModule rm = (SymbolReaderModule) pcObject;
		rm.getByteComm().turnOn();
		rm.getHttpComm().turnOn();
		rm.getInteractiveBitController().turnOn();
		rm.getInteractiveHttpController().turnOn();
		rm.getSharedResources().getInteractiveBytePowerSignal().setControlVariableValue(true);
		rm.getSharedResources().getInteractiveHttpPowerSignal().setControlVariableValue(true);
		rm.getSharedResources().getInteractiveByteConnectionSignal().setControlVariableValue(false);
		rm.getSharedResources().getInteractiveHttpConnectionSignal().setControlVariableValue(false);

		/* Turn on Tag Buffer*/
		
		SymbolReaderSharedResources ssr = rm.getSharedResources();
		
		rm.changePowerState(SymbolReaderModuleOnPowerState.getInstance());
		
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " on");
	}

}
