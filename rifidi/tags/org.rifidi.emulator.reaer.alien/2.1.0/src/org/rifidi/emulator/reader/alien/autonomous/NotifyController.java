/*
 *  AutonomousCommandController.java
 *
 *  Created:	Oct 13, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.alien.autonomous;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.reader.alien.formatter.AlienOutgoingMessageFormatter;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.emulator.reader.command.controller.CommandControllerPowerState;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandController;
import org.rifidi.emulator.reader.command.controller.abstract_.AbstractCommandControllerOperatingState;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.tagmemory.formatter.OutgoingMessageFormatter;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * A generic class which models an autonomous command controller. An autonomous
 * command controller generates commands to pass through to its current
 * operating state and gives backs it responses through the passed
 * CommunicationChannel.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class NotifyController extends AbstractCommandController {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(NotifyController.class);

	/**
	 * 
	 */
	private AbstractReaderSharedResources sharedResources;

	/**
	 * 
	 */
	private OutgoingMessageFormatter outgoingMessageFormatter;

	private NotifyTimer notifyTimer;

	/**
	 * A constructor for an autonomous command controller which takes in a map
	 * of commands and the time between command triggers. This way, a single
	 * controller can invoke multiple commands at various intervals. The
	 * controller is initially off.
	 * 
	 * @param initialOperatingState
	 *            The initial operating state to place this controller in.
	 * @param powerControlSignal
	 *            The power control signal for this AbstractCommandController to
	 *            observe. If <i>null</i> is passed, this will not observe a
	 *            power signal.
	 * @param connectionControlSignal
	 *            The connection control signal for this
	 *            AbstractCommandController to observe. If <i>null</i> is
	 *            passed, this will not observe a connection signal.
	 * @param communication
	 *            The communication channel to send command responses through.
	 * @param autoCommands
	 *            The map of commands to execute.
	 */
	public NotifyController(
			AbstractCommandControllerOperatingState initialOperatingState,
			ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal,
			Communication communication, AbstractReaderSharedResources asr,
			OutgoingMessageFormatter ogmf) {
		/* Invoke super constructor */
		super(NotifyOffCommandControllerPowerState.getInstance(),
				initialOperatingState, powerControlSignal,
				connectionControlSignal, communication);

		this.sharedResources = asr;
		this.outgoingMessageFormatter = ogmf;
		this.notifyTimer = new NotifyTimer(
				(AlienReaderSharedResources) sharedResources, this);
	}

	/**
	 * Exposes the underlying method to package members.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.
	 *      AbstractCommandController#changeCommandControllerOperatingState(org.
	 *      rifidi.emulator.reader.command.controller.
	 *      CommandControllerOperatingState)
	 */
	@Override
	protected void changeCommandControllerOperatingState(
			AbstractCommandControllerOperatingState newState) {
		/* Just call the super-class method */
		super.changeCommandControllerOperatingState(newState);

	}

	/**
	 * Exposes the underlying method to package members.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.
	 *      AbstractCommandController#changeCommandControllerPowerState(org.rifidi.
	 *      emulator.reader.command.controller.CommandControllerPowerState)
	 */
	@Override
	protected void changeCommandControllerPowerState(
			CommandControllerPowerState newState) {
		/* Just call the super-class method */
		super.changeCommandControllerPowerState(newState);

	}

	/**
	 * Exposes the underlying method to the Autonomous package members.
	 * 
	 * @see org.rifidi.emulator.reader.command.controller.abstract_.
	 *      AbstractCommandController#getCurCommunication()
	 */
	@Override
	protected Communication getCurCommunication() {
		// Simply call the super method.
		return super.getCurCommunication();

	}

	/**
	 * @return the sharedResources
	 */
	public AbstractReaderSharedResources getSharedResources() {
		return sharedResources;
	}

	/**
	 * @return the outgoingMessageFormatter
	 */
	public OutgoingMessageFormatter getOutgoingMessageFormatter() {
		return outgoingMessageFormatter;
	}

	/**
	 * This method allows either the automode states or the notify controller to
	 * send a taglist.
	 * 
	 * @param tags
	 *            A list of tags to send
	 * @param evaluationTriggerCondition
	 *            whether the evaluation loop was true or false
	 * @param eventTriggerCondition
	 *            whether or not the tag list was changed
	 * @param timedMessage
	 *            true if this method was invoked from the notifyTimer
	 * @throws CommunicationException
	 */
	public void sendMessage(Collection<RifidiTag> tags,
			EvaluationTriggerCondition evaluationTriggerCondition,
			TagEventTriggerCondition eventTriggerCondition, boolean timedMessage) {

		if (shouldSendMessage(evaluationTriggerCondition,
				eventTriggerCondition, timedMessage)) {

			// is this message sent due to an automode evaluation loop or a
			// notify timer?
			String reason;
			if (timedMessage) {
				reason = AlienOutgoingMessageFormatter.NOTIFY_TIMER;
			} else {
				reason = AlienOutgoingMessageFormatter.AUTONOMOUS_EVAL;
			}

			String msgToSend = this.getOutgoingMessageFormatter()
					.formatMessage(tags, this.getSharedResources(), reason);

			try {
				this.getCurCommunication().sendBytes(msgToSend.getBytes());
			} catch (CommunicationException e) {
				logger
						.debug("There was an error when trying to send a message");
			}
		}
	}

	/**
	 * This method evaluates whether or not a message should be sent based on
	 * the notifyTrigger variable
	 * 
	 * @param evaluationTriggerCondition
	 * @param eventTriggerCondition
	 * @return
	 */
	private boolean shouldSendMessage(EvaluationTriggerCondition evalTrig,
			TagEventTriggerCondition eventTrig, boolean timedMessage) {

		AlienReaderSharedResources asr = (AlienReaderSharedResources) sharedResources;
		String timerVal = asr.getPropertyMap().get("notifytime")
				.getPropertyStringValue();
		String notifyTrigger = asr.getPropertyMap().get("notifytrigger")
				.getPropertyStringValue();

		int tVal = Integer.parseInt(timerVal);
		if ((tVal > 0) && timedMessage) {
			return true;
		} else if (notifyTrigger.equalsIgnoreCase("truefalse")) {
			return true;
		} else if (notifyTrigger.equalsIgnoreCase("true")
				&& (evalTrig == EvaluationTriggerCondition.True)) {
			return true;
		} else if (notifyTrigger.equalsIgnoreCase("false")
				&& (evalTrig == EvaluationTriggerCondition.False)) {
			return true;
		} else if (notifyTrigger.equalsIgnoreCase("change")
				&& (eventTrig != TagEventTriggerCondition.NoChange)) {
			return true;
		} else if (notifyTrigger.equalsIgnoreCase("add")
				&& ((eventTrig == TagEventTriggerCondition.Add) || 
						(eventTrig == TagEventTriggerCondition.Change))) {
			return true;
		}else if (notifyTrigger.equalsIgnoreCase("remove")
				&& ((eventTrig == TagEventTriggerCondition.Remove) || 
						(eventTrig == TagEventTriggerCondition.Change))) {
			return true;

		}else{
			return false;
		}

	}

	public NotifyTimer getNotifyTimer() {
		return notifyTimer;
	}

}
