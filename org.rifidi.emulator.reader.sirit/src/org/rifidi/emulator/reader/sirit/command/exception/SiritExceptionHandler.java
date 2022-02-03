/*
 *  SiritExceptionHandler.java
 *
 *  Created:	09.06.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.command.exception;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.sirit.commandhandler.SiritCommon;

/**
 * This class will be used to handle any exceptions during the Interactive
 * state.
 * 
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritExceptionHandler extends GenericExceptionHandler {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(SiritExceptionHandler.class);

	/**
	 * called by the native command handler when a variable/command can not be
	 * mapped
	 */
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {

		/** check whether command or variable was passed in */
		if (obj.getCurrentQueryName().indexOf("(") != -1) {
			return this.unknownCommandError(arg, obj);	
		}
		else {
			return this.unknownVariableError(arg, obj);
		}
		
	}

	/** tells that the given variable can not be found */
	public ArrayList<Object> unknownCommandError(ArrayList<Object> arg,
			CommandObject obj) {

		return this.errorFormat("error.parser.unknown_command", "", obj);
	}
	
	/** tells that the given variable can not be found */
	public ArrayList<Object> unknownVariableError(ArrayList<Object> arg,
			CommandObject obj) {

		return this.errorFormat("error.parser.unknown_variable", "", obj);
	}

	/** called when an argument is not valid */
	public ArrayList<Object> illegalValueError(CommandObject arg) {

		return this.errorFormat("error.parser.illegal_value", "", arg);
	}

	/* Private method that takes care of any formatting involved in the process */
	private ArrayList<Object> errorFormat(String errorMsg, String retVal,
			CommandObject obj) {
		logger.debug("send error message: " + errorMsg);

		retVal += errorMsg + SiritCommon.ENDOFREPLY;
		ArrayList<Object> returnValue = new ArrayList<Object>();
		returnValue.add(retVal);
		return returnValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#
	 * invalidCommandError(java.util.ArrayList, java.lang.String,
	 * org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg0,
			String arg1, CommandObject arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#
	 * malformedMessageError(java.util.ArrayList,
	 * org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg0,
			CommandObject arg1) {
		// TODO Auto-generated method stub
		return null;
	}
}
