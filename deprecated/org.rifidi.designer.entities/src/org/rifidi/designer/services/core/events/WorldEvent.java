/*
 *  WorldEvent.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.events;

/**
 * Class the represents a event in the world.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 27, 2008
 * 
 */
public abstract class WorldEvent {
	/** Colors of the available console streams. */
	public enum Colors{RED,GREEN,BLACK};

	/** timeStamp for the moment this event occurred */
	private long timeStamp;

	/** Color of the event. */
	protected Colors color=Colors.BLACK;
	/** 
	 * @return the timeStamp
	 */
	public long getTimeStamp() {
		return this.timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();
	
	/**
	 * Get the currently set color.
	 * @return
	 */
	public Colors getColor(){
		return color;
	}
}
