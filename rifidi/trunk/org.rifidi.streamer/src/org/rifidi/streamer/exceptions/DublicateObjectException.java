/**
 * 
 */
package org.rifidi.streamer.exceptions;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class DublicateObjectException extends Exception {

	public enum Type {
		COMPONENT, SCENARIO, BATCH, TESTUNIT
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;
	private Type type;

	public DublicateObjectException(String message, Type type) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		if (message != null)
			return message;
		else
			return "Dublicate Object Exception";
	}

	public Type getExceptionType() {
		return type;
	}
}
