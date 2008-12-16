/**
 * 
 */
package org.rifidi.emulator.reader.alien.autonomous.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.autonomous.EvaluationTriggerCondition;
import org.rifidi.emulator.reader.alien.autonomous.NotifyController;
import org.rifidi.emulator.reader.alien.autonomous.TagEventTriggerCondition;
import org.rifidi.emulator.reader.alien.commandhandler.AlienTag;
import org.rifidi.emulator.reader.alien.gpio.GPOController;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.utilities.Timer;

/**
 * This class handles the execution of the Evaluation state in alien autonomous
 * mode.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AutoEvaluationState implements AutoState, Observer {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AutoEvaluationState.class);

	/**
	 * The shared resources
	 */
	private AlienReaderSharedResources asr;

	/**
	 * whether or not the output pins (either true or false) have been set long
	 * enough.
	 */
	private boolean donepausing = false;

	/**
	 * variable to prevent transition to next state if Automode has been turned
	 * off.
	 */
	private boolean stateIsStopped = false;

	private TagEventTriggerCondition tagEventTrig = TagEventTriggerCondition.NoChange;

	private EvaluationTriggerCondition evalTrig = EvaluationTriggerCondition.TrueFalse;

	private List<RifidiTag> oldTags = new ArrayList<RifidiTag>();
	private List<RifidiTag> currentTags = new ArrayList<RifidiTag>();

	private Timer pauseTimer;

	public AutoEvaluationState(AlienReaderSharedResources asr) {
		this.asr = asr;
	}

	/**
	 * This method handles the execution of the evaluation state. It first scans
	 * the antenna, sets either the autoTruePins or the autoFalsePins, then
	 * sends out a notification if it needs to.
	 */
	public void beginState() {
		stateIsStopped = false;

		logger.debug("beginning evaluation state");

		// get a taglist
		if (!stateIsStopped) {
			oldTags = currentTags;
			currentTags = scanRadio();
			setTagEventTriggerCondition();
			logger.debug("Done getting tag list");
			logger.debug("Number of tags found: " + currentTags.size());
		}

		// ifTaglist has tags, do autoTrue
		if (!stateIsStopped) {
			if (currentTags.isEmpty() || !anyNewTags(oldTags, currentTags)) {
				autoFalse();
				this.evalTrig = EvaluationTriggerCondition.False;
			} else {
				autoTrue();
				this.evalTrig = EvaluationTriggerCondition.True;
			}
			logger.debug("Done setting eval GPO");
		}

		// now try to send tags
		if (!stateIsStopped) {
			sendTags(currentTags);
			logger.debug("Done sending tags");
		}

		// now go to waiting state again
		if (!stateIsStopped) {
			logger.debug("transition to next state please");
		} else {
			logger.debug("state is stopped");
		}

	}

	/**
	 * Returns true if there are any tags in currTags that are not in oldTags.
	 * 
	 * @param oldTags
	 *            The tags that were in the previous cycle
	 * @param currTags
	 *            The tags that were in the current cycle
	 * @return True if there are any tags in the current cycle that were not in
	 *         the previous cycle.
	 */
	private boolean anyNewTags(List<RifidiTag> oldTags, List<RifidiTag> currTags) {
		for (RifidiTag curr : currTags) {
			boolean found = false;
			for (RifidiTag old : oldTags) {
				if (old.equals(curr)) {
					found = true;
				}
			}
			if (!found) {
				return true;
			}
		}
		return false;
	}

	public void stopState() {
		logger.debug("stop state called");
		stateIsStopped = true;

	}

	/**
	 * This method scans the radio
	 * 
	 * @return
	 */
	private List<RifidiTag> scanRadio() {
		// TODO: Possible problem: We are scanning every time now, instead of
		// only when notify mode is on. This may cause problems with autonomous
		// mode and a -1 persistTime.
		ArrayList<RifidiTag> response = new ArrayList<RifidiTag>();
		// if (asr.getNotifyControlSignal().getControlVariableValue() == true) {
		response = AlienTag.getTagList(this.asr);
		// } else {
		// logger.debug("Notification mode is not on "
		// + "- not processing taglist");
		// }
		return response;
	}

	/**
	 * This method evaluates the previously seen tag list and the current tag
	 * list to figure if the tag list has been added to, deleted from, both, or
	 * neither.
	 */
	private void setTagEventTriggerCondition() {
		String trigger = asr.getPropertyMap().get("notifytrigger")
				.getPropertyStringValue();
		if (trigger.equalsIgnoreCase("add")
				|| trigger.equalsIgnoreCase("remove")
				|| trigger.equalsIgnoreCase("change")) {
			boolean added = false;
			boolean removed = false;
			if (!oldTags.containsAll(currentTags)) {
				added = true;
			}
			if (!currentTags.containsAll(oldTags)) {
				removed = true;
			}
			if (added && removed) {
				this.tagEventTrig = TagEventTriggerCondition.Change;
			} else if (!added && removed) {
				this.tagEventTrig = TagEventTriggerCondition.Remove;
			} else if (added & !removed) {
				this.tagEventTrig = TagEventTriggerCondition.Add;
			} else {
				this.tagEventTrig = TagEventTriggerCondition.NoChange;
			}

		}
	}

	/**
	 * This method sets the autoTruePins to high
	 */
	private void autoTrue() {
		changeGPOPins("autotruepause");
	}

	/**
	 * This method sets the autoFlasePins to high
	 */
	private void autoFalse() {
		changeGPOPins("autofalsepause");
	}

	/**
	 * This method changes either the autoTruePins or the autoFalsePins to high
	 * for either autoTruePause or autoFalsePause ms respectivly
	 * 
	 * @param pauseType
	 *            either "autotruepause" or autofalsepause"
	 */
	private void changeGPOPins(String pauseType) {
		donepausing = false;

		GPOController gpoController;

		// figure out if we are setting the autoTrueOutput or the
		// autoFalseOutput
		if (pauseType.contains("true")) {
			gpoController = asr.getAutoTrueOutput();
		} else {
			gpoController = asr.getAutoFalseOutput();
		}

		// set the pins
		gpoController.setPins();

		// get the time to pause
		String pause = asr.getPropertyMap().get(pauseType)
				.getPropertyStringValue();

		int timeToPause = Integer.parseInt(pause);
		pauseTimer = new Timer(timeToPause);
		pauseTimer.addObserver(this);
		Thread thread = new Thread(pauseTimer, "pause Timer: " + timeToPause
				+ " ms");

		if (timeToPause > 0) {
			// start the timer
			thread.start();

			// wait for timer to be done
			while (!donepausing && !stateIsStopped) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.debug("Sleep Interupted because state is done");
				}
			}

			// unset the pins
			gpoController.unsetPins();
		}
	}

	/**
	 * This method sends the tags if we need to
	 * 
	 * @param tags
	 */
	private void sendTags(List<RifidiTag> tags) {
		logger.debug("tags size: " + tags.size());
		NotifyController controller = this.asr.getAutoStateController()
				.getNotifyController();

		if (asr.getAutoCommConnectionSignal().getControlVariableValue() == true) {

			controller.sendMessage(tags, this.evalTrig, this.tagEventTrig,
					false);

		} else {
			logger.debug("Notification mode is not on"
					+ " - not sending message");
		}

	}

	public void update(Observable o, Object arg) {
		if (o instanceof Timer) {
			logger.debug("Evaluation pausing done due to timer");
			this.donepausing = true;
		}

	}

	/**
	 * Clears the tag list, in case a clearTagList was called.
	 */
	public void clearPreviousTagList() {
		this.oldTags.clear();
	}

}
