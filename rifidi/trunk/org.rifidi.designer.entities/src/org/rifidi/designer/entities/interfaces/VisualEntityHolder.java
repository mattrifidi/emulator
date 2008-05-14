/*
 *  VisualEntityHolder.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

import java.util.List;
import java.util.Set;

import org.rifidi.designer.entities.VisualEntity;

/**
 * This interface defines an entity as a container for other entites. Example: A
 * clothing rack conatining clothin which can be dragged out or placed on it
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 26, 2008
 * 
 */
public interface VisualEntityHolder {
	/**
	 * Add a VisualEntity to the entity holder.
	 * 
	 * @param visualEntity
	 */
	void addVisualEntity(VisualEntity visualEntity);

	/**
	 * Remove a VisualEntity from the holder.
	 * 
	 * @return
	 */
	VisualEntity getVisualEntity();

	/**
	 * Remove a VisualEntity from the holder by its reference.
	 * 
	 * @param visualEntity
	 * @return
	 */
	VisualEntity getVisualEntity(VisualEntity visualEntity);

	/**
	 * Get the list of VisualEntities in the holder.
	 * 
	 * @return
	 */
	List<VisualEntity> getVisualEntityList();
	
	/**
	 * Check if the container is full.
	 * @return
	 */
	boolean isFull();
	
	/**
	 * Check if the given entity can be placed in this holder.
	 * @param visualEntity
	 * @return
	 */
	boolean accepts(VisualEntity visualEntity);
}
