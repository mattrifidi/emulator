/*
 *  LockStates.java
 *
 *  Created:	Oct 17, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *  Author:   kyle
 */
package org.rifidi.services.tags.enums;

/**
 * 
 * The following are the lock states for memory banks and for the kill and
 * access passwords. The part in brackets below applies to the access and
 * kill passwords
 * 
 * unlocked - Associated memory bank is writeable [and readable] from either
 * the open or secured states.
 * 
 * locked - Associated memory bank is writeable [and readable] from the
 * secured state but not from the open state.
 * 
 * permalocked - Associated memory bank is not writeable [and readable] from
 * any state.
 * 
 * permaunlocked - Associated memory bank is permanently writeable [and
 * readable] from either the open or secured states and may never be locked.
 * 
 * @author kyle
 * 
 */
public enum LockStates {
	unlocked, locked, permalocked, permaunlocked
};
