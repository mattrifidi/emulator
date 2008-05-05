/*
 *  AlienAutoExceptionHandler.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.command.exception;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;

/**
 * @author matt
 * 
 */
public class AlienAutoExceptionHandler extends GenericExceptionHandler {

	/**
	 * Message logger
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory
			.getLog(AlienAutoExceptionHandler.class);

	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {

		return new ArrayList<Object>();
	}

	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {

		return new ArrayList<Object>();
	}

	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,
			CommandObject obj) {

		return new ArrayList<Object>();
	}


}
