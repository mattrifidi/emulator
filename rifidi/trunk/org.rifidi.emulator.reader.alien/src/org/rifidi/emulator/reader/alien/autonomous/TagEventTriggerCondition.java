/*
 *  TagEventTriggerCondition.java
 *
 *  Created:	Jan 11, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *  Author:    Kyle Neumeier - kyle@pramari.com
 */
package org.rifidi.emulator.reader.alien.autonomous;

/**
 * This enum lists the possible trigger conditions having to do with the whether
 * or not tags were added or removed from the tag list for sending a tag list to
 * the client
 * 
 * add - tags were added to the tag list
 * 
 * remove - tags were removed from tag list
 * 
 * change - tags were either added or removed
 * 
 * NoChange - tag list was not changed
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public enum TagEventTriggerCondition {
	Add, Remove, Change, NoChange
}
