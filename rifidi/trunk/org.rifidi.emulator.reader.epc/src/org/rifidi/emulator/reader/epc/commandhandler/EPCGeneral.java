/*
 *  EPCGeneral.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.epc.commandhandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.epc.module.EPCReaderSharedResources;
import org.rifidi.emulator.reader.epc.sharedrc.memory.EPCTagMemory;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * @author matt
 * 
 */
public class EPCGeneral {

	/**
	 * Logger for exception handling
	 */
	private static Log logger = LogFactory.getLog(EPCGeneral.class);

	/**
	 * The name of the reader
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject name(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return EPCCommon.getter_setter(arg, asr);
	}

	/**
	 * The name of the reader
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject emptyMethod(CommandObject arg,
			AbstractReaderSharedResources asr) {
		arg.getReturnValue().clear();
		arg.getReturnValue().add(EPCCommon.NEWLINE + EPCCommon.NEWLINE);
		return EPCCommon.prompt(arg, asr);
	}

	/**
	 * The username of the reader
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject username(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return EPCCommon.getter_setter(arg, asr);
	}

	/**
	 * The password of the reader
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject password(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return EPCCommon.getter_setter(arg, asr);
	}

	/**
	 * The role of the reader
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject role(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return EPCCommon.getter_setter(arg, asr);
	}

	/**
	 * 
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject epc(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return EPCCommon.return_default_value(arg, asr);
	}

	/**
	 * 
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject triggerMaxNumberSupported(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return EPCCommon.return_default_value(arg, asr);
	}

	/**
	 * 
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject selectorMaxNumberSupported(CommandObject arg,
			AbstractReaderSharedResources asr) {
		return EPCCommon.return_default_value(arg, asr);
	}

	/**
	 * 
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject quit(CommandObject arg,
			AbstractReaderSharedResources asr) {
		arg.getReturnValue().clear();
		arg.getReturnValue().add(arg.getDefaultValue() + EPCCommon.NEWLINE);

		EPCReaderSharedResources epcshared = (EPCReaderSharedResources) asr;
		epcshared.getInteractiveConnectionSignal().setControlVariableValue(
				false);
		return arg;
	}

	/**
	 * 
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject read(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("In the read method in EPCGeneral");
		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];
		logger.debug("past the name split, name is: " + name);
		GenericRadio newRadio = asr.getRadio();
		EPCTagMemory memory;
		logger.debug("past the radio thing, size is: "
				+ asr.getSourceMap().get(name).getReadPoints());
		Set<Integer> antennaKeySet = new HashSet<Integer>();

		Integer z;
		for (String x : asr.getSourceMap().get(name).getReadPoints()) {
			logger.debug("antenna is: " + x);
			z = Integer.valueOf(x);
			antennaKeySet.add(z);
		}
		logger.debug("past the for loop");

		memory = (EPCTagMemory) asr.getTagMemory();
		logger.debug("formed the memory");
		newRadio.scan(antennaKeySet, memory);
		logger.debug("scanned");
		List<String> tagList = new ArrayList<String>();

		Collection<RifidiTag> tags = memory.getTagReport();

		for (RifidiTag t : tags) {
			StringBuffer retVal = new StringBuffer();
			retVal.append(ByteAndHexConvertingUtility.toHexString(
					t.getTag().readId()).replace(" ", ""));
			tagList.add(retVal.toString());
		}
		logger.debug("formed the return value");
		arg.getReturnValue().clear();
		EPCCommon.format_echo(arg, asr);

		String retVal = "";

		for (String tag : tagList) {
			retVal += tag + EPCCommon.NEWLINE;
		}

		if (tagList.size() == 0) {
			retVal += "(no tags)" + EPCCommon.NEWLINE;
		}

		arg.getReturnValue().add(retVal);

		arg.getReturnValue().add(EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);
		return arg;
	}
}
