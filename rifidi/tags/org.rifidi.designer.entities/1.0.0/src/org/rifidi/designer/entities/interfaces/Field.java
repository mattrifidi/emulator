/*
 *  Field.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

import org.rifidi.designer.entities.Entity;

/**
 * A filed is a designated area in the scene that is monitored for entites
 * appearing in it.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 4, 2008
 * 
 */
public interface Field {
	/**
	 * Called by the FieldService if an entity has entered the field.
	 * 
	 * @param entity
	 */
	public void fieldEntered(Entity entity);

	/**
	 * Called if an entity has left the field.
	 * 
	 * @param entity
	 */
	public void fieldLeft(Entity entity);
}
