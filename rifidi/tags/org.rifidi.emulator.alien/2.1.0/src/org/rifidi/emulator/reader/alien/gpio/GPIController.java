/**
 * 
 */
package org.rifidi.emulator.reader.alien.gpio;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.autonomous.states.AutoStateEnums;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;

/**
 * @author Kyle
 * 
 */
public class GPIController extends Observable implements Observer {

	/**
	 * Message logger
	 */
	private static Log logger = LogFactory.getLog(GPIController.class);

	private boolean isStartTrigger;

	private ArrayList<Integer> risingEdgePorts;
	private ArrayList<Integer> fallingEdgePorts;

	private AlienReaderSharedResources asr;

	public GPIController(boolean isStartTrigger,
			AlienReaderSharedResources asr) {
		this.isStartTrigger = isStartTrigger;
		risingEdgePorts = new ArrayList<Integer>();
		fallingEdgePorts = new ArrayList<Integer>();
		this.asr = asr;

	}

	public void setListeningPorts(int risingEdge, int fallingEdge) {
		setPorts(risingEdge, risingEdgePorts);
		setPorts(fallingEdge, fallingEdgePorts);
		if (risingEdge == 0 && fallingEdge == 0 && isCorrectState()) {
			triggerStateChange();
		}
	}

	private void setPorts(int bitMap, ArrayList<Integer> ports) {
		ports.clear();
		if ((bitMap & 1) == 1) {
			ports.add(0);
		}
		if ((bitMap & 2) == 2) {
			ports.add(1);
		}
		if ((bitMap & 4) == 4) {
			ports.add(2);
		}
		if ((bitMap & 8) == 8) {
			ports.add(3);
		}
		if ((bitMap & 16) == 16) {
			ports.add(4);
		}
		if ((bitMap & 32) == 32) {
			ports.add(5);
		}

	}

	@SuppressWarnings("unchecked")
	public void update(Observable o, Object arg) {
		if (o instanceof GPIOController) {
			GPIOController c = (GPIOController) o;
			ArrayList<Object> args = (ArrayList<Object>) arg;
			String event = (String) args.get(0);
			int portNum = (Integer) args.get(1);

			if (GPIOController.GPI_EVENT.equals(event)) {
				boolean isRisingEdgeEvent = c.getGPIState(portNum);
				if (shouldTriggerStateChange(isRisingEdgeEvent, portNum)) {
					triggerStateChange();
				}

			}

		}

	}

	private boolean shouldTriggerStateChange(boolean isRisingEdgeEvent,
			int changedPort) {

		boolean portCorrect = false;

		if (isRisingEdgeEvent && this.risingEdgePorts.contains(changedPort))
			portCorrect = true;
		else if (!isRisingEdgeEvent
				&& this.fallingEdgePorts.contains(changedPort))
			portCorrect = true;

		return portCorrect && isCorrectState();
	}

	private boolean isCorrectState() {
		if (isStartTrigger
				&& (asr.getAutoStateController().getCurrentState() == AutoStateEnums.Waiting))
			return true;
		else if (!isStartTrigger
				&& (asr.getAutoStateController().getCurrentState() == AutoStateEnums.Working))
			return true;
		else
			return false;
	}

	private void triggerStateChange() {
		logger.debug("triggering Auto mode from GPITrigger");
		this.setChanged();
		this.notifyObservers();
	}

}
