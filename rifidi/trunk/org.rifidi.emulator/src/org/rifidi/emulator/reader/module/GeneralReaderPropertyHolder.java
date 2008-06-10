/*
 *  ReaderProperty.java
 *
 *  Created:	Apr 02, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is a general-purpose holder class for all of the properties one
 * would need in creating a reader.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 */
public class GeneralReaderPropertyHolder implements Cloneable, Serializable {

	@SuppressWarnings("unused")
	private static Log logger = LogFactory
			.getLog(GeneralReaderPropertyHolder.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the reader.
	 */
	private String readerName;

	/**
	 * String representation of the ClassName
	 */
	private String readerClassName;

	/**
	 * The number of antennas this reader has.
	 */
	private int numAntennas;

	/**
	 * The number of GPIs this reader has
	 */
	private int numGPIs;

	/**
	 * The number of GPOs this reader has
	 */
	private int numGPOs;

	/**
	 * This map holds the generic Properties of a reader
	 */
	private Map<String, String> propertiesMap = new HashMap<String, String>();

	/**
	 * @return the readerName
	 */
	public String getReaderName() {
		return readerName;
	}

	/**
	 * @param readerName
	 *            the readerName to set
	 */
	public void setReaderName(String readerName) {
		this.readerName = readerName;
	}

	/**
	 * @return the readerClassName
	 */
	public String getReaderClassName() {
		return readerClassName;
	}

	/**
	 * @param readerClassName
	 *            The class name of the Module, for example
	 *            org.rifidi.emulator.reader.alien.module.AlienReaderModule
	 */
	public void setReaderClassName(String readerClassName) {
		this.readerClassName = readerClassName;
	}

	/**
	 * @return the numAntennas
	 */
	public int getNumAntennas() {
		return numAntennas;
	}

	/**
	 * @param numAntennas
	 *            the numAntennas to set
	 */
	public void setNumAntennas(int numAntennas) {
		this.numAntennas = numAntennas;
	}

	/**
	 * @return the numGPIs
	 */
	public int getNumGPIs() {
		return numGPIs;
	}

	/**
	 * @param numGPIs
	 *            the numGPIs to set
	 */
	public void setNumGPIs(int numGPIs) {
		this.numGPIs = numGPIs;
	}

	/**
	 * @return the numGPOs
	 */
	public int getNumGPOs() {
		return numGPOs;
	}

	/**
	 * @param numGPOs
	 *            the numGPOs to set
	 */
	public void setNumGPOs(int numGPOs) {
		this.numGPOs = numGPOs;
	}

	/**
	 * @return the propertiesMap
	 */
	public Map<String, String> getPropertiesMap() {
		return propertiesMap;
	}

	/**
	 * @param propertiesMap
	 *            the propertiesMap to set
	 */
	public void setPropertiesMap(Map<String, String> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}

	/**
	 * @param property
	 * @param propertyStringValue
	 */
	public void setProperty(String property, String propertyStringValue) {
		this.propertiesMap.put(property, propertyStringValue);
	}

	/**
	 * @param property
	 * @return
	 */
	public String getProperty(String property) {
		return this.propertiesMap.get(property);
	}

	/**
	 * Default Constructor for GeneralReaderPropertyHolder.
	 * 
	 * @param readerName
	 *            The name of the reader.
	 * @param readerClass
	 *            The class of the reader.
	 * @param xmlFilePath
	 *            The path of the XML of the reader.
	 */
	public GeneralReaderPropertyHolder() {
	}

	/**
	 * Returns a String representation of the object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("readerName: " + readerName + "\n");
		sb.append("readerClass: " + readerClassName + "\n");
		sb.append("numAntennas: " + numAntennas + "\n");
		sb.append("numGPI's: " + numGPIs + "\n");
		sb.append("numGPO's: " + numGPIs + "\n");
		for (String name : propertiesMap.keySet()) {
			sb.append(name + ": " + propertiesMap.get(name) + "\n");
		}
		return sb.toString();
	}

}