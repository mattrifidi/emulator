/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic.io.protocol;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.protocol.Protocol;
import org.rifidi.emulator.io.protocol.ProtocolValidationException;

/**
 * @author jmaine
 *
 */
public class ThingMagicProtocol implements Protocol {
	
	private static Log logger = LogFactory.getLog(ThingMagicProtocol.class);

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.protocol.Protocol#addProtocol(byte[])
	 */
	@Override
	public final byte[] addProtocol(final byte[] data) {
		// TODO Auto-generated method stub
		logger.debug("ThingMagicProtocal.addProtocol() called: " + new String(data));
		return data;
	}

	//TODO Send the semicolon onward. It will help with handling errors in syntax.
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.protocol.Protocol#removeProtocol(byte[])
	 */
	@Override
	public final List<byte[]> removeProtocol(final byte[] data)
			throws ProtocolValidationException {
		logger.debug("ThingMagicProtocal.removeProtocol() called: " + new String(data));
		List<byte[]> removed = new ArrayList<byte[]>();
		String stringData = new String(data);
		
		/* This regular expression verifies that each command is terminated with a semicolon. */
		if (!stringData.matches("^\\s*?([[\\w\\W]&&[^;]]*?;\\s*?)+?$")) 
			throw new ProtocolValidationException();
		
		/* force incoming command to lower case */
		//stringData = stringData.toUpperCase();
		
		/* split the string and eat the leading white spaces
		 *  in front of each semicolon.
		 */
		String[] removedString = stringData.split(";\\s*");
		
		/* eat all white space in front of the string at index zero. */
		while (removedString[0].matches("^\\s+.*")){
			removedString[0] = removedString[0].substring(1);
		}
	
		for (String s: removedString){
			removed.add(s.getBytes());
		}
		
		return removed;
	}

}
