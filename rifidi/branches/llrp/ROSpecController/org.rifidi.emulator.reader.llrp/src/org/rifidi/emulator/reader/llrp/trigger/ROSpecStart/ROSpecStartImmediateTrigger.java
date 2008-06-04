/**
 * 
 */
package org.rifidi.emulator.reader.llrp.trigger.ROSpecStart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecController;
import org.rifidi.emulator.reader.llrp.rospec.execeptions.ROSpecControllerException;
import org.rifidi.emulator.reader.llrp.trigger.Trigger;

/**
 * This Trigger is used as a Start Trigger for a ROSpec
 * @author kyle
 *
 */
public class ROSpecStartImmediateTrigger implements Trigger {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(ROSpecStartImmediateTrigger.class);
	
	private int ROSpecID;
	
	private ROSpecController controller;
	
	public ROSpecStartImmediateTrigger(int ROSpecID, ROSpecController controller){
		this.ROSpecID = ROSpecID;
		this.controller = controller;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.trigger.Trigger#cleanUp()
	 */
	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.trigger.Trigger#disable()
	 */
	@Override
	public void disable() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.trigger.Trigger#enable()
	 */
	@Override
	public void enable() {
		fireTrigger();

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.trigger.Trigger#fireTrigger()
	 */
	@Override
	public void fireTrigger() {
		try {
			controller.startROSpec(this.ROSpecID);
		} catch (ROSpecControllerException e) {
			logger.debug(e.getMessage(), e);
		}

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.trigger.Trigger#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.trigger.Trigger#suspend()
	 */
	@Override
	public void suspend() {
		// TODO Auto-generated method stub

	}

}
