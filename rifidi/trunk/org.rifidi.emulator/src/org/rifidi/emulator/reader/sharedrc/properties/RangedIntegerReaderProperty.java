/*
 *  @(#)RangedIntegerReaderProperty.java
 *
 *  Created:	Nov 9, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.sharedrc.properties;

/**
 * A reader property object which accepts a range of integers, specified at
 * construction time, as valid input.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class RangedIntegerReaderProperty extends AbstractReaderProperty<Integer> {

	/**
	 * The high endpoint of the interval.
	 */
	private int highEndPoint;

	/**
	 * True if the high endpoint is considered to be in the range, false
	 * otherwise.
	 */
	private boolean highInclusive;

	/**
	 * The low endpoint of the interval.
	 */
	private int lowEndPoint;

	/**
	 * True if the low endpoint is considered to be in the range, false
	 * otherwise.
	 */
	private boolean lowInclusive;

	/**
	 * Creates an RangedIntegerReaderProperty with the specified interval which
	 * indicates the possible range of integer values that the property may be
	 * set to.
	 * 
	 * @param lowEndPoint
	 *            The low endpoint of the interval.
	 * @param lowInclusive
	 *            True if the low endpoint is considered to be in the range,
	 *            false otherwise.
	 * @param highEndPoint
	 *            The high endpoint of the interval.
	 * @param highInclusive
	 *            True if the high endpoint is considered to be in the range,
	 *            false otherwise.
	 * @param initialValue
	 *            The initial value to set as the current value and default
	 *            value.
	 * @throws IllegalArgumentException
	 *             If the passed initial value is not in the defined range, or
	 *             the passed interval would generate an empty set.
	 */
	public RangedIntegerReaderProperty(int lowEndPoint, boolean lowInclusive,
			int highEndPoint, boolean highInclusive, Integer initialValue)
			throws IllegalArgumentException {
		/* Assign instance variables */
		super(initialValue);
		this.lowEndPoint = lowEndPoint;
		this.lowInclusive = lowInclusive;
		this.highEndPoint = highEndPoint;
		this.highInclusive = highInclusive;

		/* Make sure the range is valid */
		boolean rangeValid = true;
		if (this.lowEndPoint > this.highEndPoint) {
			/* Lower > higher means an empty set */
			rangeValid = false;
		} else if ((lowEndPoint == this.highEndPoint)
				&& ((!lowInclusive) || (!highInclusive))) {
			/* Lower is equal to higher or lower was only one less than higher */
			/* Neither are inclusive */
			rangeValid = false;

		} else if (((lowEndPoint + 1) == this.highEndPoint)
				&& ((!lowInclusive) || (!highInclusive))) {
			rangeValid = false;

		}

		/* Throw an exception if the range was not valid */
		if (!rangeValid) {
			throw new IllegalArgumentException("Invalid range specified.");
		}

		/* Do an initial set to make sure that the initialValue is valid. */
		this.setValue(initialValue);

	}

	/**
	 * Checks the passed value against the current range. Returns true if the
	 * passed value is in the range, false otherwise.
	 * 
	 * @param value
	 *            The value to check.
	 * @return True if the passed value is in the range, false otherwise.
	 */
	private boolean isInRange(Integer value) {
		/* Validate the range */
		boolean valid = true;

		/* Check against low endpoint */
		if (this.lowInclusive) {
			if (value < this.lowEndPoint) {
				valid = false;
			}

		} else {
			if (value <= this.lowEndPoint) {
				valid = false;
			}

		}

		/* Check against high endpoint */
		if (this.highInclusive) {
			if (value > this.highEndPoint) {
				valid = false;
			}

		} else {
			if (value >= this.highEndPoint) {
				valid = false;
			}
		}

		return valid;

	}

	/**
	 * Sets the default value, throwing an IllegalArgumentException if the
	 * Integer is not in range.
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed Integer is not in range.
	 * @see org.rifidi.emulator.reader.sharedrc.properties.AbstractReaderProperty#setDefaultValue(java.lang.Object)
	 */
	@Override
	public void setDefaultValue(Integer defaultValue)
			throws IllegalArgumentException {
		/* Throw an exception if out of range. */
		if (!this.isInRange(defaultValue)) {
			throw new IllegalArgumentException("Passed value out of range.");
		}

		/* Finally, call the super version of the method */
		super.setDefaultValue(defaultValue);

	}

	/**
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyDefaultValue(java.lang.String)
	 */
	public void setPropertyDefaultValue(String argument)
			throws IllegalArgumentException {
		/* Try to convert to an integer and set as value */
		try {
			this.setDefaultValue(Integer.parseInt(argument));

		} catch (NumberFormatException e) {
			/* Not a valid integer representation. */
			throw new IllegalArgumentException(e.getMessage());

		}

	}

	/**
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyValue(java.lang.String)
	 */
	public void setPropertyValue(String argument)
			throws IllegalArgumentException {
		/* Try to convert to an integer and set as value */
		try {
			this.setValue(Integer.parseInt(argument));

		} catch (NumberFormatException e) {
			/* Not a valid integer representation. */
			throw new IllegalArgumentException(e.getMessage());

		}

	}

	/**
	 * Sets the value, throwing an IllegalArgumentException if the Integer is
	 * not in range.
	 * 
	 * @throws IllegalArgumentException
	 *             If the passed Integer is not in range.
	 * @see org.rifidi.emulator.reader.sharedrc.properties.AbstractReaderProperty#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Integer newValue) throws IllegalArgumentException {
		/* Throw an exception if out of range. */
		if (!this.isInRange(newValue)) {
			throw new IllegalArgumentException("Passed value out of range.");
		}

		/* Finally, call the super version of the method */
		super.setValue(newValue);

	}

}
