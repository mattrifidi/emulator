/**
 * 
 */
package org.rifidi.emulator.reader.llrp.rospec.aispec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.rifidi.emulator.reader.llrp.rospec.ExecutableSpec;
import org.rifidi.emulator.reader.llrp.trigger.Trigger;
import org.rifidi.services.tags.impl.RifidiTag;

import edu.uark.csce.llrp.AISpec;
import edu.uark.csce.llrp.TagReportData;

/**
 * Needs to observe LLRPTagMemory
 * 
 * @author kyle
 *
 */
public class _AISpec implements ExecutableSpec, Observer{

	private Trigger stopTrigger;
	
	private Set<Integer> antennas;
	
	private ArrayList<_InventoryParameterSpec> ipSpecs;
	
	private HashMap<Long, RifidiTag> tagsSeen;
	
	public _AISpec(AISpec aiSpec){
		tagsSeen = new HashMap<Long, RifidiTag>();
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.rospec.ExecutableSpec#start()
	 */
	@Override
	public void start() {
		this.stopTrigger.enable();
		scan();

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.llrp.rospec.ExecutableSpec#stop()
	 */
	@Override
	public void stop() {
		this.stopTrigger.disable();
		scan();

	}
	
	public void scan(){
		
	}
	
	public AISpec toAISpec(){
		return null;
	}
	
	public TagReportData getTagReports(){
		return null;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
