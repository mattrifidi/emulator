/*
 *  AlienRadio.java
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
import java.util.HashSet;

import org.rifidi.emulator.reader.alien.command.exception.AlienExceptionHandler;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * This class contains all of the Radio handler methods for the Alien ALR9800.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AlienRadio {
	/**
	 * When the reader is called upon to read a tag it does so using the current
	 * AcquireMode.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acquireMode(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Sets the number of acquire cycles for a Gen 1 tag.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqCycles(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Sets the number of acquire count for a Gen 1 tag.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqCount(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqEnterWakeCount(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqExitWakeCount(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqSleepCount(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqC1EnterWakeCount(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqC1ExitWakeCount(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqC1Cycles(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqC1Count(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqC1SleepCount(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqG2Count(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqG2Cycles(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqG2Q(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqC0Cycles(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject acqC0Count(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject maxAntenna(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * The order the antennas will be called in.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject antennaSequence(CommandObject arg,
			AbstractReaderSharedResources asr) {
		// TODO: this can use any combination of both commas and strings, might
		// want to
		// think of a good way to do this
		if (!arg.getArguments().isEmpty()) {
			String temp = (String) arg.getArguments().get(0);
			temp = temp.trim();
			String[] splitString = null;
			if (temp.contains(",")) {
				splitString = (temp).split(",");
			} else {
				splitString = (temp).split(" ");
			}
			ArrayList<Integer> antennaList = new ArrayList<Integer>();
			antennaList.add(0);
			antennaList.add(1);
			antennaList.add(2);
			antennaList.add(3);
			for (String i : splitString) {
				try {
					System.out.println("Parsing an int: " + i);
					int antenna = Integer.parseInt(i);
					if (!antennaList.contains(antenna)) {
						String cur = arg.getCurrentQueryName();
						ArrayList<Object> tempVal = new ArrayList<Object>();
						tempVal.add(cur);
						ArrayList<Object> retVal = new AlienExceptionHandler()
								.antennaOutOfRangeError(tempVal, arg);
						arg.setReturnValue(retVal);
						return arg;
					}
				} catch (NumberFormatException e) {
					String cur = arg.getCurrentQueryName();
					ArrayList<Object> tempVal = new ArrayList<Object>();
					tempVal.add(cur);
					ArrayList<Object> retVal = new AlienExceptionHandler()
							.malformedMessageError(tempVal, arg);
					arg.setReturnValue(retVal);
					return arg;
				}
			}
			// need to convert string to be deliminated by space instead of
			// commas.
			String antennaString = ((String) arg.getArguments().get(0))
					.replaceAll(",", " ");
			arg.getArguments().set(0, antennaString);

			HashSet<Integer> antennasToScan = new HashSet<Integer>();

			for (String ant : splitString) {
				antennasToScan.add(new Integer(ant));
			}

			asr.getRadio().setAntennasToScan(antennasToScan);
		}

		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Sets the mask for the reader.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject mask(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * The attenuation of the antenna.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject rfattenuation(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * The power level to set the antenna at.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject rfLevel(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (!AlienCommon.checkIntegerArg(arg)) {
			ArrayList<Object> retVal;
			String i = arg.getCurrentQueryName();
			ArrayList<Object> tempVal = new ArrayList<Object>();
			tempVal.add(i);
			retVal = new AlienExceptionHandler().invalidCommandError(tempVal,
					"Integer", arg);
			arg.setReturnValue(retVal);
			return arg;
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * The modulation to set the antenna to.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject rfModulation(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

}
