/*
 *  ShortReaderProperty.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sharedrc.properties;

/**
 * @author matt
 *
 */
public class ShortReaderProperty extends AbstractReaderProperty<Short> {

	/**
	 * @param value
	 */
	public ShortReaderProperty(Short value) {
		super(value);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyDefaultValue(java.lang.String)
	 */
	public void setPropertyDefaultValue(String argument)
			throws IllegalArgumentException {
		/* Try to convert to a short and set as value */
		try {
			this.setDefaultValue(Short.parseShort(argument.trim()));

		} catch (NumberFormatException e) {
			/* Not a valid integer representation. */
			throw new IllegalArgumentException(e.getMessage());

		}
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyValue(java.lang.String)
	 */
	public void setPropertyValue(String argument)
			throws IllegalArgumentException {
		
		/* Try to convert to a short and set as value */
		try {
			this.setValue(Short.parseShort(argument.trim()));

		} catch (NumberFormatException e) {
			/* Not a valid integer representation. */
			throw new IllegalArgumentException(e.getMessage());

		}
	}

}
