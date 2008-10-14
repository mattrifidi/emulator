/*
 *  @(#)RawProtocol.java
 *
 *  Created:	Sep 18, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.protocol;

import java.util.ArrayList;
import java.util.List;


/**
 * An implementation of the protocol interface which preserves data in its raw
 * format. That is to say, the data is not changed when an add or remove
 * protocol is called.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class RawProtocol implements Protocol {

	/**
	 * Creates a raw protocol object.
	 * 
	 */
	public RawProtocol() {
		/* Do nothing */
	}

	/**
	 * This method returns the original byte array as the data is left in its
	 * raw form.
	 * 
	 * @see org.rifidi.emulator.io.protocol.Protocol#addProtocol(byte[])
	 */
	public byte[] addProtocol(byte[] data) {
		return data;
	}
	
	public String toString() {
		return "This is a raw protocol!";
	}

	/**
	 * This method returns the original byte array as the data is left in its
	 * raw form.
	 * 
	 * @see org.rifidi.emulator.io.protocol.Protocol#removeProtocol(byte[])
	 */
	public List<byte[]> removeProtocol(byte[] data) {
		List<byte[]> retVal = new ArrayList<byte[]>();
		retVal.add(data);
		return retVal;
	}

}
