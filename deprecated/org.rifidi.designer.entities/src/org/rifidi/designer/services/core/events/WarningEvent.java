/*
 *  WarningEvent.java
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
 * 
 * A event dispatched for warnings.
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 26, 2008
 * 
 */
public class WarningEvent extends WorldEvent {
	/**
	 * The warning message.
	 */
	private String message;

	/**
	 * Constructor.
	 * 
	 * @param message
	 */
	public WarningEvent(String message) {
		super();
		this.message = message;
		this.color = Colors.RED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.events.WorldEvent#toString()
	 */
	@Override
	public String toString() {
		return message;
	}

}
