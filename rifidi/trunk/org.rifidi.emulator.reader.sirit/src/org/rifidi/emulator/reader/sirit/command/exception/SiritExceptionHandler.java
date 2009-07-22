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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#
	 * commandNotFoundError(java.util.ArrayList,
	 * org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {
		
		return this.errorFormat("error.parser.unknown_variable", "", obj);
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
	
	/* Private method that takes care of any formatting involved in the process */
	private ArrayList<Object> errorFormat(String errorMsg, String retVal,
			CommandObject obj) {
		logger.debug("send error message: " + errorMsg);
		
		retVal += errorMsg + SiritCommon.ENDOFREPLY;
		if (obj.getCurrentQueryName().startsWith("\1")) {
		} else {
			retVal += SiritCommon.PROMPT;
		}
		ArrayList<Object> returnValue = new ArrayList<Object>();
		returnValue.add(retVal);
		return returnValue;
	}

}
