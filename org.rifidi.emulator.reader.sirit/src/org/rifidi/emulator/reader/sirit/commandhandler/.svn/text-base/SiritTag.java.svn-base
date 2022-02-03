/*
 *  SiritTag.java
 *
 *  Created:	14.07.2009
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
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sirit.module.SiritReaderSharedResources;
import org.rifidi.emulator.reader.sirit.tagbuffer.SiritTagDBFormatter;
import org.rifidi.emulator.reader.sirit.tagbuffer.SiritTagMemory;
import org.rifidi.tags.impl.RifidiTag;

/**
 * This is the class for all commands of sirit's "tag" namespace
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritTag {

	/** logger instance for this class. */
	private static Log logger = LogFactory.getLog(SiritTag.class);

	/**
	 * Returns the tagList currently in view of the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @param asr
	 *            An Shared Resources Object which is needed for access to
	 *            Radio, TagMemory and so on
	 * @return The CommandObject which holds the formattet output of the tag
	 *         database
	 */
	public CommandObject tag_db_get(CommandObject arg,
			AbstractReaderSharedResources asr) {

		logger.debug("SiritTag - tag_db_get()");

		/* Shared Resource Object */
		SiritReaderSharedResources ssr;
		/* The return value for this command */
		ArrayList<Object> returnArray = new ArrayList<Object>();

		String returnVal = "ok";

		try {
			ssr = (SiritReaderSharedResources) asr;
			SiritTagMemory tagMem = (SiritTagMemory) ssr.getTagMemory();

			/* create the tagdb formatter */
			SiritTagDBFormatter formatter = new SiritTagDBFormatter();

			/* read tags from tag memory */
			ArrayList<RifidiTag> tags = tagMem.getTagReport();

			/* ...and format */
			/* are there tags to be proceeded? */
			if (tags.size() > 0) {
				returnVal += SiritCommon.NEWLINE;
				returnVal += formatter.formatTagList(tags);
			}

		} catch (Exception exc) {
			logger.error("Error occured during getting tags");
			logger.error(exc.getClass() + ": " + exc.getMessage());
		}

		/* proper termination of return string */
		returnVal += SiritCommon.ENDOFREPLY;

		returnArray.add(returnVal);

		/* Set the return value */
		arg.setReturnValue(returnArray);

		return arg;

	}

}
