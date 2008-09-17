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
package org.rifidi.services.tags.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.rifidi.services.tags.IGen1Tag;
import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.impl.C1G1Tag;
import org.rifidi.services.tags.impl.C1G2Tag;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TagFactory {

	/**
	 * This method copies a tag into a new object.
	 * @param tag
	 * @return
	 */
	public static RifidiTag copyTag(RifidiTag tag) {
		TagCreationPattern tagCreationPattern = new TagCreationPattern();
		tagCreationPattern.setTagGeneration(tag.getTagGen());
		if (tag.getTagGen() == TagGen.GEN2) {
			tagCreationPattern.setAccessPass(((C1G2Tag) tag.getTag())
					.getAccessPass());
			tagCreationPattern.setLockPass(((C1G2Tag) tag.getTag())
					.getLockPass());
		}

		IGen1Tag gen1Tag = createTag(tagCreationPattern, tag.getTag().readId());

		RifidiTag copy = new RifidiTag(gen1Tag);
		copy.setAntennaLastSeen(tag.getAntennaLastSeen());
		if (tag.getDiscoveryDate() != null)
			copy.setDiscoveryDate((Date) tag.getDiscoveryDate().clone());
		copy.setTagType(tag.getTagType());
		if (tag.getLastSeenDate() != null)
			copy.setLastSeenDate((Date) tag.getLastSeenDate().clone());
		copy.setQualityRating(tag.getQualityRating());
		copy.setTagEntitiyID(tag.getTagEntitiyID());
		return copy;
	}

	/**
	 * This method creates a list of tags as defined by the TagCreationPattern.
	 * The tags that it returns are gauenteed to have unique EPC ids. The tag
	 * entity ID is undefined and should be set by the caller
	 * 
	 * @param tagCreationPattern
	 * @return
	 */
	public static ArrayList<RifidiTag> generateTags(
			TagCreationPattern tagCreationPattern) {

		HashMap<byte[], RifidiTag> ids = new HashMap<byte[], RifidiTag>();

		while (ids.keySet().size() < tagCreationPattern.getNumberOfTags()) {

			// generate ID
			byte[] tagID = null;
			tagID = tagCreationPattern.getTagType().getRandomTagData(
					tagCreationPattern.getPrefix());

			// generate Tag
			IGen1Tag tag = createTag(tagCreationPattern, tagID);

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

	private static IGen1Tag createTag(TagCreationPattern tagCreationPattern,
			byte[] tagID) {
		IGen1Tag tag = null;

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
