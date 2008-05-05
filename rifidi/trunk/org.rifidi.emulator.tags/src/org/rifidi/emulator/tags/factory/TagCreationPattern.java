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
package org.rifidi.emulator.tags.factory;

import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.id.TagType;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TagCreationPattern {

	private TagGen tagGeneration;

	private TagType tagType;

	private String prefix;

	private byte[] accessPass;

	private byte[] lockPass;

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
		return accessPass;
	}

	public void setAccessPass(byte[] accessPass) {
		if(accessPass!=null){
			this.accessPass = accessPass;
		}else{
			byte[] pass ={0x00, 0x00, 0x00, 0x00};
			this.accessPass = pass;
		}
	}

	public byte[] getLockPass() {
		return lockPass;
	}

	public void setLockPass(byte[] lockPass) {
		if(lockPass!=null){
			this.lockPass = lockPass;
		}else{
			byte[] pass ={0x00, 0x00, 0x00, 0x00};
			this.lockPass = pass;
		}
	}

	public int getNumberOfTags() {
		return numberOfTags;
	}

	public void setNumberOfTags(int numberOfTags) {
		this.numberOfTags = numberOfTags;
	}
	
	

}
