/*
 *  AlienHeartbeat.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.commandhandler;

import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * @author Matthew Dean
 * 
 */
public class AlienHeartbeat {
	/**
	 * Method for returning the heartbeat time.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject heartBeatTime(CommandObject arg,
			AbstractReaderSharedResources asr) {
		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;
		try {
			aliensr.getHearbeatController().updatePeriod(
					Integer.parseInt((String) arg.getArguments().get(0)));
		} catch (Exception e) {
			// Do nothing, it will be handled in the return value.
		}
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Method for returning the heartbeat message.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject heartBeatAddress(CommandObject arg,
			AbstractReaderSharedResources asr) {
		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;
		try {
			aliensr.getHearbeatController().setHeartbeatIP(
					(String) arg.getArguments().get(0));
		} catch (Exception e) {
			// Do nothing, it will be handled in the return value.
		}
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Method for returning the heartbeat message.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject heartBeatCount(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Method for returning the heartbeat message.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if the command is a set.
	 */
	public CommandObject heartBeatPort(CommandObject arg,
			AbstractReaderSharedResources asr) {
		AlienReaderSharedResources aliensr = (AlienReaderSharedResources) asr;
		try {
			aliensr.getHearbeatController().setHeatbeatPort(
					Integer.parseInt((String) arg.getArguments().get(0)));
		} catch (Exception e) {
			// Do nothing, it will be handled in the return value.
		}
		return AlienCommon.getter_setter(arg, asr);
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
	public CommandObject heartbeatAddress(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

}
