/*
 *  Cable.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.gpio;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.designer.entities.gpio.GPOPort.State;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Nov 12, 2008
 * 
 */
@XmlRootElement
public class Cable {
	/** Source port for the cable. */
	private GPOPort source;
	/** Target port for the cable. */
	private GPIPort target;
	/** IGPIO state currently set to the cable. */
	private State state;
	/** Unique ID of this cable. */
	private String id="";
	/**
	 * @return the source
	 */
	@XmlIDREF
	public GPOPort getSource() {
		return this.source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(GPOPort source) {
		this.source = source;
	}
	/**
	 * @return the target
	 */
	@XmlIDREF
	public GPIPort getTarget() {
		return this.target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(GPIPort target) {
		this.target = target;
	}
	/**
	 * @return the state
	 */
	public State getState() {
		return this.state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(State state) {
		this.state = state;
		if(target!=null){
			target.setState(state);
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
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
}
