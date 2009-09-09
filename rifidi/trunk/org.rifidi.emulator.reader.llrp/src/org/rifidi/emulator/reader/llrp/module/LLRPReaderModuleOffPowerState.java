package org.rifidi.emulator.reader.llrp.module;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.module.abstract_.AbstractOffPowerState;

/**
 * The off state for an LLRP Reader.
 * 
 * @author kyle
 * 
 */
public class LLRPReaderModuleOffPowerState extends AbstractOffPowerState {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(LLRPReaderModuleOffPowerState.class);

	/**
	 * The singleton instance for this class.
	 */
	private static final LLRPReaderModuleOffPowerState SINGLETON_INSTANCE = new LLRPReaderModuleOffPowerState();

	/**
	 * Returns the singleton instance for this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static LLRPReaderModuleOffPowerState getInstance() {
		return LLRPReaderModuleOffPowerState.SINGLETON_INSTANCE;

	}

	/**
	 * A basic constructor which is private to support singleton-functionality.
	 */
	private LLRPReaderModuleOffPowerState() {

	}

	public void turnOn(PowerControllable pcObject) {
		logger.info("Turning on LLRP");
		LLRPReaderModule llrp = (LLRPReaderModule) pcObject;
		
		llrp.getAdminInterface().start();
		
		String readername = llrp.getSharedResources().getReaderName();
		llrp.changePowerState(LLRPReaderModuleOnPowerState.getInstance());	
		LogFactory.getLog("console." + readername).info(readername + " on");


		// Not needed anymore because Adminconsole is doing this 
/*		if(isServer)
		{
			//llrp.getInteractiveCommunication().turnOn();
			//llrp.getInteractiveCommandController().turnOn();
			llrp.getSharedResources().getInteractivePowerSignal().setControlVariableValue(true);
			//llrp.getSharedResources().getInteractiveConnectionSignal().setControlVariableValue(false);
		}else
		{
			//TCPExtraInformation information = new TCPExtraInformation(llrp.connectionType.getAddress(), llrp.connectionType.getPort());
			//llrp.getInteractiveCommunication().turnOn(information);
			//llrp.getInteractiveCommandController().turnOn(information);
			llrp.getSharedResources().getInteractivePowerSignal()
					.setControlVariableValue(true);
			//llrp.getSharedResources().getInteractiveConnectionSignal().setControlVariableValue(true);
		}
	
*/
			
	}

}
