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
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyChangeSupport;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.rifidi.designer.annotations.Property;
import org.rifidi.designer.services.core.events.EventsService;
import org.rifidi.services.annotations.Inject;

/**
 * This is the base class for all entities in the virtualization.
 * 
 * @author Jochen Mader Oct 3, 2007
 * 
 */
@XmlSeeAlso( {
		org.rifidi.designer.entities.internal.WatchAreaEntity.class,
		org.rifidi.designer.entities.CableEntity.class})
public abstract class Entity {

	/**
	 * The name of this entity, doesn't have to be unique.
	 */
	private String name;
	/**
	 * This id has to be unique inside each project.
	 */
	private String entityId;
	/**
	 * Support for monitoring property changes.
	 */
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);
	/**
	 * The user data object for this entity.
	 */
	private Object userData;

	/**
	 * Reference to the events service.
	 */
	private EventsService eventsService;

	/**
	 * @return the entityId
	 */
	@XmlID
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
	public void addPropertyChangeListener(
			final PropertyChangeListenerProxy listener) {
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
	 * @return the eventsService
	 */
	protected EventsService getEventsService() {
		return this.eventsService;
	}

	/**
	 * @param eventsService the eventsService to set
	 */
	@XmlTransient
	@Inject
	public void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}
}