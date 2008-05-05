/**
 * 
 */
package org.rifidi.emulator.reader.alien.sharedrc.tagmemory;

import java.text.SimpleDateFormat;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.commandhandler.AlienCommon;
import org.rifidi.emulator.reader.sharedrc.properties.StringReaderProperty;
import org.rifidi.emulator.reader.sharedrc.tagmemory.formatter.TagListFormatReaderProperty;
import org.rifidi.emulator.reader.sharedrc.tagmemory.formatter.TagListFormatValues;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * This class formats Tags as needed.
 * 
 * @author kyle
 * 
 */
public class AlientTagListFormatter {

	/** The TagListFormat Property */
	private TagListFormatReaderProperty tagListFormatProperty;

	/** The TagListCustomFormat Property */
	private StringReaderProperty tagListCustomFormatProperty;

	/**
	 * The Formatter which applies the chosen format to an incoming tag
	 */
	private TagFormatter aFormatter = new TagFormatter();

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(AlientTagListFormatter.class);

	public AlientTagListFormatter(TagListFormatReaderProperty format,
			StringReaderProperty customFormatPrperty) {
		tagListFormatProperty = format;
		tagListCustomFormatProperty = customFormatPrperty;
		;
	}

	public String formatTag(Collection<RifidiTag> tags) {
		StringBuffer sb = new StringBuffer();

		SimpleDateFormat alienDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat alienTimeFormat = new SimpleDateFormat("HH:mm:ss");

		if (this.tagListFormatProperty.getValue() == TagListFormatValues.TEXT) {
			logger.debug("Text Output");
			/*
			 * Go trough List, get Tags, apply format and add it to the
			 * finalList
			 */
			String standardTextFormat = "Tag:%i, Disc:%d %t, Last:%D %T, "
					+ "Count:%r, Ant:%a, Proto:%p";

			for (RifidiTag tag : tags) {
				sb.append(this.aFormatter.formatTag(tag, standardTextFormat,
						alienDateFormat, alienTimeFormat));
				sb.append(AlienCommon.NEWLINE);
			}

		} else if (this.tagListFormatProperty.getValue() == TagListFormatValues.XML) {
			logger.debug("XML Output");

			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
			sb.append("<Alien-RFID-Tag-List>\r\n");
			String xmlTextFormat = "  <Alien-RFID-Tag>\r\n    <TagID>%i</TagID>\r\n"
					+ "    <DiscoveryTime>%d %t</DiscoveryTime>\r\n"
					+ "    <LastSeenTime>%D %T</LastSeenTime>\r\n"
					+ "    <Antenna>%a</Antenna>\r\n"
					+ "    <ReadCount>%r</ReadCount>\r\n"
					+ "    <Protocol>%p</Protocol>\r\n" + "  </Alien-RFID-Tag>";

			for (RifidiTag tag : tags) {
				sb.append(this.aFormatter.formatTag(tag, xmlTextFormat,
						alienDateFormat, alienTimeFormat));
				sb.append(AlienCommon.NEWLINE);
			}

			sb.append("</Alien-RFID-Tag-List>");
		} else if (this.tagListFormatProperty.getValue() == TagListFormatValues.TERSE) {
			logger.debug("Terse Output");
			String pattern = "%i,%a,%r\r\n";

			for (RifidiTag tag : tags) {
				sb.append(this.aFormatter.formatTag(tag, pattern,
						alienDateFormat, alienTimeFormat));
				sb.append(AlienCommon.NEWLINE);
			}

		} else if (this.tagListFormatProperty.getValue() == TagListFormatValues.CUSTOM) {
			logger.debug("Custom Output");

			for (RifidiTag tag : tags) {
				sb.append(this.aFormatter.formatTag(tag, tagListCustomFormatProperty.getDefaultValue(),
						alienDateFormat, alienTimeFormat));
				sb.append(AlienCommon.NEWLINE);
			}

		}

		return sb.toString();
	}

}
