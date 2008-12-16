/*
 *  AwidExceptionHandler.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.awid.command.exception;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;

/**
 * A class to handle exceptions with the Awid MPR reader (currently unused).
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AwidExceptionHandler extends GenericExceptionHandler {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AwidExceptionHandler.class);

	/**
	 * Constructor.
	 */
	public AwidExceptionHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#commandNotFoundError(ArrayList<Object>)
	 */
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,CommandObject obj) {
		logger.error("Command Not Found Exception occured.");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#invalidCommandError(ArrayList<Object>,
	 *      java.lang.String)
	 */
	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {
		logger.error("InvalidCommand exception occured.");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#malformedMessageError(ArrayList<Object>)
	 */
	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,CommandObject obj) {
		logger.error("Malformed message exception occured");
		return null;
	}

}
