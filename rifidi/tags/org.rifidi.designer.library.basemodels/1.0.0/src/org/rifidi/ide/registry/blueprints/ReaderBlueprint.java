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
package org.rifidi.ide.registry.blueprints;

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
	private Integer maxantennas;
	private Integer maxgpis;
	private Integer maxgpos;
	@SuppressWarnings("unchecked")
	private String readerclass;
	private String readerclassname;
	private String description;
	private List<PropertyBlueprint> properties = new ArrayList<PropertyBlueprint>();

//	/**
//	 * Maximum GPI's this reader can handle with
//	 * 
//	 * @return maximum number of GPI's
//	 */
//	public Integer getMaxGPIs() {
//		return maxgpis;
//	}
//
//	/**
//	 * Maximum GPI's this reader can handle with
//	 * 
//	 * @param maxGPIs
//	 */
//	public void setMaxGPIs(Integer maxGPIs) {
//		this.maxgpis = maxGPIs;
//	}
//
//	/**
//	 * Maximum GPO's this reader can handle with
//	 * 
//	 * @return maximum number of GPO's
//	 */
//	public Integer getMaxGPOs() {
//		return maxgpos;
//	}
//
//	/**
//	 * Maximum GPI's this reader can handle with
//	 * 
//	 * @param maxGPOs
//	 */
//	public void setMaxGPOs(Integer maxGPOs) {
//		this.maxgpos = maxGPOs;
//	}

	/**
	 * @return the maxAntennas
	 */
	/**
	 * @return
	 */
	public Integer getMaxantennas() {
		return maxantennas;
	}

	/**
	 * @param maxantennas
	 *            the maxAntennas to set
	 */
	public void setMaxantennas(Integer maxantennas) {
		this.maxantennas = maxantennas;
	}

	/**
	 * @return the readerClass
	 */
	public String getReaderclass() {
		return readerclass;
	}

	/**
	 * @param readerclass
	 *            the readerClass to set
	 */
	public void setReaderclass(String readerclass) {
		this.readerclass = readerclass;
	}

	/**
	 * @return the properties
	 */
	public List<PropertyBlueprint> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(ArrayList<PropertyBlueprint> properties) {
		this.properties = properties;
	}

	/**
	 * @param property the property blueprint to add
	 */
	public void addProperty(PropertyBlueprint property) {
		properties.add(property);
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

	public String toString() {
		return "maxantennas: " + maxantennas + "\nreaderclass: " + readerclass
				+ "\ndescription: " + description + "\nnr properties: "
				+ properties.size() + "\n";
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
		setReaderclass(readerclassname);
	}

	/**
	 * @return the maxgpis
	 */
	public Integer getMaxgpis() {
		return maxgpis;
	}

	/**
	 * @param maxgpis the maxgpis to set
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
	 * @param maxgpos the maxgpos to set
	 */
	public void setMaxgpos(Integer maxgpos) {
		this.maxgpos = maxgpos;
	}
}
