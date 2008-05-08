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
import org.rifidi.emulator.tags.impl.C1G1Tag;
import org.rifidi.emulator.tags.impl.C1G2Tag;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TagFactory {

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
		copy.setTagType(tag.getTagType());
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
			tagID = tagCreationPattern.getTagType().getRandomTagData(
					tagCreationPattern.getPrefix());

			// generate Tag
			Gen1Tag tag = createTag(tagCreationPattern, tagID);

			// store tag and make sure that it is unique
			if (tag != null) {
				RifidiTag t = new RifidiTag(tag);
				t.setTagType(tagCreationPattern.getTagType());
				// this will override the tag if the id was already given
				ids.put(tagID, t);
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
