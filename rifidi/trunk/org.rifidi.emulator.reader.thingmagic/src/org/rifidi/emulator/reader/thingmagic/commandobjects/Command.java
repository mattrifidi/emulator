package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;
import java.util.regex.Pattern;

public interface Command {
	static public Pattern TOKENIZER = Pattern.compile(
			//anything less...
			"[^\\s\\w,<>=\\(\\)\\u0027]|" +
			//groups we are looking for...
			"\\w+|" +
			"\\u0027|" +
			"\\s*<>\\*|" +
			"\\s*>=\\s*|" +
			"\\s*<=\\s*|" +
			"\\s*=\\s*|" +
			"\\s*,\\s*|" +
			"\\s*>\\s*|" +
			"\\s*<\\s*|" +
			"\\s?+|" +
			"\\(|" +
			"\\)|",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	
	public ArrayList<Object> execute();
	
	/**
	 * 
	 * @return the original unmodified command sent to
	 *  the constructor of the command object
	 */
	public String toCommandString();
}
