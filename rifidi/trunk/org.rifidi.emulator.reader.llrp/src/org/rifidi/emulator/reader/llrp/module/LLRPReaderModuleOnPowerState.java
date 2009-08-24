package org.rifidi.emulator.reader.llrp.module;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecController;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecControllerFactory;
import org.rifidi.emulator.reader.llrp.rospec._ROSpec;
import org.rifidi.emulator.reader.module.abstract_.AbstractOnPowerState;

public class LLRPReaderModuleOnPowerState extends AbstractOnPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(LLRPReaderModuleOnPowerState.class);

	/**
	 * The singleton instance for this class
	 */
	private static final LLRPReaderModuleOnPowerState SINGLETON_INSTANCE = new LLRPReaderModuleOnPowerState();

	/**
	 * Public method to return the singleton instance
	 * 
	 * @return
	 */
	public static LLRPReaderModuleOnPowerState getInstance() {
		return SINGLETON_INSTANCE;
	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 * 
	 */
	private LLRPReaderModuleOnPowerState() {

	}

	public void suspend(PowerControllable pcObject) {
		LLRPReaderModule llrp = (LLRPReaderModule) pcObject;

		llrp.getSharedResources().getTagMemory().suspend();
		llrp.getSharedResources().getGpioController().suspend();
		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) llrp
				.getSharedResources();

		llrpsr.getKeepAliveController().suspend();

		ROSpecController rsc = ROSpecControllerFactory.getInstance()
				.getReportController(llrpsr.getReaderName());

		for (_ROSpec rs : rsc.getROSpecs().values()) {
			rs.suspend();
		}

		llrp.getInteractiveCommunication().suspend();
		llrp.getInteractiveCommandController().suspend();

		llrp.changePowerState(LLRPReaderModuleSuspendPowerState.getInstance());

		String readername = llrp.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(
				readername + " suspended");

	}

	public void turnOff(PowerControllable pcObject) {
		LLRPReaderModule llrp = (LLRPReaderModule) pcObject;

		String readername = llrp.getSharedResources().getReaderName();
		logger.debug("turing off " + readername);

		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) llrp
				.getSharedResources();

		llrpsr.getKeepAliveController().stopKeepAlives();

		ROSpecController rsc = ROSpecControllerFactory.getInstance()
				.getReportController(llrpsr.getReaderName());

		rsc.cleanUp();
		llrpsr.getAdded_Rospecs().clear();
		llrpsr.accessSpecs.clear();

		llrp.getAdminInterface().stop();

		llrp.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(false);
		llrp.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);

		logger.debug("Turing off power...");

		llrp.changePowerState(LLRPReaderModuleOffPowerState.getInstance());

		LogFactory.getLog("console." + readername).info(readername + " off");

	}

}
