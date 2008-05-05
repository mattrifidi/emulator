/*
 *  ReaderBlueprint.java
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

import java.util.ArrayList;
import java.util.List;

/**
 * This is the basic description of a reader it get it's default values out of
 * the emulator.xml
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 */
public class ReaderBlueprint {

	// Maximum antennas of the Reader
	private Integer maxantennas;

	// Maximum GPI/O's of the Reader
	private Integer maxgpis;
	private Integer maxgpos;

	// Reader Class definitions
	private String readerclassname;

	// Reader description
	private String description;

	private List<PropertyBlueprint> properties = new ArrayList<PropertyBlueprint>();

	/**
	 * @return the maxantennas
	 */
	public Integer getMaxantennas() {
		return maxantennas;
	}

	/**
	 * @param maxantennas
	 *            the maxantennas to set
	 */
	public void setMaxantennas(Integer maxantennas) {
		this.maxantennas = maxantennas;
	}

	/**
	 * @return the maxgpis
	 */
	public Integer getMaxgpis() {
		return maxgpis;
	}

	/**
	 * @param maxgpis
	 *            the maxgpis to set
	 */
	public void setMaxgpis(Integer maxgpis) {
		this.maxgpis = maxgpis;
	}

	/**
	 * @return the maxgpos
	 */
	public Integer getMaxgpos() {
		return maxgpos;
	}

	/**
	 * @param maxgpos
	 *            the maxgpos to set
	 */
	public void setMaxgpos(Integer maxgpos) {
		this.maxgpos = maxgpos;
	}

	/**
	 * @return the readerclassname
	 */
	public String getReaderclassname() {
		return readerclassname;
	}

	/**
	 * @param readerclassname
	 *            the readerclassname to set
	 */
	public void setReaderclassname(String readerclassname) {
		this.readerclassname = readerclassname;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the properties
	 */
	public List<PropertyBlueprint> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<PropertyBlueprint> properties) {
		this.properties = properties;
	}
	
	/**
	 * @param property
	 */
	public void addProperty(PropertyBlueprint property) {
		properties.add(property);
	}
	

}
