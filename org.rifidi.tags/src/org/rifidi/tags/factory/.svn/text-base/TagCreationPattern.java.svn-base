/*
 *  TagCreationPattern.java
 *
 *  Project: Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright: Pramari LLC and the Rifidi Project
 *  License: Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.tags.factory;

import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.id.TagType;

/**
 * This class contains parameters required to create a list of tags. The tag
 * creation pattern will be given to a tag factory that creates tags based on
 * this pattern
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TagCreationPattern {

	/**
	 * The Generation (i.e. Gen1 or Gen2)
	 */
	private TagGen tagGeneration;

	/**
	 * The type of tag (e.g. DOD)
	 */
	private TagType tagType;

	/**
	 * For custom EPC tags, the prefix to use
	 */
	private String prefix;

	/**
	 * The access password for Gen2 tags. default is 0000
	 */
	private byte[] accessPass;

	/**
	 * The lock pass for Gen2 tags. default is 0000
	 */
	private byte[] lockPass;

	/**
	 * The number of tags to create.
	 */
	private int numberOfTags;

	/**
	 * @return the tagGeneration
	 */
	public TagGen getTagGeneration() {
		return tagGeneration;
	}

	/**
	 * @param tagGeneration
	 *            the tagGeneration to set
	 */
	public void setTagGeneration(TagGen tagGeneration) {
		this.tagGeneration = tagGeneration;
	}

	/**
	 * @return the tagType
	 */
	public TagType getTagType() {
		return tagType;
	}

	/**
	 * @param tagType
	 *            the tagType to set
	 */
	public void setTagType(TagType tagType) {
		this.tagType = tagType;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the numberOfTags
	 */
	public int getNumberOfTags() {
		return numberOfTags;
	}

	/**
	 * @param numberOfTags
	 *            the numberOfTags to set
	 */
	public void setNumberOfTags(int numberOfTags) {
		this.numberOfTags = numberOfTags;
	}

	/**
	 * @return the AccessPass
	 */
	public byte[] getAccessPass() {
		if (accessPass != null) {
			return accessPass;
		} else {
			byte[] pass = { 0x00, 0x00, 0x00, 0x00 };
			this.accessPass = pass;
			return accessPass;
		}
	}

	/**
	 * @param accessPass
	 *            the access pass to set
	 */
	public void setAccessPass(byte[] accessPass) {
		if (accessPass != null) {
			this.accessPass = accessPass;
		}
	}

	/**
	 * @return The Lock Pass
	 */
	public byte[] getLockPass() {
		if (this.lockPass != null) {
			return lockPass;
		} else {
			byte[] pass = { 0x00, 0x00, 0x00, 0x00 };
			this.lockPass = pass;
			return lockPass;
		}
	}

	/**
	 * 
	 * @param lockPass
	 *            The lockPass to set
	 */
	public void setLockPass(byte[] lockPass) {
		if (lockPass != null) {
			this.lockPass = lockPass;

		}

	}

}
