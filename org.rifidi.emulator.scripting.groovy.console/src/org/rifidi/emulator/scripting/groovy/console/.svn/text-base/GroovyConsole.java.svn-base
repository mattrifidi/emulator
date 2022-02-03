/**
 * 
 */
package org.rifidi.emulator.scripting.groovy.console;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.rifidi.emulator.scripting.groovy.GroovyExecutor;

/**
 * Command line commands for the edge server.
 * 
 * @author Jochen Mader - jochen@pramari.com
 */
public class GroovyConsole implements CommandProvider {

	private volatile GroovyExecutor executor;

	/**
	 * @param executor
	 *            the executor to set
	 */
	public void setExecutor(GroovyExecutor executor) {
		this.executor = executor;
	}

	/**
	 * Submit a test script to test the Groovy interpreter.
	 * 
	 * @param intp
	 * @return
	 */
	public Object _test(CommandInterpreter intp) {
		String groovyTest = "println 'Hello World!'";
		intp.println("Executing with id: "
				+ executor.submitScriptAsString(groovyTest));
		return null;
	}

	/**
	 * Get the ids of currently active commands.
	 * 
	 * @param intp
	 * @return
	 */
	public Object _getscriptids(CommandInterpreter intp) {
		for (Integer id : executor.getCurrentScriptIDs()) {
			intp.println("Executing with id: " + id);
		}
		return null;
	}

	/**
	 * Purge all scripts that are done executing.
	 * 
	 * @param intp
	 * @return
	 */
	public Object _purge(CommandInterpreter intp) {
		Map<Integer, String> results = executor.purge();
		intp.println("Results:");
		for (Entry<Integer, String> entry : results.entrySet()) {
			intp.println(entry.getKey() + " : " + entry.getValue());
		}
		for (Integer id : executor.getCurrentScriptIDs()) {
			intp.println("Executing with id: " + id);
		}
		return null;
	}

	/**
	 * Get the result of a script that is executing.
	 * 
	 * @param intp
	 * @return
	 */
	public Object _getresult(CommandInterpreter intp) {
		String param = intp.nextArgument();
		if (param == null) {
			intp.println("No script id given.");
			return null;
		}
		try {
			Integer id = Integer.parseInt(param);
			String result = executor.getResultFromScript(id);
			if (result == null) {
				result = "still executing";
			}
			intp.println("Result: " + result);
		} catch (NumberFormatException e) {
			intp.println(param + " is not a valid integer.");
		}
		return null;
	}

	/**
	 * Get the ids of currently active commands.
	 * 
	 * @param intp
	 * @return
	 */
	public Object _killscript(CommandInterpreter intp) {
		String param = intp.nextArgument();
		if (param == null) {
			intp.println("No script id given.");
			return null;
		}
		try {
			Integer id = Integer.parseInt(param);
			executor.killScript(id);
		} catch (NumberFormatException e) {
			intp.println(param + " is not a valid integer.");
		}
		return null;
	}

	/**
	 * Load a script from a file and execute it.
	 * 
	 * @param intp
	 * @return
	 */
	public Object _loadscript(CommandInterpreter intp) {
		String path = intp.nextArgument();
		if (path == null) {
			intp.println("No path given.");
			return null;
		}
		File file = new File(path);
		if (!file.isFile()) {
			intp.println(path + " is not a file.");
			return null;
		}
		if (!file.canRead()) {
			intp.println(path + " is not readable.");
			return null;
		}
		intp.println(executor.submitScriptAsFile(file));
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.framework.console.CommandProvider#getHelp()
	 */
	@Override
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("---Rifidi Groovy Console Commands---\n");
		buffer.append("  ----General commands----\n");
		buffer.append("\ttest - submit a Hello World script for testing\n");
		buffer
				.append("\tgetscriptids - sprint the list of currently registered scripts\n");
		buffer
				.append("\tkillscript <scriptid> - kill the script with the given id\n");
		buffer
				.append("\tloadscript <path> - load a script from the given path and execute it\n");
		buffer
				.append("\tpurge - clear shells that are done executing and display their results\n");
		buffer
				.append("\tgetresult  <scriptid> - get the result of a shell that is done executing\n");
		return buffer.toString();
	}
}
