/**
 * 
 */
package org.rifidi.emulator.scripting.groovy;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * An executor for Rifidi related groovy scripts. The scripts get a variable
 * with the name readerManager injected and can use it to manage readers.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public interface GroovyExecutor {

	/**
	 * Collect all results from she;lls that have finished executing and remove
	 * 
	 * @return
	 */
	Map<Integer, String> purge();

	/**
	 * Stop all scripts.
	 */
	void clear();

	/**
	 * Get ids of currently running scripts.
	 * 
	 * @return
	 */
	Set<Integer> getCurrentScriptIDs();

	/**
	 * Submit a string containing a groovy script.
	 * 
	 * @param script
	 * @return
	 */
	Integer submitScriptAsString(String script);

	/**
	 * Submit a file containing a groovy script.
	 * 
	 * @param script
	 * @return
	 */
	Integer submitScriptAsFile(File script);

	/**
	 * Get the result of a script that has executed. Null if the script is still
	 * executing.
	 * 
	 * @param scriptID
	 * @return
	 */
	String getResultFromScript(Integer scriptID);

	/**
	 * 
	 * @param scriptID
	 */
	void killScript(Integer scriptID);
}
