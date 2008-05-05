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

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.io.comm.ip.udp.UDPCommunicationIncomingMessageHandler;
import org.rifidi.emulator.reader.alien.gpio.GPOController;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;
import org.rifidi.emulator.reader.sharedrc.properties.ReaderProperty;

/**
 * This class contains many system and network calls that the reader has.
 * 
 * @author Matthew Dean
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class AlienSystem {
	private static Log logger =
		 LogFactory.getLog(AlienSystem.class);
	
	/**
	 * Get/set the external output pin values.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject externalOutput(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if(arg.getArguments().size()>0){
			ArrayList<Integer> selectedPorts = new ArrayList<Integer>();
			ArrayList<Integer> unselectedPorts = new ArrayList<Integer>();
			String num = (String)arg.getArguments().get(0);
			int bitMap = Integer.parseInt(num);
			GPOController.setPorts(selectedPorts, unselectedPorts, bitMap);
			
			for ( Integer i: selectedPorts){
				asr.getGpioController().setGPOHight(i);
			}
			for ( Integer i: unselectedPorts){
				asr.getGpioController().setGPOLow(i);
			}
			
		}
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Get/set the external output state.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject initExternalOutput(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return (AlienCommon.getter_setter(arg, asr));
	}

	/**
	 * Turn on/off the inversion of the external ouput pins.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject invertExternalOutput(CommandObject arg,
			AbstractReaderSharedResources asr) {
		
		int num = 0;
		for (int x = 0; x < asr.getGpioController().getNumGPOPorts(); x++ ){
			if ( asr.getGpioController().getGPOState(x) ) {
				asr.getGpioController().setGPOLow(x);
			} else {
				asr.getGpioController().setGPOHight(x);
				num += Math.pow(2,x);
			}
		}
		arg.setDisplayName("externaloutput");
		arg.getArguments().add(Integer.toString(num));
		logger.debug(num);
		arg.setCurrentQueryName("set externaloutput");
		
		return AlienCommon.getter_setter(arg, asr);
	}

	/**
	 * Turn on/off the inversion of external input pins.
	 * 
	 * @param arg
	 *            The CommandObject which contains the information from the
	 *            method.
	 * @return The CommandObject, unmodified if the command was a get, modified
	 *         if it was a set.
	 */
	public CommandObject invertExternalInput(CommandObject arg,
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
	public CommandObject getExternalInput(CommandObject arg,
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
