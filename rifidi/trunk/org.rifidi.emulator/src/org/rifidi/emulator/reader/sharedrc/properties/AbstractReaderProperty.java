/*
 *  AbstractReaderProperty.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sharedrc.properties;

import java.util.Observable;

/**
 * An abstract implementation of the ReaderProperty. Adds an underlying property
 * type, along with getters/setters for the actual value type. This class leave
 * the implementation of the get/set String methods to child classes.
 * 
 * @author Matthew Dean
 * @author John Olender
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * @param <T>
 *            The underlying type of value which this property objects holds.
 */
public abstract class AbstractReaderProperty<T> extends Observable implements
		ReaderProperty {

	/**
	 * The default value of the property.
	 */
	private T defaultValue;

	/**
	 * The value of the property.
	 */
	private T value;

	/**
	 * Constructs a AbstractReaderProperty with the passed value as the default
	 * value.
	 * 
	 * @param value
	 *            The initial value to set.
	 */
	public AbstractReaderProperty(T value) {
		this.defaultValue = value;
		this.value = value;

	}

	/**
	 * Returns the defaultValue.
	 * 
	 * @return Returns the defaultValue.
	 */
	public final T getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * Simply calls toString on the underlying default value. Subclasses may
	 * wish to override this method to fine-tune the String output.
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#getPropertyDefaultStringValue()
	 */
	public String getPropertyDefaultStringValue() {
		/* Simply call the object's toString method */
		return this.defaultValue.toString();

	}

	/**
	 * Simply calls toString on the underlying value. Subclasses may wish to
	 * override this method to fine-tune the String output.
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#getPropertyStringValue()
	 */
	public String getPropertyStringValue() {
		/* Simply call the object's toString method */
		return this.value.toString();

	}

	/**
	 * Gets the value of the current object.
	 * 
	 * @return The current value of the object.
	 */
	public final T getValue() {
		return this.value;
	}

	/**
	 * Resets the current value to the default value.
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#reset()
	 */
	public void reset() {
		/* Reset the current value to the default value */
		this.setValue(this.defaultValue);

	}

	/**
	 * Saves the current value as the default value.
	 * 
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#save()
	 */
	public void save() {
		/* Save the current value as the default value */
		this.setDefaultValue(this.value);

	}

	/**
	 * Sets defaultValue to the passed parameter, defaultValue.
	 * 
	 * @param defaultValue
	 *            The defaultValue to set.
	 */
	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;

	}

	/**
	 * Sets the current value of the object.
	 * 
	 * @param newValue
	 *            The value to set the current value of the object to.
	 */
	public void setValue(T newValue) {
		this.value = newValue;
		this.setChanged();
		this.notifyObservers();

	}

}
