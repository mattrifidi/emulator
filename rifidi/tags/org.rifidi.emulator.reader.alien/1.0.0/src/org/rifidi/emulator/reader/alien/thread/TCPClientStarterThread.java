/**
 * 
 */
package org.rifidi.emulator.reader.alien.thread;

import org.rifidi.emulator.common.ControlSignal;

/**
 * This class will start the TCPClient for the alien reader using a thread,
 * which will allow the method to return quickly.
 * 
 * @author Matthew
 */
public class TCPClientStarterThread extends Thread {

	private ControlSignal<Boolean> one;

	private ControlSignal<Boolean> two;

	/**
	 * Constructor for the TCPClientStarterThread. The ControlSignals are
	 * assumed to have the value <b>false</b> and will be switched to <b>true</b>.
	 * 
	 * @param notifyControlSignal
	 * @param autoControlSignal
	 */
	public TCPClientStarterThread(ControlSignal<Boolean> notifyControlSignal,
			ControlSignal<Boolean> autoControlSignal) {
		this.one = notifyControlSignal;
		this.two = autoControlSignal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		one.setControlVariableValue(true);
		two.setControlVariableValue(true);
	}
}
