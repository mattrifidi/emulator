/*
 *  PathItem.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml.scenario;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the PathItem consisting of a reader and the antenna number the tags
 * will appear on. It also stores the time to the next PathItem in a scenario
 * path. If there is no following pathItem it's recommended to set the
 * traveltime to zero
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class PathItem {

	private int readerID;
	private int antennaNum;
	private long travelTime;

	/**
	 * @return the readerID
	 */
	public int getReaderID() {
		return readerID;
	}

	/**
	 * @param readerID
	 *            the readerID to set
	 */
	public void setReaderID(int readerID) {
		this.readerID = readerID;
	}

	/**
	 * @return the travelTime
	 */
	public long getTravelTime() {
		return travelTime;
	}

	/**
	 * @param travelTime
	 *            the travelTime to set
	 */
	public void setTravelTime(long travelTime) {
		this.travelTime = travelTime;
	}

	/**
	 * @return
	 */
	public int getAntennaNum() {
		return antennaNum;
	}

	/**
	 * @param antennaNum
	 */
	public void setAntennaNum(int antennaNum) {
		this.antennaNum = antennaNum;
	}

}
