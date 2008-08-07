package org.rifidi.emulator.reader.thingmagic_old.command.exception;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.thingmagic_old.commandhandler.RQLEncodedCommands;

public class ThingMagicRQLExceptionHandler extends GenericExceptionHandler {
	
	private static Log logger = LogFactory.getLog(ThingMagicRQLExceptionHandler.class);
	
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,
			CommandObject obj) {
		// TODO Auto-generated method stub
		logger.debug("error!");
		return null;
	}

}
