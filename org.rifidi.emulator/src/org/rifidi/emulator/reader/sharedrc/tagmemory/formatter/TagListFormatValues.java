/*
 *  @(#)TagListFormatValues.java
 *
 *  Created:	Nov 8, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.sharedrc.tagmemory.formatter;

/**
 * An enumeration of the possible values for a TagFormat property.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public enum TagListFormatValues {

	/**
	 * The Alien text format for a taglist.
	 */
	TEXT {
		public String toString() {
			return "Text";
		}

	},

	/**
	 * The Alien XML format for a taglist.
	 */
	XML {
		public String toString() {
			return "XML";
		}

	},

	/**
	 * A custom taglist format.
	 */
	CUSTOM {
		public String toString() {
			return "Custom";
		}
	},

	
	/**
	 * A terse taglist format.
	 */
	TERSE {
		public String toString() {
			return "Terse";
		}

	}

}
