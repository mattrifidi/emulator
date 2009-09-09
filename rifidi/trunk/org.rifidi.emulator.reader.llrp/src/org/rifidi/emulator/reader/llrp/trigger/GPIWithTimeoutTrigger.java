package org.rifidi.emulator.reader.llrp.trigger;

import java.util.Observable;
import java.util.Observer;

import org.rifidi.emulator.reader.llrp.properties.GPISettings;
import org.rifidi.utilities.Timer;

/**
 * A GPI trigger with a timeout.
 * 
 * The Timer works by adding adding itself as an observer of a timer. When the
 * timer, finishes, it notifies the obserers (i.e. this class) which then
 * updates the spec state that in turn causes the spec to stop executing
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author kyle
 */
public class GPIWithTimeoutTrigger implements TimerTrigger, Observer {

	private short portNum;

	private int timeout;

	private boolean event;

	TriggerObservable specSignal;

	private boolean fireTimer;

	private GPISettings gpiPort;

	boolean suspended = false;

	/**
	 * Timeout thread
	 */
	Timer timer_thread;

	public GPIWithTimeoutTrigger(short portNum, boolean event, int timeout,
			GPISettings gpiPort) {
		this.portNum = portNum;
		this.event = event;
		this.timeout = timeout;

		gpiPort.addObserver(this);
		this.gpiPort = gpiPort;

	}

	public void startTimer() {
		fireTimer = true;
		timer_thread = new Timer(timeout);
		timer_thread.addObserver(this);
		if (timeout > 0) {
			new Thread(timer_thread).start();
		}
		if (suspended) {
			// take care of case when suspended before timer is created
			timer_thread.suspend();
		}
	}

	public void stopTimer() {
		fireTimer = false;
		this.timer_thread.deleteObserver(this);
		this.timer_thread = null;
	}

	/**
	 * @return The duration of the timeout.
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @return the event
	 */
	public boolean isEvent() {
		return event;
	}

	/**
	 * @return the portNum
	 */
	public short getPortNum() {
		return portNum;
	}

	public void setTriggerObservable(TriggerObservable specSignal) {
		this.specSignal = specSignal;

	}

	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof Timer) {
			if (fireTimer) {
				this.specSignal.fireStopTrigger(this.getClass());
				fireTimer = false;
			}
		} else if (arg0 instanceof GPISettings) {
			GPISettings gpi = (GPISettings) arg0;
			if (shouldFireGPITrigger(gpi)) {
				if (specSignal.getState()) {
					fireTimer = false;
					this.specSignal.fireStopTrigger(this.getClass());
				} else {
					this.specSignal.fireStartTrigger(this.getClass());
				}
			}

		}
	}

	private boolean shouldFireGPITrigger(GPISettings gpi) {
		if (gpi.getPortNum() == this.portNum && specSignal != null) {
			if (gpi.getGPIState() == 1 && this.event) {
				return true;
			} else if (gpi.getGPIState() == 0 && !this.event) {
				return true;
			}
		}
		return false;
	}

	public void cleanUp() {
		if (gpiPort != null) {
			this.gpiPort.deleteObserver(this);
		}
		if (this.timer_thread != null) {
			this.timer_thread.deleteObserver(this);
			timer_thread.stop();
			timer_thread = null;
		}
	}

	public void resume() {
		suspended = false;
		if (timer_thread != null) {
			this.timer_thread.resume();
		}
	}

	public void suspend() {
		suspended = true;
		if (timer_thread != null) {
			this.timer_thread.suspend();
		}
	}
}
