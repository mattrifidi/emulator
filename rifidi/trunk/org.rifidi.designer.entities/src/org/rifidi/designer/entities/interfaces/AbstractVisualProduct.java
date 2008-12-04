/*
 *  AbstractVisualProduct.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

import javax.xml.bind.annotation.XmlIDREF;

import org.rifidi.designer.entities.VisualEntity;

/**
 * Base class for products with a visual representation.
 * @see AbstractVisualProducer
 * @author Jochen Mader - jochen@pramari.com - Dec 4, 2008
 * 
 */
public abstract class AbstractVisualProduct extends VisualEntity implements IProduct<AbstractVisualProducer>{
	/**Reference to the producer that created this product.*/
	@XmlIDREF
	protected AbstractVisualProducer producer;

	/**
	 * @param producer the producer to set
	 */
	public void setProducer(AbstractVisualProducer producer) {
		this.producer = producer;
	}
	
	/* (non-Javadoc)
	 * @see org.rifidi.designer.entities.interfaces.IProduct#getProducer()
	 */
	@Override
	public AbstractVisualProducer getProducer() {
		return producer;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		producer.productDestroied(this);
	}

}
