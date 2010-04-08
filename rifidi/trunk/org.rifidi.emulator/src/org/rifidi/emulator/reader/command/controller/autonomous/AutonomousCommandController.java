/*
 *  @(#)AutonomousCommandController.java
 *
 *  Created:	Oct 13, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.autonomous;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.extra.CommandInformation;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.reader.command.controller.CommandControllerPowerState;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandController;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState;

/**
 * A generic class which models an autonomous command controller. An autonomous
 * command controller generates commands to pass through to its current
 * operating state and gives backs it responses through the passed
 * CommunicationChannel.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AutonomousCommandController extends AbstractCommandController {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(AutonomousCommandController.class);

	/**
	 * The autonomous executers that this controller is using.
	 */
	protected List<AutonomousCommandExecuter> autonomousExecuters;

	/**
	 * The control signal on whether or not the reader is connected.
	 */
	protected ControlSignal<Boolean> connectionControlSignal;

	/**
	 * A constructor for an autonomous command controller which takes in a map
	 * of commands and the time between command triggers. This way, a single
	 * controller can invoke multiple commands at various intervals. The
	 * controller is initially off.
	 * 
	 * @param initialOperatingState
	 *            The initial operating state to place this controller in.
	 * @param powerControlSignal
	 *            The power control signal for this AbstractCommandController to
	 *            observe. If <i>null</i> is passed, this will not observe a
	 *            power signal.
	 * @param connectionControlSignal
	 *            The connection control signal for this
	 *            AbstractCommandController to observe. If <i>null</i> is
	 *            passed, this will not observe a connection signal.
	 * @param communication
	 *            The communication channel to send command responses through.
	 * @param autoCommands
	 *            The map of commands to execute.
	 */
	public AutonomousCommandController(
			AbstractCommandControllerOperatingState initialOperatingState,
			ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal,
			Communication communication, Map<byte[], Integer> autoCommands) {
		/* Invoke super constructor */
		super(AutonomousOffCommandControllerPowerState.getInstance(),
				initialOperatingState, powerControlSignal,
				connectionControlSignal, communication);

		/* Instantiate the autonomousExecuters list */
		this.autonomousExecuters = new ArrayList<AutonomousCommandExecuter>();

		/* Create the autonomous executers */
		constructAutonomousExecuters(autoCommands);

	}

	/**
	 * Exposes the underlying method to package members.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandController#changeCommandControllerOperatingState(org.rifidi.emulator.reader.command.controller.CommandControllerOperatingState)
	 */
	@Override
	protected void changeCommandControllerOperatingState(
			AbstractCommandControllerOperatingState newState) {
		/* Just call the super-class method */
		super.changeCommandControllerOperatingState(newState);

	}

	/**
	 * Exposes the underlying method to package members.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandController#changeCommandControllerPowerState(org.rifidi.emulator.reader.command.controller.CommandControllerPowerState)
	 */
	@Override
	protected void changeCommandControllerPowerState(
			CommandControllerPowerState newState) {
		/* Just call the super-class method */
		super.changeCommandControllerPowerState(newState);

	}

	/**
	 * Populates the array of AutonomousCommandExecuters from the passed map.
	 * 
	 * @param autoCommands
	 *            The map of commands to execute with command-period key-value
	 *            pairs.
	 * 
	 */
	private void constructAutonomousExecuters(Map<byte[], Integer> autoCommands) {

		logger.debug("constructing multiple executers");

		/* Iterate through the map, creating a new executer for each entry */
		Iterator<byte[]> iter = autoCommands.keySet().iterator();

		while (iter.hasNext()) {
			/* Grab the command and period from the map */
			byte[] curCommand = iter.next();
			int curPeriod = autoCommands.get(curCommand).intValue();

			/* Add a new executer based on this info the list of executers */
			this.autonomousExecuters.add(new AutonomousCommandExecuter(this,
					curCommand, curPeriod));

		}

	}

	/**
	 * Some readers can have multiple autonomous method calls going at once. For
	 * those readers, you would use the regular constructAutonomousExecutors.
	 * For those readers where you can only call one set of autonomous methods
	 * at once, such as the AWID MPR, this method is a better option. This will
	 * purge any information about prior autonomous calls before setting the
	 * latest autonomous call in motion.
	 * 
	 * 
	 * @param autoCommands
	 *            The latest batch of autonomous calls to execute.
	 */
	public void constructSingleAutonomousExecuter(CommandInformation extraInfo) {
		/* Iterate through the map, creating a new executer for each entry */
		Iterator<byte[]> iter = extraInfo.getCommandMap().keySet().iterator();
		
		logger.debug("Got into the singleAutonomousExecutor");
		
		while (iter.hasNext()) {
			/* Grab the command and period from the map */
			byte[] curCommand = iter.next();
			int curPeriod = extraInfo.getCommandMap().get(curCommand).intValue();

			this.autonomousExecuters.clear();
			
			
			/* Add a new executer based on this info the list of executers */
			this.autonomousExecuters.add(new AutonomousCommandExecuter(this,
					curCommand, curPeriod, extraInfo.getValue(), extraInfo.getState()));

		}
	}

	/**
	 * Exposes the underlying method to the Autonomous package members.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandController#getCurCommunication()
	 */
	@Override
	public Communication getCurCommunication() {
		/* Simply call the super method. */
		return super.getCurCommunication();

	}

	/**
	 * Kicks off new threads for the autonomous executers.
	 */
	protected void startAutonomousExecuters() {
		/* Iterate through the list and start a new thread for each. */
		Iterator<AutonomousCommandExecuter> iter = this.autonomousExecuters
				.iterator();

		/* A counter variable to help with thread number naming. */
		int executerCount = 1;

		/* Start iterating */
		while (iter.hasNext()) {
			/* Grab the next executer and reset. */
			AutonomousCommandExecuter curExecuter = iter.next();
			curExecuter.reset();

			/* Create a new thread and kick it off */
			Thread tempThread = new Thread(curExecuter,
					"Autonomous Executer - " + executerCount);
			tempThread.start();

			/* Increment the name counter */
			executerCount++;

		}

	}

	/**
	 * Stops all of the autonomousExecuters.
	 */
	protected void stopAutonomousExecuters() {
		/* Iterate through the list. */
		Iterator<AutonomousCommandExecuter> iter = this.autonomousExecuters
				.iterator();

		/* Start iterating */
		while (iter.hasNext()) {
			/* Interrupt the executer. */
			iter.next().interrupt();

		}

	}

	/**
	 * Gets the connectionControlSignal for this controller.
	 * 
	 * @return
	 */
	public ControlSignal<Boolean> getConnectionControlSignal() {
		return connectionControlSignal;
	}

}
