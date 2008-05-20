/*
 *  LLRPLogFormatter.java
 *
 *  Created:	Dec 11, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *  Author:    Kyle Neumeier - kyle@pramari.com
 */
package org.rifidi.emulator.reader.llrp.formatter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.comm.ip.tcpserver.TCPServerCommunicationIncomingMessageHandler;
import org.rifidi.emulator.io.comm.logFormatter.LogFormatter;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

import edu.uark.csce.llrp.Message;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public class LLRPLogFormatter implements LogFormatter {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(LLRPLogFormatter.class);
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.comm.logFormatter.LogFormatter#formatMessage(byte[])
	 */
	public String formatMessage(byte[] rawMessage) {
		
		ByteArrayInputStream is = new ByteArrayInputStream(rawMessage);
		String bytes = ByteAndHexConvertingUtility.toHexString(rawMessage);
		try {
			Message m = Message.receive(is);
			return "\nBYTES (size: " + rawMessage.length +"):  \n" + bytes + "\nXML: \n" + m.toXMLString();
		} catch (Exception e) {
			logger.error("Cannot serialize the message");
			return "\nBytes: " + bytes;
		} 
	}

}
