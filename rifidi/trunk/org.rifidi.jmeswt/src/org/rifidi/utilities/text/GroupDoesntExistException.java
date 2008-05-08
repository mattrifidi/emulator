package org.rifidi.utilities.text;

/**
 * This exception occurrs when a group is requested but does not exist
 * 
 * @author Dan West - dan@pramari.com
 */
public class GroupDoesntExistException extends Exception {

	/**
	 * Constructor for the exception
	 * 
	 * @param group
	 *            the requested group that does not exist
	 */
	public GroupDoesntExistException(String group) {
		super("The requested group does not exist. (Group: " + group + ")");
	}
}