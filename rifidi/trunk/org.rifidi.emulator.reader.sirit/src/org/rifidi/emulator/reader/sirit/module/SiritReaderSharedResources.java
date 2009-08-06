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

import java.util.HashMap;
import java.util.HashSet;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandDigester;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.emulator.reader.sirit.active.SiritActiveWorker;

/**
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritReaderSharedResources extends AbstractReaderSharedResources {

	private ControlSignal<Boolean> interactivePowerSignal;
	private ControlSignal<Boolean> interactiveConnectionSignal;

	private String commandIP;
	private Integer commandPort;

	private Thread activeModeThread;

	/**
	 * Constructor
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
			ControlSignal<Boolean> interactivePowerSignal,
			ControlSignal<Boolean> interactiveConnectionSignal,
			String readerName, HashMap<String, CommandObject> allCommands,
			CommandDigester dig, GenericExceptionHandler geh, String commandIP,
			Integer commandPort, int numAntennas) {

		/* Call the super constructor */
		super(aRadio, aTagMemory, readerPowerSignal, readerName, geh, dig,
				numAntennas);

		/*
		 * 
		 * /* Assign other instance variables
		 */
		this.interactiveConnectionSignal = interactiveConnectionSignal;
		this.interactivePowerSignal = interactivePowerSignal;
		this.commandIP = commandIP;
		this.commandPort = commandPort;

		for (CommandObject i : allCommands.values()) {
			this.getPropertyMap().put(i.getDisplayName().toLowerCase(),
					i.getReaderProperty());
		}
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
	 * @return the commandIP
	 */
	public String getCommandIP() {
		return commandIP;
	}

	/**
	 * @return the commandPort
	 */
	public Integer getCommandPort() {
		return commandPort;
	}

	/**
	 * This method returns a set of integers that represent the antennas that
	 * should be scanned as defined by the antennalist variable
	 * 
	 * @return an HashSet of the antennas' numbers to be scanned
	 */
	public HashSet<Integer> getAntennas2() {
		/* read the property with the list of created antennas */
		String antennas = this.getPropertyMap().get("detectedantennas")
				.getPropertyStringValue();

		/* create return value */
		HashSet<Integer> antennasToScan = new HashSet<Integer>();

		for (String antenna : antennas.split(" ")) {
			antennasToScan.add(new Integer(antenna));
		}

		return antennasToScan;
	}

	/** Starts the active mode. The reader periodically scans for new tags */
	public void startActiveMode() {
		/* activate tag memory */
		this.getTagMemory().resume();

		/* create thread for scanning */
		SiritActiveWorker saw = new SiritActiveWorker(this.getRadio(), this
				.getTagMemory());

		activeModeThread = new Thread(saw);
		activeModeThread.start();
	}

	/** Stops the active mode. */
	public void stopActiveMode() {
		if (activeModeThread != null) {
			if (activeModeThread.isAlive()) {
				activeModeThread.interrupt();
			}
		}
	}

}
