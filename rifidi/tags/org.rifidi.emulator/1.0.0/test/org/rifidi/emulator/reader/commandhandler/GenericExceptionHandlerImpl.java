/*
 *  GenericExceptionHandlerImpl.java
 *
 *  Created:	Dec 12, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *  Author:    Kyle Neumeier - kyle@pramari.com
 */
package org.rifidi.emulator.reader.commandhandler;

import java.util.ArrayList;

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;

/**
 * 
 * This is an implementation of GenericExceptionHandler for testing purposes only
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public class GenericExceptionHandlerImpl extends GenericExceptionHandler {

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#commandNotFoundError(java.util.ArrayList, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#invalidCommandError(java.util.ArrayList, java.lang.String, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#malformedMessageError(java.util.ArrayList, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,
			CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#methodInvocationError(java.util.ArrayList, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public CommandObject methodInvocationError(ArrayList<Object> arg,
			CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
