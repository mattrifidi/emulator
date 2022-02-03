/*
 *  @(#)OnOffValues.java
 *
 *  Created:	Nov 8, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

/**
 * 
 */
package org.rifidi.emulator.reader.alien.sharedrc.properties;

/**
 * An enumeration of possible values for OnOff properties.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public enum OnOffValues {

	/**
	 * On.
	 */
	ON {
		public String toString() {
			return "ON";
		}

	},

	/**
	 * Off.
	 */
	OFF {
		public String toString() {
			return "OFF";
		}

	}

}
