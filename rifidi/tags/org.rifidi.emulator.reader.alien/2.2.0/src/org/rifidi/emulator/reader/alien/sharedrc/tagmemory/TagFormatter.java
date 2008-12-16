//TODO: extract formatter and add lgpl-header
package org.rifidi.emulator.reader.alien.sharedrc.tagmemory;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.impl.RifidiTag;


/**
 * This class is used to format a Tag class in to a specified custom format.
 * INPUT - A Tag object, Additional Tag Information Object and the Regular
 * Expression Token. OUPUT - Returns a String called formated_tag. Which is the
 * regExtoken with the specified tokens exchanged for actual values. There are
 * two helper methods included - removeWhitespace, addWhitespace
 * 
 * @author Dennis Ganesh -- dennis@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TagFormatter {

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(TagFormatter.class);

	/**
	 * This method does the search and replace of the patterns. These patters
	 * are the tokens themselves. Specified Tokens supported are: %i, %k, %d,
	 * %D, %T, %r, %a, %A, %p, %P
	 * 
	 * @param aTag
	 *            The Tag Object
	 * @param regExToken
	 *            The regExToken is the specified custom format.
	 * 
	 * @return formated_tag which is a String in the format specified.
	 */
	public String formatTag(RifidiTag aTag, String regExToken,
			SimpleDateFormat dateformat, SimpleDateFormat timeformat) {

		regExToken = regExToken
				.replaceAll("%i", this.addWhitespace(this.removeWhitespace(aTag.toString())));
		
		regExToken = regExToken.replaceAll("%k", this.removeWhitespace(aTag.toString()));
		
		String discDate = dateformat.format(aTag.getDiscoveryDate());
		regExToken = regExToken.replaceAll("%d", discDate);
		
		String discTime = timeformat.format(aTag.getDiscoveryDate());
		regExToken = regExToken.replaceAll("%t", discTime);
		
		String seenDate = dateformat.format(aTag.getLastSeenDate());
		regExToken = regExToken.replaceAll("%D", seenDate);
		
		String seenTime = timeformat.format(aTag.getLastSeenDate());
		regExToken = regExToken.replaceAll("%T", seenTime);
		
		regExToken = regExToken.replaceAll("%r", Integer.toString(aTag
				.getReadCount()));
		
		regExToken = regExToken.replaceAll("%a", Integer.toString(aTag
				.getAntennaLastSeen()));
		
		regExToken = regExToken.replaceAll("%A", Integer.toString(aTag
				.getAntennaLastSeen()));
		
		String tagProtocol;
		if(aTag.getTagGen().equals(TagGen.GEN1)) tagProtocol="1";
		else if(aTag.getTagGen().equals(TagGen.GEN2)) tagProtocol="2";
		else tagProtocol="0";
		
		regExToken = regExToken.replaceAll("%p", tagProtocol);
		
		regExToken = regExToken.replaceAll("%P", aTag.getTagGen().toString());

		String formatted_tag = regExToken;
		
		return formatted_tag;
	}

	/**
	 * This method simply removes all white space in the specified string.
	 * 
	 * @param inputStr
	 *            The String to be stripped of white spaces.
	 * 
	 * @return outputStr which is a String with the white spaces removed.
	 */
	public String removeWhitespace(String inputStr) {
		String patternStr = "\\s+";
		String replaceStr = "";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(inputStr);
		// logger.debug("replaceStr = " + matcher.replaceAll(replaceStr));
		String outputStr = matcher.replaceAll(replaceStr);
		return outputStr;
	}

	/**
	 * This method simply add white spaces after every 4th character.
	 * 
	 * @param inputStr
	 *            The String that is to be inserted with whitespace.
	 * 
	 * @return spacedStr which is a String with the white spaces inserted.
	 */
	public String addWhitespace(String inputStr) {
		StringBuffer temp_sb = new StringBuffer();
		String temp_st = inputStr;

		for (int x = 0; x < temp_st.length(); x++) {
			temp_sb.append(temp_st.subSequence(0, 4) + " ");
			temp_st = temp_st.substring(4, temp_st.length());
		}
		temp_sb.append(temp_st);
		String spacedStr = temp_sb.toString().trim();
		// logger.debug("temp_sb.toString() = " + temp_sb.toString());
		return spacedStr;
	}

}
