/**
 * 
 */
package org.rifidi.emulator.reader.alien.commandhandler;

import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.command.exception.AlienExceptionHandler;
import org.rifidi.emulator.reader.alien.gpio.GPOController;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * @author kyle
 * 
 */
public class AlienExternalIO {

	private static Log logger = LogFactory.getLog(AlienExternalIO.class);

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
		int numGPIPorts = asr.getGpioController().getNumGPIPorts();
		BigInteger bitMap = new BigInteger("0");
		for (int i = 0; i < numGPIPorts; i++) {
			if (asr.getGpioController().getGPIState(i)) {
				bitMap = bitMap.setBit(i);
			}
		}
		arg.getArguments().add(Integer.toString(bitMap.intValue()));
		return (AlienCommon.getter_setter(arg, asr));
	}

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
		if (arg.getArguments().size() > 0) {
			ArrayList<Integer> selectedPorts = new ArrayList<Integer>();
			ArrayList<Integer> unselectedPorts = new ArrayList<Integer>();
			String num = (String) arg.getArguments().get(0);
			int bitMap = Integer.parseInt(num);
			GPOController.setPorts(selectedPorts, unselectedPorts, bitMap);

			for (Integer i : selectedPorts) {
				asr.getGpioController().setGPOHight(i);
			}
			for (Integer i : unselectedPorts) {
				asr.getGpioController().setGPOLow(i);
			}

		} else {
			int numGPOPorts = asr.getGpioController().getNumGPOPorts();
			BigInteger bitMap = new BigInteger("0");
			for (int i = 0; i < numGPOPorts; i++) {
				if (asr.getGpioController().getGPOState(i)) {
					bitMap = bitMap.setBit(i);
				}
			}
			arg.getArguments().add(Integer.toString(bitMap.intValue()));
		}
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
		logger.debug("got into invertExternalOutput");
		if (arg.getArguments().size() != 0){
			logger.debug("hello");
			String temp = (String) arg.getArguments().get(0);
			if (!(temp.equalsIgnoreCase("ON") || temp.equalsIgnoreCase("OFF"))) {
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				ArrayList<String> PossibleValues = new ArrayList<String>();
				PossibleValues.add("ON");
				PossibleValues.add("OFF");
				ArrayList<Object> retVal = new AlienExceptionHandler().error10(
						tempVal, arg, PossibleValues);
				arg.setReturnValue(retVal);
				return arg;
			}
			if (temp.equalsIgnoreCase("ON")) {
				asr.getGpioController().setInvertGPO(true);
			}
			if (temp.equalsIgnoreCase("OFF")) {
				asr.getGpioController().setInvertGPO(false);
			}
			
		} 
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
		if (arg.getArguments().size() != 0) {
			String temp = (String) arg.getArguments().get(0);
			if (!(temp.equalsIgnoreCase("ON") || temp.equalsIgnoreCase("OFF"))) {
				String cur = arg.getCurrentQueryName();
				ArrayList<Object> tempVal = new ArrayList<Object>();
				tempVal.add(cur);
				ArrayList<String> PossibleValues = new ArrayList<String>();
				PossibleValues.add("ON");
				PossibleValues.add("OFF");
				ArrayList<Object> retVal = new AlienExceptionHandler().error10(
						tempVal, arg, PossibleValues);
				arg.setReturnValue(retVal);
				return arg;
			}
			if (temp.equalsIgnoreCase("ON")) {
				asr.getGpioController().setInvertGPI(true);
			}
			if (temp.equalsIgnoreCase("OFF")) {
				asr.getGpioController().setInvertGPI(false);
			}
		}
		return AlienCommon.getter_setter(arg, asr);
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

}
