/**
 * 
 */
package org.rifidi.emulator.reader.awid.module;

import java.util.ArrayList;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.extra.CommandInformation;
import org.rifidi.emulator.extra.CommandInformation.LimitedRunningState;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.reader.command.controller.CommandControllerException;
import org.rifidi.emulator.reader.command.controller.autonomous.AutonomousCommandController;
import org.rifidi.emulator.reader.command.controller.autonomous.AutonomousCommandExecuter;
import org.rifidi.utilities.ByteAndHexConvertingUtility;

/**
 * @author kyle
 * 
 */
public class AwidAutonomousCommandExecuter extends AutonomousCommandExecuter {

	private static final Log logger = LogFactory
			.getLog(AwidAutonomousCommandExecuter.class);

	/**
	 * Constructor from superclass
	 * 
	 * @param controller
	 * @param command
	 * @param period
	 */
	public AwidAutonomousCommandExecuter(
			AutonomousCommandController controller, byte[] command, int period) {
		super(controller, command, period);
	}

	/**
	 * Constructor from super class
	 * 
	 * @param awidAutonomousCommandController
	 * @param curCommand
	 * @param curPeriod
	 * @param value
	 * @param state
	 */
	public AwidAutonomousCommandExecuter(
			AwidAutonomousCommandController awidAutonomousCommandController,
			byte[] curCommand, int curPeriod, int value,
			LimitedRunningState state) {
		super(awidAutonomousCommandController, curCommand, curPeriod, value,
				state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.emulator.reader.command.controller.autonomous.
	 * AutonomousCommandExecuter#run()
	 */
	@Override
	public void run() {
		/* Keep running as long as this hasn't been stopped forcefully */
		synchronized (this) {

			long time = 0;

			/*
			 * This is to determine if the executor only runs for a limited
			 * time. If this was the case then set the start time here.
			 */
			if (state
					.equals(CommandInformation.LimitedRunningState.TIME_LIMITED)) {
				time = System.currentTimeMillis();
				logger.debug("Time Limited time = " + time);
			}

			while (!this.interrupted) {
				/*
				 * This is a cycle limited state where it only does a certain
				 * amount of reads. When the number of cycles are reached it
				 * turns off the autonomous controller completely.
				 */
				if (state
						.equals(CommandInformation.LimitedRunningState.CYCLE_LIMITED)
						&& value == 0) {
					/* Turn off when no cycles left */
					commandController.getPowerState()
							.turnOff(commandController);
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
						System.out.println("SENDING: " + obj);
						this.commandController.getCurCommunication().sendBytes(
								Hex.decodeHex(((String) obj).replace(" ", "")
										.toCharArray()));
					}
				} catch (CommunicationException e1) {
					/* The underlying communication was interrupted, stop. */
					this.interrupt();
				} catch (CommandControllerException e) {
					/* The CommandController was interrupted, stop. */
					this.interrupt();
				} catch (DecoderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (state
						.equals(CommandInformation.LimitedRunningState.TIME_LIMITED)
						&& (System.currentTimeMillis() - time) > value) {
					logger.debug("Time controller turn off");
					try {
						// TODO: CRC bytes are incorrect
						System.out.println("sending a timeout");
						this.commandController.getCurCommunication().sendBytes(
								new byte[] { 0x06, (byte) 0xFF, 0x1E,
										(byte) 0x80, 0x00, 0x00 });
					} catch (CommunicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					commandController.getPowerState()
							.turnOff(commandController);
					this.interrupted = true;
				}
			}
		}
	}

}
