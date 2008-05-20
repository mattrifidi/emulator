package org.rifidi.emulator.reader.llrp.properties;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sharedrc.GPIO.GPIOController;

public class GPOSettings implements Observer {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(GPOSettings.class);
	
	private static Log eventLogger = LogFactory.getLog("EventLogger");
	
	/**
	 * The port number. 0 is illegal
	 */
	private int GPOPortNumber;

	/**
	 * The state of this GPOPort
	 */
	private boolean GPOData;

	/**
	 * 
	 * @param portNum
	 *            The port num. 0 is illegal
	 * @param controller
	 *            The GPIOController
	 */
	public GPOSettings(int portNum, GPIOController controller) {
		this.GPOPortNumber = portNum;
		// gpo ports are numbered starting at 0 in the controller
		this.GPOData = controller.getGPOState(portNum - 1);
		controller.addObserver(this);
	}

	public int getGPOPort() {
		return this.GPOPortNumber;
	}

	@SuppressWarnings("unchecked")
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof GPIOController) {
			GPIOController c = (GPIOController) arg0;
			ArrayList<Object> args = (ArrayList<Object>) arg1;
			String event = (String) args.get(0);
			int portNum = (Integer)args.get(1);
			if (event.equals(GPIOController.GPO_EVENT)
					&& ((portNum + 1) == this.GPOPortNumber)) {
				Integer emulator_port = (Integer) args.get(1);
				boolean newState = c.getGPOState(emulator_port);
				String highOrLow;
				if (newState) {
					highOrLow = "high";
				} else {
					highOrLow = "low";
				}
				eventLogger.info("[GPO EVENT]: LLRP GPO"
						+ " port " + (portNum + 1) + " "+ highOrLow);
				this.GPOData=newState;
			}
		}

	}

	public void setGPOData(boolean data) {
		GPOData = data;
	}

	public boolean getGPOData() {
		return GPOData;
	}

}
