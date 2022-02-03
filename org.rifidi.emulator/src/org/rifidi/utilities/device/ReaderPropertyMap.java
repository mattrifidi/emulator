/*
 *  ReaderPropertyMap.java
 *
 *  Created:	Apr 5, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.utilities.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Matthew Dean - matt@pramari.com
 */
public class ReaderPropertyMap {

	private String type;

	private Integer numantennas;

	private String readerclass;

	private String id;
	
	@SuppressWarnings("unchecked")
	private Class actualClass;

	private String xmlrpcip;

	private List<PropertyMap> properties = new ArrayList<PropertyMap>();

	private HashMap<String, Object> extraInfo = new HashMap<String, Object>();

	/**
	 * @return the extraInfo
	 */
	public Map<String, Object> getExtraInfo() {
		return extraInfo;
	}

	/**
	 * @return the readerClass
	 */
	public String getReaderclass() {
		return this.readerclass;
	}

	/**
	 * @param readerClass
	 *            the readerClass to set
	 */
	public void setReaderclass(String readerclass) {
		this.readerclass = readerclass;
		try {
			this.actualClass = Class.forName(readerclass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the properties
	 */
	public List<PropertyMap> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(List<PropertyMap> properties) {
		this.properties = properties;
	}

	public void addProperty(PropertyMap property) {
		properties.add(property);
		this.extraInfo.put(property.getId(), property.getValue());
	}

	public String toString() {
		return "maxantennas: " + numantennas + "\nreaderclass: " + readerclass
				+ "\nxmlrpcip: " + this.xmlrpcip + "\nid: " + id + "\nextrainfo: "
				+ extraInfo + "\n";
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the numantennas
	 */
	public Integer getNumantennas() {
		return numantennas;
	}

	/**
	 * @param numantennas
	 *            the numantennas to set
	 */
	public void setNumantennas(Integer numantennas) {
		this.numantennas = numantennas;
	}

	/**
	 * @return the xmlrpcip
	 */
	public String getXmlrpcip() {
		return xmlrpcip;
	}

	/**
	 * @param xmlrpcip the xmlrpcip to set
	 */
	public void setXmlrpcip(String xmlrpcip) {
		this.xmlrpcip = xmlrpcip;
	}

	/**
	 * @return the actualClass
	 */
	@SuppressWarnings("unchecked")
	public Class getActualClass() {
		return actualClass;
	}
}
