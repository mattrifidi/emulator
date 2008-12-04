/*
 *  AbstractVisualProducer.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;

import org.rifidi.designer.entities.VisualEntity;

/**
 * Base class for producers that create visual entites.
 * 
 * @see AbstractVisualProduct
 * @author Jochen Mader - jochen@pramari.com - Dec 4, 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractVisualProducer extends VisualEntity implements
		IProducer<AbstractVisualProduct> {
	/** List of products this producer created. */
	@XmlIDREF
	protected List<AbstractVisualProduct> products = new ArrayList<AbstractVisualProduct>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IProducer#getProducts()
	 */
	@Override
	public List<AbstractVisualProduct> getProducts() {
		return new ArrayList<AbstractVisualProduct>(products);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.IProducer#setProducts(java.util
	 * .List)
	 */
	@Override
	public void setProducts(List<AbstractVisualProduct> entities) {
		products.clear();
		products.addAll(entities);
	}

}
