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
package org.rifidi.designer.library.basemodels.boxproducer;

import java.util.List;

import org.rifidi.designer.library.basemodels.cardbox.CardboxEntity;
import org.rifidi.designer.services.core.entities.ProductService;
import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.factory.TagCreationPattern;
import org.rifidi.services.tags.id.TagType;
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
public class BoxproducerEntityThread extends Thread {
	/**
	 * Start rotation.
	 */
	private Matrix3f slightRotMtx;
	/**
	 * Flag for killing thread.
	 */
	private boolean keepRunning = true;
	/**
	 * Flag for pausing thread.
	 */
	private boolean paused = true;
	/**
	 * Backreference to the producer.
	 */
	private BoxproducerEntity entity;
	/**
	 * Production intervall length.
	 */
	private Integer interval;
	/**
	 * Produced entities.
	 */
	private List<CardboxEntity> products;
	/**
	 * Reference to the product service.
	 */
	private ProductService productService;

	/**
	 * Constructor.
	 * 
	 * @param entity
	 *            back reference to the producer entity
	 * @param productService
	 *            reference to the product service
	 */
	public BoxproducerEntityThread(BoxproducerEntity entity,
			ProductService productService, List<CardboxEntity> products) {
		super();
		this.entity = entity;
		this.products = products;
		this.productService = productService;
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
		TagCreationPattern pattern = new TagCreationPattern();
		pattern.setNumberOfTags(10);
		pattern.setTagGeneration(TagGen.GEN2);
		pattern.setTagType(TagType.DoD96);
		List<RifidiTag> tagCache = entity.getTagRegistry().createTags(pattern);
		int counter = 0;
		while (keepRunning) {
			if (!paused) {
				RifidiTag name = null;
				if (counter != 10) {
					name = tagCache.get(counter++);
				}
				CardboxEntity ca = new CardboxEntity();
				ca.setBaseRotation(slightRotMtx);
				ca.setName(name.toString());
				ca.setUserData(name);
				Vector3f startPos = (Vector3f) entity.getNode()
						.getLocalTranslation().clone();
				startPos.setY(10);
				ca.setStartPos(startPos);
				productService.addProduct(ca);
				products.add(ca);
				if (counter == 10) {
					counter = 0;
					tagCache = entity.getTagRegistry().createTags(pattern);
				}
			}

			try {
				sleep(getInterval());
			} catch (InterruptedException e) {
				e.printStackTrace();
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
	 * @return the keepRunning
	 */
	public boolean isKeepRunning() {
		return keepRunning;
	}

	/**
	 * @param keepRunning
	 *            the keepRunning to set
	 */
	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
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

	/**
	 * @return the products
	 */
	public List<CardboxEntity> getProducts() {
		return products;
	}

}
