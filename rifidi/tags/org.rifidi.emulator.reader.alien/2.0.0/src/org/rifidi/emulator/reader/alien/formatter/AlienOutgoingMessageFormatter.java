/*
 *  @(#)AlienOutgoingMessageFormatter.java
 *
 *  Created:	Jun 7, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.formatter;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.emulator.reader.alien.sharedrc.tagmemory.TagFormatter;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.tagmemory.formatter.OutgoingMessageFormatter;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * The formatter for outgoing messages for an alien reader. Primarily used for
 * autonomous mode messages
 * 
 * @author Kyle Neumeier
 * 
 */
public class AlienOutgoingMessageFormatter implements OutgoingMessageFormatter {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(AlienOutgoingMessageFormatter.class);

	public final static String NOTIFY_TIMER = "NOTIFY";
	
	public final static String AUTONOMOUS_EVAL = "AUTONOMOUS";

	/**
	 * The format of the outgoing message. Either XML, terse or text
	 */
	private String format = null;

	/**
	 * The shared resources
	 */
	private AlienReaderSharedResources asr;

	/**
	 * Whether or not to include a header
	 */
	private boolean hasHeader = false;

	/**
	 * The reason for sending the message.  Either NOTIFY_TIMER or AUTONOMOUS_EVAL
	 */
	private String reasonForSendingMessage = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.formatter.OutgoingMessageFormatter#formatMessage(java.util.Collection,
	 *      org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources)
	 * 
	 * Controller name is the name of the controller that is calling this
	 * method. It is used for figureing out the reason that the tags are
	 * returned (see the getHeader method)
	 */
	public String formatMessage(Collection<RifidiTag> tags,
			AbstractReaderSharedResources asr, String reasonForSendingMessage) {

		logger.debug("Number of tags: " + tags.size());

		this.asr = (AlienReaderSharedResources) asr;
		this.reasonForSendingMessage = reasonForSendingMessage;

		/* Get the format */
		format = asr.getPropertyMap().get("notifyformat")
				.getPropertyStringValue();

		/* set hasHeader to true if notifyHeader is on */
		hasHeader = asr.getPropertyMap().get("notifyheader")
				.getPropertyDefaultStringValue().equalsIgnoreCase("on");

		/*
		 * the formatted message is a possible header followed by a body
		 * followed by an optional footer
		 */
		return getHeader() + formatBody(getPattern(), tags) + getFooter();

	}

	/**
	 * This method builds the correct pattern to use and returns it
	 * 
	 * @return Regular expression pattern to use for formatting the message
	 */
	private String getPattern() {
		String pattern = "";
		if (format.equalsIgnoreCase("xml")) {
			pattern = "<Alien-RFID-Tag>\r\n  <TagID>%i</TagID>\r\n"
					+ "  <DiscoveryTime>%d %t</DiscoveryTime>\r\n"
					+ "  <LastSeenTime>%D %T</LastSeenTime>\r\n"
					+ "  <Antenna>%a</Antenna>\r\n"
					+ "  <ReadCount>%r</ReadCount>\r\n"
					+ "    <Protocol>%p</Protocol>\r\n"
					+ "</Alien-RFID-Tag>\r\n";
		} else if (format.equalsIgnoreCase("terse")) {
			// The docs lied about the format pattern
			pattern = "%i,%a,%r\r\n";
		} else {
			pattern = "Tag:%i, Disc:%d %t, Last:%D %T, Count:%r, Ant:%a, Proto:%p\r\n";
		}
		return pattern;
	}

	/**
	 * This method iterates through the collection of tags and formats them.
	 * 
	 * @param pattern
	 *            The regular expression pattern
	 * @param tags
	 *            The Collection of tags
	 * @return The message body as a String object
	 */
	private String formatBody(String pattern, Collection<RifidiTag> tags) {
		StringBuilder sb = new StringBuilder();
		TagFormatter formatter = new TagFormatter();
		SimpleDateFormat alienDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat alienTimeFormat = new SimpleDateFormat("HH:mm:ss");
		Boolean emptylist = false;

		/* if automode is off, an empty tag list should be returned */
		boolean isAutoModeOn = asr.getPropertyMap().get("readername")
		.getPropertyStringValue().equalsIgnoreCase("on");
		if (isAutoModeOn || tags.size() == 0) {
			emptylist = true;
		}

		if (format.equalsIgnoreCase("xml")) {
			sb.append("<Alien-RFID-Tag-List>\r\n");
		}

		/*
		 * Automode must be on for Notify to return any tags. Otherwise it
		 * returns an emptylist
		 */
		if (!emptylist) {
			/* Step through the tags in taglist and format each on */
			for (RifidiTag t : tags) {
				logger.debug("appending tags");
				sb.append(formatter.formatTag(t, pattern, alienDateFormat, alienTimeFormat));
			}
		}
		/*
		 * If the format is string or terse and no tags should be returned,
		 * return (no tags)
		 */
		if (!(format.equals("xml")) && emptylist) {
			sb.append("(No Tags)\r\n");
		}

		if (format.equalsIgnoreCase("xml")) {
			sb.append("</Alien-RFID-Tag-List>\r\n");
		}
		return sb.toString();
	}

	/**
	 * This method builds and returns the correct header. It may return an empty
	 * string if the header is not used
	 * 
	 * @return The message header as a string object
	 */
	private String getHeader() {
		String readerName = null;
		String readerType = null;
		String readerIP = null;
		String readerPort = null;
		String MACAddress = "00:00:00:00:00:00";
		String reason = null;
		String time = null;
		String retVal = "";
		
		AlienReaderSharedResources aliensr = (AlienReaderSharedResources)asr;

		if (hasHeader) {
			readerName = asr.getPropertyMap().get("readername")
					.getPropertyStringValue();
			readerType = asr.getPropertyMap().get("readertype")
					.getPropertyDefaultStringValue();
			readerIP = aliensr.getCommandIP();
			readerPort = String.valueOf(aliensr.getCommandPort());

			reason = getReason();
			SimpleDateFormat alienDateFormat = new SimpleDateFormat(
					"yyyy/MM/dd");
			SimpleDateFormat alienTimeFormat = new SimpleDateFormat("HH:mm:ss");
			Date curDate = new Date();
			String dateString = alienDateFormat.format(curDate);
			String timeString = alienTimeFormat.format(curDate);
			time = dateString + " " + timeString;

			if (format.equalsIgnoreCase("xml")) {
				StringBuffer sb = new StringBuffer();
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
				sb.append("<Alien-RFID-Reader-Auto-Notification>\r\n"
						+ "  <ReaderName>" + readerName + "</ReaderName>\r\n");
				sb.append("  <ReaderType>" + readerType
						+ "</ReaderType>\r\n  <IPAddress>" + readerIP
						+ "</IPAddress>\r\n");
				sb
						.append("  <CommandPort>" + readerPort
								+ "</CommandPort>\r\n");
				sb.append("  <MACAddress>" + MACAddress
						+ "</MACAddress>\r\n  <Time>" + time + "</Time>\r\n  ");
				sb.append("<Reason>" + reason + "</Reason>\r\n  ");
				sb.append("<StartTriggerLines>0</StartTriggerLines>\r\n  ");
				sb.append("<StopTriggerLines>0</StopTriggerLines>\r\n");
				retVal = sb.toString();
			}
			/* if terse or string */
			else {
				StringBuffer sb = new StringBuffer();
				sb.append("#Alien RFID Reader Auto Notification Message");
				sb.append("#ReaderName: " + readerName + "\r\n");
				sb.append("#ReaderType: " + readerType + "\r\n");
				sb.append("#IPAddress: " + readerIP + "\r\n");
				sb.append("#CommandPort: " + readerPort + "\r\n");
				sb.append("#Time: " + time + "\r\n");
				sb.append("#Reason: " + reason + "\r\n");
				sb.append("#StartTriggerLines: 0\r\n");
				sb.append("#StopTriggerLines: 0\r\n");
				retVal = sb.toString();
			}
		}
		return retVal;
	}

	/**
	 * This method figures out the reason that the automode was triggered
	 * 
	 * @return Alien's description of why the automode was triggered.
	 */
	private String getReason() {
		String retVal = "";
		if (this.reasonForSendingMessage.equals(AlienOutgoingMessageFormatter.NOTIFY_TIMER)) {
			retVal = "TIMED MESSAGE";
		} else if (this.reasonForSendingMessage
				.equals(AlienOutgoingMessageFormatter.AUTONOMOUS_EVAL)) {
			String notifyTrigger = asr.getPropertyMap().get("notifytrigger")
					.getPropertyStringValue();
			if (notifyTrigger.equalsIgnoreCase("true")) {
				retVal = "AUTO MODE EVALUATES TRUE";
			} else if (notifyTrigger.equalsIgnoreCase("false")) {
				retVal = "AUTO MODE EVALUATES FALSE";
			} else if (notifyTrigger.equalsIgnoreCase("truefalse")) {
				retVal = "AUTO MODE EVALUATES TRUE OR FALSE";
			} else if (notifyTrigger.equalsIgnoreCase("add")) {
				retVal = "TAGS ADDED";
			} else if (notifyTrigger.equalsIgnoreCase("remove")) {
				retVal = "TAGS REMOVED";
			} else if (notifyTrigger.equalsIgnoreCase("change")) {
				retVal = "TAGS CHANGED";
			}
			/* Should not get to this point if the notify trigger is off */
			else if (notifyTrigger.equalsIgnoreCase("off")) {
				retVal = "NOTIFY TRIGGER IS OFF.  ERROR.";
			}
			/* Notify trigger contains an invalid value */
			else {
				retVal = "INVALID NOTIFY TRIGGER";
			}
		}
		return retVal;
	}

	/**
	 * This method builds and returns the correct footer. It may return an empty
	 * string if the footer is not used
	 * 
	 * @return The message footer as a string object
	 */
	private String getFooter() {
		String retVal = "";

		/*
		 * User the hasHeader boolean because it will not have a footer unless
		 * it has a header
		 */
		if (hasHeader) {
			if (format.equalsIgnoreCase("xml")) {
				retVal = "</Alien-RFID-Reader-Auto-Notification>\r\n";
			} else {
				retVal = "#Alien RFID Reader Auto Notification Message\r\n";
			}
		}
		return retVal + "\0";
	}

}
