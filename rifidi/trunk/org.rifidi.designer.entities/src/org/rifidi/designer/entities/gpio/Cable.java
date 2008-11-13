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

import org.rifidi.designer.entities.gpio.GPOPort.State;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Nov 12, 2008
 * 
 */
public class Cable {
	/** Source port for the cable. */
	private GPOPort source;
	/** Target port for the cable. */
	private GPIPort target;
	/** GPIO state currently set to the cable. */
	private State state;
	/**
	 * @return the source
	 */
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
		target.setState(state);
	}
	
}
