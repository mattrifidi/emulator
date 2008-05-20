/*
 *  EPCExceptionHandler.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.epc.command.exception;

import java.util.ArrayList;

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;

/**
 * @author matt
 * 
 */
public class EPCExceptionHandler extends GenericExceptionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#commandNotFoundError(java.util.ArrayList,
	 *      org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {
		ArrayList<Object> newList = new ArrayList<Object>();
		newList.add("ERROR 0002: COMMAND NOT SUPPORTED\r\n\r\nEPC rp v1.1>");
		return newList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#invalidCommandError(java.util.ArrayList,
	 *      java.lang.String, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {
		ArrayList<Object> newList = new ArrayList<Object>();
		newList.add("ERROR 0001: INVALID COMMAND\r\n\r\nEPC rp v1.1>");
		return newList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#malformedMessageError(java.util.ArrayList,
	 *      org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,
			CommandObject obj) {
		ArrayList<Object> newList = new ArrayList<Object>();
		newList.add("ERROR 0003: INVALID FORMAT\r\n\r\nEPC rp v1.1>");
		return newList;
	}

	
	public ArrayList<Object> illegalValueError() {
		ArrayList<Object> newList = new ArrayList<Object>();
		newList.add("ERROR 0005: ILLEGAL ARGUMENT\r\n\r\nEPC rp v1.1>");
		return newList;
	}
	
	
	public ArrayList<Object> parameterMissing() {
		ArrayList<Object> newList = new ArrayList<Object>();
		newList.add("ERROR 0004: PARAMETER MISSING\r\n\r\nEPC rp v1.1>");
		return newList;
	}
	
	public ArrayList<Object> unknownError() {
		ArrayList<Object> newList = new ArrayList<Object>();
		newList.add("ERROR 0001: UNKNOWN ERROR\r\n\r\nEPC rp v1.1>");
		return newList;
	}
}
