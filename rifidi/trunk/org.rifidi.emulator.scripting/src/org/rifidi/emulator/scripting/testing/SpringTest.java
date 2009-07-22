/**
 * 
 */
package org.rifidi.emulator.scripting.testing;

import org.rifidi.emulator.scripting.ReaderManager;

/**
 * @author Matthew Dean - matt@pramari.com
 *
 */
public class SpringTest {
	private ReaderManager scriptingReaderManager = null;

	/**
	 * @param scriptingReaderManager the scriptingReaderManager to set
	 */
	public void setScriptingReaderManager(ReaderManager scriptingReaderManager) {
		this.scriptingReaderManager = scriptingReaderManager;
		for(String i:this.scriptingReaderManager.getReaderTypes()) {
			System.out.println(i);
		}
	}
	
}
