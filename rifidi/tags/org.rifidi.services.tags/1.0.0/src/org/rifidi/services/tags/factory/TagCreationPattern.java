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
package org.rifidi.services.tags.factory;

import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.id.TagType;

/**
 * This class contains paramenters required to create a list of tags. The tag
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

	public TagGen getTagGeneration() {
		return tagGeneration;
	}

	public void setTagGeneration(TagGen tagGeneration) {
		this.tagGeneration = tagGeneration;
	}

	public TagType getTagType() {
		return tagType;
	}

	public void setTagType(TagType tagType) {
		this.tagType = tagType;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public byte[] getAccessPass() {
		if (accessPass != null) {
			return accessPass;
		} else {
			byte[] pass = { 0x00, 0x00, 0x00, 0x00 };
			this.accessPass = pass;
			return accessPass;
		}
	}

	public void setAccessPass(byte[] accessPass) {
		if (accessPass != null) {
			this.accessPass = accessPass;
		}
	}

	public byte[] getLockPass() {
		if (this.lockPass != null) {
			return lockPass;
		} else {
			byte[] pass = { 0x00, 0x00, 0x00, 0x00 };
			this.lockPass = pass;
			return lockPass;
		}
	}

	public void setLockPass(byte[] lockPass) {
		if (lockPass != null) {
			this.lockPass = lockPass;

		}

	}

	public int getNumberOfTags() {
		return numberOfTags;
	}

	public void setNumberOfTags(int numberOfTags) {
		this.numberOfTags = numberOfTags;
	}

}
