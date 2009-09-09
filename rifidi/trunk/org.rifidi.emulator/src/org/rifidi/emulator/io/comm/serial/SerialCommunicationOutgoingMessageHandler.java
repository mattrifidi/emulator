/*
 *  SerialCommunicationOutgoingMessageHandler.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.io.comm.serial;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.DataBuffer;
import org.rifidi.emulator.common.DataBufferInterruptedException;


/**
 * This class handles the outgoing messages for the SerialCommunication module.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class SerialCommunicationOutgoingMessageHandler implements Runnable {

	/**
	 * Message logger
	 */
	private static Log logger =
		 LogFactory.getLog(SerialCommunicationOutgoingMessageHandler.class);

	private SerialCommunication host;

	private OutputStream serialOut;

	public SerialCommunicationOutgoingMessageHandler(SerialCommunication host) {
		this.host = host;
		serialOut = host.getSerialOutputStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		byte[] message = null;
		DataBuffer<byte[]> newBuffer = host.getSendBuffer();

		boolean keepRunning = true;

		// As long as no errors occur, keep going
		while (keepRunning) {

			try {
				// Take the next message from the buffer
				logger.debug("Waiting for message");
				message = newBuffer.takeNextFromBuffer();
				String y="";
				for(int i : message){
					y+=String.valueOf(i);
				}
				logger.debug("Message recieved, preparing to send: y=" + y);
			} catch (DataBufferInterruptedException e) {
				logger.error(e.getMessage());
				keepRunning = false;
			}
			if (keepRunning) {
				try {
					// Write it out to the stream
					String x = "";
					for (byte i : message) {
						x += String.format("%1$02x", i) + " ";
					}
					serialOut.write(message);
					logger.debug("Sent message: x="+x);
					this.host.getConsoleLogger().info("<OUTPUT> "+x+"</OUTPUT>");
				} catch (IOException e) {
					logger.error(e.getMessage());
					keepRunning = false;
				}
			}
		}
	}
}
