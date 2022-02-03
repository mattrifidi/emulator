/*
 *  RegistryChangeListener.java
 *
 *  Created:	Mar 9, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.registry;

/**
 * Interface to be implemented by classes that have to listen for changes in a
 * registry.
 * 
 * e.g. ReaderRegistry
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */

public interface RegistryChangeListener {

	/**
	 * Fired when a new reader gets added to the registry.
	 * 
	 * @param event
	 */
	public void add(Object event);

	/**
	 * Fired whewn a reader is removed from the registry.
	 * 
	 * @param event
	 */
	public void remove(Object event);

	/**
	 * Fired when a reader gets updated in the registry.
	 * 
	 * @param event
	 */
	public void update(Object event);
}
