/*
 *  DoubleReaderProperty.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sharedrc.properties;

/**
 * A AbstractReaderProperty subclass whose underlying data type is Double.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class DoubleReaderProperty extends AbstractReaderProperty<Double> {

	/**
	 * Constructor for the DoubleReaderProperty
	 * 
	 * @param arg
	 *            The double to set the value to.
	 */
	public DoubleReaderProperty(Double arg) {
		super(arg);
	}

	/**
	 * Attempts to parse a Double from the String, throwing an exception if the
	 * parse fails.
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyDefaultValue(java.lang.String)
	 */
	public void setPropertyDefaultValue(String argument)
			throws IllegalArgumentException {
		/* Try to convert to a double and set as value */
		try {
			this.setDefaultValue(Double.parseDouble(argument));

		} catch (NumberFormatException e) {
			/* Not a valid double representation. */
			throw new IllegalArgumentException(e.getMessage());

		}

	}

	/**
	 * Attempts to parse a Double from the String, throwing an exception if the
	 * parse fails.
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyValue(java.lang.String)
	 */
	public void setPropertyValue(String argument)
			throws IllegalArgumentException {
		/* Try to convert to a double and set as value */
		try {
			this.setValue(Double.parseDouble(argument));

		} catch (NumberFormatException e) {
			/* Not a valid double representation. */
			throw new IllegalArgumentException(e.getMessage());
		}

	}

}
