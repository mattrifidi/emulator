/*
 *  @(#)TCPServerCommunicationOutgoingMessageHandler.java
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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.DataBufferInterruptedException;

/**
 * Monitors the current send buffer for incoming data. As long as the client
 * connection is alive, the data will be taken from the send buffer and sent out
 * to the client. Additionally, if a null piece of data is ever retreived from
 * the send buffer, this will terminate.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TCPServerCommunicationOutgoingMessageHandler implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(TCPServerCommunicationOutgoingMessageHandler.class);

	/**
	 * The TCPServerCommunication this object is bound to.
	 */
	private TCPServerCommunication hostCommunication;

	/**
	 * Reader Class that tell us how to print to the log
	 */
	@SuppressWarnings("unchecked")
	private Class readerClass;

	/**
	 * Creates a TCPServerCommunicationOutgoingMessageHandler which is bound to
	 * the passed TCPServerCommunication. This constructor has default access so
	 * that only tcpserver-package classes can create these.
	 * 
	 * @param hostCommunication
	 *            The TCPServerCommunication this object is bound to.
	 * 
	 * @param readerClass
	 *            StreamReader defines how input is read from socket, so it
	 *            tells us how it should be printed to the log
	 */
	TCPServerCommunicationOutgoingMessageHandler(
			TCPServerCommunication hostCommunication) {
		this.hostCommunication = hostCommunication;
		this.readerClass = hostCommunication.getAbstractStreamReader();
	}

	/**
	 * The main logic of this monitor. Takes data from the send buffer and sends
	 * it to the client.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/* Make a buffered writer to output the data */
		OutputStream socketOut = null;
		try {
			socketOut = new BufferedOutputStream(this.hostCommunication
					.getClientSocket().getOutputStream());
		} catch (IOException e) {
			logger.warn(e.getMessage());

		}

		/* Only proceed if we grabbed a valid output stream */
		if (socketOut != null) {

			/* The variable to check to see if this should keep running */
			boolean keepRunning = true;

			/* Run until a write error or null retreived from buffer. */
			while (keepRunning) {

				// synchronized (synch) {

				/* Grab some data from the queue. */
				byte[] dataToSend = null;
				try {
					/* Grab some data from the queue. */
					dataToSend = this.hostCommunication.getSendBuffer()
							.takeNextFromBuffer();

					this.hostCommunication.getConsoleLogger().info(
							"<OUTPUT> "
									+ hostCommunication.getLogFormatter()
											.formatMessage(dataToSend)
									+ "</OUTPUT>");

					logger.debug(hostCommunication.getLogFormatter()
							.formatMessage(dataToSend));

					socketOut.write(dataToSend);

					socketOut.flush();

				} catch (DataBufferInterruptedException e1) {
					/* Retreived bad data from the queue. (interrupted) */
					/* Drop out. */
					keepRunning = false;

				} catch (IOException e) {
					/* Bad write to client - drop out. */
					keepRunning = false;

				}
			} /* end - while(keepRunning)... */

		} /* end - if(socketOut != null)... */

	}

	@SuppressWarnings("unchecked")
	public Class getReaderClass() {
		return readerClass;
	}

}
