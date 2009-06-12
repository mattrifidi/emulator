/*
 *  SiritReaderSharedResources.java
 *
 *  Created:	09.06.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.module;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandDigester;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;

/**
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritReaderSharedResources extends AbstractReaderSharedResources {

	private ControlSignal<Boolean> interactivePowerSignal;
	private ControlSignal<Boolean> interactiveConnectionSignal;

	/**
	 * 
	 * 
	 * @param aRadio
	 * @param aTagMemory
	 * @param readerPowerSignal
	 * @param readerName
	 * @param exc
	 * @param dig
	 * @param interactiveConnectionSignal
	 * @param interactiveHttpPowerSignal
	 */
	public SiritReaderSharedResources(GenericRadio aRadio,
			TagMemory aTagMemory, ControlSignal<Boolean> readerPowerSignal,
			String readerName, GenericExceptionHandler geh,
			CommandDigester dig,
			ControlSignal<Boolean> interactiveConnectionSignal,
			ControlSignal<Boolean> interactivePowerSignal, int numAntennas) {
		super(aRadio, aTagMemory, readerPowerSignal, readerName, geh, dig,
				numAntennas);

		this.interactiveConnectionSignal = interactiveConnectionSignal;
		this.interactivePowerSignal = interactivePowerSignal;
	}

	/**
	 * @return the interactiveConnectionSignal
	 */
	public ControlSignal<Boolean> getInteractiveConnectionSignal() {
		return interactiveConnectionSignal;
	}

	/**
	 * @return the interactivePowerSignal
	 */
	public ControlSignal<Boolean> getInteractivePowerSignal() {
		return interactivePowerSignal;
	}
}
