/*
 *  ProductService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.entities;

import java.util.List;

import org.rifidi.designer.entities.interfaces.AbstractVisualProduct;

/**
 * This interface is used to allow producers to create new entities while the
 * scene is runnung. The implementation of this interface must guarantee thread
 * safety.
 * 
 * @author Jochen Mader Jan 29, 2008
 * @tags
 * 
 */
public interface ProductService {

	/**
	 * Add a new product to the scene.
	 * 
	 * @param product
	 */
	void addProduct(AbstractVisualProduct product);

	/**
	 * Delete the given entities
	 * 
	 * @param product
	 */
	void deleteProducts(List<AbstractVisualProduct> product);
}
