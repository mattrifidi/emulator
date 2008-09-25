/*
 *  EntitiesService.java
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
import java.util.Set;

import org.eclipse.swt.graphics.Point;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.octree.CollisionOctree;
import org.rifidi.designer.octree.RoomOctree;

/**
 * This service is responsible for manipulating entities and their groups,
 * 
 * @author Jochen Mader Jan 25, 2008
 * @tags
 * 
 */
public interface EntitiesService {

	/**
	 * Add a new entity to the scene.
	 * 
	 * @param ent
	 * @param newEntityListener
	 *            a callback for the submitter of the new entity.
	 * @param screenPos
	 *            position on the screen where the entity should be created
	 *            (can be null)
	 */
	void addEntity(Entity ent, NewEntityListener newEntityListener,
			Point screenPos);

	/**
	 * Delete entities from the scene
	 * 
	 * @param entities
	 *            the entities to be deleted from the scene
	 */
	void deleteEntities(final List<Entity> entities);

	/**
	 * Add the given entities to the group.
	 * 
	 * @param entityGroup
	 * @param entityIds
	 *            a \n separated list of entity ids
	 */
	void addEntitiesToGroup(EntityGroup entityGroup, String entityIds);

	/**
	 * @return a list of all the entity names
	 */
	List<String> getEntityNames();

	/**
	 * Add a new EntityGroup to the scene
	 * 
	 * @param entityGroup
	 */
	void addEntityGroup(EntityGroup entityGroup);

	/**
	 * Remove an EntityGroup from the scene. Doesn't delete the entities in the
	 * group.
	 * 
	 * @param entityGroup
	 */
	void removeEntityGroup(EntityGroup entityGroup);

	/**
	 * Returns a write protexted list of entity groups
	 * 
	 * @return
	 */
	List<EntityGroup> getEntityGroups();

	/**
	 * Remove an entity from its group.
	 * 
	 * @param entity
	 */
	void ungroupEntity(Entity entity);

	/**
	 * Check for collisions. If none are found the returned list is empty.
	 * 
	 * @param visualEntity
	 * @return
	 */
	Set<VisualEntity> getColliders(VisualEntity visualEntity);

	/**
	 * Check if the given entity collides with a scene wall.
	 * 
	 * @param visualEntity
	 * @return
	 */
	boolean collidesWithScene(VisualEntity visualEntity);

	/**
	 * Get the current octree for collisions.
	 * 
	 * @return
	 */
	CollisionOctree getCollisionOctree();

	/**
	 * Get the current octree for room collisions.
	 * 
	 * @return
	 */
	RoomOctree getRoomOctree();
}
