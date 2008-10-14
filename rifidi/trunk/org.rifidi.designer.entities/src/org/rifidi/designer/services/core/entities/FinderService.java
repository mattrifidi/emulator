/*
 *  FinderService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.entities;

import java.util.List;

import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.grouping.EntityGroup;

import com.jme.scene.Node;

/**
 * This service is used to find/get information from entities.
 * 
 * @author Jochen Mader Jan 29, 2008
 * @tags
 * 
 */
public interface FinderService {

	/**
	 * Check if the given node is associated with an entity
	 * 
	 * @param node
	 * @return
	 */
	VisualEntity getVisualEntityByNode(Node node);

	/**
	 * Check if the given group exists in the scene
	 * 
	 * @param entityGroup
	 * @return
	 */
	boolean entityGroupExists(EntityGroup entityGroup);

	/**
	 * Check if the entity is part of a group.
	 * 
	 * @param entity
	 * @return
	 */
	boolean isEntityGrouped(Entity entity);
	
	/**
	 * Get a list of entities depending on their type.
	 * @return
	 */
	List<Entity> getEntitiesByType(Class<?> type);
	
}
