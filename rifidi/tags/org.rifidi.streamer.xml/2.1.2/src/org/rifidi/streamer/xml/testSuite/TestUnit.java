/*
 *  TestUnit.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml.testSuite;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.streamer.xml.actions.Action;

/**
 * The TestUnit is a list of Actions, describing how the batches enter the
 * different Scenarios.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class TestUnit {

	private int iterations;
	private int ID;
	private long preWait;
	private long postWait;

	/**
	 * List of Actions in this TestUnit
	 */
	private List<Action> actions = new ArrayList<Action>();

	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}

	/**
	 * @param iterations
	 *            the iterations to set
	 */
	@XmlAttribute
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	/**
	 * @return the actions
	 */
	public List<Action> getActions() {
		return actions;
	}

	/**
	 * @param actions
	 *            the actions to set
	 */
	@XmlElementRef
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

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
	 * @return preWait
	 */
	public long getPreWait() {
		return preWait;
	}

	/**
	 * @param preWait
	 */
	public void setPreWait(long preWait) {
		this.preWait = preWait;
	}

	/**
	 * @return postWait 
	 */
	public long getPostWait() {
		return postWait;
	}

	/**
	 * @param postWait
	 */
	public void setPostWait(long postWait) {
		this.postWait = postWait;
	}

}
