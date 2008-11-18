/*
 *  GPIPort.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.gpio;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.designer.entities.gpio.GPOPort.State;

/**
 * Represents a GP Input port.
 * 
 * @author Jochen Mader - jochen@pramari.com - Nov 4, 2008
 * 
 */
@XmlRootElement
public class GPIPort {
	/** Support for monitoring properties. */
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);
	/** Cable connected to this port */
	private Cable cable;
	/** State this port is in. */
	private State state = State.LOW;
	/** The default state if nothing is connected. */
	private State defaultState = State.LOW;
	/** Nr of this port */
	private int nr = 0;
	/** Global unique ID.*/
	private String id = "";
	
	/**
	 * @return the defaultState
	 */
	public State getDefaultState() {
		return this.defaultState;
	}

	/**
	 * @param defaultState
	 *            the defaultState to set
	 */
	public void setDefaultState(State defaultState) {
		this.defaultState = defaultState;
		if (cable == null) {
			state = defaultState;
		}
	}

	/**
	 * @return the cable
	 */
	@XmlIDREF
	public Cable getCable() {
		return this.cable;
	}

	/**
	 * @param cable
	 *            the cable to set
	 */
	public void setCable(Cable cable) {
		changeSupport.firePropertyChange("cable", this.cable,
				this.cable = cable);
		if (cable == null) {
			setState(defaultState);
		}
	}

	/**
	 * Get the state
	 * 
	 * @return the state
	 */
	public State getState() {
		return this.state;
	}

	/**
	 * Method triggered by a connected GPO to change the state of the input.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(State state) {
		changeSupport.firePropertyChange("state", this.state,
				this.state = state);
	}

	/**
	 * @return the nr
	 */
	public int getNr() {
		return this.nr;
	}

	/**
	 * @param nr
	 *            the nr to set
	 */
	public void setNr(int id) {
		this.nr = id;
	}

	/**
	 * @return the id
	 */
	@XmlID
	public String getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Add a listener for property changes.
	 * 
	 * @param name
	 * @param l
	 */
	public void addPropertyChangeListener(String name, PropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(name, l);
	}

	/**
	 * Remove a listener for property changes.
	 *
	 * æparam name
	 * @param l
	 */
	public void removePropertyChangeListener(String name, PropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(name, l);
	}

}
