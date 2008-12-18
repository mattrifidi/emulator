/*
 *  Scenario.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml.scenario;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Scenario is the ordered list of pathItmes. If a Batch enters a scenario
 * it will go along this path. Starting form the first pathItem and ending at
 * the last one.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class Scenario {

	/**
	 * list of pathItems building this scenario (list is also giving the order
	 * of the path)
	 */
	private List<PathItem> pathItems;

	/**
	 * ID of this Scenario
	 */
	private int ID;

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param id
	 *            the iD to set
	 */
	@XmlAttribute
	public void setID(int id) {
		ID = id;
	}

	/**
	 * @return the pathItems
	 */
	public List<PathItem> getPathItems() {
		return pathItems;
	}

	/**
	 * @param pathItems
	 *            the pathItems to set
	 */
	@XmlElement(name = "pathItem")
	public void setPathItems(List<PathItem> pathItems) {
		this.pathItems = pathItems;
	}

}
