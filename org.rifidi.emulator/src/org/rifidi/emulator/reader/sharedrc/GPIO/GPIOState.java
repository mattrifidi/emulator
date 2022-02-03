/**
 * 
 */
package org.rifidi.emulator.reader.sharedrc.GPIO;

/**
 * 
 * This class represents a single GPI/O port
 * 
 * @author kyle
 * 
 */
public class GPIOState {

	/**
	 * The state of the port
	 */
	private boolean state = false;

	/**
	 * Set the state high
	 */
	public void setStateHigh() {
		state = true;

	}

	/**
	 * Set the state low
	 * 
	 * @throws GPIOPortNotEnabledException
	 */
	public void setStateLow() {
		state = false;

	}

	/**
	 * Get the state
	 * 
	 * @return
	 */
	public boolean getState() {
		return state;
	}
}
