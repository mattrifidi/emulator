/*
 *  @(#)StringReaderProperty.java
 *
 *  Created:	Nov 8, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.sharedrc.properties;

/**
 * A reader property which has a String as its underlying type.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class StringReaderProperty extends AbstractReaderProperty<String> {

	/**
	 * Constructor for an StringReaderProperty which takes an initial value.
	 * 
	 * @param value
	 *            The initial (as well as default) value to set.
	 */
	public StringReaderProperty(String value) {
		super(value);
	}

	/**
	 * Simply sets the underlying default value to the passed string.
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyDefaultValue(java.lang.String)
	 */
	public void setPropertyDefaultValue(String argument)
			throws IllegalArgumentException {
		this.setDefaultValue(argument);

	}

	/**
	 * Simply sets the underlying value to the passed string.
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyValue(java.lang.String)
	 */
	public void setPropertyValue(String argument)
			throws IllegalArgumentException {
		this.setValue(argument);

	}

}
