/*
 *  WatchAreaEvent.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.internal;

import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.services.core.events.WorldEvent;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 26, 2008
 * 
 */
public class WatchAreaEvent extends WorldEvent {

	/**
	 * true if something entered the field, false if something left it.
	 */
	private boolean entered = false;
	/**
	 * The origin of the event.
	 */
	private WatchAreaEntity watchAreaEntity;
	/**
	 * The source of the collision.
	 */
	private Entity collider;

	/**
	 * @param entered
	 * @param watchAreaEntity
	 * @param collider
	 */
	public WatchAreaEvent(boolean entered, WatchAreaEntity watchAreaEntity,
			Entity collider) {
		super();
		this.entered = entered;
		this.watchAreaEntity = watchAreaEntity;
		this.collider = collider;
	}

	/**
	 * @return the entered
	 */
	public boolean isEntered() {
		return this.entered;
	}

	/**
	 * @return the watchAreaEntity
	 */
	public WatchAreaEntity getWatchAreaEntity() {
		return this.watchAreaEntity;
	}

	/**
	 * @return the collider
	 */
	public Entity getCollider() {
		return this.collider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.events.WorldEvent#toString()
	 */
	@Override
	public String toString() {
		if(isEntered()){
			return collider.getName()+" has entered "+watchAreaEntity.getName();
		}
		return collider.getName()+" has left "+watchAreaEntity.getName();
	}

}
