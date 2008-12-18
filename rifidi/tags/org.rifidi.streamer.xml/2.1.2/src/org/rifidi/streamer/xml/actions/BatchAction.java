/*
 *  BatchAction.java
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

/**
 * This is the implementation of a BatchAction.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class BatchAction extends Action {

	private int batchID;
	private int scenarioID;

	/**
	 * @return the batchID
	 */
	public int getBatchID() {
		return batchID;
	}

	/**
	 * @param batchID
	 *            the batchID to set
	 */
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	/**
	 * @return the scenarioID
	 */
	public int getScenarioID() {
		return scenarioID;
	}

	/**
	 * @param scenarioID
	 *            the scenarioID to set
	 */
	public void setScenarioID(int scenarioID) {
		this.scenarioID = scenarioID;
	}

}
