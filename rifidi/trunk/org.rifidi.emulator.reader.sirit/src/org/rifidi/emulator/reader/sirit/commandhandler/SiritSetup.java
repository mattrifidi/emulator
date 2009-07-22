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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

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
	 * the autonomous mode is to be enabled.
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
		logger.debug("SiritSetup.setup_operatingmode() just got called!");

		/* start autonomous mode */
		// todo
		return SiritCommon.getter_setter(arg, asr);
	}

}
