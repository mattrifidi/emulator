/*
 *  TagAction.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml.actions;

import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.id.TagType;

/**
 * This ist the TagAction implementation
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class TagAction extends Action {

	//private Log logger = LogFactory.getLog(GPIAction.class);
	private long execDuration;
	private int number;
	private String prefix;
	private TagType tagType;
	private TagGen tagGen;
	private boolean regenerate;

	/**
	 * @return the regenerate
	 */
	public boolean isRegenerate() {
		return regenerate;
	}

	/**
	 * @param regenerate
	 *            the regenerate to set
	 */
	public void setRegenerate(boolean regenerate) {
		this.regenerate = regenerate;
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
	 * @return the tagGen
	 */
	public TagGen getTagGen() {
		return tagGen;
	}

	/**
	 * @param tagGen
	 *            the tagGen to set
	 */
	public void setTagGen(TagGen tagGen) {
		this.tagGen = tagGen;
	}

	/**
	 * @return the execDuration
	 */
	public long getExecDuration() {
		return execDuration;
	}

	/**
	 * @param execDuration
	 *            the execDuration to set
	 */
	public void setExecDuration(long execDuration) {
		this.execDuration = execDuration;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

}
