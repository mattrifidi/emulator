/*
 *  SiritCommon.java
 *
 *  Created:	17.06.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.commandhandler;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.comm.ip.udp.UDPCommunicationIncomingMessageHandler;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;

/**
 * This is the class for commonly used methods across all Sirit Command
 * Handlers.
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritCommon {

	/** logger instance for this class. */
	private static Log logger = LogFactory
			.getLog(UDPCommunicationIncomingMessageHandler.class);

	/** String to pass back for the Sirit prompt */
	public static final String PROMPT = ">>>";

	/** A constant string which the reader sends out on the end of a reply */
	public static final String ENDOFREPLY = "\r\n\r\n";

	/** A constant string which represents an equal */
	public static final String EQUAL = "=";

	/** A constant string which represents an equal covered by scpaces */
	public static final String EQUALSPACED = " = ";

	/** A constant string that does a newline */
	public static final String NEWLINE = "\r\n";

	/**
	 * A constant string that is used to look for the New Line literal in the
	 * sirit xml file to replace with correct escape char.
	 */
	public static final String NEWLINELITERAL = "[SIRITNEWLINE]";

	/**
	 * Generic getter/setter method. If there is an argument, the currentValue
	 * is set to that argument. Then, the CurrentValue is put in the returnValue
	 * and the CommandObject is returned.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public static CommandObject getter_setter(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("STARTING THE GETTER SETTER");

		/* creating the return value object */
		ArrayList<Object> retVal = new ArrayList<Object>();

		/* getting the properties of the current reader */
		HashMap<String, ReaderProperty> comMap = (HashMap<String, ReaderProperty>) asr
				.getPropertyMap();
		// logger.debug( arg.getDisplayName() +
		// " is the command, before the hashMap/newProperty" );

		/* creating the new reader property object */
		ReaderProperty newProp = comMap.get(arg.getDisplayName().toLowerCase());
		// logger.debug( arg.getDisplayName() + " is the command, after the
		// hashMap/newProperty" );

		/* is property to be set? */
		if (arg.getArguments().size() > 0) {
			/* yes, so look it up and set new value */
			if (comMap.containsKey(arg.getDisplayName().toLowerCase())) {
				newProp.setPropertyValue((String) arg.getArguments().get(0));

				logger.debug("setting property '" + arg.getDisplayName()
						+ "' to new value '"
						+ (String) arg.getArguments().get(0) + "'");

				// retVal.add(newProp.getPropertyStringValue());
			} else {
				logger.error("ERROR: The XML file does not sync up with "
						+ "the handler methods: " + arg.getDisplayName());
			}
		}
		// logger.debug("The current value is: " + arg.getCurrentValue());
		arg.setReturnValue(retVal);
		return formatResponse(arg, asr);
	}

	/**
	 * Utility method that simply returns the Command Object with the current
	 * value of the object mapped to the return value
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject with the returnValue from the current Value.
	 */
	public static CommandObject returnCurrentValue(CommandObject arg,
			AbstractReaderSharedResources asr) {
		HashMap<String, ReaderProperty> comMap = (HashMap<String, ReaderProperty>) asr
				.getPropertyMap();
		ReaderProperty newProp = comMap.get(arg.getDisplayName().toLowerCase());
		ArrayList<Object> retVal = new ArrayList<Object>();
		retVal.add(newProp.getPropertyStringValue());
		arg.setReturnValue(retVal);
		return formatResponse(arg, asr);
	}

	/**
	 * Utility method that simply returns the Command Object with the default
	 * value of the object mapped to the return value
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject with the returnValue from the default Value.
	 */
	public static CommandObject returnDefaultValue(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<Object> retVal = arg.getReturnValue();
		retVal.add(arg.getDefaultValue());
		return arg;
	}

	/**
	 * Utility method that simply returns the Command Object with the default
	 * value of the object mapped to the return value
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject with the returnValue from the default Value.
	 */
	public static CommandObject returnCommandDefaultValue(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<Object> retVal = arg.getReturnValue();
		if (!arg.getPromptSuppress()) {
			retVal.add(SiritCommon.NEWLINE);
		}
		retVal.add(arg.getDefaultValue());
		retVal.add(SiritCommon.ENDOFREPLY);
		if (!arg.getPromptSuppress()) {
			retVal.add(SiritCommon.PROMPT);
		}
		return arg;
	}

	/**
	 * Utility method used to format the response from a command. This method
	 * determines how to format the message based on whether there is an alien
	 * prompt escape character set.<br>
	 * <br>
	 * 
	 * This method is called with a value already set in the CommandObject and
	 * this will return the correctly formatted CommandObject
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject with the formatted returnValue.
	 */
	public static CommandObject formatResponse(CommandObject arg,
			AbstractReaderSharedResources asr) {

		/* Let's create an array that will represent the formatted response */
		ArrayList<Object> retVal = new ArrayList<Object>();
		HashMap<String, ReaderProperty> comMap = (HashMap<String, ReaderProperty>) asr
				.getPropertyMap();
		ReaderProperty newProp = comMap.get(arg.getDisplayName().toLowerCase());

		/*
		 * If the prompt suppress variable is false then we need to add an echo
		 * of the command passed in
		 * 
		 * if (!arg.getPromptSuppress()) { The echo should be the first thing
		 * that is returned String formattedEcho = arg.getCurrentQueryName() +
		 * NEWLINE; retVal.add(formattedEcho); }
		 */

		/* the response only features the property value on 'get' */
		String formattedReturnValue = "ok"
				+ ((arg.getArguments().size() > 0) ? "" : " "
						+ newProp.getPropertyStringValue());
		retVal.add(formattedReturnValue);

		/* To "terminate" the response we have to add two NEWLINEs */
		retVal.add(ENDOFREPLY);

		/* Now let's set this to return the fully formatted value */
		arg.setReturnValue(retVal);
		logger.debug("ENDING THE FORMAT RESPONSE");
		return arg;
	}
}
