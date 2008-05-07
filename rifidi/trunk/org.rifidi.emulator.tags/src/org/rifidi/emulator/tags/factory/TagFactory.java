/**
 *  TagFactory.java
 *
 *  Project: Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright: Pramari LLC and the Rifidi Project
 *  License: Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 **/
package org.rifidi.emulator.tags.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TagFactory {

	public static List<String> getSupportedTagFormats() {
		List<String> ret = new ArrayList<String>();
		ret.add(GID96.tagFormat);
		ret.add(DoD96.tagFormat);
		ret.add(SGTIN96.tagFormat);
		ret.add(SSCC96.tagFormat);
		ret.add(CustomEPC96.tagFormat);
		return ret;
	}

	// TODO: odd method. Delete that method once we know what it was used for
	// and we have the new TagRegsityService
	public static RifidiTag copyTag(RifidiTag tag) {
		TagCreationPattern tagCreationPattern = new TagCreationPattern();
		tagCreationPattern.setTagGeneration(tag.getTagGen());
		if (tag.getTagGen() == TagGen.GEN2) {
			tagCreationPattern.setAccessPass(((C1G2Tag) tag.getTag())
					.getAccessPass());
			tagCreationPattern.setLockPass(((C1G2Tag) tag.getTag())
					.getLockPass());
		}

		Gen1Tag gen1Tag = createTag(tagCreationPattern, tag.getTag().getId());

		RifidiTag copy = new RifidiTag(gen1Tag);
		copy.setAntennaLastSeen(tag.getAntennaLastSeen());
		copy.setDiscoveryDate((Date) tag.getDiscoveryDate().clone());
		copy.setIdFormat(new String(tag.getIdFormat()));
		copy.setLastSeenDate((Date) tag.getLastSeenDate().clone());
		copy.setQualityRating(tag.getQualityRating());
		copy.setTagEntitiyID(tag.getTagEntitiyID());
		return copy;
	}

	public static List<RifidiTag> generateTags(
			TagCreationPattern tagCreationPattern) {

		HashMap<byte[], RifidiTag> ids = new HashMap<byte[], RifidiTag>();

		while (ids.keySet().size() < tagCreationPattern.getNumberOfTags()) {

			// generate ID
			byte[] tagID = null;

			if (TagType.GID96 == tagCreationPattern.getTagType()) {
				tagID = GID96.getRandomTagData();
			} else if (TagType.DoD96 == tagCreationPattern.getTagType()) {
				tagID = DoD96.getRandomTagData();
			} else if (TagType.SGTIN96 == tagCreationPattern.getTagType()) {
				tagID = SGTIN96.getRandomTagData();
			} else if (TagType.SSCC96 == tagCreationPattern.getTagType()) {
				tagID = SSCC96.getRandomTagData();
			} else if (TagType.CustomEPC96 == tagCreationPattern.getTagType()) {
				if (tagCreationPattern.getPrefix() == null
						&& !tagCreationPattern.getPrefix().isEmpty()) {
					// throw Exception
				}
				tagID = CustomEPC96.getRandomTagData(tagCreationPattern
						.getPrefix());
			} else {
				// throw exception
			}

			Gen1Tag tag = createTag(tagCreationPattern, tagID);

			if (tag != null) {
				ids.put(tagID, new RifidiTag(tag));
			}

		}

		return new ArrayList<RifidiTag>(ids.values());

	}

	private static Gen1Tag createTag(TagCreationPattern tagCreationPattern,
			byte[] tagID) {
		Gen1Tag tag = null;

		if (tagCreationPattern.getTagGeneration() == TagGen.GEN1) {
			tag = new C1G1Tag(tagID);
		} else if (tagCreationPattern.getTagGeneration() == TagGen.GEN2) {
			tag = new C1G2Tag(tagID, tagCreationPattern.getAccessPass(),
					tagCreationPattern.getLockPass());
		} else {
			// throw exception;
		}

		return tag;
	}
}
