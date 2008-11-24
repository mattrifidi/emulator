/*
 *  AwidTagReader.java
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
import org.rifidi.emulator.extra.CommandInformation;
import org.rifidi.emulator.extra.ExtraInformation;
import org.rifidi.emulator.reader.awid.module.AwidReaderSharedResources;
import org.rifidi.emulator.reader.awid.sharedrc.tagmemory.AwidTagMemory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * This class handles any methods where a tag is being read. They will turn on
 * control signals so that other methods can be called repeatedly.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidTagReader {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger =
		 LogFactory.getLog(AwidTagReader.class);

	/**
	 * Turn on the control signal that will activate the single_epc_c1_gen2
	 * method.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject single_epc_c1_gen2_handler(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("starting epc gen2 method");

		// addTags((AlienRadio)asr.getRadio());

		ArrayList<Object> retVal = new ArrayList<Object>();

		AwidReaderSharedResources awidsr = (AwidReaderSharedResources) asr;
		HashMap<byte[], Integer> commandMap = new HashMap<byte[], Integer>();

		commandMap.put("single_epc_c1_gen2".getBytes(), 150);

		((AwidTagMemory)asr.getTagMemory()).resume();
		
		ExtraInformation extraInfo = new CommandInformation(commandMap, 150,
				CommandInformation.LimitedRunningState.NOT_LIMITED);

		awidsr.getAutonomousPowerSignal().setExtraInformation(extraInfo);

		retVal.add(new String(new byte[] { 0x30, 0x30 }));
		arg.setReturnValue(retVal);

		awidsr.getAutonomousPowerSignal().setControlVariableValue(true);

		return arg;
	}

	/**
	 * Turn on the control signal that will activate the single_epc_c1_gen1
	 * method.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject single_epc_c1_gen1_handler(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("starting epc gen1 method");

		// addTags((AlienRadio)asr.getRadio());

		ArrayList<Object> retVal = new ArrayList<Object>();

		AwidReaderSharedResources awidsr = (AwidReaderSharedResources) asr;
		HashMap<byte[], Integer> commandMap = new HashMap<byte[], Integer>();

		commandMap.put("single_epc_c1_gen1".getBytes(), 150);

		ExtraInformation extraInfo = new CommandInformation(commandMap, 150,
				CommandInformation.LimitedRunningState.NOT_LIMITED);
		
		((AwidTagMemory)asr.getTagMemory()).resume();

		// commandMap.put(null, 0);
		awidsr.getAutonomousPowerSignal().setExtraInformation(extraInfo);

		retVal.add(new String(new byte[] { 0x30, 0x30 }));
		arg.setReturnValue(retVal);

		awidsr.getAutonomousPowerSignal().setControlVariableValue(true);

		return arg;
	}

	/**
	 * Turn on the control signal that will activate the epc_c1_gen1 method.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject epc_c1_gen1_handler(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("starting epc gen1 method");

		// addTags((AlienRadio)asr.getRadio());

		ArrayList<Object> retVal = new ArrayList<Object>();

		AwidReaderSharedResources awidsr = (AwidReaderSharedResources) asr;
		HashMap<byte[], Integer> commandMap = new HashMap<byte[], Integer>();

		commandMap.put("epc_c1_gen1".getBytes(), 150);
		
		((AwidTagMemory)asr.getTagMemory()).resume();
		
		ExtraInformation extraInfo = new CommandInformation(commandMap, 150,
				CommandInformation.LimitedRunningState.NOT_LIMITED);

		awidsr.getAutonomousPowerSignal().setExtraInformation(extraInfo);

		retVal.add(new String(new byte[] { 0x30, 0x30 }));
		arg.setReturnValue(retVal);

		awidsr.getAutonomousPowerSignal().setControlVariableValue(true);

		return arg;
	}
	
	/**
	 * Turn on the control signal that will activate the epc_c1_gen2 method.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject epc_c1_gen2_handler(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("starting epc gen2 method");

		ArrayList<Object> retVal = new ArrayList<Object>();

		AwidReaderSharedResources awidsr = (AwidReaderSharedResources) asr;
		HashMap<byte[], Integer> commandMap = new HashMap<byte[], Integer>();

		commandMap.put("epc_c1_gen2".getBytes(), 150);
		
		((AwidTagMemory)asr.getTagMemory()).resume();
		
		ExtraInformation extraInfo = new CommandInformation(commandMap, 150,
				CommandInformation.LimitedRunningState.NOT_LIMITED);

		awidsr.getAutonomousPowerSignal().setExtraInformation(extraInfo);

		retVal.add(new String(new byte[] { 0x30, 0x30 }));
		arg.setReturnValue(retVal);

		awidsr.getAutonomousPowerSignal().setControlVariableValue(true);

		return arg;
	}
	
	/**
	 * Generic method for portal ids.  Not yet supported.  
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject portal_ids(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.info("METHOD NOT YET SUPPORTED: " + arg.getCurrentQueryName());
		return AwidCommon.return_zero(arg, asr);
	}
}
