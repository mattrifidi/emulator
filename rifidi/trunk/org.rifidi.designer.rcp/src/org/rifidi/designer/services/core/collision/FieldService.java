/*
 *  FieldService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.collision;

import java.util.List;

import org.rifidi.designer.entities.interfaces.Field;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;

/**
 * This service is responsible for monitoring entities for collisions. The
 * important thing is that it checks if an entity enters a field and monitors it
 * until it leaves the field.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 4, 2008
 * 
 */
public interface FieldService extends SceneDataChangedListener {
	/**
	 * Register a field for monitoring it for collision.
	 * 
	 * @param field
	 */
	public void registerField(Field field);

	/**
	 * Unregister a field.
	 * 
	 * @param field
	 */
	public void unregisterField(Field field);

	/**
	 * Returns a list containing all currently registered fields.
	 * 
	 * @return
	 */
	public List<Field> getCurrentFieldsList();

	/**
	 * Blocking method that is called by the {@link UpdateThread} to check if an
	 * entity has left a field.
	 */
	public void checkFields();
}
