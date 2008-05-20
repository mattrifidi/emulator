/*
 *  @(#)TCPServerCommunicationIncomingMessageHandler.java
 *
 *  Created:	Sep 28, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.ip.tcpserver;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.DataBufferInterruptedException;
import org.rifidi.emulator.io.comm.streamreader.AbstractStreamReader;
import org.rifidi.emulator.io.comm.streamreader.GenericByteStreamReader;
import org.rifidi.emulator.io.comm.streamreader.GenericCharStreamReader;
import org.rifidi.emulator.io.protocol.ProtocolValidationException;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

/**
 * A runnable which monitors for incoming messages from a
 * TCPServerCommunication's client and stores the them as they arrive. Runs
 * until the client socket is closed.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPServerCommunicationIncomingMessageHandler implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(TCPServerCommunicationIncomingMessageHandler.class);

	/**
	 * The TCPServerCommunication this object is bound to.
	 */
	private TCPServerCommunication hostCommunication;

	/**
	 * The Abstract stream Reader class to use
	 */
	private Class readerClass;

	/**
	 * Creates a TCPServerCommunicationIncomingMessageHandler which is bound to
	 * the passed TCPServerCommunication. This constructor has default access so
	 * that only tcpserver-package classes can create these.
	 * 
	 * It also takes in a class which implements the AbstractStreamReader class
	 * that is used to parse the bytes on the input socket in a way required by
	 * the specific RFID reader being emulated.
	 * 
	 * @param hostCommunication
	 *            The TCPServerCommunication this object is bound to.
	 * @param reader
	 *            The AbstractStreamReader with an overridden read() method.
	 */
	public TCPServerCommunicationIncomingMessageHandler(
			TCPServerCommunication hostCommunication) {

		this.hostCommunication = hostCommunication;

		Class reader = hostCommunication.getAbstractStreamReader();

		Class[] interfaces = reader.getInterfaces();
		boolean interfaceFound = false;
		for (Class i : interfaces) {
			if (i.equals(AbstractStreamReader.class)) {
				interfaceFound = true;
			}
		}
		if (interfaceFound) {
			this.readerClass = reader;
		} else {
			logger
					.error("Expected class that implemets AbstractStreamReader, but got class of type: "
							+ reader.getClass());
			throw (new RuntimeException(
					"Expected class that implemets AbstractStreamReader, but got class of type: "
							+ reader.getClass()));
		}
	}

	/**
	 * The main logic of the monitor. Reads in data from the client socket until
	 * the client socket is closed/disconnected.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Socket clientSocket = this.hostCommunication.getClientSocket();

		/* Make a reader to get data from the client socket. */
		AbstractStreamReader socketIn = null;

		Constructor c = null;
		try {
			/* The class has to have a single paramater of type InputStream */
			c = readerClass.getConstructor(new Class[] { InputStream.class });
		} catch (SecurityException e1) {
			logger.error("Security exception while getting the Constructor\n"
					+ e1.getMessage());
			throw (new RuntimeException(
					"Security exception while getting constructor"));
		} catch (NoSuchMethodException e1) {
			logger
					.error("No such method exception while getting the constructor.  "
							+ "Make sure that the class has a signle paramater of type Input Stream\n "
							+ e1.getMessage());
			throw (new RuntimeException(
					"No such method exception while getting the constructor"));
		}

		try {
			socketIn = (AbstractStreamReader) c.newInstance(clientSocket
					.getInputStream());
		} catch (IllegalArgumentException e1) {
			logger
					.error("Arugment of type java.io.Reader expected, but not found\n "
							+ e1.getMessage());
			throw (new RuntimeException(
					"Arugment of type java.io.Reader expected, but not found"));
		} catch (InstantiationException e1) {
			logger
					.error("Instantion exception while instantiating a new bufferedreader\n "
							+ e1.getMessage());
			throw (new RuntimeException(
					"Instantion exception while instantiating a new bufferedreader"));
		} catch (IllegalAccessException e1) {
			logger
					.error("Illegal Access Exception while instantiating a new bufferedreader\n "
							+ e1.getMessage());
			throw (new RuntimeException(
					"Illegal Access Exception while instantiating a new bufferedreader\n"));
		} catch (InvocationTargetException e1) {
			logger
					.error("Invocation Target Exception while instantiating a new bufferedreader\n "
							+ e1.getMessage());
			throw (new RuntimeException(
					"Invocation Target Exception while instantiating a new bufferedreader\n"));
		} catch (IOException e1) {
			logger
					.error("IO exception while opening the input stelse return bytes.toArray();ream\n "
							+ e1.getMessage());
			throw (new RuntimeException(
					"IO exception while opening the input stream\n"));
		}

		/* Only proceed if we grabbed a valid reader */
		if (socketIn != null) {

			/* Variable to control reading. */
			boolean keepRunning = true;

			/* Read in while this connection is open. */
			while (keepRunning) {
				// synchronized (synch) {

				/* Some buffers to hold single line and whole message */
				byte[] buffer = null;

				/* Read lines into the buffer */
				if (socketIn != null) {

					/* Read the first line in */
					try {
						buffer = socketIn.read();
					} catch (IOException e) {
						/* Drop the socket */
						buffer = null;
					}
				}

				/* Throw the received data into the received buffer */
				if (buffer != null) {
					/* Grab the bytes of the received data */
					/* Remove protocol before throwing in the buffer. */
					try {

						List<byte[]> listOfBytes = this.hostCommunication
								.getProtocol().removeProtocol(buffer);
						for (byte[] b : listOfBytes) {

							String message = this.hostCommunication
									.getLogFormatter().formatMessage(b);
							
							this.hostCommunication.getConsoleLogger().info(
									"<INPUT> " + message + "</INPUT>");

							this.hostCommunication.getReceiveBuffer()
									.addToBuffer(b);
						}
					} catch (DataBufferInterruptedException e) {
						/* The DataBuffer has been interrupted, close client */
						keepRunning = false;

					} catch (ProtocolValidationException e) {
						/*
						 * Received a malformed message, report to debug logs
						 */
						logger.debug(e.getMessage());

					}

				} else {
					keepRunning = false;

				}
				// }

			} /* end: while(isDisconnected) */

			/* Socket disconnected/closed -- disconnect the communication. */
			this.hostCommunication.disconnect();

		} /* end: if(socketIn != null)... */

	}
}
