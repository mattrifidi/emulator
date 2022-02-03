/*
 *  @(#)InetAddressReaderProperty.java
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

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A property object which contains an InetAddress as the underlying data type.
 * This class offloads the work of validating hostname strings to the
 * InetAddress.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class InetAddressReaderProperty extends
		AbstractReaderProperty<InetAddress> {

	/**
	 * Constructor for an InetAddressReaderProperty which takes an initial
	 * value.
	 * 
	 * @param value
	 *            The initial (as well as default) value to set.
	 */
	public InetAddressReaderProperty(InetAddress value) {
		super(value);
	}

	/**
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyDefaultValue(java.lang.String)
	 */
	public void setPropertyDefaultValue(String argument)
			throws IllegalArgumentException {
		/* Try to get a new InetAddress based on the string. */
		try {
			this.setDefaultValue(InetAddress.getByName(argument));

		} catch (UnknownHostException e) {
			/* Invalid IP or could not resolve host name, throw illegal argument */
			throw new IllegalArgumentException(e.getMessage());

		}

	}

	/**
	 * @see org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty#setPropertyValue(java.lang.String)
	 */
	public void setPropertyValue(String argument)
			throws IllegalArgumentException {
		/* Try to get a new InetAddress based on the string. */
		try {
			this.setValue(InetAddress.getByName(argument));

		} catch (UnknownHostException e) {
			/* Invalid IP or could not resolve host name, throw illegal argument */
			throw new IllegalArgumentException(e.getMessage());

		}

	}

}
