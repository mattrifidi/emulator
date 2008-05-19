/*
 *  BoxproducerEntityGID96.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.boxproducerGID96;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.interfaces.SceneControl;
import org.rifidi.designer.entities.interfaces.Switch;
import org.rifidi.designer.entities.placement.BinaryPattern;
import org.rifidi.designer.library.basemodels.cardbox.CardboxEntity;
import org.rifidi.designer.services.core.entities.ProductService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.tags.TagService;
import org.rifidi.utilities.node.NodeHelper;

import com.jme.bounding.BoundingBox;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jme.util.export.binary.BinaryImporter;

/**
 * BoxproducerEntityGID96: Used for generating boxes.
 * 
 * @author Jochen Mader Oct 8, 2007
 * @author Dan West
 */
@MonitoredProperties(names = { "IDGenerator", "name" })
public class BoxproducerEntityGID96 extends VisualEntity implements SceneControl,
		Switch {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(BoxproducerEntityGID96.class);
	/**
	 * Seconds per box.
	 */
	private float speed;
	/**
	 * Production thread.
	 */
	private BoxproducerEntityThread thread;
	/**
	 * State of the switch.
	 */
	private boolean running = false;
	/**
	 * Is the entity paused.
	 */
	private boolean paused = true;
	/**
	 * Source for shared meshes.
	 */
	private Node model;
	/**
	 * Reference to the product service.
	 */
	private ProductService productService;
	/**
	 * List of products this producer created.
	 */
	private List<CardboxEntity> products = new ArrayList<CardboxEntity>();
	
	private TagService tagService;
	
	/**
	 * Constructor
	 */
	public BoxproducerEntityGID96(){
		this.speed=2;
		setName("Boxproducer (GID96)");
	}
	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	@Property(displayName = "Production speed", description = "production rate of boxes", readonly = false, unit = "sec/box")
	public void setSpeed(float speed) {
		this.speed = speed;
		if (thread != null) {
			thread.setInterval((int) speed * 1000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		if (model == null) {
			URI modelpath = null;
			try {
				modelpath = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/basemodels/boxproducer/blankdisc.jme")
						// .getResource("org/rifidi/designer/library/basemodels/boxproducer/boxproducer_saucer.jme")
						.toURI();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			try {
				model = (Node) BinaryImporter.getInstance().load(
						modelpath.toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BinaryPattern pattern = new BinaryPattern();
		pattern.setPattern(new boolean[][] { { false, true, true, false },
				{ true, true, true, true }, { true, true, true, true },
				{ false, true, true, false }, });
		setPattern(pattern);
		setCollides(false);

		AlphaState as = DisplaySystem.getDisplaySystem().getRenderer()
				.createAlphaState();
		as.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		as.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		as.setBlendEnabled(true);
		as.setEnabled(true);

		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setDiffuse(new ColorRGBA(.2f, .75f, .8f, 1).multLocal(.7f));
		ms.setEnabled(true);
		model.setRenderState(ms);

		Node node = new Node(getEntityId());
		Node sharednode = new SharedNode("shared_", model);
		sharednode.setLocalTranslation(0, 12, 0);
		// sharednode.setRenderState(ms);
		node.attachChild(sharednode);

		node.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		node.setRenderState(as);

		node.setModelBound(new BoundingBox());
		node.updateModelBound();

		setNode(node);
		logger.debug(NodeHelper.printNodeHierarchy(getNode(), 3));

		thread = new BoxproducerEntityThread(this, productService, products);
		thread.setInterval((int) speed * 1000);
		thread.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		if (model == null) {
			URI modelpath = null;
			try {
				modelpath = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/basemodels/boxproducer/blankdisc.jme")
						// .getResource("org/rifidi/designer/library/basemodels/boxproducer/boxproducer_saucer.jme")
						.toURI();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			try {
				model = (Node) BinaryImporter.getInstance().load(
						modelpath.toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		thread = new BoxproducerEntityThread(this, productService, products);
		thread.setInterval((int) speed * 1000);
		thread.start();
		if (running)
			turnOn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Switch#turnOff()
	 */
	public void turnOff() {
		thread.setPaused(true);
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Switch#turnOn()
	 */
	public void turnOn() {
		if (!paused) {
			thread.setPaused(false);
		}
		running = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.SceneControl#pause()
	 */
	public void pause() {
		if (thread != null) {
			thread.setPaused(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#isRunning()
	 */
	public boolean isRunning() {
		return running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.SceneControl#start()
	 */
	public void start() {
		paused = false;
		if (running) {
			thread.setPaused(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.SceneControl#stop()
	 */
	public void reset() {
		paused = true;
		thread.setPaused(true);
		productService.deleteProducts(new ArrayList<Entity>(thread
				.getProducts()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		thread.setKeepRunning(false);
		getNode().removeFromParent();
	}

	public void setRunning(boolean newrunning) {
		running = newrunning;
	}

	/**
	 * Set the product service.
	 * @param productService
	 */
	@Inject
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	/**
	 * @return the tagService
	 */
	public TagService getTagService() {
		return tagService;
	}
	/**
	 * @param tagService the tagService to set
	 */
	@XmlTransient
	@Inject
	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}
	/**
	 * @return the products
	 */
	public List<CardboxEntity> getProducts() {
		return this.products;
	}

	/**
	 * @param products
	 *            the products to set
	 */
	@XmlIDREF
	public void setProducts(List<CardboxEntity> products) {
		this.products = products;
	}

}
