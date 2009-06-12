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

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;

/**
 * @author Stefan Fahrnbauer - stefan@pramari.com
 *
 */
public class SiritExceptionHandler extends GenericExceptionHandler{

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#commandNotFoundError(java.util.ArrayList, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg0,
			CommandObject arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#invalidCommandError(java.util.ArrayList, java.lang.String, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg0,
			String arg1, CommandObject arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#malformedMessageError(java.util.ArrayList, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg0,
			CommandObject arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
