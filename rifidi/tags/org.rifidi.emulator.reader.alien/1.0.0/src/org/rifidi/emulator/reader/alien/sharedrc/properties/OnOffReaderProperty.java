/*
 *  @(#)OnOffReaderProperty.java
 *
 *  Created:	Nov 8, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.alien.sharedrc.properties;

import org.rifidi.emulator.reader.sharedrc.properties.AbstractReaderProperty;

/**
 * A property whose valid values are ON and OFF.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class OnOffReaderProperty extends AbstractReaderProperty<OnOffValues> {

	/**
	 * TODO: Change the name of this class and move it to a non-alien 
	 * specific package
	 * 
	 * Constructor for an OnOffReaderProperty which takes an initial value.
	 * 
	 * @param value
	 *            The initial (as well as default) value to set.
	 */
	public OnOffReaderProperty(OnOffValues value) {
		super(value);

	}

	/**
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyDefaultValue(java.lang.String)
	 */
	public void setPropertyDefaultValue(String argument)
			throws IllegalArgumentException {
		/* Check if the passed string matches one of the enumerations */
		if (argument.equalsIgnoreCase(OnOffValues.OFF.toString())) {
			this.setDefaultValue(OnOffValues.OFF);

		} else if (argument.equalsIgnoreCase(OnOffValues.ON.toString())) {
			this.setDefaultValue(OnOffValues.ON);

		} else {
			/* No match... throw an IllegalArgumentException */
			throw new IllegalArgumentException("Invalid OnOffValue.");

		}

	}

	/**
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyValue(java.lang.String)
	 */
	public void setPropertyValue(String argument)
			throws IllegalArgumentException {
		/* Check if the passed string matches one of the enumerations */
		if (argument.equalsIgnoreCase(OnOffValues.OFF.toString())) {
			this.setValue(OnOffValues.OFF);

		} else if (argument.equalsIgnoreCase(OnOffValues.ON.toString())) {
			this.setValue(OnOffValues.ON);

		} else {
			/* No match... throw an IllegalArgumentException */
			throw new IllegalArgumentException("Invalid OnOffValue.");

		}

	}

}
