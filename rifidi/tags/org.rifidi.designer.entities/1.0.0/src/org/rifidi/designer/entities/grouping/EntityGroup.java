/*
 *  EntityGroup.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.grouping;

import java.util.List;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.adapters.EntityGroupActionFilterAdapter;
import org.rifidi.designer.entities.adapters.EntityGroupWorkbenchAdapter;
import org.rifidi.designer.entities.databinding.annotations.MonitorThisList;
import org.rifidi.designer.entities.interfaces.Switch;

/**
 * This is a container for entities. It is used to organize entities into
 * logical groups.
 * 
 * @author Jochen Mader Nov 14, 2007
 * 
 */

@SuppressWarnings("unchecked")
@MonitorThisList(name = "entities")
public class EntityGroup implements IAdaptable {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(EntityGroup.class);
	/**
	 * Name of this group.
	 */
	private String name;
	/**
	 * The scene this group is a part of.
	 */
	private SceneData sceneData;
	/**
	 * The entities in this group.
	 */
	private List<Entity> entities = new WritableList();
	/**
	 * A flag that indicates if this group is locked or not.
	 */
	private boolean locked = false;

	/**
	 * @return the name
	 */
	@XmlID
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Returns a write protected list of entities. This should only be used by
	 * JAXB.
	 * 
	 * @return the entities
	 */
	@XmlIDREF
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	@SuppressWarnings("unchecked")
	public void setEntities(final List<Entity> entities) {
		WritableList writableEntities = new WritableList(entities, Entity.class);
		writableEntities.getRealm();
		this.entities = writableEntities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof EntityGroup) {
			return obj.hashCode() == hashCode();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(final Class adapter) {
		if (IWorkbenchAdapter.class.equals(adapter)) {
			return new EntityGroupWorkbenchAdapter();
		}
		if (IActionFilter.class.equals(adapter)) {
			return new EntityGroupActionFilterAdapter();
		}
		return null;
	}

	/**
	 * Add an entity to the group.
	 * 
	 * @param entity
	 *            the entity to be added.
	 */
	public void addEntity(final Entity entity) {
		if (!((WritableList) entities).getRealm().equals(Realm.getDefault())) {
			((WritableList) entities).getRealm().asyncExec(new Runnable() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					entities.add(entity);
				}

			});
			return;
		}
		entities.add(entity);
	}

	/**
	 * Remove an entity to the group.
	 * 
	 * @param entity
	 *            the entity to be removed.
	 */
	public void removeEntity(final Entity entity) {
		if (!((WritableList) entities).getRealm().equals(Realm.getDefault())) {
			((WritableList) entities).getRealm().asyncExec(new Runnable() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					entities.remove(entity);
				}

			});
			return;
		}
		entities.remove(entity);
	}

	/**
	 * @return the sceneData
	 */
	@XmlTransient
	public SceneData getSceneData() {
		return sceneData;
	}

	/**
	 * @param sceneData
	 *            the sceneData to set
	 */
	public void setSceneData(final SceneData sceneData) {
		this.sceneData = sceneData;
	}

	/**
	 * Start all entities in this group.
	 */
	public void start() {
		for (Entity entity : entities) {
			if (entity instanceof Switch) {
				((Switch) entity).turnOn();
			}
		}
	}

	/**
	 * Stop all entities in this group.
	 */
	public void stop() {
		for (Entity entity : entities) {
			if (entity instanceof Switch) {
				((Switch) entity).turnOff();
			}
		}
	}

	/**
	 * @return the locked
	 */
	public boolean getLocked() {
		return locked;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return true if this entity group contains any entities that are
	 *         switchable
	 */
	public boolean hasSwitchables() {
		for (Entity e : entities)
			if (e instanceof Switch)
				return true;
			else
				logger.warn(e.getName() + " not instanceof Switch");
		return false;
	}

}
