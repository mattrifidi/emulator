/*
 *  @(#)InteractiveCommandController.java
 *
 *  Created:	Oct 13, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.interactive;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.reader.command.controller.CommandControllerPowerState;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandController;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState;
import org.rifidi.emulator.reader.command.sentinel.CommandSentinel;
import org.rifidi.emulator.reader.command.sentinel.RawCommandSentinel;

/**
 * A generic class which models an interactive command controller. An
 * interactive command controller takes its commands from its
 * CommunicationChannel and gives backs it responses through the same
 * CommunicationChannel.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class InteractiveCommandController extends AbstractCommandController {

	/*
	 * The CommandSentinel is a pre-processing class that can be used to process
	 * special commands. This class intercepts commands on its list and creates
	 * multiple commands out of them.
	 */
	private CommandSentinel sentinel;

	/**
	 * Creates an interactive command controller which is bound to the passed
	 * communication channel and is initially off.
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
	 *            The Communication which this controller interacts with.
	 */
	public InteractiveCommandController(
			AbstractCommandControllerOperatingState initialOperatingState,
			ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal,
			Communication communication) {
		/* Call the super constructor, using an off power state */
		super(InteractiveOffCommandControllerPowerState.getInstance(),
				initialOperatingState, powerControlSignal,
				connectionControlSignal, communication);
		/* Since the sentinel is a very special case scenario we always set
		 * it to a RawCommandSentinel (which does nothing) by default.
		 */
		sentinel = new RawCommandSentinel();
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
	 * Exposes the underlying method to the Autonomous package members.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandController#getCurCommunication()
	 */
	@Override
	protected Communication getCurCommunication() {
		/* Simply call the super method. */
		return super.getCurCommunication();

	}

	/**
	 * Gets the sentinel from this controller.
	 * 
	 * @return the sentinel
	 */
	public CommandSentinel getSentinel() {
		return sentinel;
	}

	/**
	 * Set a sentinel for this controller.
	 * 
	 * @param sentinel
	 *            the sentinel to set
	 */
	public void setSentinel(CommandSentinel sentinel) {
		this.sentinel = sentinel;
	}

}
