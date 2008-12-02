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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.adapters.EntityGroupActionFilterAdapter;
import org.rifidi.designer.entities.adapters.EntityGroupWorkbenchAdapter;
import org.rifidi.designer.entities.databinding.IEntityObservable;
import org.rifidi.designer.entities.interfaces.IHasSwitch;

/**
 * This is a container for entities. It is used to organize entities into
 * logical groups.
 * 
 * @author Jochen Mader Nov 14, 2007
 * 
 */

@SuppressWarnings("unchecked")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityGroup implements IAdaptable, IEntityObservable {
	/**
	 * Logger for this class.
	 */
	@XmlTransient
	private static Log logger = LogFactory.getLog(EntityGroup.class);
	/**
	 * Name of this group.
	 */
	@XmlID
	private String name;
	/**
	 * The scene this group is a part of.
	 */
	@XmlTransient
	private SceneData sceneData;
	/**
	 * The entities in this group.
	 */
	@XmlIDREF
	private List<Entity> entities = new WritableList();
	/**
	 * A flag that indicates if this group is locked or not.
	 */
	private boolean locked = false;

	/**
	 * @return the name
	 */
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

	// /**
	// * Returns a list that can be monitored for changes. Do not write in it!!
	// *
	// * @return
	// */
	// public IObservableList getObservableEntities() {
	// return (IObservableList) entities;
	// }

	/**
	 * Returns the list of entities contained in this group.
	 * 
	 * @return the entities
	 */
	public List<Entity> getEntities() {
		return new ArrayList<Entity>(entities);
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(final List<Entity> entities) {
		this.entities.clear();
		this.entities.addAll(entities);
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
	 * Add entities to the group.
	 * 
	 * @param entity
	 *            the entity to be added.
	 */
	public void addEntities(final List<Entity> addentities) {
		if (!((WritableList) entities).getRealm().equals(Realm.getDefault())) {
			((WritableList) entities).getRealm().asyncExec(new Runnable() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					entities.addAll(addentities);
				}

			});
			return;
		}
		entities.addAll(addentities);
	}

	/**
	 * Remove an entity from the group.
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
	 * Remove entities from the group.
	 * 
	 * @param entity
	 *            the entity to be removed.
	 */
	public void removeEntities(final List<Entity> rementities) {
		if (!((WritableList) entities).getRealm().equals(Realm.getDefault())) {
			((WritableList) entities).getRealm().asyncExec(new Runnable() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					entities.removeAll(rementities);
				}

			});
			return;
		}
		entities.removeAll(rementities);
	}

	/**
	 * @return the sceneData
	 */
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
			if (entity instanceof IHasSwitch) {
				((IHasSwitch) entity).turnOn();
			}
		}
	}

	/**
	 * Stop all entities in this group.
	 */
	public void stop() {
		for (Entity entity : entities) {
			if (entity instanceof IHasSwitch) {
				((IHasSwitch) entity).turnOff();
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
			if (e instanceof IHasSwitch)
				return true;
			else
				logger.warn(e.getName() + " not instanceof IHasSwitch");
		return false;
	}

	/**
	 * Check if the given entity is part of this group.
	 * 
	 * @param entity
	 * @return
	 */
	public boolean contains(Entity entity) {
		return entities.contains(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.designer.entities.interfaces.IEntityObservable#
	 * addListChangeListener
	 * (org.eclipse.core.databinding.observable.list.IListChangeListener)
	 */
	@Override
	public void addListChangeListener(IListChangeListener changeListener) {
		((WritableList) entities).addListChangeListener(changeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.designer.entities.interfaces.IEntityObservable#
	 * removeListChangeListener
	 * (org.eclipse.core.databinding.observable.list.IListChangeListener)
	 */
	@Override
	public void removeListChangeListener(IListChangeListener changeListener) {
		((WritableList) entities).removeListChangeListener(changeListener);
	}

}
