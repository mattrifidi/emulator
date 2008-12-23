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
package org.rifidi.designer.library.basemodels.boxproducercont;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.monklypse.core.NodeHelper;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.databinding.IEntityObservable;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.interfaces.AbstractVisualProducer;
import org.rifidi.designer.entities.interfaces.AbstractVisualProduct;
import org.rifidi.designer.entities.interfaces.IHasSwitch;
import org.rifidi.designer.library.basemodels.cardbox.CardboxEntity;
import org.rifidi.designer.services.core.entities.ProductService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.services.tags.exceptions.RifidiTagNotAvailableException;
import org.rifidi.services.tags.model.IRifidiTagContainer;
import org.rifidi.tags.impl.RifidiTag;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;

/**
 * BoxproducerContinuousEntity: Used for generating boxes.
 * 
 * @author Jochen Mader Oct 8, 2007
 * @author Dan West
 */
@MonitoredProperties(names = { "name" })
public class BoxproducerContinuousEntity extends AbstractVisualProducer
		implements IHasSwitch, IRifidiTagContainer, IEntityObservable, PropertyChangeListener {

	/** Logger for this class. */
	@XmlTransient
	private static Log logger = LogFactory
			.getLog(BoxproducerContinuousEntity.class);
	/** Seconds per box. */
	private float speed;
	/** Production thread. */
	@XmlTransient
	private BoxproducerContinuousEntityThread thread;
	/** State of the switch. */
	private boolean running = false;
	/** Is the entity paused. */
	private boolean paused = true;
	/** Source for shared meshes. */
	@XmlTransient
	private Node model;
	/** Reference to the product service. */
	@XmlTransient
	private ProductService productService;
	/** Set containing all available tags. */
	@XmlIDREF
	private List<RifidiTag> tags;
	@XmlTransient
	private List<RifidiTag> sharedtags;
	/** Reference to the tag service. */
	@XmlTransient
	private IRifidiTagService tagService;
	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public BoxproducerContinuousEntity() {
		this.speed = 4;
		this.tags = new WritableList();
		sharedtags = new ArrayList<RifidiTag>();
		setName("Continuous Boxproducer");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.designer.entities.interfaces.IEntityObservable#
	 * addListChangeListener
	 * (org.eclipse.core.databinding.observable.list.IListChangeListener)
	 */
	@Override
	public void addListChangeListener(IListChangeListener changeListener) {
		((WritableList) tags).addListChangeListener(changeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.designer.entities.interfaces.IEntityObservable#
	 * removeListChangeListener
	 * (org.eclipse.core.databinding.observable.list.IListChangeListener)
	 */
	@Override
	public void removeListChangeListener(IListChangeListener changeListener) {
		((WritableList) tags).removeListChangeListener(changeListener);
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
			model = new Node();
			model.attachChild(new Box("producer", new Vector3f(0, 12f, 0), 3f,
					.5f, 3f));
		}
		setCollides(false);

		BlendState as = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		as.setBlendEnabled(true);
		as.setSourceFunction(SourceFunction.SourceAlpha);
		as.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		as.setBlendEnabled(true);
		as.setEnabled(true);

		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setDiffuse(new ColorRGBA(.2f, .75f, .8f, 1).multLocal(.7f));
		ms.setEnabled(true);
		model.setRenderState(ms);

		Node node = new Node(getEntityId());
		Node sharednode = new SharedNode("maingeometry", model);
		node.attachChild(sharednode);

		sharednode.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		sharednode.setRenderState(as);

		sharednode.setModelBound(new BoundingBox());
		sharednode.updateModelBound();

		setNode(node);

		Node _node = new Node("hiliter");
		Box box = new Box("hiliter", new Vector3f(0, 12f, 0), 3f, .5f, 3f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);
		_node.setModelBound(new BoundingBox());
		_node.updateModelBound();
		_node.setCullHint(CullHint.Always);
		getNode().attachChild(_node);

		logger.debug(NodeHelper.printNodeHierarchy(getNode(), 3));

		thread = new BoxproducerContinuousEntityThread(this, productService,
				products, sharedtags);
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
			model = new Node();
			model.attachChild(new Box("producer", new Vector3f(0, 12f, 0), 3f,
					.5f, 3f));
		}

		Set<RifidiTag> temptags = new HashSet<RifidiTag>(tags);
		for (AbstractVisualProduct vis : products) {
			temptags.remove(((CardboxEntity) vis).getRifidiTag());
		}
		thread = new BoxproducerContinuousEntityThread(this, productService,
				products, sharedtags);
		thread.setInterval((int) speed * 1000);
		thread.setPaused(paused);
		sharedtags.clear();
		sharedtags.addAll(tags);
		thread.start();
		if (running)
			turnOn();
		for(RifidiTag tag:tags){
			try {
				tagService.takeRifidiTag(tag, this);
			} catch (RifidiTagNotAvailableException e) {
				logger.error(e);
			}
			tag.addPropertyChangeListener(this);
		}
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
	 * @see org.rifidi.designer.entities.interfaces.IHasSwitch#isRunning()
	 */
	public boolean isRunning() {
		return running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#start()
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
	 * @see org.rifidi.designer.entities.Entity#stop()
	 */
	public void reset() {
		paused = true;
		thread.setPaused(true);
		productService.deleteProducts(new ArrayList<AbstractVisualProduct>(
				products));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#pause()
	 */
	public void pause() {
		paused = true;
		if (thread != null) {
			thread.setPaused(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		thread.interrupt();
		getNode().removeFromParent();
	}

	/**
	 * Used to control the running state of the producer
	 * 
	 * @param newrunning
	 */
	public void setRunning(boolean newrunning) {
		running = newrunning;
	}

	/**
	 * Set the product service.
	 * 
	 * @param productService
	 */
	@Inject
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#setLOD(int)
	 */
	@Override
	public void setLOD(int lod) {
		// No LOD for this one.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#getBoundingNode()
	 */
	@Override
	public Node getBoundingNode() {
		return (Node) getNode().getChild("hiliter");
	}
	
	/**
	 * Get a string representation of the tags this producer owns.
	 * 
	 * @return
	 */
	public String getTagList() {
		StringBuffer buf = new StringBuffer();
		for (RifidiTag tag : tags) {
			buf.append(tag + "\n");
		}
		return buf.toString();
	}

	@Property(displayName = "Tags", description = "tags assigned to this producer", readonly = true, unit = "")
	public void setTagList(String tagList) {

	}

	/**
	 * @return the tags
	 */
	public List<RifidiTag> getTags() {
		return this.tags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.IProducer#productDestroied(org
	 * .rifidi.designer.entities.interfaces.IProduct)
	 */
	@Override
	public void productDestroied(AbstractVisualProduct product) {
		products.remove(product);
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if("deleted".equals(evt.getPropertyName())){
			tags.remove(((RifidiTag)evt.getSource()));
		}
	}

	/* (non-Javadoc)
	 * @see org.rifidi.services.tags.model.IRifidiTagContainer#addTags(java.util.Collection)
	 */
	@Override
	public void addTags(Collection<RifidiTag> tags) {
		for(RifidiTag tag:tags){
			tag.addPropertyChangeListener(this);
		}
		// remove dups
		tags.removeAll(this.tags);
		this.tags.addAll(tags);
		sharedtags.clear();
		sharedtags.addAll(tags);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.services.tags.model.IRifidiTagContainer#removeTags(java.util.Collection)
	 */
	@Override
	public void removeTags(Collection<RifidiTag> tags) {
		this.tags.removeAll(tags);
		sharedtags.clear();
		sharedtags.addAll(tags);
		for(RifidiTag tag:tags){
			tag.removePropertyChangeListener(this);	
		}
	}

	/**
	 * @param tagService the tagService to set
	 */
	@Inject
	public void setTagService(IRifidiTagService tagService) {
		this.tagService = tagService;
	}

}
