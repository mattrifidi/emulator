/*
 *  SiritSetup.java
 *
 *  Created:	10.07.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.commandhandler;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sirit.command.exception.SiritExceptionHandler;
import org.rifidi.emulator.reader.sirit.module.SiritReaderSharedResources;

/**
 * This is the class for all commands of sirit's "setup" namespace
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritSetup {

	/** logger instance for this class. */
	private static Log logger = LogFactory.getLog(SiritSetup.class);

	/**
	 * Gets and sets the reader's operating mode variable. When set to "active"
	 * the active mode is to be enabled.
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
	public CommandObject setup_operatingMode(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("SiritSetup - setup_operatingMode() "
				+ arg.getArguments().toString());

		/* validate argument value. valid values: active, standby */
		if (arg.getArguments().size() > 0) {
			String mode = arg.getArguments().get(0).toString();	

			if (mode.equalsIgnoreCase("active")) {
				/* start active mode */
				((SiritReaderSharedResources) asr).startActiveMode();
			} else if (mode.equalsIgnoreCase("standby")) {
				/* stop active mode */
				((SiritReaderSharedResources) asr).stopActiveMode();
			}
			else {
				ArrayList<Object> retVal = new SiritExceptionHandler().illegalValueError(arg);
				arg.setReturnValue(retVal);
				return arg;
			}
		}
		
		return SiritCommon.getter_setter(arg, asr);
	}

	/**
	 * Gets and sets the reader's default login level (user role).
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
	public CommandObject setup_defaultLoginLevel(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("SiritSetup - setup_defaultLoginLevel() "
				+ arg.getArguments().toString());

		// todo: only allow "admin"
		return SiritCommon.getter_setter(arg, asr);
	}
}
