/**
 * 
 */
package org.rifidi.ui.console.service.impl;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.rifidi.ui.console.service.ConsoleService;

/**
 * @author kyle
 * 
 */
public class ConsoleServiceImpl implements ConsoleService {

	private HashMap<String, MessageConsoleStream> streams = new HashMap<String, MessageConsoleStream>();
	private HashMap<String, MessageConsole> consoles = new HashMap<String, MessageConsole>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.console.service.ConsoleService#createConsole(java.lang.
	 * String)
	 */
	@Override
	public void createConsole(String consoleName) {
		// TODO: abstract this into a service that prototyper plugin provides!
		MessageConsole console = new MessageConsole(consoleName, null);
		consoles.put(consoleName, console);
		streams.put(consoleName, console.newMessageStream());
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[] { console });

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.console.service.ConsoleService#destroyConsole(java.lang
	 * .String)
	 */
	@Override
	public void destroyConsole(String consoleName) {
		MessageConsole console = consoles.remove(consoleName);
		if (console != null) {
			console.destroy();
			ConsolePlugin.getDefault().getConsoleManager().removeConsoles(
					new IConsole[] { console });
		}
		try {
			streams.remove(consoleName).close();
		} catch (IOException e) {
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.console.service.ConsoleService#write(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void write(String consoleName, String msg) {
		MessageConsoleStream stream = streams.get(consoleName);
		if (stream != null) {
			stream.print(msg);
		}

	}

}
