/*
 *  ThingMagicProtocol.java
 *
 *  Created:	May 5, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.io.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.protocol.Protocol;
import org.rifidi.emulator.io.protocol.ProtocolValidationException;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class ThingMagicProtocol implements Protocol {
	
	private static Log logger = LogFactory.getLog(ThingMagicProtocol.class);
	
	/*
	 * This regex breaks up the command into a series of strings that end in a semicolon--
	 * counting for the case that there might not be a semicolon at the very end of the string.
	 * 
	 * This is made "private final" because the pattern does not change, and thus
	 * does not need to be recreated each time "removeProtocol" is called.
	 * 
	 * There might be a case where a semicolon is surrounded by quotes.
	 */
	private final Pattern tokenizer = Pattern.compile(
			"[^;]+;|[^;]+", Pattern.CASE_INSENSITIVE
			| Pattern.DOTALL);

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.protocol.Protocol#addProtocol(byte[])
	 */
	@Override
	public final byte[] addProtocol(final byte[] data) {
		// TODO Auto-generated method stub
		logger.debug("ThingMagicProtocol.addProtocol() called: " + new String(data));
		/*
		 * we do nothing here but return the data as is.
		 */
		return data;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.io.protocol.Protocol#removeProtocol(byte[])
	 */
	/*
	 * Here we get the command as a raw string of bytes that we must chop up
	 * and transform into more meaningful commands.
	 * One example of this may be to strip HTTP out and send it on,
	 * but here we split it up using semicolons as delimiters.
	 */
	@Override
	public final List<byte[]> removeProtocol(final byte[] data)
			throws ProtocolValidationException {
		logger.debug("ThingMagicProtocal.removeProtocol() called: " + new String(data));
		List<byte[]> removed = new ArrayList<byte[]>();
		String stringData = new String(data);
		
		List<String> removedString = new ArrayList<String>();
		
				Matcher tokenFinder = tokenizer.matcher(stringData);

				while (tokenFinder.find()) {
					String temp = tokenFinder.group();
					/*
					 * no need to add empty strings as tokens or
					 * strings with just white space.
					 */
					if (temp.equals("") || temp.matches("\\s+"))
						continue;
					logger.debug(temp);
					removedString.add(temp);
				}
		

		
	
		for (String s: removedString){
			removed.add(s.getBytes());
		}
		
		
		/*
		 * What is returned here eventually goes to reader's CommandFormatter.decode()
		 * to be turned into more meaningful data that can be acted upon.
		 * In this case it is sent to ThingMagicRQLCommandFormatter object to be
		 * dealt with.
		 */
		return removed;
	}

}
