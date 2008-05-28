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

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.services.tags.factory.TagCreationPattern;

/**
 * This is the TagAction implementation
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kule Neumeier - kyle@pramari.com
 * 
 */
@XmlRootElement
public class TagAction extends Action {

	//private Log logger = LogFactory.getLog(GPIAction.class);
	private long execDuration;
	private List<TagCreationPattern> tagCreationPattern;
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
	 * @return the tagCreationPattern
	 */
	public List<TagCreationPattern> getTagCreationPattern() {
		return tagCreationPattern;
	}

	/**
	 * @param tagCreationPattern the tagCreationPattern to set
	 */
	public void setTagCreationPattern(List<TagCreationPattern> tagCreationPattern) {
		this.tagCreationPattern = tagCreationPattern;
	}

}
