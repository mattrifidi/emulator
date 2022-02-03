/*
 *  SiritInfo.java
 *
 *  Created:	23.06.2009
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
 * This is the class for all commands of sirit's "info" namespace.
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritInfo {

	/** logger instance for this class. */
	private static Log logger = LogFactory.getLog(SiritInfo.class);

	/**
	 * Gets and sets the reader's name variable in namespace info
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
	public CommandObject info_name(CommandObject arg,
			AbstractReaderSharedResources asr) {
		
		/* sirit debug info */
		logger.debug("SiritInfo - info_name()");

		return SiritCommon.getter_setter(arg, asr);
	}

}
