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
import org.rifidi.utilities.Timer;

/**
 * 
 * This class handles the execution of the working state for alien autonomous
 * mode. It observes both the GPIController and a timer.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AutoWorkingState implements AutoState, Observer {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AutoWorkingState.class);

	/**
	 * A variable that is set to false when the Autostoptimer goes is up or the
	 * autostoptrigger GPI pins are set
	 */
	private boolean doneWithWork;

	/**
	 * variable to prevent transition to next state if Automode has been turned
	 * off.
	 */
	private boolean stateIsStopped = false;

	/**
	 * The shared resources
	 */
	private AlienReaderSharedResources asr;

	public AutoWorkingState(AlienReaderSharedResources asr) {
		this.asr = asr;
		asr.getAutoStopTrigger().addObserver(this);
	}

	/**
	 * This method controlls the execution of the working state. It sets the
	 * autoworkoutput GPO pins, waits for either a GPI trigger or a timer, then
	 * moves to the evaluation state
	 */
	public void beginState() {
		logger.debug("working state started");
		doneWithWork = false;
		stateIsStopped = false;
		// set up timer
		String autoPause = asr.getPropertyMap().get("autostoptimer")
				.getPropertyStringValue();
		String autoStopTrigger = asr.getPropertyMap().get("autostoptrigger")
				.getPropertyStringValue();
		int pauseTime = Integer.parseInt(autoPause);
		if (pauseTime > 0) {
			Timer timer = new Timer(pauseTime);
			timer.addObserver(this);
			Thread t = new Thread(timer, "autostoptimer thread");
			t.start();
		}

		// set output lines.
		asr.getAutoWorkOutput().setPins();

		if ((pauseTime <= 0) && autoStopTrigger.equals("0 0"))
			doneWithWork = true;

		// wait until we get a trigger
		while (!doneWithWork && !stateIsStopped) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.debug("State has been interrupted because state is stopped");
			}
		}

		// unset output pins
		asr.getAutoWorkOutput().unsetPins();

		// go to evaluation state.
		if (!stateIsStopped) {
			logger.debug("Transitioning to next state");
		}else{
			logger.debug("state is stopped");
		}

	}

	public void stopState() {
		logger.debug("stop state called");
		stateIsStopped = true;

	}

	/**
	 * This class observes a timer and the AutoStopTrigger GPI lines. When
	 * either the autostopTimer fires or the autostoptrigger sees the correct
	 * pins, it will transition to the next state.
	 */
	public void update(Observable o, Object arg) {
		if (o instanceof Timer) {
			if (!doneWithWork) {
				logger.debug("AutoWorkingState ended due to AutoStopTimer");
				doneWithWork = true;
			}
		} else if (o instanceof GPIController) {
			if (!doneWithWork) {
				logger.debug("AutoWorkingState ended due to AutoStopTrigger");
				doneWithWork = true;
			}
		}

	}

}
