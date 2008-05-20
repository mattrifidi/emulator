/*
 *  @(#)InteractiveCommandControllerSampleServer.java
 *
 *  Created:	Oct 25, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.command.controller.interactive;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.io.comm.ip.tcpserver.TCPServerCommunication;
import org.rifidi.emulator.io.comm.logFormatter.GenericStringLogFormatter;
import org.rifidi.emulator.io.comm.streamreader.GenericCharStreamReader;
import org.rifidi.emulator.io.protocol.RawProtocol;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandController;
import org.rifidi.emulator.reader.control.adapter.CommandAdapter;

/**
 * A sample server made using an InteractiveCommandController as the basis.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class InteractiveCommandControllerSampleServer implements CommandAdapter {

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory
			.getLog(InteractiveCommandControllerSampleServer.class);

	/**
	 * The signal for connections.
	 */
	private ControlSignal<Boolean> connectionControlSignal;

	/**
	 * The signal for power.
	 */
	private ControlSignal<Boolean> powerControlSignal;

	/**
	 * Constructs a test sample server.
	 * 
	 * @param powerControlSignal
	 *            The power signal to alert.
	 * @param connectionControlSignal
	 *            The connection signal to alert.
	 * 
	 */
	public InteractiveCommandControllerSampleServer(
			ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal) {
		/* Assign instance variables */
		this.connectionControlSignal = connectionControlSignal;
		this.powerControlSignal = powerControlSignal;

	}

	/**
	 * Echos the command, adding a prompt afterwards.
	 * 
	 * @see org.rifidi.emulator.reader.control.adapter.CommandAdapter#executeCommand(byte[])
	 */
	public ArrayList<Object> executeCommand(byte[] command) {
		/* The string to eventually return. */
		String retString = new String(command);

		/* Simple command parser */
		if (retString.equalsIgnoreCase("quit")) {
			retString += "\nGoodbye!";
			/* Force a disconnect */
			this.connectionControlSignal.setControlVariableValue(false);

		} else if (retString.equalsIgnoreCase("shutdown")) {
			retString += "\nShutting down...";
			/* Force a shutdown */
			this.connectionControlSignal.setControlVariableValue(false);
			this.powerControlSignal.setControlVariableValue(false);

		} else if (retString.startsWith("authenticate")) {
			if (retString.endsWith("user pass")) {
				retString = "true";

			} else {
				retString = "false";

			}

		} else if (retString.equalsIgnoreCase("authenticationError")) {
			retString = "Error! Bad User/Pass\n";

		} else if (retString.equalsIgnoreCase("authenticationSuccess")) {
			retString = "Authenticated! Horray!\n";

		} else if (retString.equalsIgnoreCase("disconnectUser")) {
			/* Force a disconnect */
			this.connectionControlSignal.setControlVariableValue(false);

		} else if (retString.equalsIgnoreCase("passwordPrompt")) {
			retString = "Password> ";

		} else if (retString.startsWith("showPassword")) {
			retString = retString.substring("showPassword".length()).trim()
					+ "\n";

		} else if (retString.startsWith("showUsername")) {
			retString = retString.substring("showUsername".length()).trim()
					+ "\n";

		} else if (retString.equalsIgnoreCase("usernamePrompt")) {
			retString = "Username> ";

		} else if (retString.equalsIgnoreCase("welcome")) {
			retString = "Welcome to the "
					+ "InteractiveCommandControllerSampleServer. " + "\n";

		} else if (retString.equalsIgnoreCase("commandPrompt")) {
			retString = "> ";

		} else if (retString.equalsIgnoreCase("heartbeat")) {
			retString = "Test Heartbeat\n";

		} else {
			retString += "\n> ";

		}

		/* Return */
		//return retString.getBytes();
		return null;
	}

	/**
	 * The main program.
	 * 
	 * @param args
	 *            Command-line arguments (ignored).
	 */
	public static void main(String[] args) {

		/* Start with connection signal off, power signal on */
		ControlSignal<Boolean> connectionControlSignal = new ControlSignal<Boolean>(
				false);
		ControlSignal<Boolean> powerControlSignal = new ControlSignal<Boolean>(
				true);

		/* Make a TCPServerCommunication */
		TCPServerCommunication communication = new TCPServerCommunication(
				new RawProtocol(), powerControlSignal, connectionControlSignal,
				"127.0.0.1", 30000, "fasdf", GenericCharStreamReader.class, new GenericStringLogFormatter());

		/* Make a new CommandAdapter (out test server) */
		CommandAdapter testAdapter = new InteractiveCommandControllerSampleServer(
				powerControlSignal, connectionControlSignal);

		/* Now, create the InteractiveCommandController */
		@SuppressWarnings("unused")
		AbstractCommandController commandController = new InteractiveCommandController(
				new LoginUnconnectedCommandControllerOperatingState(
						testAdapter, testAdapter, 3), powerControlSignal,
				connectionControlSignal, communication);

		/* Turn on the communication. */
		communication.turnOn();

		/* All done with this... main methods -- only threads remain */

	}
}
