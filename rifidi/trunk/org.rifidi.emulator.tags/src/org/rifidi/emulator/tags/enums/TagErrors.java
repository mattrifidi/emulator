/*
 *  TagErrors.java
 *
 *  Created:	Sep 25, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.tags.enums;

/**
 * @author Jochen Mader
 *
 */
public enum TagErrors {
	TagIsReadOnly,
	OnlyOneWriteAllowed,
	InvalidWritingPosition,
	InvalidWriteLength,
	InvalidReadingPosition,
	InvalidReadLength,
	TargetMemoryIsLocked,
	TargetMemoryIsNotLocked,
	TargetMemoryIsPermalocked,
	TargetMemoryIsPermaunlocked,
	InvalidMemoryBank
}
