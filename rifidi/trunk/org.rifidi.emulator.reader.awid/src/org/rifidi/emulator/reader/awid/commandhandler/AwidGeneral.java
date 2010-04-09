/*
 *  AwidGeneral.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.commandhandler;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.awid.module.AwidReaderSharedResources;
import org.rifidi.emulator.reader.awid.sharedrc.tagmemory.AwidTagMemory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;

/**
 * This class contains all of the interactive commands for the Awid reader.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidGeneral {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AwidGeneral.class);

	/**
	 * Stop the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject stop(CommandObject arg,
			AbstractReaderSharedResources asr) {
		AwidReaderSharedResources awidsr = (AwidReaderSharedResources) asr;
		awidsr.getAutonomousPowerSignal().setControlVariableValue(false);

		((AwidTagMemory) asr.getTagMemory()).suspend();

		// The return value array
		ArrayList<Object> retVal = new ArrayList<Object>();
		// add the current value to the return value
		retVal.add("00");
		arg.setReturnValue(retVal);

		return arg;
	}

	/**
	 * Get the status for the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject system(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("Calling the system command");
		return (AwidCommon.returnDefaultValue(arg, asr));
	}
	
	/**
	 * Welcome message for the AWID.  
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject welcome(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("Calling the welcome command");
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject rf_power_level_control(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ReaderProperty readProp = asr.getPropertyMap().get(
				arg.getDisplayName().toLowerCase());
		readProp.setPropertyValue((String) arg.getArguments().get(0));
		ArrayList<Object> retVal = new ArrayList<Object>();
		retVal.add(new String(new byte[] { 0x00, 0x00 }));
		arg.setReturnValue(retVal);
		return (arg);
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject iq_chip_version(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject antenna_switch_rate(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}
	
	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject antenna_switch(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * The temperature of the
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject temperature(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject rf_power_on(CommandObject arg,
			AbstractReaderSharedResources asr) {
		HashMap<String, ReaderProperty> comMap = (HashMap<String, ReaderProperty>) asr
				.getPropertyMap();

		AwidReaderSharedResources awidsr = (AwidReaderSharedResources) asr;

		awidsr.setRf_power(true);

		// The properties for the current object
		ReaderProperty newProp = comMap.get(arg.getDisplayName().toLowerCase());

		newProp.setPropertyValue(arg.getDefaultValue());

		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject rf_power_off(CommandObject arg,
			AbstractReaderSharedResources asr) {
		HashMap<String, ReaderProperty> comMap = (HashMap<String, ReaderProperty>) asr
				.getPropertyMap();

		AwidReaderSharedResources awidsr = (AwidReaderSharedResources) asr;

		awidsr.setRf_power(false);

		// The properties for the current object
		ReaderProperty newProp = comMap.get(arg.getDisplayName().toLowerCase());

		newProp.setPropertyValue(arg.getDefaultValue());

		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject reader_status(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("Calling the status command");
		CommandObject o = AwidCommon.returnDefaultValue(arg, asr);
		
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject antenna_1(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject antenna_2(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject soft_reset(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AwidCommon.return_zero(arg, asr);
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject portal_id_filter(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject protocol_data_rate(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject change_baud_rate(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject write_rf_power_level_control(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * Returns the class 1 chip choice for
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject class_1_chip_choice(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * Turns on/off the output delay before ouput (default is off)
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject output_delay(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject pulse_shaping_control(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.returnDefaultValue(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject antenna_source(CommandObject arg,
			AbstractReaderSharedResources asr) {
		AwidReaderSharedResources arsr = (AwidReaderSharedResources) asr;
		String source = (String) arg.getArguments().get(0);
		if (source != null) {
			if(source.equals("01")) {
				arsr.setAntenna_source(true);
			} else {
				arsr.setAntenna_source(false);
			}
		}

		return (AwidCommon.setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject debug_message(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AwidCommon.setter(arg, asr));
	}
}
