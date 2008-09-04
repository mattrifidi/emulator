package org.rifidi.emulator.reader.thingmagic.formatter;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.formatter.CommandFormatter;
import org.rifidi.emulator.reader.thingmagic.commandobjects.DeclareCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.ErrorCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.FetchCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SelectCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.SetCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.UpdateCommand;
import org.rifidi.emulator.reader.thingmagic.commandobjects.exceptions.CommandCreationExeption;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

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

		/* the command handler name */
		retVal.add("execute");

		/* split around the white spaces */
		String temp[] = commandTrimmed.split("\\s");
		/* grab the command name */
		String commandName = temp[0];

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

			if (commandName.toLowerCase().equals("set")) {
				retVal.add(new SetCommand(command));
				return retVal;
			}
		} catch (CommandCreationExeption e) {
			logger.debug(e.getMessage());
			retVal.add(new ErrorCommand(e.getMessage()));
			return retVal;
		}

		retVal.add(new ErrorCommand("Error 0100:     syntax error at '"
				+ commandName + "'"));

		return retVal;
	}

	@Override
	public ArrayList<Object> encode(ArrayList<Object> arg) {
		logger.debug("encode() has been called: " + arg);
		ArrayList<Object> retVal = new ArrayList<Object>();

		for (Object o : arg) {
			retVal.add(o + "\n");
		}

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
