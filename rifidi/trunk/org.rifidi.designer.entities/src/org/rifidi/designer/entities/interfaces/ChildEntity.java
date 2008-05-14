/*
 *  ChildEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

import org.rifidi.designer.entities.VisualEntity;

/**
 * This interface describes an entity that is the child of another entity. That
 * means that this entities node is attached to the node of the parent.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 5, 2008
 * 
 */
public interface ChildEntity {
	/**
	 * Set the parent for this entity.
	 * 
	 * @param entity
	 */
	public void setParent(VisualEntity entity);

	/**
	 * Get the parent for this entity
	 * 
	 * @return
	 */
	public VisualEntity getParent();
}
