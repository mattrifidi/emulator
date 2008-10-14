/*
 *  EvaluationTriggerCondition.java
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
 * This enum lists the possible trigger conditions having to do with the
 * evaluation state of the autonomous mode loop for sending a tag list to the
 * client
 * 
 * true - evaluation loop saw at least one tag
 * 
 * false - evaluation loop saw no tags
 * 
 * truefalse - evaluation loop either saw a tag or did not see a tag (always
 * true)
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public enum EvaluationTriggerCondition {
	True, False, TrueFalse
}
