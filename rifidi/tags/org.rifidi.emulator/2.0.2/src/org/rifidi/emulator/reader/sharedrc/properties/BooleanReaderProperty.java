/*
 *  BooleanReaderProperty.java
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
 * Boolean Property for the ReaderProperties. Anything that is not false, null,
 * or 0 is considered "true"
 * 
 * @author matt
 */
public class BooleanReaderProperty extends AbstractReaderProperty<Boolean> {

	/**
	 * 
	 * 
	 * @param arg
	 */
	public BooleanReaderProperty(Boolean arg) {
		super(arg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyDefaultValue(java.lang.String)
	 */
	public void setPropertyDefaultValue(String argument)
			throws IllegalArgumentException {
		this.setDefaultValue(Boolean.parseBoolean(argument));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyValue(java.lang.String)
	 */
	public void setPropertyValue(String argument)
			throws IllegalArgumentException {
		this.setValue(Boolean.parseBoolean(argument));
	}

}
