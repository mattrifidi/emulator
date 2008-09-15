package org.rifidi.emulator.reader.thingmagic.commandobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderSharedResources;

public class SetCommand implements Command {
	private static Log logger = LogFactory.getLog(SelectCommand.class);
	
	private String command;
	private ThingMagicReaderSharedResources tmsr;

	public SetCommand(String command, ThingMagicReaderSharedResources tmsr) {
		// TODO Auto-generated constructor stub
		this.command = command;
		this.tmsr = tmsr;
		
		List<String> tokens = new ArrayList<String>();

		logger.debug("Parsing command: " + command);

		Pattern tokenizer = Pattern.compile(
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
		Matcher tokenFinder = tokenizer.matcher(command.toLowerCase().trim());

		while (tokenFinder.find()) {
			String temp = tokenFinder.group();
			/*
			 * no need to add empty strings at tokens.
			 */
			// TODO: Figure out why we are getting empty stings as tokens.
			if (temp.equals(""))
				continue;
			tokens.add(temp);
		}

		ListIterator<String> tokenIterator = tokens.listIterator();

		String token = tokenIterator.next();
	}

	@Override
	public ArrayList<Object> execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toCommandString() {
		// TODO Auto-generated method stub
		return command;
	}

}
