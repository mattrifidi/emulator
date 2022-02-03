/**
 * 
 */
package org.rifidi.emulator.reader.alien.commandhandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * A class that represents all of the time functions for the alien.  
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class AlienTime {
	
	public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	
	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(AlienAutonomous.class);
	
	/**
	 * Set the address that will be connected to for autonomous.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject time(CommandObject arg,
			AbstractReaderSharedResources asr) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		logger.debug("In the time method");
		
		arg.getReaderProperty().setPropertyValue(sdf.format(cal.getTime()));
		return AlienCommon.returnCurrentValue(arg, asr);
	}
	
	/**
	 * Set the address that will be connected to for autonomous.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject timeServer(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("In the timeserver method");
		return AlienCommon.getter_setter(arg, asr);
	}
	
	/**
	 * Set the address that will be connected to for autonomous.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject timeZone(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("In the timeZone method");
		return AlienCommon.getter_setter(arg, asr);
	}
}
