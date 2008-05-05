/**
 * 
 */
package org.rifidi.emulator.reader.alien.autonomous.states;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.autonomous.NotifyController;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;

/**
 * This class handles the switching of the state of the autonomous mode. The
 * progression is Off->Waiting->Woking->Evaluation->Waiting... It follows the
 * state machine found in chapter two of the alien reader guide
 * 
 * It implements runnable so that it can start a new thread of execution for
 * autonomous mode functionality.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AutoStateController implements Runnable {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AutoStateController.class);

	/**
	 * The current state of autonomous mode
	 */
	private AutoStateEnums currentState;

	/**
	 * The autonomous command controller. Used to send outgoing messages.
	 */
	private NotifyController commandController;

	/**
	 * The shared resources
	 */
	private AlienReaderSharedResources asr;

	/**
	 * Boolean that will kill the thread when automode should be turned off
	 */
	private boolean automodeOn = false;

	/**
	 * The thread that handles execution of autonomous mode
	 */
	private Thread autoModeThread = null;

	private int threadNum = 0;

	/**
	 * The object that handles the execution of the waiting state
	 */
	private AutoWaitState waitState;

	/**
	 * The object that handles the execution of the working state
	 */
	private AutoWorkingState workingState;

	/**
	 * The object that handles the execution of the evaluation state
	 */
	private AutoEvaluationState evaluationState;

	/**
	 * @param asr
	 *            The AlienReaderSharedResources
	 * @param commandController
	 *            The command Controller
	 */
	public AutoStateController(AlienReaderSharedResources asr,
			NotifyController commandController) {
		this.asr = asr;
		this.commandController = commandController;
		this.currentState = AutoStateEnums.Off;
		waitState = new AutoWaitState(asr);
		workingState = new AutoWorkingState(asr);
		evaluationState = new AutoEvaluationState(asr);
	}

	/**
	 * This method kicks off automode by causing the automode to enter the
	 * waiting state
	 */
	public void startAutoMode() {
		if (currentState == AutoStateEnums.Off && autoModeThread == null) {
			automodeOn = true;
			logger.debug("Starting Auto Mode in waiting state");

			autoModeThread = new Thread(this, "Autonomous State Machine "
					+ threadNum++);
			autoModeThread.start();
		}
	}

	/**
	 * This method stops automode by stopping the current state and then
	 * resetting variables.
	 */
	public void stopAutoMode() {
		automodeOn = false;
		if (currentState == AutoStateEnums.Waiting) {
			waitState.stopState();
		} else if (currentState == AutoStateEnums.Working) {
			workingState.stopState();
		} else if (currentState == AutoStateEnums.Evaluating) {
			evaluationState.stopState();
		}
		if (autoModeThread != null) {
			autoModeThread.interrupt();
		}
	}

	public NotifyController getNotifyController() {
		return commandController;
	}

	public AutoStateEnums getCurrentState() {
		return this.currentState;
	}

	public void run() {
		while (automodeOn) {
			currentState = AutoStateEnums.Waiting;
			waitState.beginState();
			if (automodeOn) {
				currentState = AutoStateEnums.Working;
				workingState.beginState();
			}
			if (automodeOn) {
				currentState = AutoStateEnums.Evaluating;
				evaluationState.beginState();
			}
		}
		logger.debug("AutoMode is off");
		this.autoModeThread = null;
		currentState = AutoStateEnums.Off;

	}

}
