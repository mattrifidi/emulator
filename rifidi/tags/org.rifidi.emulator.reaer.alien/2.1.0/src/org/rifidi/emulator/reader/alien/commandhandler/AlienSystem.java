/*
 *  AlienSystem.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.alien.commandhandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;

/**
 * This class contains many system and network calls that the reader has.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AlienSystem {
	
	@SuppressWarnings("unused")
	private static Log logger =
		 LogFactory.getLog(AlienSystem.class);

	/**
	 * 
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject macAddress(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject dhcp(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject ipAddress(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject gateway(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject netmask(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject dns(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject networkTimeout(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject commandPort(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject hostname(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject upgradeAddress(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject upgradePort(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject networkUpgrade(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject ping(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject username(CommandObject arg,
			AbstractReaderSharedResources asr) {
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
	public CommandObject password(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Resets the values for the network mode.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject networkReset(CommandObject arg,
			AbstractReaderSharedResources asr) {
		for (@SuppressWarnings("unused")
		ReaderProperty prop : asr.getPropertyMap().values()) {
			// if(prop.getClass().equals(AlienReaderSharedNetworkProperties.class))
			// {
			// prop.reset();
			// }
		}

		return (arg);
	}

}
