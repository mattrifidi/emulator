/*
 *  EPCCommon.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.epc.commandhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * @author matt
 * 
 */
public class EPCCommon {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(EPCCommon.class);

	/**
	 * The prompt, with no newlines around it
	 */
	public static final String PROMPT_STRING = "EPC v1.1>";

	/**
	 * The newline, consisting of a carriage return and a newline
	 */
	public static final String NEWLINE = "\r\n";

	public static final String NEWLINELITERAL = "[EPCNEWLINE]";

	/**
	 * Call this command for traditional get/set commands.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public static CommandObject getter_setter(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("in the getter_setter");
		// TODO: if there is an argument with a "get", should we return an
		arg = EPCCommon.format_echo(arg, asr);

		// This command gets the first 3 letters from the command to see if it
		// is a get or a set
		// String[] splitString = arg.getCurrentQueryName().split(".");
		// String firstLetters = splitString[1].substring(0,3);

		logger.debug("past the first letters");
		if (arg.getCurrentQueryName().contains("set")) {
			if (arg.getArguments().size() == 1) {
				try {
					asr.getPropertyMap()
							.get(arg.getDisplayName().toLowerCase())
							.setPropertyValue(
									(String) arg.getArguments().get(0));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			} else {
				logger.debug("in the else in getter_setter");
			}
		}
		logger.debug("past the set 'if'");
		arg.getReturnValue().add(
				arg.getDisplayName()
						+ " = "
						+ asr.getPropertyMap().get(
								arg.getDisplayName().toLowerCase())
								.getPropertyStringValue());
		arg.getReturnValue().add(EPCCommon.NEWLINE + EPCCommon.NEWLINE);

		return EPCCommon.prompt(arg, asr);
	}

	/**
	 * Call this command to return the default value of commands.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public static CommandObject return_default_value(CommandObject arg,
			AbstractReaderSharedResources asr) {
		// TODO: if there is an argument with a "get", should we return an
		// error?
		arg = EPCCommon.format_echo(arg, asr);

		arg.getReturnValue().add(EPCCommon.NEWLINE + EPCCommon.NEWLINE);

		return EPCCommon.prompt(arg, asr);
	}

	/**
	 * Call this command to return the
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public static CommandObject format_echo(CommandObject arg,
			AbstractReaderSharedResources asr) {
		arg.getReturnValue().clear();
		arg.getReturnValue().add(arg.getCurrentQueryName() + EPCCommon.NEWLINE);
		return arg;
	}

	/**
	 * Call this command to return the
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public static CommandObject prompt(CommandObject arg,
			AbstractReaderSharedResources asr) {
		arg.getReturnValue().add(EPCCommon.PROMPT_STRING);
		return arg;
	}
}
