/*
 *  CommandHandlerInvoker.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.commandhandler;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.alien.command.exception.AlienExceptionHandler;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.emulator.reader.alien.sharedrc.tagmemory.AlienTagMemory;
import org.rifidi.emulator.reader.alien.speed.SpeedFilter;
import org.rifidi.emulator.reader.alien.uptime.AlienUptime;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;

/**
 * This is the class that contains general handler methods for the Alien AL9800
 * Reader. <br />
 * <br />
 * It is used for testing the Handler classes and also for simple operations.<br />
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AlienGeneral {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(AlienGeneral.class);

	/**
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject speedFilter(CommandObject arg,
			AbstractReaderSharedResources asr) {
		try {
			AlienReaderSharedResources arsr = (AlienReaderSharedResources) asr;
			String floats = null;

			floats = (String) arg.getArguments().get(0);

			floats = floats.trim();
			String[] splitString = floats.split("\\|");
			ArrayList<Float> floatArray = new ArrayList<Float>();

			for (String i : splitString) {
				// System.out.println("1VALUE SPLITSTRING: " + i);
				i = i.trim();
				String[] splitFloats = i.split(" ");
				for (String f : splitFloats) {
					// System.out.println("2VALUE SPLITSTRING: " + f);
					if (!f.equals("")) {
						floatArray.add(Float.valueOf(f));
					}
				}
			}

			SpeedFilter sf = new SpeedFilter(floatArray
					.toArray(new Float[floatArray.size()]));

			AlienTagMemory atm = (AlienTagMemory) arsr.getTagMemory();
			atm.setSpeedFilter(sf);

		} catch (Exception e) {
			ArrayList<Object> retVal = arg.getReturnValue();
			String cur = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(cur);
			retVal = new AlienExceptionHandler().malformedMessageError(tempVal,
					arg);
			arg.setReturnValue(retVal);
			return arg;
		}

		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject rssiFilter(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * The help file for this reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject help(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<Object> retVal = arg.getReturnValue();

		if (!arg.getPromptSuppress()) {
			retVal.add(arg.getCurrentQueryName());
		}

		if (!arg.getPromptSuppress()) {
			retVal.add(AlienCommon.NEWLINE);
		}

		retVal.add(arg.getDefaultValue());

		retVal.add(AlienCommon.NEWLINE);

		retVal.add(arg.getDefaultValue());
		retVal.add(AlienCommon.ENDOFREPLY);

		if (!arg.getPromptSuppress()) {
			retVal.add(AlienCommon.NONZEROPROMPT);
		}

		return arg;
	}

	/**
	 * Experimental function. Ignore.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject lbt(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	public CommandObject connect(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("Connect message handled");
		return arg;
	}

	/**
	 * Experimental function. Ignore.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject lbtLimit(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Experimental function. Ignore.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject lbtValue(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Experimental function. Ignore.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject acqG2Session(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Experimental function. Ignore.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject acqG2Select(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * The information about this reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject info(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("In the info");
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * This command saves the current values to permenant storage.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject save(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Quit the current session.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject quit(CommandObject arg,
			AbstractReaderSharedResources asr) {

		logger.debug("GOT INTO THE QUIT");
		/* Get the alien control resources */
		AlienReaderSharedResources readerControl = (AlienReaderSharedResources) asr;

		/* Get the connection signal for the reader */
		ControlSignal<Boolean> interactiveConnectionSignal = readerControl
				.getInteractiveConnectionSignal();
		ArrayList<Object> retVal = new ArrayList<Object>();

		if (!arg.getPromptSuppress()) {
			retVal.add(arg.getCurrentQueryName());
			retVal.add(AlienCommon.NEWLINE);
		}
		retVal.add(arg.getDefaultValue());
		retVal.add(AlienCommon.ENDOFREPLY);
		logger.debug("JUST before setReturnValue: " + retVal);

		arg.setReturnValue(retVal);
		/* Get the goodbye message */
		/* now set the power to off thereby turning off the reader */
		interactiveConnectionSignal.setControlVariableValue(false);

		return arg;
	}

	/**
	 * Gets the current uptime for the reader.
	 * 
	 * TODO: This uptime isn't correct, we need to find some way to make it
	 * correct.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject uptime(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ReaderProperty rp = asr.getPropertyMap().get(
				arg.getDisplayName().toLowerCase());
		System.out.println("The readerProperty: " + rp);
		rp.setPropertyValue(String.valueOf(AlienUptime.getInstance()
				.getUptimeInSeconds()));
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Set all values to default.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject factorySettings(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<ReaderProperty> commandList = new ArrayList<ReaderProperty>(
				asr.getPropertyMap().values());
		for (ReaderProperty prop : commandList) {
			prop.reset();
		}

		ArrayList<Object> retVal = new ArrayList<Object>();

		if (!arg.getPromptSuppress()) {
			retVal.add(AlienCommon.NEWLINE);
		}
		retVal.add(AlienCommon.ENDOFREPLY);
		if (!arg.getPromptSuppress()) {
			retVal.add(AlienCommon.NONZEROPROMPT);
		}

		arg.setReturnValue(retVal);
		return arg;
	}

	/**
	 * Reboot the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject reboot(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return arg;
	}

	/**
	 * Pings another object.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject ping(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return arg;
	}

	/**
	 * Returns the name of the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject readerName(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Returns the type of the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject readerType(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Returns the version of the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject readerVersion(CommandObject arg,
			AbstractReaderSharedResources asr) {

		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Returns the number of the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject readerNumber(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Returns the text for an error. This is a custom method with hardcoded
	 * error values. In the future it will read from an error text file.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject error(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * This is the handler that gets called if no command is entered. It simply
	 * outputs a newline, end of reply, and a prompt.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject emptyMethod(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<Object> argList = new ArrayList<Object>();
		if (!arg.getPromptSuppress()) {
			argList.add(AlienCommon.NEWLINE + AlienCommon.ENDOFREPLY
					+ AlienCommon.NONZEROPROMPT);
		} else {
			argList.add(AlienCommon.ZEROCHAR);
		}
		arg.setReturnValue(argList);
		return arg;
	}

	/**
	 * Doesn't actually do anything.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject function(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!arg.getArguments().isEmpty()) {
			String function = ((String) arg.getArguments().get(0));
			String validValues = "programmer|reader";
			if (!validValues.contains(function.toLowerCase())) {
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				ArrayList<String> PossibleValues = new ArrayList<String>();
				PossibleValues.add("Programmer");
				PossibleValues.add("Reader");
				ArrayList<Object> retVal = new AlienExceptionHandler().error10(
						tempVal, arg, PossibleValues);
				arg.setReturnValue(retVal);
				return arg;
			}
		}

		return AlienCommon.getter_setter(arg, asr);
	}

}
