/*
 *  @(#)AlienReaderSharedResources.java
 *
 *  Created:	Oct 26, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.alien.module;

import java.util.HashMap;
import java.util.Map;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.alien.autonomous.states.AutoStateController;
import org.rifidi.emulator.reader.alien.gpio.GPIController;
import org.rifidi.emulator.reader.alien.gpio.GPOController;
import org.rifidi.emulator.reader.alien.heartbeat.HeartbeatController;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.command.xml.CommandDigester;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;

/**
 * Shared resources which are specific to a particular Alien reader.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AlienReaderSharedResources extends AbstractReaderSharedResources {

	/**
	 * The power signal for the reader's HeartbeatCommandProcessor and its
	 * underlying communication.
	 */
	@SuppressWarnings("unused")
	private ControlSignal<Boolean> heartbeatPowerSignal;

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

	private ControlSignal<Boolean> notifyControlSignal;

	private ControlSignal<Boolean> autoCommConnectionSignal;

	private ControlSignal<Boolean> autoCommCommunicationPower;

	private String commandIP;

	private Integer commandPort;

	private AutoStateController autoStateController;

	private GPOController autoTrueOutput;

	private GPOController autoFalseOutput;

	private GPOController autoWaitOutput;

	private GPOController autoWorkOutput;

	private GPIController autoStartTrigger;

	private GPIController autoStopTrigger;
	
	private HeartbeatController hearbeatController;

	/**
	 * A constructor which takes in all necessary shared resources to be a
	 * complete Alien reader.
	 * 
	 * @param aRadio
	 *            The shared radio module to use.
	 * @param antennas
	 *            A map of antennas to use with a String IDs as the keys and
	 *            Antenna object as the values.
	 * @param aTagMemory
	 *            The shared tag memory module to use.
	 * @param readerPowerSignal
	 *            The power signal which controls the global power switch of the
	 *            reader.
	 * @param interactivePowerSignal
	 *            The power signal to use for the reader's
	 *            InteractiveCommandProcessor and its underlying communication.
	 * @param interactiveConnectionSignal
	 *            The connection signal to use for the reader's
	 *            InteractiveCommandProcessor and its underlying communication.
	 */
	public AlienReaderSharedResources(GenericRadio aRadio,
			Map<String, Antenna> antennas, TagMemory aTagMemory,
			ControlSignal<Boolean> readerPowerSignal,
			ControlSignal<Boolean> interactivePowerSignal,
			ControlSignal<Boolean> interactiveConnectionSignal,
			ControlSignal<Boolean> autoCommCommunicationPower,
			ControlSignal<Boolean> notifyControlSignal,
			ControlSignal<Boolean> autoCommConnectionSignal, String readerName,
			HashMap<String, CommandObject> allCommands, CommandDigester dig,
			GenericExceptionHandler geh, String commandIP, Integer commandPort,
			int numAntennas) {
		/* Call the super constructor */
		super(aRadio, aTagMemory, readerPowerSignal, readerName, geh, dig,
				numAntennas);

		/* Assign other instance variables */
		this.interactivePowerSignal = interactivePowerSignal;
		this.interactiveConnectionSignal = interactiveConnectionSignal;
		this.notifyControlSignal = notifyControlSignal;
		this.autoCommConnectionSignal = autoCommConnectionSignal;
		this.autoCommCommunicationPower = autoCommCommunicationPower;
		this.commandIP = commandIP;
		this.commandPort = commandPort;

		for (CommandObject i : allCommands.values()) {
			this.getPropertyMap().put(i.getDisplayName().toLowerCase(),
					i.getReaderProperty());
		}

		this.autoTrueOutput = new GPOController(this.getGpioController());
		this.autoFalseOutput = new GPOController(this.getGpioController());
		this.autoWaitOutput = new GPOController(this.getGpioController());
		this.autoWorkOutput = new GPOController(this.getGpioController());

	}


	/**
	 * Returns the interactiveConnectionSignal.
	 * 
	 * @return Returns the interactiveConnectionSignal.
	 */
	public final ControlSignal<Boolean> getInteractiveConnectionSignal() {
		return this.interactiveConnectionSignal;
	}

	/**
	 * Returns the interactivePowerSignal.
	 * 
	 * @return Returns the interactivePowerSignal.
	 */
	public final ControlSignal<Boolean> getInteractivePowerSignal() {
		return this.interactivePowerSignal;
	}

	/**
	 * @return the notifyControlSignal
	 */
	public ControlSignal<Boolean> getNotifyControlSignal() {
		return notifyControlSignal;
	}

	/**
	 * @return the notifyConnectionSignal
	 */
	public ControlSignal<Boolean> getAutoCommConnectionSignal() {
		return autoCommConnectionSignal;
	}

	/**
	 * @return the autonomousCommunicationPower
	 */
	public ControlSignal<Boolean> getAutoCommCommunicationPower() {
		return autoCommCommunicationPower;
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

	public GPIController getAutoStartTrigger() {
		return autoStartTrigger;
	}

	public void setAutoStartTrigger(GPIController autoStartTrigger) {
		this.autoStartTrigger = autoStartTrigger;
	}

	public GPIController getAutoStopTrigger() {
		return autoStopTrigger;
	}

	protected void setAutoStopTrigger(GPIController autoStopTrigger) {
		this.autoStopTrigger = autoStopTrigger;
	}

	public AutoStateController getAutoStateController() {
		return autoStateController;
	}

	protected void setAutoStateController(
			AutoStateController autoStateController) {
		this.autoStateController = autoStateController;
	}

	public GPOController getAutoTrueOutput() {
		return autoTrueOutput;
	}

	public GPOController getAutoFalseOutput() {
		return autoFalseOutput;
	}

	public GPOController getAutoWaitOutput() {
		return autoWaitOutput;
	}

	public GPOController getAutoWorkOutput() {
		return autoWorkOutput;
	}

	/**
	 * @return the hearbeatController
	 */
	public HeartbeatController getHearbeatController() {
		return hearbeatController;
	}

	/**
	 * @param hearbeatController the hearbeatController to set
	 */
	public void setHearbeatController(HeartbeatController hearbeatController) {
		this.hearbeatController = hearbeatController;
	}
}
