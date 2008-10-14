/*
 *  AlienTag.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.commandhandler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.command.exception.AlienExceptionHandler;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.emulator.reader.alien.sharedrc.tagmemory.AlienTagMemory;
import org.rifidi.emulator.reader.alien.sharedrc.tagmemory.AlientTagListFormatter;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.StringReaderProperty;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.formatter.TagListFormatReaderProperty;
import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * This Class provides all the commands which are needed for proper operation of
 * the TagMemory. This includes commands to set PersistTime, TagFormat,
 * CustomFormat. It also provides the get TagList command.
 * 
 * @author Matthew Dean
 * @author Mike Graupner
 */
public class AlienTag {

	/**
	 * The EmuLogger instance for this class.
	 */
	private static Log logger = LogFactory.getLog(AlienTag.class);

	/**
	 * A convenience method to use to scan the radio and get tags back from the
	 * tag memory. It also increments the read count of the tags.
	 * 
	 * @param asr
	 * @return
	 */
	public static ArrayList<RifidiTag> getTagList(AlienReaderSharedResources asr) {
		GenericRadio aRadio = asr.getRadio();
		AlienTagMemory aTagMemory = (AlienTagMemory) asr.getTagMemory();

		/* scan radio */
		aRadio.scan(AlienTagMemory.getAntennas(asr), aTagMemory);

		/* Get tags from TagMemory */
		ArrayList<RifidiTag> tags = aTagMemory.getTagReport();

		for (RifidiTag t : tags) {
			t.incrementReadCount();
		}
		return tags;
	}

	/**
	 * Returns the tagList currently in view of the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Alien Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject getTagList(CommandObject arg,
			AbstractReaderSharedResources asr) {

		logger.debug("starting the getTagList");

		/* Shared Resource Object */
		AlienReaderSharedResources aAlienSharedResource;
		/* The return value for this command */
		ArrayList<Object> returnArray = new ArrayList<Object>();

		String returnVal = "";

		/* If no prompt suppress add echo to the output */
		if (!arg.getPromptSuppress()) {
			String formattedEcho = arg.getCurrentQueryName()
					+ AlienCommon.NEWLINE;
			returnVal += formattedEcho;
		}

		try {

			aAlienSharedResource = (AlienReaderSharedResources) asr;
			GenericRadio aRadio = aAlienSharedResource.getRadio();
			AlienTagMemory aTagMemory = (AlienTagMemory) asr.getTagMemory();

			// scan radio
			aRadio.scan(AlienTagMemory.getAntennas(aAlienSharedResource),
					aTagMemory);

			/* Get formatted tags from TagMemory */
			TagListFormatReaderProperty tfrp = (TagListFormatReaderProperty) asr
					.getPropertyMap().get("taglistformat");

			StringReaderProperty srp = (StringReaderProperty) asr
					.getPropertyMap().get("taglistcustomformat");

			AlientTagListFormatter formatter = new AlientTagListFormatter(tfrp,
					srp);

			ArrayList<RifidiTag> tags = aTagMemory.getTagReport();

			for (int i = 0; i < tags.size(); i++) {
				tags.get(i).incrementReadCount();
			}

			/* If there is something in the TagMemory add it to the Output */
			if (tags.size() != 0) {
				returnVal += formatter.formatTag(tags);
			} else {
				returnVal += "(No tags)";
				returnVal += AlienCommon.NEWLINE;
			}

		} catch (Exception exc) {
			logger.error("Error occured during getting tags");
			logger.error(exc.getClass() + ": " + exc.getMessage());
		}

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

	public CommandObject tagList_Pre(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("got to the getTagList_pre");
		/* The return value for this command */
		ArrayList<Object> returnVal = new ArrayList<Object>();

		/* If no prompt supress add echo to the output */
		if (!arg.getPromptSuppress()) {
			logger.debug("in the not prompt suppress");
			String formattedEcho = "Get TagList" + AlienCommon.NEWLINE;
			returnVal.add(formattedEcho);
		}

		/* Set the return value */
		arg.setReturnValue(returnVal);

		return arg;
	}

	/**
	 * Gets and sets the persistTime variable
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Alien Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject persistTime(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}

		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Returns the tagListFormat that is currently set for the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Alien Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject tagListFormat(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Specify whether to combine reads of a tag from different antennas into
	 * one TagList entry.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Alien Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject tagListAntennaCombine(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return arg;
	}

	/**
	 * Get the timer on the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Alien Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject timer(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return arg;
	}

	/**
	 * Represents the custom format that can be set for some types of tag.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Alien Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject tagListCustomFormat(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Clears the current TagList
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Alien Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject clearTagList(CommandObject arg,
			AbstractReaderSharedResources asr) {
		
		try {
			asr.getTagMemory().clear();
			AlienReaderSharedResources arsr = (AlienReaderSharedResources) asr;
			arsr.getAutoStateController().clearAutoEvluationStateTags();
		} catch (Exception e) {
		}

		return AlienCommon.returnCommandDefaultValue(arg, asr);
	}

	/**
	 * Gets and sets the type of the tag to return.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Alien Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject tagType(CommandObject arg,
			AbstractReaderSharedResources asr) {

		ArrayList<Object> retValArray = new ArrayList<Object>();
		if (arg.getArguments().size() > 0) {

			if (!AlienCommon.checkIntegerArg(arg)) {
				ArrayList<Object> retVal;
				String i = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(i);
				retVal = new AlienExceptionHandler().invalidCommandError(
						tempVal, "Integer", arg);
				arg.setReturnValue(retVal);
				return arg;
			}

			Integer intVal = new Integer(((String) arg.getArguments().get(0)));
			if (intVal < 1 || intVal > 31) {
				// Here we have to catch a possible range-based error
				if (!arg.getPromptSuppress()) {
					retValArray.add(arg.getCurrentQueryName()
							+ AlienCommon.NEWLINE
							+ "Error 10: Value out of range.  Legal limits"
							+ " are between 1 and 31." + AlienCommon.NEWLINE
							+ AlienCommon.ENDOFREPLY
							+ AlienCommon.NONZEROPROMPT);
				} else {
					retValArray
							.add(arg.getCurrentQueryName()
									+ AlienCommon.NEWLINE
									+ "Error 10: Value out of range.  Legal limits"
									+ " are between 1 and 31."
									+ AlienCommon.ENDOFREPLY);
				}
				arg.setReturnValue(retValArray);
				return arg;
			}

			BigInteger newInt = new BigInteger(String
					.valueOf(intVal.intValue()));
			BigInteger sixteen = new BigInteger("16");
			BigInteger four = new BigInteger("4");
			BigInteger two = new BigInteger("2");
			BigInteger one = new BigInteger("1");

			HashSet<TagGen> types = new HashSet<TagGen>();

			// Doing bitwise operations with BigInteger. If any of the 3 least
			// significant bits are 1, we read gen1 tags
			if (newInt.and(four).intValue() != 0
					|| newInt.and(two).intValue() != 0
					|| newInt.and(one).intValue() != 0) {
				types.add(TagGen.GEN1);
			}

			// If the most significant bit is 1, we read gen2 tags
			if (newInt.and(sixteen).intValue() != 0) {
				types.add(TagGen.GEN2);
			}

			AlienTagMemory tm = (AlienTagMemory) asr.getTagMemory();
			tm.setTagTypeSelection(types);
		}

		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Command does nothing
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Alien Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject wake(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.returnCommandDefaultValue(arg, asr);
	}
}
