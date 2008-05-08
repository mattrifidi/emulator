package org.rifidi.utilities.text;

/**
 * This exception indicates that an attempt to create a group that already
 * exists occurred
 * 
 * @author Dan West - dan@pramari.com
 */
@SuppressWarnings("serial")
public class GroupAlreadyExistsException extends Exception {

	/**
	 * Constructor for the exception
	 * 
	 * @param groupName
	 *            the name of the group that triggered this exception
	 */
	public GroupAlreadyExistsException(String groupName) {
		super("A group named \'" + groupName + "\' already exists!");
	}
}