/*
 *  IParentEntity.java
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

import org.rifidi.designer.entities.VisualEntity;

/**
 * This interface describes the parent of a {@link IChildEntity}.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 5, 2008
 * 
 */
public interface IParentEntity {
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
