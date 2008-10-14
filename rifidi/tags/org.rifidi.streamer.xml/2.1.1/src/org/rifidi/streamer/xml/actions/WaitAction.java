/*
 *  WaitAction.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml.actions;

import java.util.Random;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the WaitAction implementation
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class WaitAction extends Action {

	// private Log logger = LogFactory.getLog(GPIAction.class);
	private long minWaitTime;
	private long maxWaitTime;
	private boolean isRandom;

	/**
	 * @return the minWaitTime
	 */
	public long getMinWaitTime() {
		return minWaitTime;
	}

	/**
	 * @param minWaitTime
	 *            the minWaitTime to set
	 */
	public void setMinWaitTime(long minWaitTime) {
		this.minWaitTime = minWaitTime;
	}

	/**
	 * @return the maxWaitTime
	 */
	public long getMaxWaitTime() {
		return maxWaitTime;
	}

	/**
	 * @param maxWaitTime
	 *            the maxWaitTime to set
	 */
	public void setMaxWaitTime(long maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	/**
	 * @return the isRandom
	 */
	public boolean isRandom() {
		return isRandom;
	}

	/**
	 * @param isRandom
	 *            the isRandom to set
	 */
	public void setRandom(boolean isRandom) {
		this.isRandom = isRandom;
	}

	/**
	 * Calculates a random wait time in between the min and max range if time is
	 * random, otherwise returns the minTime
	 * 
	 * @return the time to wait in ms
	 */
	public long getWaitTime() {
		if (isRandom) {
			Random r = new Random();
			Double d = r.nextDouble();
			long temp = (maxWaitTime + 1) - minWaitTime;
			d = d * temp;
			d = d + minWaitTime;
			return d.longValue();

		} else {
			return minWaitTime;
		}
	}
}
