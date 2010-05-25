/*
 *  IProduct.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

/**
 * This interface describs a product.A product is an entity that got produced
 * during the run of a scene.
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 4, 2008
 * 
 */
@SuppressWarnings("unchecked")
public interface IProduct<T extends IProducer> {
	public T getProducer();
}
