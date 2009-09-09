/*
 *  SerialCommunicationIncomingMessageHandler.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.serial;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.DataBuffer;
import org.rifidi.emulator.common.DataBufferInterruptedException;
import org.rifidi.emulator.io.protocol.ProtocolValidationException;

/**
 * This class handles the incoming messages for the SerialCommunication module.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class SerialCommunicationIncomingMessageHandler implements Runnable {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory
			.getLog(SerialCommunicationIncomingMessageHandler.class);

	private SerialCommunication host;

	private InputStream serialIn;

	public SerialCommunicationIncomingMessageHandler(SerialCommunication host) {
		this.host = host;
		serialIn = host.getSerialInputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// byte[] message = null;
		DataBuffer<byte[]> newBuffer = host.getReceiveBuffer();
		ArrayList<Byte> receivedData = new ArrayList<Byte>();

		boolean keepRunning = true;

		DataInputStream dataStream = new DataInputStream(serialIn);

		while (keepRunning) {

			boolean eof = false;

			/* Read as fully as we can */
			while (!eof) {

				try {
					/* Attempt to read in a byte */
					receivedData.add(new Byte(dataStream.readByte()));
					if (dataStream.available() <= 0) {
						eof = true;
					}
				} catch (EOFException eofe) {
					/* Indicate end of file */
					eof = true;
				} catch (IOException e) {
					logger.error(e.getMessage());
					eof = true;
				}
			}

			/* Check that we have something to "receive" */
			if (!receivedData.isEmpty()) {
				logger.debug("Back in the if statement");
				/* Construct an array of bytes */
				byte[] receivedBytes = new byte[receivedData.size()];
				for (int i = 0; i < receivedData.size(); i++) {
					receivedBytes[i] = receivedData.get(i).byteValue();
				}
				receivedData.clear();
				if (keepRunning) {
					/* Actually add to the receive buffer */
					try {
						logger.debug("Adding to buffer: " + receivedBytes);
						List<byte[]> listOfBytes = this.host.getProtocol()
								.removeProtocol(receivedBytes);
						for (byte[] b : listOfBytes) {
							newBuffer.addToBuffer(b);
						}
						String x = "";
						for (byte i : receivedBytes) {
							x += String.format("%1$02x", i) + " ";
						}
						this.host.getConsoleLogger().info("<INPUT> " + x + "</INPUT>");
					} catch (DataBufferInterruptedException e) {
						logger.error(e.getMessage());
						keepRunning = false;
					} catch (ProtocolValidationException e) {
						logger.error(e.getMessage());
						keepRunning = false;
					}
				}
			}
		}
	}
}
