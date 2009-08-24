/*
 *  @(#)AbstractCommandController.java
 *
 *  Created:	Sep 18, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.abstract_;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.reader.command.controller.CommandController;
import org.rifidi.emulator.reader.command.controller.CommandControllerException;
import org.rifidi.emulator.reader.command.controller.CommandControllerPowerState;
import org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule;

/**
 * An AbstractCommandController defines the basic relationships which a
 * CommandController has. It contains a Communication to send command responses
 * through. It has a current CommandControllerOperatingState and current
 * CommandControllerPowerState which are used to dictate the functionality of
 * the processCommand method. This class implements the shuttling of data
 * between the Communication, but does not incorporate any private methods for
 * aiding state.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractCommandController extends AbstractPowerModule
		implements CommandController, Observer {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(AbstractCommandController.class);

	/**
	 * A constant command which is called when a connection event is handled.
	 */
	public static final byte[] CONNECTION_COMMAND = "connect".getBytes();

	/**
	 * A control signal which this controller will observe for changes which
	 * relates to the connection of the communication is using. The abstract
	 * controller will take appropriate action based on these changes.
	 */
	private ControlSignal<Boolean> connectionControlSignal;

	/**
	 * The current CommunicationChannel this is monitoring.
	 */
	private Communication curCommunication;

	/**
	 * The current command processor operating state.
	 */
	private AbstractCommandControllerOperatingState curOperatingState;

	/**
	 * The initial command processor operating state. When a logical reset of
	 * the controller takes place, this is the state which it will initially
	 * hold.
	 */
	private AbstractCommandControllerOperatingState initialOperatingState;

	/**
	 * Interruption flag for wait actions.
	 */
	private boolean interrupted;

	/**
	 * Suspension flag for wait actions.
	 */
	private boolean suspended;

	/**
	 * The lock for the suspension and interruption flags.
	 */
	private Object suspensionLock;

	/**
	 * Creates a new AbstractCommandController object with passed initial power
	 * and operating states, along with a CommunicationChannel object to send
	 * processed command responses to.
	 * 
	 * @param initialPowerState
	 *            The initial power state to place this controller in.
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
	 *            The Communication which this controller interacts with.
	 */
	public AbstractCommandController(
			CommandControllerPowerState initialPowerState,
			AbstractCommandControllerOperatingState initialOperatingState,
			ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal,
			Communication communication) {
		/* Call the AbstractPowerModule's constructor */
		super(initialPowerState, powerControlSignal);

		logger.debug("powerstate: " + initialPowerState);
		logger.debug("cur power state: " + super.getPowerState());

		/* Assign instance variables */
		this.initialOperatingState = initialOperatingState;
		this.curOperatingState = initialOperatingState;
		this.curCommunication = communication;
		this.connectionControlSignal = connectionControlSignal;

		/* Register as connection observer */
		if (this.connectionControlSignal != null) {
			this.connectionControlSignal.addObserver(this);

		}

		/* Set wait variables to defaults */
		this.suspensionLock = new Object();
		this.interrupted = false;
		this.suspended = false;

		/* Synchronize on the passed processor's suspension lock */
		Object lock = this.getSuspensionLock();
		synchronized (lock) {
			/* Turn on interruption status */
			this.setInterrupted(true);

			/* Notify anything waiting */
			lock.notifyAll();

		}

	}

	/**
	 * Changes the current operating state to the passed operating state.
	 * 
	 * @param newState
	 *            The new operating state to set.
	 */
	protected void changeCommandControllerOperatingState(
			AbstractCommandControllerOperatingState newState) {
		this.curOperatingState = newState;

	}

	/**
	 * Changes the current power state to the passed power state.
	 * 
	 * @param newState
	 *            The new power state to set.
	 */
	protected void changeCommandControllerPowerState(
			CommandControllerPowerState newState) {
		/* Invoke AbstractPowerModule's implementation */
		super.changePowerState(newState);

	}

	/**
	 * Returns the curCommunication.
	 * 
	 * @return Returns the curCommunication.
	 */
	protected Communication getCurCommunication() {
		return this.curCommunication;
	}

	/**
	 * Set the curCommunication. (Used for LLRPReader)
	 * 
	 * @param curCommunication
	 */
	public void setCurCommunication(Communication curCommunication) {
		this.curCommunication = curCommunication;
	}

	/**
	 * Returns the curOperatingState. []
	 * 
	 * @return Returns the curOperatingState.
	 */
	protected AbstractCommandControllerOperatingState getCurOperatingState() {
		return this.curOperatingState;
	}

	/**
	 * Returns the initialOperatingState.
	 * 
	 * @return Returns the initialOperatingState.
	 */
	protected AbstractCommandControllerOperatingState getInitialOperatingState() {
		return this.initialOperatingState;

	}

	/**
	 * Returns the suspensionLock.
	 * 
	 * @return Returns the suspensionLock.
	 */
	protected Object getSuspensionLock() {
		return this.suspensionLock;
	}

	/**
	 * Returns the interrupted flag. Callers of this method should be sure to be
	 * synchonized on the suspensionLock before calling this.
	 * 
	 * @return Returns the interrupted.
	 */
	protected boolean isInterrupted() {
		return this.interrupted;
	}

	/**
	 * Returns the suspended flag. Callers of this method should be sure to be
	 * synchonized on the suspensionLock before calling this.
	 * 
	 * @return Returns the suspended.
	 */
	protected boolean isSuspended() {
		return this.suspended;
	}

	/**
	 * Processes the passed command and returns a response.
	 * 
	 * @param command
	 *            The command to process.
	 * @return A response message for the command.
	 * @throws CommandControllerException
	 *             If this method is called an the processCommand cannot be
	 *             completed.
	 */
	public ArrayList<Object> processCommand(byte[] command)
			throws CommandControllerException {
		/* Call the current power state's corresponding method */
		return ((CommandControllerPowerState) this.getPowerState())
				.processCommand(command, this);

	}

	/**
	 * Sets interrupted to the passed parameter, interrupted. Callers of this
	 * method should be sure to be synchonized on the suspensionLock before
	 * calling this.
	 * 
	 * @param interrupted
	 *            The interrupted to set.
	 */
	protected void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	/**
	 * Sets suspended to the passed parameter, suspended. Callers of this method
	 * should be sure to be synchonized on the suspensionLock before calling
	 * this.
	 * 
	 * @param suspended
	 *            The suspended to set.
	 */
	protected void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	/**
	 * An abstract implementation of the update method. Checks the source of the
	 * update before proceeding. If the source is the connectionControlSignal,
	 * turns this controller on/off and connects/disconnects the communication.
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		/* Call AbstractPowerModule's implementation first */
		super.update(o, arg);
		/* Now, check for a connection signal update */
		if (o == this.connectionControlSignal) {
			/* If a connection turns on, turn this on */
			if (this.connectionControlSignal.getControlVariableValue()) {
				/* Only turn this thing on if the power control variable is on */
				if (this.getPowerControlSignal().getControlVariableValue()) {

					logger
							.debug("connection is turing on because connection signal changed to true");

					/* Connect */
					this.getCurCommunication().connect();

					/*
					 * This block turns on the object based on if there is extra
					 * information or not in the object.
					 */
					if (this.connectionControlSignal.getExtraInformation() != null) {
						/* Turn on with the command to execute */
						this.turnOn(this.connectionControlSignal
								.getExtraInformation());
					} else {
						/* Turn on */
						this.turnOn();
					}

					// certain operating states, such as the loginUnconnected,
					// need to do something as soon as a new connection is made
					if (this.connectionControlSignal.getExtraInformation() == null) {
						this.getCurOperatingState().initialize(this,
								this.getCurCommunication());
					}

				}

			} else {
				/* Disconnection signal... turn this off and disconnect. */
				logger
						.debug("abstract command controller is turing off and disconnecting because connection signal changed to false");
				this.turnOff();
				this.getCurCommunication().disconnect();

			}

		}

	}

}
