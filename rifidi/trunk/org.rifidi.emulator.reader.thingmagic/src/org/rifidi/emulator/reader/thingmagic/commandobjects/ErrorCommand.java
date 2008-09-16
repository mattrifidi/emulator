package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;

public class ErrorCommand implements Command {

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
