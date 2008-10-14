/*
 *  ErrorCommand.java
 *
 *  Created:	August 12, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class ErrorCommand extends Command {

	private String errorMessage;

	public ErrorCommand(String errorMessage){
		/* error message must not be null*/
		if (errorMessage == null) 
			throw new NullPointerException();
		
		this.errorMessage = errorMessage;
	}
	
	@Override
	public ArrayList<Object> execute() {
		ArrayList<Object> retVal = new ArrayList<Object>();
		retVal.add(errorMessage);
		retVal.add("");
		return retVal;
	}

	@Override
	public String toCommandString() {
		return "Error Message: " + errorMessage;
	}

}
