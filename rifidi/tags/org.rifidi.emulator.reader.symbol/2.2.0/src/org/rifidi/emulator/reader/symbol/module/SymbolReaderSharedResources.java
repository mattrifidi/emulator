/*
 *  SymbolReaderSharedResources.java
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

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandDigester;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class SymbolReaderSharedResources extends AbstractReaderSharedResources {

	private ControlSignal<Boolean> interactiveBytePowerSignal;

	private ControlSignal<Boolean> interactiveHttpPowerSignal;

	private ControlSignal<Boolean> interactiveByteConnectionSignal;

	private ControlSignal<Boolean> interactiveHttpConnectionSignal;

	/**
	 * 
	 * 
	 * @param aRadio
	 * @param aTagMemory
	 * @param readerPowerSignal
	 * @param readerName
	 * @param exc
	 * @param dig
	 * @param interactiveBytePowerSignal
	 * @param interactiveByteConnectionSignal
	 * @param interactiveHttpConnectionSignal
	 * @param interactiveHttpPowerSignal
	 */
	public SymbolReaderSharedResources(
			GenericRadio aRadio,
			TagMemory aTagMemory,
			ControlSignal<Boolean> readerPowerSignal,
			String readerName,
			GenericExceptionHandler exc,
			CommandDigester dig,
			ControlSignal<Boolean> interactiveBitCommunicationPowerSignal,
			ControlSignal<Boolean> interactiveBitCommunicationConnectionSignal,
			ControlSignal<Boolean> interactiveHttpCommunicationConnectionSignal,
			ControlSignal<Boolean> interactiveHttpCommunicationPowerSignal,
			int numAntennas) {
		super(aRadio, aTagMemory, readerPowerSignal, readerName, exc, dig,
				numAntennas);

		this.interactiveByteConnectionSignal = interactiveBitCommunicationConnectionSignal;
		this.interactiveBytePowerSignal = interactiveBitCommunicationPowerSignal;
		this.interactiveHttpConnectionSignal = interactiveHttpCommunicationConnectionSignal;
		this.interactiveHttpPowerSignal = interactiveHttpCommunicationPowerSignal;
	}

	/**
	 * @return the interactiveByteConnectionSignal
	 */
	public ControlSignal<Boolean> getInteractiveByteConnectionSignal() {
		return interactiveByteConnectionSignal;
	}

	/**
	 * @return the interactiveBytePowerSignal
	 */
	public ControlSignal<Boolean> getInteractiveBytePowerSignal() {
		return interactiveBytePowerSignal;
	}

	/**
	 * @return the interactiveHttpConnectionSignal
	 */
	public ControlSignal<Boolean> getInteractiveHttpConnectionSignal() {
		return interactiveHttpConnectionSignal;
	}

	/**
	 * @return the interactiveHttpPowerSignal
	 */
	public ControlSignal<Boolean> getInteractiveHttpPowerSignal() {
		return interactiveHttpPowerSignal;
	}

}
