package org.rifidi.emulator.tags.exceptions;

import org.rifidi.emulator.tags.enums.TagErrors;

/**
 * Exception class for memory related exceptions.
 * @author Jochen Mader
 *
 */
public class InvalidMemoryAccessException extends Exception{
	
	private static final long serialVersionUID = 1L;

	private TagErrors error;
	
	public InvalidMemoryAccessException(TagErrors error) {
		super();
		this.error=error;
	}

	/**
	 * @return the error
	 */
	public TagErrors getError() {
		return error;
	}
}
