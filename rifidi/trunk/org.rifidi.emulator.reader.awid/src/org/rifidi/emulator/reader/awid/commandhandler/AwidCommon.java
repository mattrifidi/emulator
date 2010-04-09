/*
 *  AwidCommon.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.commandhandler;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;

/**
 * This class contains common methods that all Awid handler methods can call.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidCommon {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AwidCommon.class);

	/**
	 * Get/Set the value for the Awid.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public static CommandObject setter(CommandObject arg,
			AbstractReaderSharedResources asr) {

		// Map to obtain the properties for the current object
		HashMap<String, ReaderProperty> comMap = (HashMap<String, ReaderProperty>) asr
				.getPropertyMap();
		// The properties for the current object
		ReaderProperty newProp = comMap.get(arg.getDisplayName().toLowerCase());
		// Set the argument
		newProp.setPropertyValue((String) arg.getArguments().get(0));

		// The return value array
		ArrayList<Object> retVal = new ArrayList<Object>();
		// add the current value to the return value
		retVal.add("00");

		arg.setReturnValue(retVal);

		return arg;
	}

	/**
	 * This method returns the string inside the "default" tag in the
	 * reader.xml. If the string starts with an ACK byte (i.e. 00 or FF), divide
	 * the return val into two elements in the return array.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public static CommandObject returnDefaultValue(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<Object> retVal = new ArrayList<Object>();
		String defVal = arg.getDefaultValue();
		if (defVal != null && !defVal.equals("")) {
			// If the default return value starts with a 00, add a 00
			if (defVal.startsWith("00") || defVal.startsWith("FF")) {
				if (defVal.startsWith("00")) {
					retVal.add("00");
				} else if (defVal.startsWith("FF")) {
					retVal.add("FF");
				}
				// add the rest to the next argument, if there is anything there
				if (defVal.length() > 3) {
					retVal.add(defVal.substring(3));
				}
			} else {
				// if the default value does not start with an ACK, just add it
				retVal.add(arg.getDefaultValue());
			}
		}

		arg.setReturnValue(retVal);

		return arg;
	}

	/**
	 * 
	 * 
	 * @param arg
	 * @return
	 */
	public static String formatTagString(String arg) {
		String retVal = "";
		for (int i = 0; i < arg.length(); i += 2) {
			try {
				retVal += arg.substring(i, i + 2);
				retVal += " ";
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("ArrayIndexOutOfBounds on tagRead");
			}
		}
		retVal = retVal.trim();
		return retVal;
	}

	public static CommandObject return_zero(CommandObject arg,
			AbstractReaderSharedResources asr) {

		arg.getReturnValue().add(new byte[] { 0x30, 0x30 });

		return arg;
	}
}
