/*
 *  IProducer.java
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
 * Interface for entites that produce other visual entities.
 * 
 * @author Jochen Mader - jochen@pramari.com - Nov 27, 2008
 * 
 */
public interface IProducer {
	/**
	 * Get the list of produced entites
	 * @return
	 */
	public List<VisualEntity> getProducts();
	
	/**
	 * Set the list of produced entites
	 * @param entities
	 */
	public void setProducts(List<VisualEntity> entities);
}
