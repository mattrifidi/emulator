/**
 * 
 */
package org.rifidi.emulator.reader.llrp.trigger;

/**
 * This interface is for triggers who have some kind of timer in them, whether
 * it is their main function (such as DurationTrigger), or as a timeout (such as
 * TagObservationTrigger)
 * 
 * @author kyle
 * 
 */
public interface TimerTrigger extends Trigger {

	public void startTimer();
	
	public void stopTimer();

}
