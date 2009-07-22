/*
 *  SiritCommandFormatter.java
 *
 *  Created:	24.06.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.formatter;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.formatter.CommandFormatter;

/**
 * A formatter for the Sirit reader.
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritCommandFormatter implements CommandFormatter {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(SiritCommandFormatter.class);

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	public ArrayList<Object> decode(byte[] arg) {
		String clientInput = new String(arg);
		clientInput = clientInput.trim();
		logger.debug("Input from client: " + clientInput);

		/* the return value consists of the command and the arguments */
		ArrayList<Object> retVal = new ArrayList<Object>();

		/*
		 * The sirit reader interface offers properties and methods being
		 * divided into namespaces.
		 * 
		 * A method call can take one to many arguments that are issued in
		 * brackets "()" and split by comma ",".
		 * 
		 * Properties are read by simply typing their name and set by adding an
		 * "=" following the new value.
		 */

		/* decide what the client wants: set or get property or call method */
		String command = "";
		String arguments = null;
		if (clientInput.indexOf('(') > 0) {
			/* method call */
			/* get the command with namespace */
			command = clientInput.substring(0, clientInput.indexOf("("));
			
			/* get the arguments */
			arguments = clientInput.substring(clientInput.indexOf("(") + 1,
					clientInput.indexOf(")"));
			arguments.replaceAll("\\s+", "");
		}
		else if (clientInput.indexOf('=') > 0) {
			/* property setter */
			/* get the command with namespace */
			command = clientInput.substring(0, clientInput.indexOf("="));
			/* get the arguments */
			arguments = clientInput.substring(clientInput.indexOf("=") + 1);
		}
		else {
			/* property getter */
			command = clientInput;
		}
		
		retVal.add(command.trim());
		if (arguments != null) {
			retVal.add(arguments.trim());
		}
				
		return retVal;
	}
	
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#encode(java.util.ArrayList)
	 */
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		StringBuffer retVal = new StringBuffer();
		for (Object i : arg) {
			retVal.append(i);
		}
		arg.clear();
		arg.add(retVal.toString());

		logger.debug("COMMAND IS: " + retVal);

		return arg;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#promptSuppress()
	 */
	public boolean promptSuppress() {
		logger.debug("SiritCommandFormatter - promptSuppress()");
		
		return true;
	}
	
	public String getActualCommand(byte[] arg) {
		logger.debug("SiritCommandFormatter - getActualcommand()");
		
		return new String(arg);
	}
	
	
}
