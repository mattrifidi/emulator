/**
 * 
 */
package org.rifidi.emulator.reader.alien.io.protocol;

import java.util.ArrayList;
import java.util.List;

import org.rifidi.emulator.io.protocol.Protocol;
import org.rifidi.emulator.io.protocol.ProtocolValidationException;

/**
 * @author Matthew
 *
 */
public class AlienProtocol implements Protocol {

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.protocol.Protocol#addProtocol(byte[])
	 */
	//@Override
	public byte[] addProtocol(byte[] data) {
		return data;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.protocol.Protocol#removeProtocol(byte[])
	 */
	//@Override
	public List<byte[]> removeProtocol(byte[] data)
			throws ProtocolValidationException {
		List<byte[]> retVal = new ArrayList<byte[]>();
		String strData = new String(data);
		String[] arrayData = strData.split("\n");
		for( String i:arrayData ) {
			retVal.add(i.getBytes());
		}
		return retVal;
	}

}
