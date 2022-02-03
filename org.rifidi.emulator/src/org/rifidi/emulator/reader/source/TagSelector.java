/*
 *  TagSelector.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.source;

/**
 * This represents a TagSelector object.  
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class TagSelector {
	
	/**
	 * The name of the selector
	 */
	private String name;
	
	/**
	 * The mask value of the selector.  This is used when filtering tags.  
	 */
	private String mask;
	
	/**
	 * The value of the selector.  This is used when filtering tags.  
	 */
	private String value;
	
	/**
	 * 
	 */
	private String tagField;
	
	/**
	 * 
	 */
	private Boolean inclusiveFlag;
	
	/**
	 * Creates a readerSelector with a given name
	 * 
	 * @param name
	 */
	public TagSelector(String name, String tagField, String value, String mask, String inclusiveFlag) {
		this.name = name;
		this.value = value;
		this.mask = mask;
		this.inclusiveFlag = Boolean.parseBoolean(inclusiveFlag);
		this.tagField = tagField;
	}

	/**
	 * 
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the TagSelector.  
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The mask value of the selector.  This is used when filtering tags.  
	 * @return the mask
	 */
	public String getMask() {
		return mask;
	}

	/**
	 * The mask value of the selector.  This is used when filtering tags.  
	 * @param mask the mask to set
	 */
	public void setMask(String mask) {
		this.mask = mask;
	}

	/**
	 * The value of the selector.  This is used when filtering tags.  
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * The value of the selector.  This is used when filtering tags.  
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the inclusiveFlag
	 */
	public Boolean getInclusiveFlag() {
		return inclusiveFlag;
	}

	/**
	 * @param inclusiveFlag the inclusiveFlag to set
	 */
	public void setInclusiveFlag(Boolean inclusiveFlag) {
		this.inclusiveFlag = inclusiveFlag;
	}

	/**
	 * @return the tagField
	 */
	public String getTagField() {
		return tagField;
	}

	/**
	 * @param tagField the tagField to set
	 */
	public void setTagField(String tagField) {
		this.tagField = tagField;
	}
	
}
