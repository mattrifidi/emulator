/*
 *  SymbolBitCommandFormatter.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.symbol.formatter;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.utilities.comm.CRC.CRC16;

/**
 * A formatter for the symbol reader.  
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class SymbolBitCommandFormatter implements CommandFormatter {

	/*
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(SymbolBitCommandFormatter.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	public ArrayList<Object> decode(byte[] arg) {

		if (arg == null || arg.length < 4) {
			return null;
		}
		ArrayList<Object> retVal = new ArrayList<Object>();
		retVal.add(ByteAndHexConvertingUtility.toHexString(arg[3]));
		retVal.add(arg);

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#encode(java.util.ArrayList)
	 */
	public ArrayList<Object> encode(ArrayList<Object> arg) {

		ArrayList<Object> retVal = new ArrayList<Object>();

		for (Object o : arg) {
			byte[] command = (byte[]) o;

			/*
			 * Create outgoing array list that is 3 slots bigger than incoming
			 * one: one for start of frame and two for crc bytes
			 */
			byte[] outgoingCommand = new byte[command.length + 3];
			for (int i = 0; i < command.length; i++) {
				outgoingCommand[i + 1] = command[i];
			}

			/* Add Start of Frame Byte */
			outgoingCommand[0] = 0x01;

			// Calculate CRC
			int crc = CRC16.calculateCRC(command, 0xBEEF,
					CRC16.XR400_CRC_TABLE, true);
			byte[] crcBytes = ByteAndHexConvertingUtility
					.intToByteArray(crc, 2);

			// put crc in outgoingCommand, LSB first
			outgoingCommand[outgoingCommand.length - 2] = crcBytes[1];
			outgoingCommand[outgoingCommand.length - 1] = crcBytes[0];
			
			logger.debug(ByteAndHexConvertingUtility.toHexString(outgoingCommand));
			
			
			retVal.add(outgoingCommand);

		}

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#getActualCommand(byte[])
	 */
	public String getActualCommand(byte[] arg) {
		return new String(arg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#promptSuppress()
	 */
	public boolean promptSuppress() {
		return false;
	}

}
