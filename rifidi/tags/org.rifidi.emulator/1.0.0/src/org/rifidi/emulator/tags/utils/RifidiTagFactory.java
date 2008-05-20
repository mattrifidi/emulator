/**
 * 
 */
package org.rifidi.emulator.tags.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.tags.Gen1Tag;
import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.id.CustomEPC96;
import org.rifidi.emulator.tags.id.DoD96;
import org.rifidi.emulator.tags.id.GID96;
import org.rifidi.emulator.tags.id.SGTIN96;
import org.rifidi.emulator.tags.id.SSCC96;
import org.rifidi.emulator.tags.id.TagType;
import org.rifidi.emulator.tags.impl.C1G1Tag;
import org.rifidi.emulator.tags.impl.C1G2Tag;
import org.rifidi.emulator.tags.impl.RifidiTag;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

/**
 * TagFactory to create RifidiTags
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class RifidiTagFactory {

	private static final Log logger = LogFactory.getLog(RifidiTagFactory.class);

	public static List<String> getSupportedTagFormats() {
		List<String> ret = new ArrayList<String>();
		ret.add(GID96.tagFormat);
		ret.add(DoD96.tagFormat);
		ret.add(SGTIN96.tagFormat);
		ret.add(SSCC96.tagFormat);
		ret.add(CustomEPC96.tagFormat);
		return ret;
	}

	
	/**
	 * @param generation
	 * @param tagFormat
	 * @param byteID
	 * @param prefix
	 * @return
	 */
	public static RifidiTag createTag(TagGen generation, TagType tagFormat, byte[] byteID, String prefix) {
		String id = null;
		byte[] tagID;
		
		if(byteID == null)
		{
			if (TagType.GID96 == tagFormat) {
				id = GID96.getRandomTagData();
			} else if (TagType.DoD96 == tagFormat) {
				id = DoD96.getRandomTagData();
			} else if (TagType.SGTIN96 == tagFormat) {
				id = SGTIN96.getRandomTagData();
			} else if (TagType.SSCC96 == tagFormat) {
				id = SSCC96.getRandomTagData();
			} else if (TagType.CustomEPC96 == tagFormat) {
				id = CustomEPC96.getRandomTagData(prefix);
			}
			tagID = ByteAndHexConvertingUtility.fromHexString(id);
		}else
		{
			tagID = byteID;
		}

		RifidiTag ret = createTag(generation, tagID);
		ret.setIdFormat(tagFormat.toString());
		return ret;
	}
	
	/**
	 * @param tagType
	 * @param tagFormat
	 * @param prefix
	 * @return
	 */
	public static RifidiTag createTag(TagGen tagType, String tagFormat, String prefix) {
		String id = null;

		if (tagFormat.equals(GID96.tagFormat)) {
			id = GID96.getRandomTagData();
		} else if (tagFormat.equals(DoD96.tagFormat)) {
			id = DoD96.getRandomTagData();
		} else if (tagFormat.equals(SGTIN96.tagFormat)) {
			id = SGTIN96.getRandomTagData();
		} else if (tagFormat.equals(SSCC96.tagFormat)) {
			id = SSCC96.getRandomTagData();
		} else if (tagFormat.equals(CustomEPC96.tagFormat)) {
			id = CustomEPC96.getRandomTagData(prefix);
		}
		byte[] tagID = ByteAndHexConvertingUtility.fromHexString(id);
		RifidiTag ret = createTag(tagType, tagID);
		ret.setIdFormat(tagFormat);
		return ret;
	}

	/**
	 * Use this method to create a rifidiTag with an epc tag inside of it
	 * 
	 * @param tagType
	 *            Either Gen1 or Gen2
	 * @param ID
	 *            The ID of the tag
	 * @return
	 */
	public static RifidiTag createTag(TagGen tagType, byte[] ID) {

		byte[] pass = { 0x00, 0x00, 0x00, 0x00 };
		return createTag(tagType, ID, pass, pass.clone());
	}

	/**
	 * Use this method to create a rifidiTag with epc tag inside of it. If a
	 * Gen2 tag is specified, the accessPass and the lockPass must be at 4 bytes
	 * long
	 * 
	 * @param tagType
	 *            Gen1 or Gen2
	 * @param ID
	 *            The ID to use
	 * @param accessPass
	 *            Must be 4 bytes long
	 * @param lockPass
	 *            Must be 4 bytes long
	 * @return
	 */
	public static RifidiTag createTag(TagGen tagType, byte[] ID,
			byte[] accessPass, byte[] lockPass) {
		Gen1Tag tag = null;
		if (tagType.equals(TagGen.GEN1) ) {
			tag = new C1G1Tag(ID);
		} else if (tagType.equals(TagGen.GEN2) ) {
			try {
				tag = new C1G2Tag(ID, accessPass, lockPass);
			} catch (IllegalArgumentException ex) {
				logger.debug(ex.getMessage());
				logger.debug("setting passwords to 0");
				byte[] pass = { 0x00, 0x00, 0x00, 0x00 };
				tag = new C1G2Tag(ID, pass, pass.clone());
			}
		}
		return new RifidiTag(tag);
	}

}
