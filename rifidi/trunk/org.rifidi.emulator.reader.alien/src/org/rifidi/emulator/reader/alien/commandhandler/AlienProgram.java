/*
 *  AlienProgram.java
 *
 *  Created:	Dec 5, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *  Author:    Kyle Neumeier - kyle@pramari.com
 */
package org.rifidi.emulator.reader.alien.commandhandler;

import java.util.ArrayList;

import javax.naming.AuthenticationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.alien.command.exception.AlienExceptionHandler;
import org.rifidi.emulator.reader.alien.sharedrc.tagmemory.AlienTagMemory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.IntegerReaderProperty;
import org.rifidi.emulator.reader.sharedrc.radio.C1G2Operations;
import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.exceptions.InvalidMemoryAccessException;
import org.rifidi.emulator.tags.impl.C1G2Tag;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * This class handles alien commands that have to do with programming (writing,
 * erasing, killing, locking) tags in the alien reader
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AlienProgram {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(AlienProgram.class);

	public CommandObject progAntenna(CommandObject arg,
			AbstractReaderSharedResources asr) {

		if (!arg.getArguments().isEmpty()) {
			String antenna = ((String) arg.getArguments().get(0));
			String validValues = "0|1|2|3";
			if (!validValues.contains(antenna)) {
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				ArrayList<String> PossibleValues = new ArrayList<String>();
				PossibleValues.add("0");
				PossibleValues.add("1");
				PossibleValues.add("2");
				PossibleValues.add("3");
				ArrayList<Object> retVal = new AlienExceptionHandler().error10(
						tempVal, arg, PossibleValues);
				arg.setReturnValue(retVal);
				return arg;
			}
		}
		return AlienCommon.getter_setter(arg, asr);
	}

	public CommandObject programTag(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<Object> retVal = arg.getReturnValue();

		String bytes = "";
		// Get argument
		if (!arg.getArguments().isEmpty()) {
			bytes = (String) arg.getArguments().get(0);
		}

		// convert argument to byte array
		byte[] newID;
		try {
			newID = ByteAndHexConvertingUtility.fromHexString(bytes);
		} catch (IllegalArgumentException ex) {
			String cur = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(cur);
			retVal = new AlienExceptionHandler().malformedMessageError(tempVal,
					arg);
			arg.setReturnValue(retVal);
			return arg;
		}

		// error if byte[] is not 8 bytes or 12 bytes
		if (newID.length != 8 && newID.length != 12) {
			String cur = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(cur);
			ArrayList<String> PossibleValues = new ArrayList<String>();
			PossibleValues.add("8 byte array in the form of xx xx ...");
			PossibleValues.add("12 byte array in the form of xx xx ...");
			retVal = new AlienExceptionHandler().error10(tempVal, arg,
					PossibleValues);
			arg.setReturnValue(retVal);
			return arg;
		}

		String function = asr.getPropertyMap().get("function")
				.getPropertyStringValue();

		Integer progAntenna = ((IntegerReaderProperty) asr.getPropertyMap()
				.get("progantenna")).getValue();

		// make sure we are in programmer mode
		if (function.equalsIgnoreCase("Programmer")) {
			AlienTagMemory mem = (AlienTagMemory) asr.getTagMemory();
			asr.getRadio().scan(null, mem);
			ArrayList<RifidiTag> tags = mem.getTagReport(progAntenna);

			// make sure there is only one tag on the antenna and it is a GEN2
			// tag
			if (tags.size() == 1 && tags.get(0).getTagGen() == TagGen.GEN2) {
				C1G2Tag tag = (C1G2Tag) tags.get(0).getTag();

				try {
					C1G2Operations.C1G2WriteID(tag, newID, C1G2Operations
							.getAccessPass(tag), asr.getCallbackManager(), asr
							.getRadio().getAntennas().get(progAntenna));
				} catch (AuthenticationException e) {
					String cur = arg.getCurrentQueryName();
					ArrayList<Object> tempVal = new ArrayList<Object>();
					tempVal.add(cur);
					retVal = new AlienExceptionHandler().error137(tempVal, arg);
					e.printStackTrace();
				} catch (InvalidMemoryAccessException e) {
					e.printStackTrace();
				}
			} else {
				// handle case if there is not 1 tag on antenna and/or it is not
				// GEN2
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				retVal = new AlienExceptionHandler().error134(tempVal, arg);
				arg.setReturnValue(retVal);
				return arg;
			}
		} else {
			// handle case when function!=programmer
			String cur = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(cur);
			retVal = new AlienExceptionHandler().error134(tempVal, arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		String returnVal = "";
		ArrayList<Object> returnArray = new ArrayList<Object>();

		returnVal = "Program Tag = " + bytes;

		/* If no prompt suppress add endofreply and prompt to the output */
		if (!arg.getPromptSuppress()) {
			returnVal += AlienCommon.ZEROCHAR;
			returnVal += AlienCommon.NEWLINE;
			returnVal += AlienCommon.NONZEROPROMPT;
		} else {
			returnVal += AlienCommon.ZEROCHAR;
		}

		returnArray.add(returnVal);

		/* Set the return value */
		arg.setReturnValue(returnArray);

		return arg;
	}

}
