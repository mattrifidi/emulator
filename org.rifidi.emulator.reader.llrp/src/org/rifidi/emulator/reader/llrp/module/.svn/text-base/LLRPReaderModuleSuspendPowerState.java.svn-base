package org.rifidi.emulator.reader.llrp.module;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecController;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecControllerFactory;
import org.rifidi.emulator.reader.llrp.rospec._ROSpec;
import org.rifidi.emulator.reader.module.abstract_.AbstractSuspendedPowerState;

public class LLRPReaderModuleSuspendPowerState extends
		AbstractSuspendedPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(LLRPReaderModuleSuspendPowerState.class);

	/**
	 * The singleton instance for this class
	 */
	private static final LLRPReaderModuleSuspendPowerState SINGLETON_INSTANCE = new LLRPReaderModuleSuspendPowerState();

	/**
	 * Public method to return the singleton instance
	 * 
	 * @return
	 */
	public static LLRPReaderModuleSuspendPowerState getInstance() {
		return SINGLETON_INSTANCE;
	}

	/**
	 * Private constructor to allow for Singletons
	 * 
	 */
	private LLRPReaderModuleSuspendPowerState() {

	}

	public void resume(PowerControllable pcObject) {
		LLRPReaderModule llrp = (LLRPReaderModule) pcObject;

		llrp.getSharedResources().getTagMemory().resume();
		llrp.getSharedResources().getGpioController().resume();
		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) llrp
				.getSharedResources();

		llrpsr.getKeepAliveController().resume();

		ROSpecController rsc = ROSpecControllerFactory.getInstance()
				.getReportController(llrpsr.getReaderName());

		for (_ROSpec rs : rsc.getROSpecs().values()) {
			rs.resume();
		}
		llrp.getInteractiveCommunication().resume();
		llrp.getInteractiveCommandController().resume();

		llrp.changePowerState(LLRPReaderModuleOnPowerState.getInstance());

		String readername = llrp.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername)
				.info(readername + " resumed");
	}

	public void turnOff(PowerControllable pcObject) {

		LLRPReaderModule llrp = (LLRPReaderModule) pcObject;

		llrp.getInteractiveCommandController().turnOff();
		llrp.getInteractiveCommunication().turnOff();

		llrp.getSharedResources().getInteractivePowerSignal()
				.setControlVariableValue(false);
		llrp.getSharedResources().getInteractiveConnectionSignal()
				.setControlVariableValue(false);

		llrp.changePowerState(LLRPReaderModuleOffPowerState.getInstance());

		String readername = llrp.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");

	}

}
