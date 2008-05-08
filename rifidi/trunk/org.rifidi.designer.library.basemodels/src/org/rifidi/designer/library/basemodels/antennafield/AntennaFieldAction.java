/*
 *  AntennaFieldAction.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.antennafield;

import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * Just a little container for add/delete actions on the emulator.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 12, 2008
 * 
 */
public class AntennaFieldAction {
	/**
	 * flag for add/delete action
	 */
	public boolean add;
	/**
	 * The tag we are working with
	 */
	public RifidiTag tag;

	/**
	 * Constructor.
	 * 
	 * @param add
	 *            true if the given tag should be added to the field
	 * @param tag
	 */
	public AntennaFieldAction(boolean add, RifidiTag tag) {
		super();
		this.add = add;
		this.tag = tag;
	}

}
