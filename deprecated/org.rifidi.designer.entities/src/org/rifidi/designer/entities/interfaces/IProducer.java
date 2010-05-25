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

/**
 * Interface for entites that produce other visual entities.
 * 
 * @author Jochen Mader - jochen@pramari.com - Nov 27, 2008
 * 
 */
@SuppressWarnings("unchecked")
public interface IProducer<T extends IProduct> {
	/**
	 * Get the list of produced entites.
	 * @return
	 */
	public List<T> getProducts();
	
	/**
	 * Set the list of produced entites
	 * @param entities
	 */
	public void setProducts(List<T> entities);
	
	/**
	 * Return a product to the producer.
	 */
	public void productDestroied(T product);
}
