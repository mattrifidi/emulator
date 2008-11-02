/*
 *  ThingMagicRQLCommandFormatter.java
 *
 *  Created:	May 5, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic.formatter;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.emulator.reader.thingmagic.commandobjects.CloseCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.DeclareCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.ErrorCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.FetchCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.ResetCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SelectCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SetCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.UpdateCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationException;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

/**
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class ThingMagicRQLCommandFormatter implements CommandFormatter {
	private static Log logger = LogFactory
			.getLog(ThingMagicRQLCommandFormatter.class);
	private ThingMagicReaderSharedResources tmsr;

	public ThingMagicRQLCommandFormatter(ThingMagicReaderSharedResources tmsr) {

		this.tmsr = tmsr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.reader.formatter.CommandFormatter#decode(byte[])
	 */
	// The CommmandFactory
	@Override
	public ArrayList<Object> decode(byte[] arg) {
		String command = new String(arg);
		logger.debug("Command: " + command);
		ArrayList<Object> retVal = new ArrayList<Object>();

		String commandTrimmed = command.trim();

		/* the command handler name
		 * All command objects inherit from the abstract class Command
		 * which as one primary method "execute". All this command handler
		 * does is run that method and take the return value and pass it to
		 * "CommandObject.setReturnValue" without modification.
		 * See: RQLEncodedCommands
		 * 
		 * The RIFIDI Core takes this command name and looks up the corresponding
		 * class.method() that it represents from the "reader.xml" file that each
		 * reader emulator contains and uses reflection to call it.
		 */
		retVal.add("execute");
		
		/* split around the white spaces */
		String temp[] = commandTrimmed.split("\\s");
		/* grab the command name */
		String commandName = temp[0];

		/*
		 * Here we look up the command name and test it against known commands.
		 * Then we create a command object that corresponds to that command
		 * (i.e. implements it.) Second we add it to the return value list so
		 * that the RIFIDI Core and pass it along to the ThingMagic executor.
		 * 
		 * Each Command constructor throws a "CommandCreationException"
		 * if the command had any syntax errors. If we get that exception,
		 * we create a ErrorCommand and pass the message we got from the exception
		 * into its constructor.
		 */
		try {
			if (commandName.toLowerCase().equals("select")) {
				retVal.add(new SelectCommand(command, tmsr));
				return retVal;
			}

			if (commandName.toLowerCase().equals("update")) {
				retVal.add(new UpdateCommand(command, tmsr));
				return retVal;
			}

			if (commandName.toLowerCase().equals("declare")) {
				retVal.add(new DeclareCommand(command, tmsr));
				return retVal;
			}

			if (commandName.toLowerCase().equals("fetch")) {
				retVal.add(new FetchCommand(command, tmsr));
				return retVal;
			}

			if (commandName.toLowerCase().equals("close")) {
				retVal.add(new CloseCommand(command, tmsr));
				return retVal;
			}

			if (commandName.toLowerCase().equals("set")) {
				retVal.add(new SetCommand(command, tmsr));
				return retVal;
			}
			
			if (commandName.toLowerCase().equals("reset")) {
				retVal.add(new ResetCommand(command, tmsr));
				return retVal;
			}
		} catch (CommandCreationException e) {
			logger.debug(e.getMessage());
			
			/*
			 * We got an error message. Create an ErrorCommand and send it
			 * the message we got as is.
			 */
			retVal.add(new ErrorCommand(e.getMessage()));
			return retVal;
		}

		/*
		 * No matching command. Create ErrorCommand and pass the error message
		 * to the user or the remote system sent the command.
		 */
		retVal.add(new ErrorCommand("Error 0100:     syntax error at '"
				+ commandName + "'"));

		return retVal;
	}

	@Override
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		logger.debug("encode() has been called: " + arg);
		ArrayList<Object> retVal = new ArrayList<Object>();

		
		/*
		 * Each object is considered as one and only one line of text,
		 * including but not limited to empty strings.
		 * So we just append a new line character to the end of it.
		 * Note: Objects here are really Strings.
		 * 
		 */
		for (Object o : arg) {
			retVal.add(o + "\n");
		}

		/*
		 * These objects eventually get converted to an array of bytes
		 * and sent to Protocol.addProtocol() to add an optional 
		 * protocol layer that may be needed on a case by case basis
		 * for each reader. 
		 * Note: The version for the ThingMagic reader (ThingMagicProtocol) does
		 * nothing but send the bytes along as is.
		 */
		return retVal;
	}

	@Override
	public String getActualCommand(byte[] arg) {
		// TODO Auto-generated method stub
		logger
				.debug("ThingMagicRQLCommandFormatter.getActualCommand() has been called.");
		return new String(arg);
	}

	@Override
	public boolean promptSuppress() {
		// TODO Auto-generated method stub
		logger
				.debug("ThingMagicRQLCommandFormatter.promptSuppress() has been called.");
		return false;
	}

}
