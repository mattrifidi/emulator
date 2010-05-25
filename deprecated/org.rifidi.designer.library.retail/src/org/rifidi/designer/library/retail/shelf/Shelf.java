/*
 *  Clothing.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.retail.shelf;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.databinding.IEntityObservable;
import org.rifidi.designer.entities.interfaces.IContainer;
import org.rifidi.designer.entities.interfaces.IProducer;
import org.rifidi.designer.entities.internal.RifidiTagWithParent;
import org.rifidi.designer.library.retail.clothing.Clothing;
import org.rifidi.designer.library.retail.retailbox.RetailBox;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.services.tags.exceptions.RifidiTagNotAvailableException;
import org.rifidi.services.tags.model.IRifidiTagContainer;
import org.rifidi.tags.impl.RifidiTag;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.util.export.binary.BinaryImporter;

/**
 * FIXME: Class comment.
 * 
 * @author Jochen Mader - jochen@pramari.com - Apr 3, 2008
 * 
 */
public class Shelf extends VisualEntity implements IContainer,
		IEntityObservable, IRifidiTagContainer, PropertyChangeListener,
		IProducer<RetailBox> {
	/** Logger for this class. */
	private static Log logger = LogFactory.getLog(Shelf.class);
	/** Capacity of the container. */
	private int capacity = 9;
	/** Number of currently stored items. */
	private int itemCount = 0;
	/** Model for shared meshes */
	@XmlTransient
	private static Node model = null;
	/** Box model. */
	@XmlTransient
	private static Node boxmodel = null;
	/** Set containing all available tags. */
	@XmlIDREF
	private List<RifidiTag> tags;
	@XmlTransient
	private Stack<RifidiTag> tagStack;
	/** List of wrapper objects that bind tags and container together. */
	@XmlTransient
	private WritableList wrappers;
	/** Reference to the tag service. */
	@XmlTransient
	private IRifidiTagService tagService;
	/** Reference to the entities service. */
	@XmlTransient
	private EntitiesService entitiesService;
	/** Container for entities inside the holder. */
	@XmlIDREF
	private List<VisualEntity> products;

	/**
	 * Constructor.
	 */
	public Shelf() {
		super();
		setName("Shelf");
		tags = new ArrayList<RifidiTag>();
		wrappers = new WritableList();
		tagStack = new Stack<RifidiTag>();
		products = new ArrayList<VisualEntity>();
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
		wrappers.addListChangeListener(changeListener);
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
		wrappers.removeListChangeListener(changeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		List<Entity> del=new ArrayList<Entity>();
		del.addAll(products);
		entitiesService.deleteEntities(del);
		for (RifidiTag tag : tags) {
			tagService.releaseRifidiTag(tag, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		prepare();
		Node mainNode = new Node();
		mainNode.setModelBound(new BoundingBox());
		Node node = new Node("maingeometry");
		node.setModelBound(new BoundingBox());
		mainNode.attachChild(node);

		model.setLocalTranslation(new Vector3f(0, 3.7f, 0));
		model.setLocalScale(5.0f);
		model.setLocalRotation(new Quaternion(new float[] {
				(float) Math.toRadians(270), 0, 0 }));
		node.attachChild(model);
		setNode(mainNode);
		getNode().updateGeometricState(0f, true);
		getNode().updateModelBound();

		Node _node = new Node("hiliter");
		Box box = new Box("hiliter", ((BoundingBox) getNode().getWorldBound())
				.getCenter().clone().subtractLocal(
						getNode().getLocalTranslation()).add(
						new Vector3f(0, 2, 0)), 4f, 4f, 1f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);
		_node.setModelBound(new BoundingBox());
		_node.updateModelBound();
		_node.setCullHint(CullHint.Always);
		getNode().attachChild(_node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		prepare();
		for (RifidiTag tag : getTags()) {
			RifidiTagWithParent r = new RifidiTagWithParent();
			r.parent = this;
			r.tag = tag;
			wrappers.add(r);
		}
		List<RifidiTag> stackTags = new ArrayList<RifidiTag>(tags);
		for (VisualEntity prod : products) {
			stackTags.remove(((Clothing) prod).getRifidiTag());
		}
		tagStack.addAll(stackTags);
	}

	private void prepare() {
		try {
			URI modelpath = getClass().getClassLoader().getResource(
					"org/rifidi/designer/library/retail/shelf/shelf.jme")
					.toURI();
			model = (Node) BinaryImporter.getInstance().load(modelpath.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			URI modelpath = getClass().getClassLoader().getResource(
					"org/rifidi/designer/library/retail/retailbox/box.jme")
					.toURI();
			boxmodel = (Node) BinaryImporter.getInstance().load(
					modelpath.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IContainer#addVisualEntity
	 * (org.rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public void addVisualEntity(final VisualEntity visualEntity) {
		if (accepts(visualEntity) && !isFull()
				&& products.contains(visualEntity)) {
			List<Entity> del = new ArrayList<Entity>();
			del.add(visualEntity);
			entitiesService.deleteEntities(del);
			addBox();
			tagStack.push(((RetailBox) visualEntity).getRifidiTag());
			return;
		}
		throw new RuntimeException("Stupid!! Wrong type or full: "
				+ accepts(visualEntity) + " " + isFull());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IContainer#getVisualEntity
	 * ()
	 */
	@Override
	public VisualEntity getVisualEntity() {
		try {
			removeBox();
			RifidiTag tag = tagStack.pop();
			RetailBox box = new RetailBox();
			box.setRifidiTag(tag);
			box.setName(box.getName() + " " + tag);
			box.setProducer(this);
			box.setStartTranslation(getNode().getLocalTranslation().clone());
			entitiesService.addEntity(box, null, null);
			products.add(box);
			return box;
		} catch (EmptyStackException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IContainer#getVisualEntity
	 * (org.rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public VisualEntity getVisualEntity(VisualEntity visualEntity) {
		logger.debug("direct selection not supported");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.IContainer#getVisualEntitySet ()
	 */
	@Override
	public List<VisualEntity> getVisualEntityList() {
		return Collections.unmodifiableList(products);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IContainer#isFull()
	 */
	@Override
	public boolean isFull() {
		return capacity <= itemCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IContainer#accepts(org
	 * .rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public boolean accepts(VisualEntity visualEntity) {
		return visualEntity instanceof RetailBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#setLOD(int)
	 */
	@Override
	public void setLOD(int lod) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.model.IRifidiTagContainer#addTags(java.util.
	 * Collection)
	 */
	@Override
	public void addTags(Collection<RifidiTag> tags) {
		for (RifidiTag tag : tags) {
			if (this.tags.size() < capacity) {
				try {
					tagService.takeRifidiTag(tag, this);
					tag.addPropertyChangeListener(this);
					RifidiTagWithParent r = new RifidiTagWithParent();
					r.parent = this;
					r.tag = tag;
					addBox();
					wrappers.add(r);
					this.tags.add(tag);
					tagStack.push(tag);
				} catch (RifidiTagNotAvailableException e) {
					logger.error("Tag not available: " + e);
				}
			}
		}
	}

	private void addBox() {
		update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				// TODO: Possible race condition
				itemCount++;
				Node box = new Node("box " + itemCount);
				SharedNode shared = new SharedNode(boxmodel);
				box.attachChild(shared);
				box.getLocalTranslation().addLocal(calcPos(itemCount));
				box.setLocalScale(0.5f);
				getNode().attachChild(box);
				box.updateRenderState();
				return null;
			}

		});
	}

	private Vector3f calcPos(int count) {
		Vector3f ret = new Vector3f();
		int pos = (count - 1) % 3;
		int pos2 = (int) Math.floor((count - 1) / 3);
		ret.x = (float) (-1.9 + (1.9 * pos));
		ret.y = (float) (1 + (pos2 * 2.5f));
		ret.z = 0;
		return ret;
	}

	private void removeBox() {
		update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				getNode().getChild("box " + itemCount).removeFromParent();
				itemCount--;
				return null;
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.model.IRifidiTagContainer#removeTags(java.util
	 * .Collection)
	 */
	@Override
	public void removeTags(Collection<RifidiTag> tags) {
		// only remove tags that are currently available.
		if (this.tagStack.containsAll(tags)) {
			tagStack.removeAll(tags);
			this.tags.removeAll(tags);
			Set<RifidiTagWithParent> rem = new HashSet<RifidiTagWithParent>();
			for (Object wrapper : wrappers) {
				if (tags.contains(((RifidiTagWithParent) wrapper).tag)) {
					((RifidiTagWithParent) wrapper).tag
							.removePropertyChangeListener(this);
					rem.add((RifidiTagWithParent) wrapper);
				}
			}
			wrappers.removeAll(rem);
			tagService.releaseRifidiTags(tags, this);
		}
	}

	/**
	 * @return the wrappers
	 */
	public WritableList getWrappers() {
		return this.wrappers;
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

	@Property(displayName = "Tags", description = "tags assigned to this rack", readonly = true, unit = "")
	public void setTagList(String tagList) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.tags.model.IRifidiTagContainer#getTags()
	 */
	@Override
	public Collection<RifidiTag> getTags() {
		return tags;
	}

	/**
	 * @param tagService
	 *            the tagService to set
	 */
	@Inject
	public void setTagService(IRifidiTagService tagService) {
		this.tagService = tagService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IProducer#getProducts()
	 */
	@Override
	public List<RetailBox> getProducts() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.IProducer#productDestroied(org
	 * .rifidi.designer.entities.interfaces.IProduct)
	 */
	@Override
	public void productDestroied(RetailBox product) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.IProducer#setProducts(java.util
	 * .List)
	 */
	@Override
	public void setProducts(List<RetailBox> entities) {
	}

	/**
	 * @param entitiesService
	 *            the entitiesService to set
	 */
	@Inject
	public void setEntitiesService(EntitiesService entitiesService) {
		this.entitiesService = entitiesService;
	}

}
