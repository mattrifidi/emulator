/*
 *  @(#)TagListFormatReaderProperty.java
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
package org.rifidi.emulator.reader.sharedrc.tagmemory.formatter;

import org.rifidi.emulator.reader.sharedrc.properties.AbstractReaderProperty;

/**
 * A property whose valid values are those listed in the TagListFormatValues
 * enumeration.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class TagListFormatReaderProperty extends
		AbstractReaderProperty<TagListFormatValues> {

	/**
	 * Constructor for an TagListFormatReaderProperty which takes an initial
	 * value.
	 * 
	 * @param value
	 *            The initial (as well as default) value to set.
	 */
	public TagListFormatReaderProperty(TagListFormatValues value) {
		super(value);
	}

	/**
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyDefaultValue(java.lang.String)
	 */
	public void setPropertyDefaultValue(String argument)
			throws IllegalArgumentException {
		/* Check if the passed string matches one of the enumerations */
		if (argument.equalsIgnoreCase(TagListFormatValues.CUSTOM.toString())) {
			this.setDefaultValue(TagListFormatValues.CUSTOM);

		} else if (argument.equalsIgnoreCase(TagListFormatValues.TEXT
				.toString())) {
			this.setDefaultValue(TagListFormatValues.TEXT);
		} else if (argument
				.equalsIgnoreCase(TagListFormatValues.XML.toString())) {
			this.setDefaultValue(TagListFormatValues.XML);
		} else if (argument.equalsIgnoreCase(TagListFormatValues.TERSE
				.toString())) {
			this.setDefaultValue(TagListFormatValues.TERSE);

		} else {
			/* No match... throw an IllegalArgumentException */
			throw new IllegalArgumentException("Invalid TagListFormat value.");

		}

	}

	/**
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyValue(java.lang.String)
	 */
	public void setPropertyValue(String argument)
			throws IllegalArgumentException {
		/* Check if the passed string matches one of the enumerations */
		if (argument.equalsIgnoreCase(TagListFormatValues.CUSTOM.toString())) {
			this.setValue(TagListFormatValues.CUSTOM);

		} else if (argument.equalsIgnoreCase(TagListFormatValues.TEXT
				.toString())) {
			this.setValue(TagListFormatValues.TEXT);

		} else if (argument
				.equalsIgnoreCase(TagListFormatValues.XML.toString())) {
			this.setValue(TagListFormatValues.XML);

		} 
		else if (argument.equalsIgnoreCase(TagListFormatValues.TERSE
				.toString())) {
			this.setValue(TagListFormatValues.TERSE);

		}else {
			/* No match... throw an IllegalArgumentException */
			throw new IllegalArgumentException("Invalid TagListFormat value.");

		}

	}

}
