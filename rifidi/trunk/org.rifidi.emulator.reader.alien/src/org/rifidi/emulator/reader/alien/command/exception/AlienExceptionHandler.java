/*
 *  AlienExceptionHandler.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.command.exception;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.commandhandler.AlienCommon;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;

/**
 * This class will be used to handle any exceptions during the Interactive
 * state.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AlienExceptionHandler extends GenericExceptionHandler {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(AlienExceptionHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#
	 * commandNotFoundError(byte[])
	 */
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {
		String retVal = "";
		for (Object i : arg) {
			retVal += i.toString();
		}
		return this
				.errorFormat("Error 1: Command not understood.", retVal, obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#
	 * invalidCommandError(byte[])
	 */
	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {
		String retVal = "";
		for (Object i : arg) {
			retVal += i.toString();
		}
		return this.errorFormat("Error 24: Invalid format. " + value
				+ " Expected.", retVal, obj);
	}

	/**
	 * This error is used when the argument that the user passed in was not a
	 * possible value
	 * 
	 * @param arg
	 * @param obj
	 * @param PossibleValues
	 *            The list of possible values
	 * @return
	 */
	public ArrayList<Object> error10(ArrayList<Object> arg, CommandObject obj,
			ArrayList<String> PossibleValues) {
		String retVal = "";
		for (Object i : arg) {
			retVal += i.toString();
		}
		StringBuffer sb = new StringBuffer();
		for (String s : PossibleValues) {
			sb.append(s + "|");
		}
		String values = sb.substring(0, sb.length() - 1);
		return this.errorFormat("Error 10: String must be a member of: "
				+ values, retVal, obj);
	}

	/**
	 * This is used when a programming function (program, erase, etc.) is used,
	 * but no tag could be found
	 * 
	 * @param arg
	 * @param obj
	 * @return
	 */
	public ArrayList<Object> error134(ArrayList<Object> arg, CommandObject obj) {
		String retVal = "";
		for (Object i : arg) {
			retVal += i.toString();
		}
		return this.errorFormat("Error 134: No Tag Found", retVal, obj);
	}

	/**
	 * This is used when a programming function (program, erase, etc.) is used,
	 * but the tag is locked
	 * 
	 * @param arg
	 * @param obj
	 * @return
	 */
	public ArrayList<Object> error137(ArrayList<Object> arg, CommandObject obj) {
		String retVal = "";
		for (Object i : arg) {
			retVal += i.toString();
		}
		return this.errorFormat("Error 137: Tag is locked", retVal, obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#
	 * malformedMessageError(byte[])
	 */
	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,
			CommandObject obj) {
		String retVal = "";
		for (Object i : arg) {
			retVal += i.toString();
		}
		return this.errorFormat("Error: Malformed Message.", retVal, obj);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Object> outOfRangeError(ArrayList<Object> arg,
			CommandObject obj) {
		String retVal = "";
		for (Object i : arg) {
			retVal += i.toString();
		}
		return this.errorFormat("Error 10: Value out of range.  Legal "
				+ "limits are between 0 and 255", retVal, obj);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Object> antennaOutOfRangeError(ArrayList<Object> arg,
			CommandObject obj) {
		String retVal = "";
		for (Object i : arg) {
			retVal += i.toString();
		}
		return this.errorFormat("Error 10: Value out of range.  Legal "
				+ "limits are between 0 and 3", retVal, obj);
	}

	/* Private method that takes care of any formatting involved in the process */
	private ArrayList<Object> errorFormat(String errorMsg, String retVal,
			CommandObject obj) {
		logger.debug("command value = " + retVal);
		retVal += (AlienCommon.NEWLINE + errorMsg + AlienCommon.NEWLINE + AlienCommon.ENDOFREPLY);
		if (obj.getCurrentQueryName().startsWith("\1")) {
		} else {
			retVal += AlienCommon.NONZEROPROMPT;
		}
		ArrayList<Object> returnValue = new ArrayList<Object>();
		returnValue.add(retVal);
		return returnValue;
	}

	@Override
	public CommandObject methodInvocationError(ArrayList<Object> arg,
			CommandObject obj) {
		return null;
	}
}
