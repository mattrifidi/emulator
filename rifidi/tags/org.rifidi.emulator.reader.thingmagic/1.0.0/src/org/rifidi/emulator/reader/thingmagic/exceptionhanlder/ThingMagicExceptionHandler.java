/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic.exceptionhanlder;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.thingmagic.tagbuffer.ThingMagicTagMemory;

/**
 * @author jmaine
 *
 */
public class ThingMagicExceptionHandler extends GenericExceptionHandler {
	
	private static Log logger = LogFactory.getLog(ThingMagicExceptionHandler.class);


	/**
	 * 
	 */
	public ThingMagicExceptionHandler() {
		// TODO Auto-generated constructor stub
		super();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#commandNotFoundError(java.util.ArrayList, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {
		// TODO Auto-generated method stub
		logger.debug("ThingMagicProtocol.commandNotFoundError() called.");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#invalidCommandError(java.util.ArrayList, java.lang.String, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {
		// TODO Auto-generated method stub
		logger.debug("ThingMagicProtocol.invalidCommandError() called.");
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.command.exception.GenericExceptionHandler#malformedMessageError(java.util.ArrayList, org.rifidi.emulator.reader.command.CommandObject)
	 */
	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,
			CommandObject obj) {
		logger.debug("ThingMagicProtocol.malformedMessageError() called.");

		// TODO Auto-generated method stub
		return null;
	}

}
