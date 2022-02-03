/*
 *  @(#)Protocol.java
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

import java.util.List;

/**
 * A protocol defines two methods which aid in adding and removing a protocol to
 * a piece of data.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public interface Protocol {

	/**
	 * Constructs a new byte array which contains a specific protocol in
	 * addition to the original data.
	 * 
	 * @param data
	 *            The data to add the protocol to.
	 * @return A new byte array with an added protocol.
	 */
	public byte[] addProtocol(byte[] data);

	/**
	 * Constructs a new byte array with protocol information stripped out.
	 * 
	 * @param data
	 *            The data to remove the protocol from.
	 * @return A new byte array with the protocol removed.
	 * @throws ProtocolValidationException
	 *             If there is an error removing the protocol due to a protocol
	 *             validation error. This may occur if the protocol does not
	 *             exists, is malformed, or error-checking incidates a corrupt
	 *             message.
	 */
	public List<byte[]> removeProtocol(byte[] data)
			throws ProtocolValidationException;

}
