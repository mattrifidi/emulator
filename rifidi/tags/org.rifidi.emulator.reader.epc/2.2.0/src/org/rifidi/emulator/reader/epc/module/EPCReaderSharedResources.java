/*
 *  EPCReaderSharedResources.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.epc.module;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.xml.CommandDigester;
import org.rifidi.emulator.reader.epc.command.exception.EPCExceptionHandler;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.emulator.reader.source.TagSelector;

/**
 * @author matt
 * 
 */
public class EPCReaderSharedResources extends AbstractReaderSharedResources {

	/**
	 * Message logger
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory
			.getLog(EPCReaderSharedResources.class);

	/**
	 * The connection signal for the reader's InteractiveCommandProcessor and
	 * its underlying communication.
	 */
	private ControlSignal<Boolean> interactiveConnectionSignal;

	/**
	 * The power signal for the reader's InteractiveCommandProcessor and its
	 * underlying communication.
	 */
	private ControlSignal<Boolean> interactivePowerSignal;

	/**
	 * The exception handler
	 */
	private EPCExceptionHandler exc;
	
	/**
	 * A map of tagSelectors.  
	 */
	private HashMap<String,TagSelector> tagSelectorMap;

	/**
	 * 
	 * @param aRadio
	 * @param antennas
	 * @param aTagMemory
	 * @param readerPowerSignal
	 * @param interactivePowerSignal
	 * @param interactiveConnectionSignal
	 * @param readerName
	 */
	public EPCReaderSharedResources(GenericRadio epcRadio, TagMemory epcTagMemory,
			ControlSignal<Boolean> readerPowerSignal,
			ControlSignal<Boolean> interactivePowerSignal,
			ControlSignal<Boolean> interactiveConnectionSignal,
			String readerName, HashMap<String, CommandObject> allCommands,
			CommandDigester dig, EPCExceptionHandler exc, int numAntennas) {
		super(epcRadio, epcTagMemory, readerPowerSignal, readerName, exc, dig, numAntennas);
		this.interactivePowerSignal = interactivePowerSignal;
		this.interactiveConnectionSignal = interactiveConnectionSignal;
		this.exc = exc;
		this.tagSelectorMap = new HashMap<String, TagSelector>();

		for (CommandObject i : allCommands.values()) {
			// logger.debug("Command Object to add is: " + i.getDisplayName());
			this.getPropertyMap().put(i.getDisplayName().toLowerCase(),
					i.getReaderProperty());
		}
	}

	/**
	 * @return the interactiveConnectionSignal
	 */
	public final ControlSignal<Boolean> getInteractiveConnectionSignal() {
		return interactiveConnectionSignal;
	}

	/**
	 * @return the interactivePowerSignal
	 */
	public final ControlSignal<Boolean> getInteractivePowerSignal() {
		return interactivePowerSignal;
	}

	/**
	 * @return the exc
	 */
	public EPCExceptionHandler getExc() {
		return exc;
	}

	/**
	 * @return the tagSelectorMap
	 */
	public HashMap<String, TagSelector> getTagSelectorMap() {
		return tagSelectorMap;
	}
	
	/**
	 * @param name
	 */
	public void addSelector(TagSelector selector) {
		this.tagSelectorMap.put(selector.getName(), selector);
	}

}
