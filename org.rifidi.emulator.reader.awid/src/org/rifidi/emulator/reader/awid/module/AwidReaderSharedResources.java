/*
 *  AwidReaderSharedResources.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.module;

import java.util.HashMap;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandDigester;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;

/**
 * This class contains all of the resources that the Awid reader needs to
 * operate.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidReaderSharedResources extends AbstractReaderSharedResources {

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
	 * The power signal for the reader's InteractiveCommandProcessor and its
	 * underlying communication.
	 */
	private ControlSignal<Boolean> autonomousPowerSignal;

	private boolean rf_power;
	
	private boolean antenna_source;

	/**
	 * Constructor for the AwidReaderSharedResouces class.
	 * 
	 * @param aRadio
	 * @param antennas
	 * @param aTagMemory
	 * @param readerPowerSignal
	 */
	public AwidReaderSharedResources(GenericRadio aRadio, TagMemory aTagMemory,
			ControlSignal<Boolean> autonomousPowerSignal,
			ControlSignal<Boolean> interactivePowerSignal,
			ControlSignal<Boolean> interactiveConnectionSignal,
			ControlSignal<Boolean> readerPowerSignal, String readerName,
			HashMap<String, CommandObject> allCommands, CommandDigester dig,
			GenericExceptionHandler geh, int numAntennas) {
		super(aRadio, aTagMemory, readerPowerSignal, readerName, geh, dig,
				numAntennas);
		this.autonomousPowerSignal = autonomousPowerSignal;
		this.interactivePowerSignal = interactivePowerSignal;
		this.interactiveConnectionSignal = interactiveConnectionSignal;

		for (CommandObject i : allCommands.values()) {
			this.getPropertyMap().put(i.getDisplayName().toLowerCase(),
					i.getReaderProperty());
		}

		this.rf_power = true;
	}

	
	
	/**
	 * 
	 * @return
	 */
	public boolean isRf_power() {
		return rf_power;
	}

	/**
	 *  @param rf_power
	 */
	public void setRf_power(boolean rf_power) {
		this.rf_power = rf_power;
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

	/**
	 * Returns the power signal for the autonomous part of the thread.
	 * 
	 * @return
	 */
	public ControlSignal<Boolean> getAutonomousPowerSignal() {
		return autonomousPowerSignal;
	}



	/**
	 * @param antenna_source the antenna_source to set
	 */
	public void setAntenna_source(boolean antenna_source) {
		this.antenna_source = antenna_source;
	}



	/**
	 * @return the antenna_source
	 */
	public boolean isAntenna_source() {
		return antenna_source;
	}

}
