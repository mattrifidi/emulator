/**
 * 
 */
package org.rifidi.emulator.reader.alien.autonomous.states;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.gpio.GPIController;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;

/**
 * This class handles the execution of the wait state for alien autonomous mode.
 * It observes the GPIController
 * 
 * @author Kyle
 * 
 */
public class AutoWaitState implements AutoState, Observer {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AutoWaitState.class);

	/**
	 * A variable that is set to true when the correct GPI trigger has be set.
	 */
	private boolean doneWaiting = false;

	/**
	 * variable to prevent transition to next state if Automode has been turned
	 * off.
	 */
	private boolean stateIsStopped = false;

	/**
	 * The shared resources
	 */
	private AlienReaderSharedResources asr;

	public AutoWaitState(AlienReaderSharedResources asr) {
		this.asr = asr;
		asr.getAutoStartTrigger().addObserver(this);
	}

	/**
	 * This method executes the state. It sets the AutoWaitOutput GPO ports,
	 * waits for a GPI trigger, then waits autoStartPause seconds.
	 */
	public void beginState() {
		doneWaiting = false;
		stateIsStopped = false;

		// set autoWaitOuput GPO pins
		asr.getAutoWaitOutput().setPins();

		String autostarttrigger = asr.getPropertyMap().get("autostarttrigger")
				.getPropertyStringValue();
		if (autostarttrigger.equals("0 0")) {
			doneWaiting = true;
			logger.debug("autostartTrigger is 0 0");
		}

		// wait for GPI trigger
		while (!doneWaiting && !stateIsStopped) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.debug("Sleep interrupted because state should end");
			}
		}

		logger.debug("done waiting for auto start trigger");

		// unset autowait output pins
		asr.getAutoWaitOutput().unsetPins();

		String autoPause = asr.getPropertyMap().get("autostartpause")
				.getPropertyStringValue();
		int pauseTime = Integer.parseInt(autoPause);

		// wait for autostartpause milisconds.
		if (pauseTime > 0 && !stateIsStopped) {
			try {
				Thread.sleep(pauseTime);
			} catch (InterruptedException e) {
				logger.debug("sleep interrupted because state is done");
			}
		}

		logger.debug("done waiting for auto start pause");

		// go to working state
		if (!stateIsStopped) {
			logger.debug("transition to work state please");
		} else {
			logger.debug("state is stopped");
		}

	}

	public void stopState() {
		logger.debug("stop state called");
		stateIsStopped = true;

	}

	/**
	 * This state observes the AutoStartTrigger. When the autostarttrigger has
	 * seen the correct tag pattern it will let this object know.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof GPIController) {
			doneWaiting = true;
		}

	}

}
