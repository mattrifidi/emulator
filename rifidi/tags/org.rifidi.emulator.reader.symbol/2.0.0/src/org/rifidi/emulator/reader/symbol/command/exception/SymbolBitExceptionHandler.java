/*
 *  SymbolBitExceptionHandler.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.symbol.command.exception;

import java.util.ArrayList;

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;

/**
 * Exception handler for the Symbol reader.  
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class SymbolBitExceptionHandler extends GenericExceptionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler
	 *      #commandNotFoundError(java.util.ArrayList,
	 *      org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {
		System.out.println("returning a commandNotFound"
				+ " exception in the Symbol");
		return new ArrayList<Object>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler
	 *      #invalidCommandError(java.util.ArrayList, java.lang.String,
	 *      org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {
		System.out.println("returning an invalid command"
				+ " exception in the Symbol");
		return new ArrayList<Object>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler
	 *      #malformedMessageError(java.util.ArrayList,
	 *      org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,
			CommandObject obj) {
		System.out.println("returning a malformed message"
					+ " exception in the Symbol");
		return new ArrayList<Object>();
	}

}
