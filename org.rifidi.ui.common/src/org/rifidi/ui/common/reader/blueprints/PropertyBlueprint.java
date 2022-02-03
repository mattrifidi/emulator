/*
 *  PropertyBlueprint.java
 *
 *  Created:	Apr 5, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.reader.blueprints;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Blueprint for properties defined in the Emulator.xml
 * 
 * @author Jochen Mader - jochen@pramari.com
 */
public class PropertyBlueprint {
	
	private Boolean required;

	private String name;

	private String display;

	private String defaultvalue;

	private String tooltip;
	
	private Log logger = LogFactory.getLog(PropertyBlueprint.class);

	@SuppressWarnings("unchecked")
	private Class validatorclass;

	private String validatorclassname;

	/**
	 * @return the defaultvalue
	 */
	public String getDefaultvalue() {
		return defaultvalue;
	}

	/**
	 * @param defaultvalue
	 *            the defaultvalue to set
	 */
	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @param validator
	 *            the validator to set
	 */
	public void setValidatorclass(String validatorclass) {
		try {
			if (!validatorclass.contains(".")) {
				validatorclass = "org.rifidi.ui.common.validators."
						+ validatorclass;
			}
			this.validatorclass = getClass().getClassLoader().loadClass(
					validatorclass);
		} catch (ClassNotFoundException cnfe) {
			logger.warn("No validator found: " + validatorclass);
		}
	}

	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param tooltip
	 *            the tooltip to set
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String toString() {
		return "required: " + required + "\nname: " + name + "\ndisplay: "
				+ display + "\ndefaultvalue: " + defaultvalue + "\ntooltip: "
				+ tooltip + "\nvalidatorclassname: " + validatorclassname
				+ "\n";
	}

	/**
	 * @return the validatorclassname
	 */
	public String getValidatorclassname() {
		return validatorclassname;
	}

	/**
	 * @param validatorclassname
	 *            the validatorclassname to set
	 */
	public void setValidatorclassname(String validatorclassname) {
		this.validatorclassname = validatorclassname;
		setValidatorclass(validatorclassname);
	}

	/**
	 * @return the validatorclass
	 */
	@SuppressWarnings("unchecked")
	public Class getValidatorclass() {
		return validatorclass;
	}
}
