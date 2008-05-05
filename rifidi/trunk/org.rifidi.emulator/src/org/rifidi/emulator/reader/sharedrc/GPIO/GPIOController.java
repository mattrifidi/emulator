/**
 * 
 */
package org.rifidi.emulator.reader.sharedrc.GPIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.rmi.client.ClientCallbackInterface;

/**
 * 
 * This class controls access to the GPIOPorts. Classes who are interested in
 * being updated when a GPIO port changes should register themselves with this
 * object. Then, when a port changes state, the controller will notify them. In
 * addition in will send some arguments in the update method. The first argument
 * is a string that tells the observer if the event is a GPI event or a GPO
 * event. The second argument tells the observer which port changed states
 * 
 * @author kyle
 * 
 */
public class GPIOController extends Observable {
	
	private static Log logger = LogFactory.getLog(GPIOController.class);

	private static Log eventLogger = LogFactory.getLog("EventLogger");

	public final static String GPI_EVENT = "GPI_EVENT";

	public final static String GPO_EVENT = "GPO_EVENT";

	private HashMap<Integer, GPIOState> gpiSignals;

	private HashMap<Integer, GPIOState> gpoSignals;

	public ClientCallbackInterface callbackInterface;

	private boolean suspended = false;
	
	/**
	 * The name of this reader
	 */
	private String readerName;

	public GPIOController(String readerName) {
		gpiSignals = new HashMap<Integer, GPIOState>();
		gpoSignals = new HashMap<Integer, GPIOState>();
		this.readerName = readerName;
	}

	public void addGPIPort(int portNum) {
		gpiSignals.put(portNum, new GPIOState());
	}

	public void setGPIHigh(int portNum) {
		if (gpiSignals.get(portNum) != null) {
			if (!gpiSignals.get(portNum).getState()) {

				eventLogger.info("[GPI EVENT]:Rifidi Engine GPI port "
						+ portNum + " high on reader " + readerName);
				gpiSignals.get(portNum).setStateHigh();
				if (!suspended) {
					this.setChanged();
					this.notifyObervers(GPI_EVENT, portNum);
				}

			}
		} else {
			throw new IllegalArgumentException("GPIPort " + portNum
					+ " is not a valid GPIPort");
		}
	}

	public void setGPILow(int portNum) {
		if (gpiSignals.get(portNum) != null) {
			if (gpiSignals.get(portNum).getState()) {
				eventLogger.info("[GPI EVENT]:Rifidi Engine GPI port "
						+ portNum + " low on reader " + readerName );
				gpiSignals.get(portNum).setStateLow();
				if (!suspended) {
					this.setChanged();
					this.notifyObervers(GPI_EVENT, portNum);
				}

			}
		} else {
			throw new IllegalArgumentException("GPIPort " + portNum
					+ " is not a valid GPIPort");
		}
	}

	public boolean getGPIState(int portNum) {
		return gpiSignals.get(portNum).getState();
	}

	public int getNumGPIPorts() {
		return gpiSignals.size();
	}

	public boolean GPIPortExists(int portNum) {
		return this.gpiSignals.containsKey(portNum);
	}

	public void addGPOPort(int portNum) {
		gpoSignals.put(portNum, new GPIOState());
	}

	public void setGPOHight(int portNum) {
		if (gpoSignals.get(portNum) != null) {
			if (!gpoSignals.get(portNum).getState()) {
				eventLogger.info("[GPO EVENT]:Rifidi Engine GPI port "
						+ portNum + " high on reader " + readerName );
				gpoSignals.get(portNum).setStateHigh();

				this.setChanged();
				this.notifyObervers(GPO_EVENT, portNum);

				// make sure we have a callback interface
				if (callbackInterface != null) {
					callbackInterface.GPOPortSetHigh(portNum);
				}

			}

		} else {
			throw new IllegalArgumentException("GPOPort " + portNum
					+ " is not a valid GPOPort");
		}
	}

	public void setGPOLow(int portNum) {
		if (gpoSignals.get(portNum) != null) {
			if (gpoSignals.get(portNum).getState()) {

				eventLogger.info("[GPO EVENT]:Rifidi Engine GPI port "
						+ portNum + " low on reader " + readerName );
				gpoSignals.get(portNum).setStateLow();

				this.setChanged();
				this.notifyObervers(GPO_EVENT, portNum);
				if (callbackInterface != null) {
					callbackInterface.GPOPortSetLow(portNum);
				}

			}
		} else {
			throw new IllegalArgumentException("GPOPort " + portNum
					+ " is not a valid GPOPort");
		}
	}

	public boolean getGPOState(int portNum) {
		return gpoSignals.get(portNum).getState();
	}

	public int getNumGPOPorts() {
		return gpoSignals.size();
	}

	public boolean GPOPortExists(int gpoPortNum) {
		return this.gpoSignals.containsKey(gpoPortNum);
	}

	private void notifyObervers(String GPIO, int portNum) {

		ArrayList<Object> args = new ArrayList<Object>();
		args.add(GPIO);
		args.add(portNum);
		this.notifyObservers(args);
	}

	public void setCallbackInterface(ClientCallbackInterface callbackInterface) {
		this.callbackInterface = callbackInterface;
	}

	public void suspend() {
		logger.debug("GPIO suspended");
		this.suspended = true;
	}

	public void resume() {
		logger.debug("GPIO resumed");
		this.suspended = false;
	}

}
