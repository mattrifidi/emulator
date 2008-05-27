package org.rifidi.streamer.exceptions;

public class NotInitializedException extends Exception {
	
	/**
	 * Serial Number (not really used)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Internal Error description
	 */
	private String message;

	public NotInitializedException(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
