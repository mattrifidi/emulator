/**
 * 
 */
package org.rifidi.ui.console.service;

/**
 * @author kyle
 * 
 */
public interface ConsoleService {

	public void createConsole(String consoleName);

	public void destroyConsole(String consoleName);

	public void write(String consoleName, String msg);

}
