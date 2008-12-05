/*
 *  GateEntityThread.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.boxproducercont;

import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.impl.LogKitLogger;
import org.rifidi.designer.entities.interfaces.AbstractVisualProduct;
import org.rifidi.designer.library.basemodels.cardbox.CardboxEntity;
import org.rifidi.designer.services.core.entities.ProductService;
import org.rifidi.services.tags.impl.RifidiTag;

import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * Thread for producing the box entites
 * 
 * @author Jochen Mader Oct 11, 2007
 * 
 */
public class BoxproducerContinuousEntityThread extends Thread {
	/** Start rotation. */
	private Matrix3f slightRotMtx;
	/** Flag for pausing thread. */
	private boolean paused = true;
	/** Backreference to the producer. */
	private BoxproducerContinuousEntity entity;
	/** Production intervall length. */
	private Integer interval;
	/** Produced entities. */
	private List<AbstractVisualProduct> products;
	/** Reference to the product service. */
	private ProductService productService;
	/** Stack for RifidiTags, shared with the entity. */
	private Stack<RifidiTag> tagStack;
	/** */
	private List<RifidiTag> tags;
	/**
	 * Constructor.
	 * 
	 * @param entity
	 *            back reference to the producer entity
	 * @param productService
	 *            reference to the product service
	 * @param products
	 * 
	 */
	public BoxproducerContinuousEntityThread(BoxproducerContinuousEntity entity,
			ProductService productService,
			List<AbstractVisualProduct> products, List<RifidiTag> tags) {
		super();
		this.entity = entity;
		this.products = products;
		this.productService = productService;
		this.tagStack = new Stack<RifidiTag>();
		this.tags=tags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Quaternion rot = new Quaternion();
		rot.fromAngleAxis(0.001f, Vector3f.UNIT_Y);
		slightRotMtx = rot.toRotationMatrix();
		while (!isInterrupted()) {
			if (!paused && !tagStack.isEmpty()) {
				RifidiTag name = tagStack.pop();
				CardboxEntity ca = new CardboxEntity();
				ca.setProducer(entity);
				ca.setBaseRotation(slightRotMtx);
				ca.setRifidiTag(name);
				Vector3f startPos = (Vector3f) entity.getNode()
						.getLocalTranslation().clone();
				startPos.setY(10);
				ca.setStartPos(startPos);
				productService.addProduct(ca);
				products.add(ca);
			}
			if(tagStack.isEmpty()){
				tagStack.addAll(tags);
			}
			try {
				sleep(getInterval());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}
	}

	/**
	 * Get the production interval length
	 * 
	 * @return interval length in seconds
	 */
	public Integer getInterval() {
		return interval;
	}

	/**
	 * Get the production interval length
	 * 
	 * @param interval
	 *            interval length in seconds
	 */
	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	/**
	 * @return the paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param paused
	 *            the paused to set
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
}
