package org.rifidi.emulator.reader.awid.module;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.extra.CommandInformation;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState;
import org.rifidi.emulator.reader.command.controller.autonomous.AutonomousCommandController;

/**
 * An autonomous command controller for the Awid reader. needs to create a
 * custom CommandExecutor
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AwidAutonomousCommandController extends
		AutonomousCommandController {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(AwidAutonomousCommandController.class);

	public AwidAutonomousCommandController(
			AbstractCommandControllerOperatingState initialOperatingState,
			ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal,
			Communication communication, Map<byte[], Integer> autoCommands) {
		super(initialOperatingState, powerControlSignal,
				connectionControlSignal, communication, autoCommands);
	}

	/**
	 * Some readers can have multiple autonomous method calls going at once. For
	 * those readers, you would use the regular constructAutonomousExecutors.
	 * For those readers where you can only call one set of autonomous methods
	 * at once, such as the AWID MPR, this method is a better option. This will
	 * purge any information about prior autonomous calls before setting the
	 * latest autonomous call in motion.
	 * 
	 * 
	 * @param autoCommands
	 *            The latest batch of autonomous calls to execute.
	 */
	public void constructSingleAutonomousExecuter(CommandInformation extraInfo) {
		/* Iterate through the map, creating a new executer for each entry */
		Iterator<byte[]> iter = extraInfo.getCommandMap().keySet().iterator();

		logger.debug("Got into the singleAutonomousExecutor");

		while (iter.hasNext()) {
			/* Grab the command and period from the map */
			byte[] curCommand = iter.next();
			int curPeriod = extraInfo.getCommandMap().get(curCommand)
					.intValue();

			this.autonomousExecuters.clear();

			/* Add a new executer based on this info the list of executers */
			this.autonomousExecuters.add(new AwidAutonomousCommandExecuter(this,
					curCommand, curPeriod, extraInfo.getValue(), extraInfo
							.getState()));

		}
	}

}
