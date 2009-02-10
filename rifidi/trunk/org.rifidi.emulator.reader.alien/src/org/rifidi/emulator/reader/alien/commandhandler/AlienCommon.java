/*
 *  AlienCommon.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.commandhandler;

import java.util.ArrayList;
import java.util.HashMap;

import org.rifidi.emulator.io.comm.ip.udp.UDPCommunicationIncomingMessageHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;

/**
 * This is the class for commonly used methods across all Alien Command
 * Handlers.
 * 
 * @author Prasith Govin
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AlienCommon {

	/**
	 * Message logger
	 */
	private static Log logger =
		 LogFactory.getLog(UDPCommunicationIncomingMessageHandler.class);

	/* String to pass back for the Alien Prompt */
	public static final String PROMPT = "Alien >\0";
	
	/* String to pass back for the Alien Prompt */
	public static final String NONZEROPROMPT = "Alien >";

	/* A constant string which the reader sends out on the end of a reply */
	public static final String ENDOFREPLY = "\r\n\0";

	/* A constant string which the reader sends out on the end of a reply */
	public static final String EQUAL = "=";

	/* A constant string which the reader sends out on the end of a reply */
	public static final String EQUALSPACED = " = ";

	/* A constant string that does a newline */
	public static final String NEWLINE = "\r\n";

	/* A constant string that represents zero escape char */
	public static final String ZEROCHAR = "\0";

	/*
	 * A constant string that is used to look for the New Line literal in the
	 * alien xml file to replace with correct escape char.
	 */
	public static final String NEWLINELITERAL = "[ALIENNEWLINE]";

	/*
	 * A constant string that is used to look for the Zero Char literal in the
	 * alien xml file to replace with correct escape char.
	 */
	public static final String ZEROCHARLITERAL = "[ALIENZEROCHAR]";

	/**
	 * Generic getter/setter method. If there is an argument, The currentValue
	 * is set to that argument. Then, the CurrentValue is put in the returnValue
	 * and the CommandObject is returned.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public static CommandObject getter_setter(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("STARTING THE GETTER SETTER");
		ArrayList<Object> retVal = new ArrayList<Object>();
		HashMap<String, ReaderProperty> comMap = (HashMap<String, ReaderProperty>) asr
				.getPropertyMap();
		// logger.debug( arg.getDisplayName() + " is the command, before the
		// hashMap/newProperty" );
		ReaderProperty newProp = comMap.get(arg.getDisplayName().toLowerCase());
		// logger.debug( arg.getDisplayName() + " is the command, after the
		// hashMap/newProperty" );
		if (arg.getArguments().size() > 0) {
			// logger.debug("Into the if!");
			if (comMap.containsKey(arg.getDisplayName().toLowerCase())) {
				newProp.setPropertyValue((String) arg.getArguments().get(0));

				logger.debug("IN THE GETTER_SETTER property value = "
						+ newProp.getPropertyStringValue());
				retVal.add(newProp.getPropertyStringValue());
			} else {
				logger.error("ERROR: The XML file does not sync up with "
						+ "the handler methods: " + arg.getDisplayName());
			}
		} else if (arg.getCurrentQueryName().length() >= 3
				&& arg.getCurrentQueryName().trim().substring(0, 3).equals("set")) {
			if (!arg.getPromptSuppress()) {
				retVal.add(arg.getCurrentQueryName() + NEWLINE
						+ "Error: Malformed message." + NEWLINE + ENDOFREPLY
						+ NONZEROPROMPT);
			} else {
				retVal.add(arg.getCurrentQueryName() + NEWLINE
						+ "Error: Malformed message." + ENDOFREPLY);
			}
			arg.setReturnValue(retVal);
			return arg;
		} else {

			// retVal.add(arg.getCurrentValue());
		}
		// logger.debug("The current value is: " + arg.getCurrentValue());
		arg.setReturnValue(retVal);
		return AlienCommon.formatResponse(arg, asr);
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
		return formatResponse(arg,asr);
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
	 * @return The CommandObject with the returnValue from the default Value.
	 */
	public static CommandObject returnCommandDefaultValue(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<Object> retVal = arg.getReturnValue();
		if(!arg.getPromptSuppress()) {
			retVal.add(AlienCommon.NEWLINE);
		}
		retVal.add(arg.getDefaultValue());
		retVal.add(AlienCommon.ENDOFREPLY);
		if(!arg.getPromptSuppress()) {
			retVal.add(AlienCommon.NONZEROPROMPT);
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
		 */
		if (!arg.getPromptSuppress()) {
			/* The echo should be the first thing that is returned */
			String formattedEcho = arg.getCurrentQueryName() + NEWLINE;
			retVal.add(formattedEcho);
		}

		/*
		 * Now we need to add the actual value to the response The actual value
		 * is formatted to output the display name and add an equal sign then
		 * the value
		 */
		String formattedReturnValue = arg.getDisplayName() + EQUALSPACED
				+ newProp.getPropertyStringValue() + NEWLINE;
		retVal.add(formattedReturnValue);

		/*
		 * If the prompt suppress variable is false then we need the end of
		 * reply value or else add nothing
		 */
		if (!arg.getPromptSuppress()) {
			retVal.add(ENDOFREPLY);
			retVal.add(NONZEROPROMPT);
		} else {
			retVal.add(AlienCommon.ZEROCHAR);
		}
		/* Now let's set this to return the fully formatted value */
		arg.setReturnValue(retVal);
		logger.debug("ENDING THE FORMAT RESPONSE");
		return arg;
	}

	/**
	 * Method that checks if the argument given to the CommandObject is a valid
	 * integer or not.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject with the formatted returnValue.
	 */
	public static boolean checkIntegerArg(CommandObject arg) {
		boolean retVal = true;
		ArrayList<Object> argList = arg.getArguments();
		try {
			for (Object i : argList) {
				Integer.parseInt((String) i);
			}
		} catch (NumberFormatException e) {
			retVal = false;
		}
		logger.debug("Check integer arg is + " + retVal);
		return retVal;
	}

	/**
	 * Utility method used to format the end of the response from a command.
	 * This method is different from format response in that it doesn't format
	 * the return value by adding the display name. This is primarily used for
	 * login type commands or others that don't echo the command.<br>
	 * <br>
	 * 
	 * This method is called with a value already set in the CommandObject and
	 * this will return the correctly formatted CommandObject
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject with the formatted returnValue.
	 */
	public static CommandObject addLoginEndResponse(CommandObject arg) {

		/* Let's create an array that will represent the formatted response */
		ArrayList<Object> retVal = new ArrayList<Object>();

		/* Add the return value from the command */
		retVal.add(arg.getReturnValue().get(0));

		/*
		 * If the prompt variable is true then we need the end of reply value or
		 * else add nothing
		 */
		if (arg.getPromptSuppress()) {
			retVal.add(NEWLINE);
		} else {
			retVal.add(ENDOFREPLY);
		}

		/* Now let's set this to return the fully formatted value */
		arg.setReturnValue(retVal);
		return arg;
	}

	/**
	 * Utility method to convert a string read in from a file to one that
	 * correctly tranlates to a byte[] with the correct escape characters
	 * 
	 * @param arg
	 *            The CommandObject which contains the ASCII formatted value.
	 * @return The CommandObject with the java formatted value.
	 */
	public static CommandObject formatEscapeCharacters(CommandObject arg) {
		String oldAsciiString = (String) arg.getReturnValue().get(0);
		String newFormattedString = "";

		/* Replace all instances of escape chars with the right ones */
		newFormattedString = oldAsciiString.replace(NEWLINELITERAL, NEWLINE);
		newFormattedString = newFormattedString.replace(ZEROCHARLITERAL,
				ZEROCHAR);

		/* Let's create an array that will represent the formatted response */
		ArrayList<Object> retVal = new ArrayList<Object>();

		retVal.add(newFormattedString);

		/* Now let's set this to return the fully formatted value */
		arg.setReturnValue(retVal);
		return arg;
	}

}
