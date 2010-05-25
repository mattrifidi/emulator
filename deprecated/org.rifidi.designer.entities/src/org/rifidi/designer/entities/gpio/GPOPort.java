/*
 *  GPOPort.java
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a GP Output port.
 * 
 * @author Jochen Mader - jochen@pramari.com - Nov 4, 2008
 * 
 */
@XmlRootElement
public class GPOPort {
	/** Enum that defines the states a port can have. */
	public enum State {
		HIGH, LOW
	};

	/** Support for monitoring properties. */
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);
	/** Cable connected to this port. */
	private Cable cable;
	/** State this port is in. */
	private State state = State.LOW;
	/** Nr of this port */
	private int nr = 0;
	/** Global unique ID. */
	private String id = "";

	/**
	 * Default constructor used by JAXB.
	 */
	public GPOPort() {
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return cable != null;
	}

	/**
	 * Get the state of the port.
	 * 
	 * @return the state
	 */
	public State getState() {
		return this.state;
	}

	/**
	 * Set the state of the port.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(State state) {
		if (this.state != state) {
			this.state = state;
		}
		if (cable != null) {
			cable.setState(state);
		}
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
	public void setNr(int nr) {
		this.nr = nr;
	}

	/**
	 * @return the cable
	 */
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
		if (cable != null) {
			cable.setState(state);
		}
	}

	/**
	 * @return the id
	 */
	@XmlID
	public String getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param name
	 * @param l
	 */
	public void removePropertyChangeListener(String name,
			PropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(name, l);
	}

}
