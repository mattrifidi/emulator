/*
 *  AlienAutonomous.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.commandhandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.extra.CommandInformation;
import org.rifidi.emulator.extra.ExtraInformation;
import org.rifidi.emulator.extra.TCPExtraInformation;
import org.rifidi.emulator.reader.alien.command.exception.AlienExceptionHandler;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.emulator.reader.alien.thread.AutoModeStarterThread;
import org.rifidi.emulator.reader.alien.thread.TCPClientStarterThread;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;

/**
 * This class contains the autonomous calls that the system can have.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AlienAutonomous {

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
	public CommandObject notifyAddress(CommandObject arg,
			AbstractReaderSharedResources asr) {

		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;

		if (arg.getArguments().size() > 0) {
			String ip = (String) arg.getArguments().get(0);
			if (ip.contains(":")) {
				String[] splitString = ip.split(":");
				logger.debug("splitstring length is: " + splitString.length);
				try {
					logger.debug("splitstring[0]: " + splitString[0]);
					logger.debug("splitstring[1]: " + splitString[1]);
					InetAddress newAddress = InetAddress
							.getByName(splitString[0]);

					ExtraInformation extraInfo = new TCPExtraInformation(
							newAddress, Integer.parseInt(splitString[1]));

					aliensr.getAutoCommCommunicationPower()
							.setExtraInformation(extraInfo);
				} catch (UnknownHostException e) {
					logger.error("Bad address: " + e.getMessage());
				}
			}
		}

		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Set the format the autonomous commands will be formatted in.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified.
	 */
	public CommandObject notifyFormat(CommandObject arg,
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
	public CommandObject notifyTime(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (arg.getArguments().size() > 0) {

			AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;
			Integer arg1 = Integer.valueOf((String) arg.getArguments().get(0));
			arg.getArguments().clear();
			arg.getArguments().add(arg1.toString());

			CommandObject retVal = AlienCommon.getter_setter(arg, asr);

			String notifyMode = aliensr.getPropertyMap().get("notifymode")
					.getPropertyStringValue();
			if ((arg1 > 0) && notifyMode.equalsIgnoreCase("on")) {
				aliensr.getAutoStateController().getNotifyController()
						.getNotifyTimer().startNotifyTimer();
			} else {
				aliensr.getAutoStateController().getNotifyController()
						.getNotifyTimer().stopNotifyTimer();
			}
			return retVal;

		} else {

			return AlienCommon.getter_setter(arg, asr);
		}
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
	public CommandObject notifyTrigger(CommandObject arg,
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
	public CommandObject notifyRetryPause(CommandObject arg,
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
	public CommandObject notifyRetryCount(CommandObject arg,
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
	public CommandObject notifyHeader(CommandObject arg,
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
	public CommandObject notifyKeepAliveTime(CommandObject arg,
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
	public CommandObject mailFrom(CommandObject arg,
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
	public CommandObject mailServer(CommandObject arg,
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
	public CommandObject notifyMode(CommandObject arg,
			AbstractReaderSharedResources asr) {

		AlienReaderSharedResources alienres = (AlienReaderSharedResources) asr;

		HashMap<byte[], Integer> commandMap = new HashMap<byte[], Integer>();

		commandMap.put("autotaglist".getBytes(), -1);

		ExtraInformation extraInfo = new CommandInformation(commandMap, 150,
				CommandInformation.LimitedRunningState.NOT_LIMITED);

		alienres.getNotifyControlSignal().setExtraInformation(extraInfo);

		ControlSignal<Boolean> notifyCS = alienres.getNotifyControlSignal();
		ControlSignal<Boolean> notifyConn = alienres
				.getAutoCommConnectionSignal();
		ControlSignal<Boolean> autoCommPower = alienres
				.getAutoCommCommunicationPower();

		// if valid argument
		if (arg.getArguments().size() > 0) {
			String argument = (String) arg.getArguments().get(0);
			if (argument.equalsIgnoreCase("on")
					|| argument.equalsIgnoreCase("off")) {
				// if auto is on

				logger.debug("NotifyControlSignal is "
						+ alienres.getNotifyControlSignal()
								.getControlVariableValue());
				logger.debug("Auto Comm power is "
						+ alienres.getAutoCommCommunicationPower()
								.getControlVariableValue());
				logger.debug("NotifyConnecitonSignal is "
						+ alienres.getAutoCommConnectionSignal()
								.getControlVariableValue());

				if (!notifyConn.getControlVariableValue()) {
					notifyCS.setControlVariableValue(false);
				}

				if (argument.equalsIgnoreCase("on")
						&& !notifyCS.getControlVariableValue()) {
					// notifyCS.setControlVariableValue(true);
					// autoCommPower.setControlVariableValue(true);
					// New-fangled starter thread works great
					TCPClientStarterThread newThread = new TCPClientStarterThread(
							notifyCS, autoCommPower);
					newThread.start();
					String notifyTime = alienres.getPropertyMap().get(
							"notifytime").getPropertyStringValue();
					int time = Integer.parseInt(notifyTime);
					if (time > 0) {
						alienres.getAutoStateController().getNotifyController()
								.getNotifyTimer().startNotifyTimer();
					}
				}

				else if (argument.equalsIgnoreCase("off")
						&& notifyCS.getControlVariableValue()) {
					notifyCS.setControlVariableValue(false);
					notifyConn.setControlVariableValue(false);
					alienres.getAutoStateController().getNotifyController()
							.getNotifyTimer().stopNotifyTimer();
				}
			}
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Switch autonomous modes on and off.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoMode(CommandObject arg,
			AbstractReaderSharedResources asr) {

		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;

		if (arg.getArguments().size() > 0) {
			String temp = (String) arg.getArguments().get(0);
			if (temp.equalsIgnoreCase("on")) {
				// aliensr.getAutoStateController().startAutoMode();
				AutoModeStarterThread newAutoThread = new AutoModeStarterThread(
						aliensr);
				newAutoThread.start();
			} else if (temp.equalsIgnoreCase("off")) {
				aliensr.getAutoStateController().stopAutoMode();
			} else {
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				ArrayList<String> PossibleValues = new ArrayList<String>();
				PossibleValues.add("on");
				PossibleValues.add("off");
				ArrayList<Object> retVal = new AlienExceptionHandler().error10(
						tempVal, arg, PossibleValues);
				arg.setReturnValue(retVal);
				return arg;
			}

		}

		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Specify the value of the output pins while in wait mode.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoWaitOutput(CommandObject arg,
			AbstractReaderSharedResources asr) {
		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;
		if (arg.getArguments().size() > 0) {
			String temp = (String) arg.getArguments().get(0);
			String bitMap = temp.trim();
			try {
				int bm = Integer.parseInt(bitMap);
				aliensr.getAutoWaitOutput().setListeningPorts(bm);
			} catch (NumberFormatException ex) {
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				ArrayList<String> PossibleValues = new ArrayList<String>();
				PossibleValues.add("0-15");
				ArrayList<Object> retVal = new AlienExceptionHandler().error10(
						tempVal, arg, PossibleValues);
				arg.setReturnValue(retVal);
				return arg;
			}
		}

		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Get and set the trigger that sends the autoMode state into working state.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoStartTrigger(CommandObject arg,
			AbstractReaderSharedResources asr) {

		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;

		if (arg.getArguments().size() > 0) {
			String temp = (String) arg.getArguments().get(0);
			String[] arguments = temp.split(",");
			ArrayList<Integer> edges = new ArrayList<Integer>(2);
			for (String bitMap : arguments) {
				bitMap = bitMap.trim();
				try {
					edges.add((Integer.parseInt(bitMap)));
				} catch (NumberFormatException ex) {
					String cur = arg.getCurrentQueryName();
					ArrayList<Object> tempVal = new ArrayList<Object>();
					tempVal.add(cur);
					ArrayList<String> PossibleValues = new ArrayList<String>();
					PossibleValues.add("0-15");
					ArrayList<Object> retVal = new AlienExceptionHandler()
							.error10(tempVal, arg, PossibleValues);
					arg.setReturnValue(retVal);
					return arg;
				}
			}
			aliensr.getAutoStartTrigger().setListeningPorts(edges.get(0),
					edges.get(1));

			String s = edges.get(0) + " " + edges.get(1);
			arg.getArguments().set(0, s);
		}

		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Get and set the time to wait after recieving a start trigger before
	 * starting auto mode.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoStartPause(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Specify the value of the output pins while in work mode.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoWorkOutput(CommandObject arg,
			AbstractReaderSharedResources asr) {
		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;
		if (arg.getArguments().size() > 0) {
			String temp = (String) arg.getArguments().get(0);
			String bitMap = temp.trim();
			try {
				int bm = Integer.parseInt(bitMap);
				aliensr.getAutoWorkOutput().setListeningPorts(bm);
			} catch (NumberFormatException ex) {
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				ArrayList<String> PossibleValues = new ArrayList<String>();
				PossibleValues.add("0-15");
				ArrayList<Object> retVal = new AlienExceptionHandler().error10(
						tempVal, arg, PossibleValues);
				arg.setReturnValue(retVal);
				return arg;
			}
		}

		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Set and get the external trigger that will move the auto mode state from
	 * work mode to evaluate mode.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoStopTrigger(CommandObject arg,
			AbstractReaderSharedResources asr) {

		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;

		if (arg.getArguments().size() > 0) {
			String temp = (String) arg.getArguments().get(0);
			String[] arguments = temp.split(",");
			ArrayList<Integer> edges = new ArrayList<Integer>(2);
			for (String bitMap : arguments) {
				bitMap = bitMap.trim();
				try {
					edges.add((Integer.parseInt(bitMap)));
				} catch (NumberFormatException ex) {
					String cur = arg.getCurrentQueryName();
					ArrayList<Object> tempVal = new ArrayList<Object>();
					tempVal.add(cur);
					ArrayList<String> PossibleValues = new ArrayList<String>();
					PossibleValues.add("0-15");
					ArrayList<Object> retVal = new AlienExceptionHandler()
							.error10(tempVal, arg, PossibleValues);
					arg.setReturnValue(retVal);
					return arg;
				}
			}
			aliensr.getAutoStopTrigger().setListeningPorts(edges.get(0),
					edges.get(1));

			String s = edges.get(0) + " " + edges.get(1);
			arg.getArguments().set(0, s);
		}

		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Get and set the action to perform in auto mode.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoAction(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Set and get the timer that will move the auto mode state from work mode
	 * to evaluate mode.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoStopTimer(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Set and get the pause after a true output for an autonomous command.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoTrueOutput(CommandObject arg,
			AbstractReaderSharedResources asr) {
		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;

		if (arg.getArguments().size() > 0) {
			String temp = (String) arg.getArguments().get(0);
			String bitMap = temp.trim();
			try {
				int bm = Integer.parseInt(bitMap);
				aliensr.getAutoTrueOutput().setListeningPorts(bm);
			} catch (NumberFormatException ex) {
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				ArrayList<String> PossibleValues = new ArrayList<String>();
				PossibleValues.add("0-15");
				ArrayList<Object> retVal = new AlienExceptionHandler().error10(
						tempVal, arg, PossibleValues);
				arg.setReturnValue(retVal);
				return arg;
			}
		}

		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Set and get the pause time after a true condition from the auto mode
	 * evaluation.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoTruePause(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Set and get pause time after autoMode returns a false condition.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoFalsePause(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Specify the values of the output pins.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoFalseOutput(CommandObject arg,
			AbstractReaderSharedResources asr) {
		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;

		if (arg.getArguments().size() > 0) {
			String temp = (String) arg.getArguments().get(0);
			String bitMap = temp.trim();
			try {
				int bm = Integer.parseInt(bitMap);
				aliensr.getAutoFalseOutput().setListeningPorts(bm);
			} catch (NumberFormatException ex) {
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				ArrayList<String> PossibleValues = new ArrayList<String>();
				PossibleValues.add("0-15");
				ArrayList<Object> retVal = new AlienExceptionHandler().error10(
						tempVal, arg, PossibleValues);
				arg.setReturnValue(retVal);
				return arg;
			}
		}

		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Force a trigger event to occur.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoModeTriggerNow(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Reset the autonomous mode values.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoModeReset(CommandObject arg,
			AbstractReaderSharedResources asr) {
		ArrayList<ReaderProperty> commandList = new ArrayList<ReaderProperty>(
				asr.getPropertyMap().values());
		for (@SuppressWarnings("unused")
		ReaderProperty prop : commandList) {
			// if(prop.getClass().equals(AlienReaderSharedAutonomousProperties.class))
			// {
			// prop.reset();
			// }
		}
		arg.getReturnValue().add(
				"All auto-mode settings have been reset!"
						+ AlienCommon.ENDOFREPLY);
		return (arg);
	}

	/**
	 * Get the status of the autoMode.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject autoModeStatus(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

}
