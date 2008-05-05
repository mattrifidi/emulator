/*
 *  @(#)TCPServerCommunicationSampleServer.java
 *
 *  Created:	Oct 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.ip.tcpserver;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.io.comm.abstract_.AbstractCommunication;
import org.rifidi.emulator.io.comm.logFormatter.GenericStringLogFormatter;
import org.rifidi.emulator.io.comm.streamreader.GenericCharStreamReader;
import org.rifidi.emulator.io.protocol.RawProtocol;

/**
 * A sample test server for a TCPServerCommunication.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPServerCommunicationSampleServer implements Runnable, Observer {

	/**
	 * Is anyone connected?
	 */
	private boolean connected;

	/**
	 * The connection control signal.
	 */
	private ControlSignal<Boolean> connectionControlSignal;

	/**
	 * The current communication.
	 */
	private AbstractCommunication communication;

	/**
	 * Basic constructor.
	 * 
	 * @param communication
	 *            The communication.
	 * @param connSignal
	 *            The connection signal to observe.
	 */
	public TCPServerCommunicationSampleServer(
			AbstractCommunication communication,
			ControlSignal<Boolean> connSignal) {
		this.communication = communication;
		this.connected = false;
		this.connectionControlSignal = connSignal;

	}

	/**
	 * Oh yeah.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/* Add as an observer. */
		this.connectionControlSignal.addObserver(this);

		/* Condition variable for continuing execution. */
		boolean keepRunning = true;

		/* Run until a quit command executed (or program killed) */
		while (keepRunning) {
			logger.debug("MiniServer - waiting...");

			/* Wait until connected */
			synchronized (this) {
				while (!this.connected) {
					/* Keep waiting until connected */
					try {
						this.wait();
					} catch (InterruptedException e) {
						/* Do nothing */

					}

				}
			}

			logger.debug("MiniServer - done waiting.");

			/* Send a welcome message */
			try {
				this.communication.sendBytes("Welcome to the echo server!\n> "
						.getBytes());
			} catch (CommunicationException e1) {
				this.connected = false;

			}

			logger.debug("MiniServer - Sent welcome message"
					+ "; waiting for incoming messages.");

			/* Receive messages until disconnected */
			while (this.connected) {

				try {
					/* Get a message */
					String lastIncomingMessage = new String(this.communication
							.receiveBytes());

					/* Echo */
					this.communication.sendBytes((lastIncomingMessage + "\n> ")
							.getBytes());

					/* Detect a quit command */
					if (lastIncomingMessage.equals("quit")) {
						logger.debug("Quitting...");
						keepRunning = false;
						this.communication.turnOff(this.getClass());

					}

				} catch (CommunicationException e) {
					logger.info(e.getMessage());
					this.connected = false;

				}

			}

		}

		/* Stop observing. */
		this.connectionControlSignal.deleteObserver(this);

		logger.info("Mini-server is done.");

	}

	/**
	 * Updates the connected variable.
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		synchronized (this) {
			this.connected = this.connectionControlSignal
					.getControlVariableValue();
			this.notifyAll();

		}

	}

	/**
	 * The logger for this class.
	 */
	private static final Log logger = LogFactory
			.getLog(TCPServerCommunicationSampleServer.class);

	/**
	 * The main program.
	 * 
	 * @param args
	 *            Command-line arguments (ignored).
	 */
	public static void main(String[] args) {

		String host = "127.0.0.1";
		int port = 30000;

		/* The control signal to use for power */
		ControlSignal<Boolean> powerControlSignal = new ControlSignal<Boolean>(
				true);

		/* The control signal to use for connections */
		ControlSignal<Boolean> connectionControlSignal = new ControlSignal<Boolean>(
				false);

		/* Make the communication. */
		final TCPServerCommunication communication = new TCPServerCommunication(
				new RawProtocol(), powerControlSignal, connectionControlSignal,
				host, port, "fasdf", GenericCharStreamReader.class, new GenericStringLogFormatter());

		/* Turn it on */
		communication.turnOn();

		/* Create a little server */
		TCPServerCommunicationSampleServer miniServer = new TCPServerCommunicationSampleServer(
				communication, connectionControlSignal);

		/* Kick off the server */
		new Thread(miniServer, "TCPTest MiniServer ").start();

		/* All done, program will go until server dies */

	}
}
