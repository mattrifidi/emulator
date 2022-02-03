package org.rifidi.emulator.reader.llrp.properties;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.llrp.report.LLRPReportController;
import org.rifidi.emulator.reader.llrp.report.LLRPReportControllerFactory;
import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;

import edu.uark.csce.llrp.GPIEvent;
import edu.uark.csce.llrp.ReaderEventNotificationData;

/**
 * This class is the middle man between the "hardware" GPI module in the
 * GPIOController and the rest of the LLRP Reader. It observes the
 * GPIOController (and should be the only thing in the LLRP reader that does
 * so). When a GPIPort is changed in the GPIOContoller, it notifies this, which
 * handles it in an LLRP specific way. This LLRPSpecfic handling involves: 1)
 * send out a READER_EVENT_NOTIFICATION if required, and 2) notifying a trigger
 * that might be listening for GPI events. Therefore it is both an observer and
 * an observable.
 * 
 * @author kyle
 * 
 */
public class GPISettings extends Observable implements Observer {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(GPISettings.class);
	
	private static Log eventLogger = LogFactory.getLog("EventLogger");

	/**
	 * The port of this GPI. 0 is illegal
	 */
	private int GPIPortNumber;

	/**
	 * If this is false, the GPIState cannot change
	 */
	private boolean GPIConfig = false;

	/**
	 * 0 - port is low, 1 - port is high, 2 - unknown
	 */
	private int GPIState;

	private LLRPReaderSharedResources llrpsr;

	/**
	 * 
	 * @param portNum
	 *            The port of this GPI. 0 is illegal
	 * @param config
	 *            true for enabled, false for disabled
	 * @param gpio
	 *            The GPIOController
	 */
	public GPISettings(int portNum, boolean config,
			LLRPReaderSharedResources llrpsr) {
		this.GPIPortNumber = portNum;
		this.GPIConfig = config;
		// gpio ports are numbered starting at 0 in the controller
		setGPIState(llrpsr.getGpioController().getGPIState(portNum - 1));
		llrpsr.getGpioController().addObserver(this);
		this.llrpsr = llrpsr;
	}

	public int getPortNum() {
		return this.GPIPortNumber;
	}

	public void setGPIState(boolean state) {
		if (state) {
			this.GPIState = 1;
		} else {
			this.GPIState = 0;
		}
	}

	public int getGPIState() {
		return this.GPIState;
	}

	/**
	 * This GPISettings object listens for events from a corresponding GPI port
	 * in the GPIController. This method updates the GPISetting's current state
	 * based on that port. In addition, this method sends out an event
	 * notification and notifies observers (i.e. GPI Start or Stop Triggers), if
	 * this GPISettings is enabled (i.e. if GPIConfig==true)
	 */
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof GPIOController) {
			GPIOController c = (GPIOController) arg0;
			ArrayList<Object> args = (ArrayList<Object>) arg1;
			String event = (String) args.get(0);
			int portNum = (Integer) args.get(1);

			// if event is for us
			if (event.equals(GPIOController.GPI_EVENT)
					&& ((portNum + 1) == this.GPIPortNumber)) {
				Integer emulator_port = (Integer) args.get(1);
				boolean newState = c.getGPIState(emulator_port);
				String highOrLow;
				if (newState) {
					highOrLow = "high";
				} else {
					highOrLow = "low";
				}
				eventLogger.info("[GPI EVENT]: LLRP GPI"
						+ " port " + (portNum + 1) + " " + highOrLow);
				setGPIState(newState);

				// if this GPISettings is enabled, send event and notify
				// observers
				if (this.GPIConfig) {
					if (shouldSendEvent()) {
						sendGPIEvent(c, emulator_port);
					}

					this.setChanged();
					this.notifyObservers();
				}

			}
		}

	}

	private boolean shouldSendEvent() {
		EventNotificationTable table = llrpsr.getProperties().eventNotificaionTable;
		if (table.getEventNotificaiton(EventNotificationTable.GPI_EVENT_TYPE)) {
			return true;
		} else
			return false;
	}

	private void sendGPIEvent(GPIOController c, int port) {
		logger.debug("GPIEvent should be fired");
		ReaderEventNotificationData rend = new ReaderEventNotificationData();
		GPIEvent gpievent = new GPIEvent();
		gpievent.setGPIEvent(c.getGPIState(port));
		gpievent.setGPIPortNumber((short) this.GPIPortNumber);
		rend.setGPIEventParam(gpievent);
		LLRPReportController controller = LLRPReportControllerFactory
				.getInstance().getReportController(llrpsr.getReaderName());
		controller.sendEvent(rend);

		// sleep to make sure the event notification is sent out
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean getGPIConfig() {
		return GPIConfig;
	}

	public void setGPIConfig(boolean config) {
		GPIConfig = config;
	}

}
