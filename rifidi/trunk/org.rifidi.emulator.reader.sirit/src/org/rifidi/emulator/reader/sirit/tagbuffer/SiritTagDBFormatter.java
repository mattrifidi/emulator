/*
 *  SiritTagDBFormatter.java
 *
 *  Created:	13.07.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.tagbuffer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sirit.commandhandler.SiritCommon;
import org.rifidi.emulator.reader.sirit.commandhandler.SiritSetup;
import org.rifidi.tags.impl.RifidiTag;

/**
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritTagDBFormatter {

	/** logger instance for this class */
	private static Log logger = LogFactory.getLog(SiritTagDBFormatter.class);

	/** date and time format for output */
	SimpleDateFormat siritDateFormat = new SimpleDateFormat("yyyy-mm-dd");
	SimpleDateFormat siritTimeFormat = new SimpleDateFormat("HH:mm:ss.S");

	/** format for one entry in taglist */
	String tagFormatString = "(tag_id=%i, type=ISOC, first=%dT%t, last=%DT%T, antenna=%a, repeat=%r)";

	/**
	 * Formats the output of the tag database.
	 * 
	 * @param tags
	 *            the list of tags currently in the reader's antennas' fields
	 * @return a string with the correct format of the taglist output, depending
	 *         on the given fields to report
	 */
	public String formatTagList(ArrayList<RifidiTag> tags) {

		/* sirit debug info */
		logger.debug("SiritTagDBFormatter - formatTagList()");

		/* to hold the formatted output */
		StringBuffer sb = new StringBuffer();

		/* proceed tags */
		for (RifidiTag tag : tags) {
			sb.append(this.formatTag(tag));
			sb.append(SiritCommon.NEWLINE);
		}
		
		/* no NEWLINE after the last tag */
		String retVal = sb.toString(); 
		retVal = retVal.substring(0, retVal.lastIndexOf(SiritCommon.NEWLINE));
		
		return retVal;
	}

	/**
	 * Formats a single entry of the taglist. The tag's fields to be reported
	 * are defined in the variable "tag.reporting.taglist_fields"
	 * 
	 * @param tag
	 *            the source tag for the taglist entry
	 * @return a string with the correct format of the tag for the taglist
	 */
	private String formatTag(RifidiTag tag) {
		String retVal = this.tagFormatString;

		/* tag id */
		retVal = retVal.replaceAll("%i", tag.toString());

		/* first seen date and time */
		retVal = retVal.replaceAll("%d", siritDateFormat.format(tag
				.getDiscoveryDate()));
		retVal = retVal.replaceAll("%t", siritTimeFormat.format(tag
				.getDiscoveryDate()));

		/* last seen date and time */
		retVal = retVal.replaceAll("%D", siritDateFormat.format(tag
				.getLastSeenDate()));
		retVal = retVal.replaceAll("%T", siritTimeFormat.format(tag
				.getLastSeenDate()));

		/* antenna id */
		retVal = retVal.replaceAll("%a", Integer.toString(tag
				.getAntennaLastSeen()));

		/* read count */
		retVal = retVal.replaceAll("%r", Integer.toString(tag.getReadCount()));

		/* protocol */
		// todo
		retVal = retVal.replaceAll("%p", "ISO");

		return retVal;
	}
}
