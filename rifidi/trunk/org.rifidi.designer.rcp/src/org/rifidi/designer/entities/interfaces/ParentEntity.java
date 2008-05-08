/*
 *  ParentEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

import java.util.List;

import org.rifidi.designer.entities.VisualEntity;

/**
 * This interface describes the parent of a {@link ChildEntity}.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 5, 2008
 * 
 */
public interface ParentEntity {
	/**
	 * Get the list of child entities.
	 * 
	 * @return
	 */
	public List<VisualEntity> getChildEntites();

	/**
	 * Set the list of child entities
	 * 
	 * @param children
	 */
	public void setChildEntites(List<VisualEntity> children);
}
