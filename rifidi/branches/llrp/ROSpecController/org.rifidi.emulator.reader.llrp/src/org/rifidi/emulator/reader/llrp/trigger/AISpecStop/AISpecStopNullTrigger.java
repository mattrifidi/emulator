/**
 * 
 */
package org.rifidi.emulator.reader.llrp.trigger.AISpecStop;

import org.rifidi.emulator.reader.llrp.rospec._ROSpec;
import org.rifidi.emulator.reader.llrp.trigger.NullTrigger;

/**
 * @author kyle
 *
 */
public class AISpecStopNullTrigger extends NullTrigger {

	private _ROSpec rospec = null;
	
	private int AISpecIndex;
	
	public AISpecStopNullTrigger(_ROSpec rospec, int AISpecIndex){
		this.rospec = rospec;
		this.AISpecIndex = AISpecIndex;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.trigger.Trigger#fireTrigger()
	 */
	@Override
	public void fireTrigger() {
		rospec.stopCurrentSpec(AISpecIndex);

	}

}
