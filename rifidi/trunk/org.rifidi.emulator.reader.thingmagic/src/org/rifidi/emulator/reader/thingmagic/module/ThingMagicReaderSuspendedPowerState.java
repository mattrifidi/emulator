/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic.module;

import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.reader.module.abstract_.AbstractSuspendedPowerState;

/**
 * @author jmaine
 *
 */
public class ThingMagicReaderSuspendedPowerState extends AbstractSuspendedPowerState {
	
	private static ThingMagicReaderSuspendedPowerState instance = new ThingMagicReaderSuspendedPowerState();
	
	/**
	 * Get the instance that represents this reader being suspended. 
	 * @return The single instance of this class.
	 */
	public static ThingMagicReaderSuspendedPowerState getInstance(){
		return instance;
	}

	/**
	 * 
	 */
	public ThingMagicReaderSuspendedPowerState() {
		// TODO Auto-generated constructor stub
		super();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common.PowerControllable)
	 */
	@Override
	public void resume(PowerControllable pcObject) {
		// TODO Auto-generated method stub
		
		/* cast pcObject to something useful */
		ThingMagicReaderModule rm = (ThingMagicReaderModule) pcObject;

		/* resume the tcp connection */
		rm.getRQLComm().resume();
		rm.getInteractiveRQLController().resume();

		/* resume the tag memory */
		ThingMagicReaderSharedResources tmsr = rm.getSharedResources();		
		tmsr.getTagMemory().resume();
		 
		/* switch the reader state to on */
		rm.changePowerState(ThingMagicReaderModuleOnPowerState.getInstance());
		
		/* send to the console that we have resumed */
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " resumed");
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable, java.lang.Class)
	 */
	@Override
	public void turnOff(PowerControllable pcObject, Class callingClass) {
		// TODO Auto-generated method stub
		/* cast the pcObject to something we can use */
		ThingMagicReaderModule rm = (ThingMagicReaderModule) pcObject;
		
		/* turn off tcp connection */
		//rm.getInteractiveRQLController().turnOff(this.getClass());
		
		/* more things to do to turn off the tcp connection */
		rm.getSharedResources().getInteractiveRQLPowerSignal()
		.setControlVariableValue(false);
		rm.getSharedResources().getInteractiveRQLConnectionSignal()
		.setControlVariableValue(false);

		/* set the reader to the off position */
		rm.changePowerState(ThingMagicReaderModuleOffPowerState.getInstance());
		
		/* send to the console that this reader is not turned off. */
		String readername = rm.getSharedResources().getReaderName();
		LogFactory.getLog("console." + readername).info(readername + " off");
	}

}
