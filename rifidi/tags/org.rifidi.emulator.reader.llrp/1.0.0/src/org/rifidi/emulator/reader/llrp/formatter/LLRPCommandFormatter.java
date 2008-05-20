/*
 *  LLRPCommandFormatter.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.formatter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.emulator.reader.llrp.exception.LLRPException;
import org.rifidi.emulator.reader.llrp.util.LLRPUtilities;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

import edu.uark.csce.llrp.Message;

/**
 * @author matt
 * 
 */
public class LLRPCommandFormatter implements CommandFormatter {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(LLRPCommandFormatter.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	public ArrayList<Object> decode(byte[] arg) {

		// Prepare ArrayList<Object>
		ArrayList<Object> retval = new ArrayList<Object>();

		//check for invalid message number
		if (!LLRPUtilities.checkMessageVersion(arg, 1)) {
			retval.add("InvalidLLRPVersion");
			retval.add(arg);
			return retval;
		}

		//handle command
		
		int msgType = LLRPUtilities.calculateMessageNumber(arg);

		// Set ID to first object
		logger.debug("LLRPCOMMANDFORMATTER: Command to " + "call is: "
				+ msgType);
		retval.add(Integer.toString(msgType));

		// Set raw message to second argument
		retval.add(arg);

		return retval;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#encode(java.util.ArrayList)
	 */
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		if (!arg.isEmpty()) {
			ArrayList<Object> retVal = new ArrayList<Object>();
			try {
				retVal.add(((Message) arg.get(0)).serialize());
			} catch (IOException e) {
				logger.error("Cannot convert message to byte array. ");
				// e.printStackTrace();
				// return retVal;
			}
			return retVal;
		}
		return arg;
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
