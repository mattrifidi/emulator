/**
 * 
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
		
		List<String> removedString = new ArrayList<String>();
		Pattern tokenizer = Pattern.compile(
				"[^;]+;|[^;]+", Pattern.CASE_INSENSITIVE
								| Pattern.DOTALL);
				Matcher tokenFinder = tokenizer.matcher(stringData);

				while (tokenFinder.find()) {
					String temp = tokenFinder.group();
					/*
					 * no need to add empty strings at tokens.
					 */
					// TODO: Figure out why we are getting empty stings as tokens.
					if (temp.equals(""))
						continue;
					logger.debug(temp);
					removedString.add(temp);
				}
		

		
	
		for (String s: removedString){
			removed.add(s.getBytes());
		}
		
		return removed;
	}

}
