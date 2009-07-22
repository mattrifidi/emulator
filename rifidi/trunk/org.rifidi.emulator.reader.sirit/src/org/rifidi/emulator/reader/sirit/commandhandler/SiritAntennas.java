/*
 *  SiritAntenna.java
 *
 *  Created:	17.07.2009
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
 * This is the class ta handle commands of sirit's "antenna" namespace
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 *
 */
public class SiritAntennas {
	
	/** logger instance for this class. */
	private static Log logger = LogFactory.getLog(SiritTag.class);
	
	/**
	 * Gets the list of antennas connected to the reader
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
	public CommandObject antennas_detected(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("SiritAntenna - antenna_detected");

		return SiritCommon.getter_setter(arg, asr);
	}

}
