/**
 * 
 */
package org.rifidi.emulator.reader.alien.autonomous.states;

/**
 * 
 * This internface specifies the methods that each class that is an automode
 * state should implement
 * 
 * @author Kyle Neumeier - Kyle@pramari.com
 * 
 */
public interface AutoState {

	/**
	 * This method begins the state
	 */
	public void beginState();

	/**
	 * This method ends the state
	 */
	public void stopState();

}
