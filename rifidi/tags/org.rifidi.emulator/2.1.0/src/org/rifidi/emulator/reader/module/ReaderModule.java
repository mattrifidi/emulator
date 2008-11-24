/*
 *  ReaderModule.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

/**
 * Interface that the modules for all readers should follow.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public interface ReaderModule {

	/**
	 * Return the name of the reader
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Returns a list of all commands bound to there DisplayNames.
	 * 
	 * @return
	 */
	public HashMap<String, CommandObject> getCommandList();

	/**
	 * Returns a list of all categories.
	 * 
	 * @return
	 */
	public Collection<String> getAllCategories();

	/**
	 * Gets all commands that are listed under the given category.
	 * 
	 * @param category
	 * @return
	 */
	public Collection<CommandObject> getCommandsByCategory(String category);

	/**
	 * Returns the shared resources of the reader.
	 * 
	 * @return
	 */
	public AbstractReaderSharedResources getSharedResources();

	/**
	 * Turns the reader on.
	 */
	public void turnOn();

	/**
	 * Turns the reader off.
	 */
	@SuppressWarnings("unchecked")
	public void turnOff(Class callingClass);

	/**
	 * Suspends the reader.
	 */
	public void suspend();

	/**
	 * Restarts the reader.
	 */
	public void resume();

	/**
	 * Cleans up any loose ends that the reader has if it closes.
	 */
	public void finalize();

	/**
	 * Returns the GPI port numbers in the standard the reader uses. If the
	 * reader does not support GPI, simply return null.
	 * 
	 * @return
	 */
	public List<String> getGPIPortNumbers(int numberOfPorts);
	
	/**
	 * Returns the GPO port numbers in the standard the reader uses. If the
	 * reader does not support GPI, simply return null.
	 * 
	 * @return
	 */
	public List<String> getGPOPortNumbers(int numberOfPorts);
}
