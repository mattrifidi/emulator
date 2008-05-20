/**
 * 
 */
package org.rifidi.emulator.reader.epc.commandhandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.epc.command.exception.EPCExceptionHandler;
import org.rifidi.emulator.reader.epc.module.EPCReaderSharedResources;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.source.TagSelector;

/**
 * 
 * 
 * @author Matthew Dean
 */
public class EPCTagSelector {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(EPCTagSelector.class);

	/**
	 * String that matches "Interactive" in the reader xml file.
	 */
	static final String INTERACTIVE_STRING = "Interactive";

	/**
	 * The handlermethod for the TagSelectors
	 */
	static final String TAG_SELECTOR_HANDLER_STRING = "tagselector.handler";

	/**
	 * All of the letters and numbers
	 */
	static final String LETTERS_AND_NUMBERS = "abcdefghijklmnopqrstuvwxyzABCDE"
			+ "FGHIJKLMNOPQRSTUVWXYZ1234567890";

	/**
	 * String representation of this class.
	 */
	static final String TAG_SELECTOR_CLASS = "org.rifidi.emulator.reader"
			+ ".epc.commandhandler.EPCTagSelector";

	/**
	 * 
	 */
	protected HashSet<String> letterSet = new HashSet<String>();

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

		String[] splitCommand = null;
		String arguments = (String) arg.getArguments().get(0);
		splitCommand = arguments.split(",");

		if (splitCommand.length != 5) {
			EPCExceptionHandler exc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(exc.illegalValueError());
			return arg;
		}

		EPCReaderSharedResources share = (EPCReaderSharedResources) asr;
		String name = splitCommand[0];

		boolean checkBool = true;
		char[] nameArray = name.toCharArray();
		for (char i : nameArray) {
			if (!letterSet.contains(String.valueOf(i))) {
				checkBool = false;
			}
		}

		try {
			new BigInteger(splitCommand[2], 16);
			new BigInteger(splitCommand[3], 16);
		} catch (NumberFormatException e) {
			EPCExceptionHandler exc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(exc.illegalValueError());
			return arg;
		}

		if (!share.getSourceMap().containsKey(name)
				&& !share.getTagSelectorMap().containsKey(name) && checkBool) {
			share.addSelector(new TagSelector(splitCommand[0], splitCommand[1],
					splitCommand[2], splitCommand[3], splitCommand[4]));
			share.getCommandsByState(INTERACTIVE_STRING).put(
					name,
					share.getCommandsByState(INTERACTIVE_STRING).get(
							TAG_SELECTOR_HANDLER_STRING));
		} else {
			EPCExceptionHandler exc = (EPCExceptionHandler) asr
					.getExceptionHandler();
			arg.setReturnValue(exc.illegalValueError());
			return arg;
		}

		String returnString = arg.getCurrentQueryName() + EPCCommon.NEWLINE
				+ "Creating new Selector: " + name + EPCCommon.NEWLINE
				+ EPCCommon.NEWLINE + EPCCommon.PROMPT_STRING;

		arg.getReturnValue().clear();
		arg.getReturnValue().add(returnString);
		return arg;
	}

	/**
	 * A generic handler for TagSelector objects.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public CommandObject tagSelectorHandler(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String className = TAG_SELECTOR_CLASS;
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
	 * A generic handler for TagSelector objects.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject getValue(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		EPCReaderSharedResources share = (EPCReaderSharedResources) asr;

		TagSelector selector = share.getTagSelectorMap().get(name);

		EPCCommon.format_echo(arg, asr);

		arg.getReturnValue().add(
				name + ".value = " + selector.getValue() + EPCCommon.NEWLINE
						+ EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);

		return arg;
	}

	/**
	 * A generic handler for TagSelector objects.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject getMask(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		EPCReaderSharedResources share = (EPCReaderSharedResources) asr;

		TagSelector selector = share.getTagSelectorMap().get(name);

		EPCCommon.format_echo(arg, asr);

		arg.getReturnValue().add(
				name + ".mask = " + selector.getMask() + EPCCommon.NEWLINE
						+ EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);

		return arg;
	}

	/**
	 * A generic handler for TagSelector objects.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject getInclusiveFlag(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		EPCReaderSharedResources share = (EPCReaderSharedResources) asr;

		TagSelector selector = share.getTagSelectorMap().get(name);

		EPCCommon.format_echo(arg, asr);

		arg.getReturnValue().add(
				name + ".inclusiveFlag = " + selector.getInclusiveFlag()
						+ EPCCommon.NEWLINE + EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);

		return arg;
	}

	/**
	 * A generic handler for TagSelector objects.
	 * 
	 * @param arg
	 * @param asr
	 * @return
	 */
	public CommandObject getTagField(CommandObject arg,
			AbstractReaderSharedResources asr) {
		String[] nameSplit = arg.getCurrentQueryName().split("\\.");
		String name = nameSplit[0];

		EPCReaderSharedResources share = (EPCReaderSharedResources) asr;

		TagSelector selector = share.getTagSelectorMap().get(name);

		EPCCommon.format_echo(arg, asr);

		arg.getReturnValue().add(
				name + ".tagField = " + selector.getTagField()
						+ EPCCommon.NEWLINE + EPCCommon.NEWLINE);

		EPCCommon.prompt(arg, asr);

		return arg;
	}
}
