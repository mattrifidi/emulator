package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;
import java.util.regex.Pattern;

public interface Command {

	static public String A_WORD = "\\w+";

	static public String WHITE_SPACE = "\\s+";

	static public String COMMA_WITH_WS = "\\s*,\\s*";

	static public String EQUALS_WITH_WS = "\\s*=\\s*";

	static public Pattern TOKENIZER = Pattern.compile(
			// anything less...
			"[^\\s\\w,<>=\\(\\)\\u0027]|"
					+
					// groups we are looking for...
					"\\w+|" + "\\u0027|" + "\\s*<>\\*|" + "\\s*>=\\s*|"
					+ "\\s*<=\\s*|" + "\\s*=\\s*|" + "\\s*,\\s*|"
					+ "\\s*>\\s*|" + "\\s*<\\s*|" + "\\s?+|" + "\\(|" + "\\)|",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	public ArrayList<Object> execute();

	/**
	 * 
	 * @return the original unmodified command sent to the constructor of the
	 *         command object
	 */
	public String toCommandString();
}
