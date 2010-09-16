package org.rifidi.prototyper.map.controller;

/**
 * An interface for classes to implement who want to be notified of changes to
 * the edit mode.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public interface EditModeListener {

	/**
	 * This method is called when there is a change to the edit mode.
	 * 
	 * @param toggle
	 *            - true if edit mode is turned on. False if it is turned off.
	 */
	public void setEditMode(boolean toggle);

}