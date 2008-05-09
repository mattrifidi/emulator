/*
 *  CablingService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.cabling;
import java.util.List;

import org.rifidi.designer.entities.interfaces.GPI;
import org.rifidi.designer.entities.interfaces.GPO;
import org.rifidi.designer.entities.internal.CableEntity;

/**
 * A CablingService is responsible for handling the virtual cables between GPIO
 * enabled entities.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 27, 2008
 * 
 */
public interface CablingService {

	/**
	 * Create a new cable.
	 */
	void createCable(CableEntity cableEntity);

	/**
	 * Remove a cable.
	 * 
	 * @param cableEntity
	 */
	void destroyCable(CableEntity cableEntity);

	/**
	 * Check if the given cable already exists.
	 */
	boolean cableExists(CableEntity cableEntity);
	
	/**
	 * Recreate a cable.
	 * Should only be used if the cable got loaded back from storage.
	 */
	void recreateCable(CableEntity cableEntity);
	
	/**
	 * Set a GPI port to high.
	 * @param source the source GPO
	 * @param port the port to set high
	 */
	void setHigh(GPO source, int port);
	
	/**
	 * Set a GPI port to low.
	 * @param source the source GPO
	 * @param port the port to set low
	 */
	void setLow(GPO source, int port);
	
	/**
	 * Get the list of targets for the given GPO.
	 * @param gpo
	 * @return
	 */
	List<CableEntity> getTargets(GPO gpo);
	
	/**
	 * Get the list of sources for the given GPI.
	 * @param gpi
	 * @return
	 */
	List<CableEntity> getSources(GPI gpi);
	
	/**
	 * Add a listener to listen for cable changes.
	 * @param listener
	 */
	void addCableChangeListener(CableChangeListener cableChangeListener);
	
	/**
	 * Remove a listener to listen for cable changes.
	 * @param listener
	 */
	void removeCableChangeListener(CableChangeListener cableChangeListener);
}
