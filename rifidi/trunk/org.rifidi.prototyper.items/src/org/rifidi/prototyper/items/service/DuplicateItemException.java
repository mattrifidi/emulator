/**
 * 
 */
package org.rifidi.prototyper.items.service;

/**
 * Thrown if two instances of an item are registered
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class DuplicateItemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DuplicateItemException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DuplicateItemException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DuplicateItemException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DuplicateItemException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
