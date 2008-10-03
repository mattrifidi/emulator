/*
 *  ThingMagicRQLExceptionHandler.java
 *
 *  Created:	May 5, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.command.exception;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class ThingMagicRQLExceptionHandler extends GenericExceptionHandler {
	
	private static Log logger = LogFactory.getLog(ThingMagicRQLExceptionHandler.class);
	
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,
			CommandObject obj) {
		// TODO Auto-generated method stub
		logger.debug("error!");
		return null;
	}

}
