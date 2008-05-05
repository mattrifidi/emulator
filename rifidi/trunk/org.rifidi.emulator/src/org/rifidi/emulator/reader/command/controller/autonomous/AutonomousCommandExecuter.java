/*
 *  @(#)AutonomousCommandExecuter.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.extra.CommandInformation;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.reader.command.controller.CommandControllerException;

/**
 * Generates a command for the associated CommandController to execute every
 * specified time unit. This may be suspended or ended by invoking a few of the
 * control methods described.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AutonomousCommandExecuter implements Runnable {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(AutonomousCommandExecuter.class);

	/**
	 * Interruption flag to control thread execution.
	 */
	private boolean interrupted;

	/**
	 * The period between commands to send.
	 */
	private int period;

	/**
	 * Used to identify the number of seconds or number of cycles to run the
	 * command
	 */
	private int value;

	/**
	 * The command to send for each period.
	 */
	private byte[] command;

	/**
	 * Is the command limited somehow in running?
	 */
	private CommandInformation.LimitedRunningState state;

	/**
	 * The command controller that this is generating commands for.
	 */
	private AutonomousCommandController commandController;

	/**
	 * Creates an AutonomousCommandExectuer which generates the passed command
	 * every passed period, in milliseconds, to the passed controller.
	 * 
	 * @param controller
	 *            The controller to send commands to.
	 * @param command
	 *            The command to send.
	 * @param period
	 *            The period between command generations.
	 */
	public AutonomousCommandExecuter(AutonomousCommandController controller,
			byte[] command, int period) {
		this.commandController = controller;
		this.command = command;
		this.period = period;
		this.interrupted = false;
		this.state = CommandInformation.LimitedRunningState.NOT_LIMITED;
	}

	/**
	 * Creates an AutonomousCommandExecuter which generates the passed command
	 * every passed period, in milliseconds, to the passed controller.
	 * 
	 * @param controller
	 *            The controller to send commands to.
	 * @param command
	 *            The command to send.
	 * @param period
	 *            The period between command generations.
	 * @param value
	 * 			TODO: Possibly change this to a long?  
	 * 			  The amount of time in ms the command will run for, or the
	 * 			  number of cycles the command will run for.  
	 * @param state
	 * 			  
	 */
	public AutonomousCommandExecuter(AutonomousCommandController controller,
			byte[] command, int period, int value,
			CommandInformation.LimitedRunningState state) {
		this.commandController = controller;
		this.command = command;
		this.period = period;
		this.value = value;
		this.state = state;
		this.interrupted = false;
	}

	/**
	 * Generates a command for the associated CommandController to execute every
	 * specified time unit. This may be suspended or ended by invoking a few of
	 * the control methods described.
	 * 
	 * If you are running multiple autonomous commands then you cannot use any of
	 * the limited states.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/* Keep running as long as this hasn't been stopped forcefully */
		synchronized (this) {

			long time = 0;

			/*
			 * This is to determine if the executor only runs for a limited
			 * time. If this was the case then set the start time here.
			 */
			if (state.equals(CommandInformation.LimitedRunningState.TIME_LIMITED)) {
				time = System.currentTimeMillis();
				logger.debug("Time Limited time = " + time);
			}

			while (!this.interrupted) {
				/*
				 * This is a cycle limited state where it only does a certain
				 * amount of reads.  When the number of cycles are reached it turns off
				 * the autonomous controller completely.
				 */
				if (state
						.equals(CommandInformation.LimitedRunningState.CYCLE_LIMITED)
						&& value == 0) {
					/* Turn off when no cycles left */
					commandController.getPowerState()
							.turnOff(commandController, this.getClass());
				} else if (state
						.equals(CommandInformation.LimitedRunningState.CYCLE_LIMITED)) {
					value--;
				}

				try {
					logger.debug("going to call command " + new String(command)
							+ " " + period);
					/* Wait for required period. */
					try {
						this.wait(this.period);
					} catch (InterruptedException e) {
						/* Do nothing -- either interrupted or suspended. */

					}

					/* Process the command */
					ArrayList<Object> response = this.commandController
							.processCommand(this.command);
					logger.debug("Calling method: " + new String(command));

					/* Send each response line as its own TCP package */
					for (Object obj : response) {
						this.commandController.getCurCommunication().sendBytes(
								((String) obj).getBytes());
					}
				} catch (CommunicationException e1) {
					/* The underlying communication was interrupted, stop. */
					this.interrupt();

				} catch (CommandControllerException e) {
					/* The CommandController was interrupted, stop. */
					this.interrupt();

				}

				if (state
						.equals(CommandInformation.LimitedRunningState.TIME_LIMITED)
						&& (System.currentTimeMillis() - time) > value) {
					logger.debug("Time controller turn off");
					commandController.getPowerState()
							.turnOff(commandController, this.getClass());
				}
			}
		}

	}

	/**
	 * Interrupts this command executer, causing the run method to stop.
	 * 
	 */
	public void interrupt() {
		/* Interrupt */
		synchronized (this) {
			this.interrupted = true;
			this.notifyAll();

		}

	}

	/**
	 * Resets the command executer to the normal state.
	 * 
	 */
	public void reset() {
		/* Reset */
		synchronized (this) {
			this.interrupted = false;
			this.notifyAll();

		}

	}

}
