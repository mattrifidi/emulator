/*
 *  ReaderProperty.java
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
 * An interface for getting/setting properties using String objects, rather than what the underlying value may actually be.
 * @author  Matthew Dean
 * @author  John Olender
 * @since  <$INITIAL_VERSION$>
 * @version  <$CURRENT_VERSION$>
 */
public interface ReaderProperty {

	/**
	 * Gets the default value of the property in String form.
	 * 
	 * @return The default value associated with this property in string form.
	 */
	public String getPropertyDefaultStringValue();

	/**
	 * Gets the value of the property in String form.
	 * 
	 * @return The value of the property in String form.
	 */
	public String getPropertyStringValue();

	/**
	 * Resets the current reader property value to its default value.
	 */
	public void reset();

	/**
	 * Saves the current reader property value as the default value.
	 */
	public void save();

	/**
	 * Sets the property's default value to the given argument, converting from
	 * String format to the underlying property format.
	 * 
	 * @param argument
	 *            The value to set the default value to.
	 * @throws IllegalArgumentException
	 *             If the String value cannot be converted to the underlying
	 *             property type or for other reasons otherwise fails
	 *             validation.
	 */
	public void setPropertyDefaultValue(String argument)
			throws IllegalArgumentException;

	/**
	 * Sets the property's value to the given argument, converting from String
	 * format to the underlying property format.
	 * 
	 * @param argument
	 *            The value to set the object to.
	 * @throws IllegalArgumentException
	 *             If the String cannot be converted to the underlying property
	 *             type or for other reasons fails validation.
	 */
	public void setPropertyValue(String argument)
			throws IllegalArgumentException;

}
