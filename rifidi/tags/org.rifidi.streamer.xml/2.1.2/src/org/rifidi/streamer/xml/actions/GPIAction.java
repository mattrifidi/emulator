/*
 *  GPIAction.java
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
 * This is the GPIAction implementation
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class GPIAction extends Action {

	//private Log logger = LogFactory.getLog(GPIAction.class);
	private int port;
	private boolean signal;

	/**
	 * @return the signal
	 */
	public boolean isSignal() {
		return signal;
	}

	/**
	 * @param signal
	 *            the signal to set
	 */
	public void setSignal(boolean signal) {
		this.signal = signal;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

}
