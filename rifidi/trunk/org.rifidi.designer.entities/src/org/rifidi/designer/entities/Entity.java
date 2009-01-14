/*
 *  Entity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

import org.rifidi.designer.entities.annotations.Property;

/**
 * This is the base class for all entities in the virtualization.
 * 
 * @author Jochen Mader Oct 3, 2007
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Entity {

	/** The name of this entity, doesn't have to be unique. */
	private String name;
	/** This id has to be unique inside each project. */
	@XmlID
	private String entityId;
	/** Support for monitoring property changes. */
	@XmlTransient
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);
	/** The user data object for this entity. */
	private Object userData;
	/** Set to true if the entity is delted. */
	private boolean deleted = false;
	/** Used to hide an entity from the entities view. */
	protected boolean visible = true;

	/**
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(final String entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@Property(displayName = "Name", description = "guess what", readonly = false, unit = "")
	public final void setName(final String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	/**
	 * This method ends the lifecycle of the entity. All cleanup should take
	 * place in this method
	 */
	public abstract void destroy();

	/**
	 * Inform listeners about a change to a property.
	 * 
	 * @param name
	 * @param oldValue
	 * @param newValue
	 */
	protected void firePropertyChange(final String name, final Object oldValue,
			final Object newValue) {
		changeSupport.firePropertyChange(name, oldValue, newValue);
	}

	/**
	 * Add a listener for changes to a property.
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * Add a listener for changes to a property.
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(
			final PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	/**
	 * @return the userData
	 */
	public Object getUserData() {
		return this.userData;
	}

	/**
	 * @param userData
	 *            the userData to set
	 */
	public void setUserData(Object userData) {
		this.userData = userData;
	}

	/**
	 * As it might happen that a controlelr in eclipse holds a reference to an
	 * entity that got deleted by someone else we need to check if the entity is
	 * still valid.
	 * 
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return this.deleted;
	}

	/**
	 * Set to true to indicate that this entity is delted and shouldn't be used
	 * anymore.
	 * 
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Start the entity. Intended to be overwritten.
	 */
	public void start() {
	}

	/**
	 * Pause an entity. On a subsequent start the entity should resume on the
	 * point where it was paused. Intended to be overwritten.
	 */
	public void pause() {
	}

	/**
	 * Stop the entity, should also reset internal state. Intended to be
	 * overwritten.
	 */
	public void reset() {
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return this.visible;
	}
	
}