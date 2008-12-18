/*
 *  Batch.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml.batch;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.streamer.xml.actions.Action;

/**
 * This is the implementation of a Batch containing a set of Actions appropriate
 * to the Batch
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class Batch {

	/**
	 * ID of this batch
	 */
	private int ID;

	/**
	 * List of Actions in this batch (this list is giving the order of the
	 * actions)
	 */
	private List<Action> actions = new ArrayList<Action>();

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

}
