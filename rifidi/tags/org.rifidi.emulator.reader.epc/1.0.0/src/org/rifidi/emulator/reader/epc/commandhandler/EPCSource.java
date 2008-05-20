/*
 *  EPCSource.java
 *
 *  Project:		RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.epc.commandhandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.epc.command.exception.EPCExceptionHandler;
import org.rifidi.emulator.reader.epc.module.EPCReaderSharedResources;
import org.rifidi.emulator.reader.epc.sharedrc.memory.EPCTagMemory;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.source.ReaderSource;
import org.rifidi.emulator.reader.source.TagSelector;
import org.rifidi.emulator.tags.impl.RifidiTag;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class EPCSource {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(EPCSource.class);

	/**
	 * String that matches "Interactive" in the reader xml file.
	 */
	static final String INTERACTIVE_STRING = "Interactive";

	/**
	 * 
	 */
	static final String SOURCE_HANDLER_STRING = "source.handler";

	/**
	 * 
	 */
	static final String SOURCE_CLASS = "org.rifidi.emulator.reader"
			+ ".epc.commandhandler.EPCSource";

	/**
	 * 
	 */
	static final String LETTERS_AND_NUMBERS = "abcdefghijklmnopqrstuvwxyzABCDE"
			+ "FGHIJKLMNOPQRSTUVWXYZ1234567890";

	/**
	 * 
	 */
	protected HashSet<String> letterSet = new HashSet<String>();

	/**
	 * Creates a new Source object and adds it to the master list.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject sourceCreate(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (this.letterSet.size() == 0) {
			char[] letters = LETTERS_AND_NUMBERS.toCharArray();
			for (char i : letters) {
				letterSet.add(String.valueOf(i));
			}
		}

		EPCReaderSharedResources share = (EPCReaderSharedResources) asr;
		String name = (String) arg.getArguments().get(0);

		boolean checkBool = true;
		char[] nameArray = name.toCharArray();
		for (char i : nameArray) {
			if (!letterSet.contains(String.valueOf(i))) {
				checkBool = false;
			}
		}

		if (!share.getSourceMap().containsKey(name)
				&& !share.getTagSelectorMap().containsKey(name) && checkBool) {
			share.addSource(new ReaderSource(name));
			logger.debug("creating command: "
					+ name
					+ ", "
					+ share.getCommandsByState(INTERACTIVE_STRING).get(
							SOURCE_HANDLER_STRING));
			share.getCommandsByState(INTERACTIVE_STRING).put(
					name,
					share.getCommandsByState(INTERACTIVE_STRING).get(
							SOURCE_HANDLER_STRING));
		} else {
			EPCExceptionHandler exc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(exc.illegalValueError());
			return arg;
		}

		String returnString = arg.getCurrentQueryName() + EPCCommon.NEWLINE
				+ "Creating new Source: " + name + EPCCommon.NEWLINE
				+ EPCCommon.NEWLINE + EPCCommon.PROMPT_STRING;

		arg.getReturnValue().clear();
		arg.getReturnValue().add(returnString);
		return arg;
	}

	/**
	 * Creates a new TagSelector object and adds it to the master list.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject tagSelectorCreate(CommandObject arg,
			AbstractReaderSharedResources asr) {
		if (this.letterSet.size() == 0) {
			char[] letters = LETTERS_AND_NUMBERS.toCharArray();
			for (char i : letters) {
				letterSet.add(String.valueOf(i));
			}
		}

		EPCReaderSharedResources share = (EPCReaderSharedResources) asr;
		String name = (String) arg.getArguments().get(0);

		boolean checkBool = true;
		char[] nameArray = name.toCharArray();
		for (char i : nameArray) {
			if (!letterSet.contains(String.valueOf(i))) {
				checkBool = false;
			}
		}

		if (!share.getSourceMap().containsKey(name)
				&& !share.getTagSelectorMap().containsKey(name) && checkBool) {
			share.addSource(new ReaderSource(name));
			logger.debug("creating command: "
					+ name
					+ ", "
					+ share.getCommandsByState(INTERACTIVE_STRING).get(
							SOURCE_HANDLER_STRING));
			share.getCommandsByState(INTERACTIVE_STRING).put(
					name,
					share.getCommandsByState(INTERACTIVE_STRING).get(
							SOURCE_HANDLER_STRING));
		} else {
			EPCExceptionHandler exc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(exc.illegalValueError());
			return arg;
		}

		String returnString = arg.getCurrentQueryName() + EPCCommon.NEWLINE
				+ "Creating new Source: " + name + EPCCommon.NEWLINE
				+ EPCCommon.NEWLINE + EPCCommon.PROMPT_STRING;

		arg.getReturnValue().clear();
		arg.getReturnValue().add(returnString);
		return arg;
	}

	/**
	 * A generic handler for source objects.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public CommandObject sourceHandler(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String className = SOURCE_CLASS;
		String[] splitCommand = arg.getCurrentQueryName().split("=");
		String[] splitCommand2 = splitCommand[0].split("\\.");
		String methodName = splitCommand2[1].trim();
		logger.debug(methodName
				+ " is the method about to be called, from class " + className);

		Object[] arguments = new Object[] { arg, asr };
		Class[] parameter = new Class[] { CommandObject.class,
				AbstractReaderSharedResources.class };
		Method newMethod;
		Class c;

		CommandObject retVal = null;

		try {
			c = Class.forName(className);
			newMethod = c.getMethod(methodName, parameter);
			Object instance = null;

			instance = c.newInstance();

			retVal = (CommandObject) newMethod.invoke(instance, arguments);

		} catch (ClassNotFoundException e) {
			EPCExceptionHandler epc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(epc.commandNotFoundError(null, null));
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			EPCExceptionHandler epc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(epc.commandNotFoundError(null, null));
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			EPCExceptionHandler epc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(epc.commandNotFoundError(null, null));
			e.printStackTrace();
		} catch (InstantiationException e) {
			EPCExceptionHandler epc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(epc.invalidCommandError(null, null, null));
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			EPCExceptionHandler epc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(epc.illegalValueError());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			EPCExceptionHandler epc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(epc.unknownError());
			e.printStackTrace();
		}
		if (retVal == null) {
			return arg;
		}
		return retVal;
	}

	/**
	 * Add read points to a given source.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject addReadPoints(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] splitCommand;
		String arguments = (String) arg.getArguments().get(0);
		splitCommand = arguments.split(",");

		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		if (splitCommand.length == 0) {
			EPCExceptionHandler epc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(epc.parameterMissing());
			return arg;
		}

		arg.getReturnValue().clear();
		EPCCommon.format_echo(arg, asr);

		for (String i : splitCommand) {
			asr.getSourceMap().get(name).addReadPoint(i);
			arg.getReturnValue().add(
					"Adding read point: " + i + EPCCommon.NEWLINE);
		}

		arg.getReturnValue().add(EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);
		return arg;
	}

	/**
	 * Add read points to a given source.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject isFixed(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		arg.getReturnValue().clear();
		EPCCommon.format_echo(arg, asr);

		arg.getReturnValue().add(name + ".isFixed = FALSE");

		arg.getReturnValue().add(EPCCommon.NEWLINE + EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);
		return arg;
	}

	/**
	 * Add read points to a given source.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject removeReadPoints(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] splitCommand;
		String arguments = (String) arg.getArguments().get(0);
		splitCommand = arguments.split(",");

		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		arg.getReturnValue().clear();
		EPCCommon.format_echo(arg, asr);

		for (String i : splitCommand) {
			asr.getSourceMap().get(name).removeReadPoint(i);
			arg.getReturnValue().add(
					"Removing read point: " + i + EPCCommon.NEWLINE);
		}

		arg.getReturnValue().add(EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);
		return arg;
	}

	/**
	 * Remove all read points from a given source.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject removeAllReadPoints(CommandObject arg,
			AbstractReaderSharedResources asr) {

		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		arg.getReturnValue().clear();
		EPCCommon.format_echo(arg, asr);

		asr.getSourceMap().get(name).removeAllReadPoints();

		arg.getReturnValue().add("All read points removed" + EPCCommon.NEWLINE);

		arg.getReturnValue().add(EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);
		return arg;
	}

	/**
	 * Adds tag selectors with given names.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject addTagSelectors(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] splitCommand;
		String arguments = (String) arg.getArguments().get(0);
		splitCommand = arguments.split(",");

		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		EPCReaderSharedResources share = (EPCReaderSharedResources) asr;

		ArrayList<TagSelector> selectorList = new ArrayList<TagSelector>();
		for (String i : splitCommand) {
			if (share.getTagSelectorMap().containsKey(i)) {
				// If the Source Map already contains the key, the key is not
				// added a second time, and
				// instead nothing happpens.
				if (!asr.getSourceMap().get(name).getTagSelectors()
						.containsKey(i)) {
					selectorList.add(share.getTagSelectorMap().get(i));
				}
			} else {
				EPCExceptionHandler exc = (EPCExceptionHandler) share
						.getExceptionHandler();
				arg.getReturnValue().addAll(exc.illegalValueError());
				return arg;
			}
		}

		asr.getSourceMap().get(name).addTagSelectors(selectorList);

		EPCCommon.format_echo(arg, asr);

		for (TagSelector i : selectorList) {
			arg.getReturnValue().add(
					"Adding TagSelector to " + name + ": " + i.getName()
							+ EPCCommon.NEWLINE);
		}

		arg.getReturnValue().add(EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);

		return arg;
	}

	/**
	 * Adds tag selectors with given names.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject removeTagSelectors(CommandObject arg,
			AbstractReaderSharedResources asr) {

		String[] splitCommand;
		String arguments = (String) arg.getArguments().get(0);
		splitCommand = arguments.split(",");

		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		ArrayList<String> selectorList = new ArrayList<String>();
		for (String i : splitCommand) {
			selectorList.add(i);
		}

		asr.getSourceMap().get(name).removeTagSelectors(selectorList);

		EPCCommon.format_echo(arg, asr);

		for (String i : selectorList) {
			arg.getReturnValue().add(
					"Adding TagSelector to " + name + ": " + i
							+ EPCCommon.NEWLINE);
		}

		arg.getReturnValue().add(EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);

		return arg;
	}

	/**
	 * Gets a list of tags
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject rawReadIDs(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		GenericRadio newRadio =  asr.getRadio();
		EPCTagMemory memory;

		Set<Integer> antennaKeySet = new HashSet<Integer>();

		Integer z;
		for (String x : asr.getSourceMap().get(name).getReadPoints()) {
			z = Integer.valueOf(x);
			antennaKeySet.add(z);
		}

		memory = (EPCTagMemory) asr.getTagMemory();
		newRadio.scan(antennaKeySet, memory);

		List<String> tagList = new ArrayList<String>();

		Collection<RifidiTag> tags = memory.getTagReport();

		for (RifidiTag t : tags) {
			StringBuffer retVal = new StringBuffer();
			retVal.append(ByteAndHexConvertingUtility.toHexString(
					t.getTag().readId()).replace(" ", ""));
			tagList.add(retVal.toString());
		}

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

	/**
	 * Gets a list of tags using the TagSelectors.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject readIDs(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		GenericRadio newRadio =  asr.getRadio();
		EPCTagMemory memory;

		Set<Integer> antennaKeySet = new HashSet<Integer>();

		Integer z;
		for (String x : asr.getSourceMap().get(name).getReadPoints()) {
			z = Integer.valueOf(x);
			antennaKeySet.add(z);
		}

		memory = (EPCTagMemory) asr.getTagMemory();
		newRadio.scan(antennaKeySet, memory);
		Collection<RifidiTag> tagList = memory.getTagReport();

		Set<String> tagListSet = new HashSet<String>();

		for (RifidiTag t : tagList) {
			tagListSet.add(ByteAndHexConvertingUtility.toHexString(
					t.getTag().readId()).replace(" ", ""));
		}

		for (TagSelector selector : asr.getSourceMap().get(name)
				.getTagSelectors().values()) {
			ArrayList<String> removeString = new ArrayList<String>();
			for (String tag : tagListSet) {
				BigInteger mask = new BigInteger(selector.getMask(), 16);
				BigInteger value = new BigInteger(selector.getValue(), 16);
				BigInteger tagBig = new BigInteger(tag, 16);
				if (selector.getInclusiveFlag()) {
					if (!tagBig.and(mask).equals(tagBig.and(value))) {
						removeString.add(tag);
					}
				} else {
					if (tagBig.and(mask).equals(tagBig.and(value))) {
						removeString.add(tag);
					}
				}
			}
			for (String i : removeString) {
				tagListSet.remove(i);
			}
		}

		arg.getReturnValue().clear();
		EPCCommon.format_echo(arg, asr);

		String retVal = "";

		for (String tag : tagListSet) {
			retVal += tag + EPCCommon.NEWLINE;
		}

		if (tagListSet.size() == 0) {
			retVal += "(no tags)" + EPCCommon.NEWLINE;
		}

		arg.getReturnValue().add(retVal);

		arg.getReturnValue().add(EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);
		return arg;
	}

}
