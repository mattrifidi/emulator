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
package org.rifidi.designer.library.retail.clothingrack;

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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.databinding.IEntityObservable;
import org.rifidi.designer.entities.interfaces.IContainer;
import org.rifidi.designer.entities.interfaces.IProducer;
import org.rifidi.designer.entities.internal.RifidiTagWithParent;
import org.rifidi.designer.library.retail.Position;
import org.rifidi.designer.library.retail.clothing.Clothing;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.services.tags.model.IRifidiTagContainer;
import org.rifidi.tags.impl.RifidiTag;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.SwitchNode;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.util.export.binary.BinaryImporter;

/**
 * A rack can contain multiple pieces of clothing.Handling is almost the same as
 * with the box producer.
 * 
 * @author Jochen Mader - jochen@pramari.com - Apr 3, 2008
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ClothingRack extends VisualEntity implements IContainer,
		IEntityObservable, IRifidiTagContainer, PropertyChangeListener,
		IProducer<Clothing> {
	/** Logger for this class. */
	@XmlTransient
	private static final Log logger = LogFactory.getLog(ClothingRack.class);
	/** Container for entities inside the holder. */
	@XmlIDREF
	private List<VisualEntity> products;
	/** Capacity of the container. */
	private int capacity = 10;
	/** Number of items currently in the list. */
	private int itemCount = 0;
	/** Model for shared meshes */
	@XmlTransient
	private static Node[] lod = null;
	@XmlTransient
	private static Node clotModel = null;
	/** Node that contains the different lods. */
	@XmlTransient
	private SwitchNode switchNode;
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

	/**
	 * Constructor.
	 */
	public ClothingRack() {
		super();
		setName("Clothing rack");
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
		for (VisualEntity child : getVisualEntityList()) {
			if (child != null) {
				child.destroy();
			}
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
		if (lod == null) {
			lod = new Node[3];
			URI modelpath = null;
			for (int count = 0; count < 3; count++) {
				try {
					modelpath = getClass().getClassLoader().getResource(
							"org/rifidi/designer/library/retail/clothingrack/rack"
									+ count + ".jme").toURI();
				} catch (URISyntaxException e) {
					logger.debug(e);
				}
				try {
					lod[count] = (Node) BinaryImporter.getInstance().load(
							modelpath.toURL());
					lod[count].setLocalRotation(new Quaternion().fromAngleAxis(
							270 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
					lod[count].setModelBound(new BoundingBox());
					lod[count].setLocalScale(new Vector3f(0.87f, 1f, 1f));
					lod[count].updateGeometricState(0f, true);
					lod[count].updateModelBound();
					lod[count].updateWorldBound();
					if (count == 3) {
						lod[count]
								.setLocalScale(new Vector3f(1.0f, 0.9f, 1.0f));
					}
				} catch (MalformedURLException e) {
					logger.debug(e);
				} catch (IOException e) {
					logger.debug(e);
				}
			}
		}
		switchNode = new SwitchNode("switchnode");
		switchNode.attachChildAt(new SharedNode("sharedRack0", lod[0]), 0);
		switchNode.attachChildAt(new SharedNode("sharedRack1", lod[1]), 1);
		switchNode.attachChildAt(new SharedNode("sharedRack2", lod[2]), 2);
		switchNode.attachChildAt(new SharedNode("sharedRack2", lod[2]), 3);
		switchNode.setActiveChild(0);
		switchNode.setLocalTranslation(new Vector3f(0, 3.7f, 0));
		node.attachChild(switchNode);
		products = new ArrayList<VisualEntity>(capacity);
		setNode(mainNode);
		getNode().updateGeometricState(0f, true);
		getNode().updateModelBound();

		Node _node = new Node("hiliter");
		Box box = new Box("hiliter", ((BoundingBox) getNode().getWorldBound())
				.getCenter().clone().subtractLocal(
						getNode().getLocalTranslation()), 4f, 4f, 4f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);
		_node.setModelBound(new BoundingBox());
		_node.updateModelBound();
		_node.setCullHint(CullHint.Always);
		getNode().attachChild(_node);
	}

	private Vector3f calcPos(int count) {
		Vector3f ret = new Vector3f();
		float r = 2.4f;
		float alpha = count * 30;
		float mult = 1;
		if (alpha > 180) {
			alpha -= 180;
			mult = -1;
		}
		ret.y = 5.3f;
		ret.z = mult * (float) (r * Math.cos(alpha * Math.PI / 180));
		ret.x = mult * (float) Math.sqrt(r * r - ret.z * ret.z);
		return ret;
	}

	private Quaternion calcRot(int count) {
		float rad = 0;
		if (count <= 6) {
			rad = 90 + count * 30;
		} else {
			rad = 90 + count * 30;
		}
		Quaternion quat = new Quaternion(new float[] { 0,
				(float) (rad * Math.PI / 180), 0 });
		return quat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
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
		if (clotModel == null) {
			try {
				URI modelpath = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/retail/clothing/cloth0.jme")
						.toURI();
				clotModel = (Node) BinaryImporter.getInstance().load(
						modelpath.toURL());
			} catch (MalformedURLException e) {
				logger.error("Can't load model: " + e);
			} catch (IOException e) {
				logger.error("Can't load model: " + e);
			} catch (URISyntaxException e) {
				logger.error("Can't load model: " + e);
			}
			Quaternion quat = new Quaternion(new float[] {
					(float) Math.toRadians(270f), 0f, 0f });
			clotModel.setLocalRotation(quat);
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
		// if (accepts(visualEntity) && !isFull()) {
		// entities.add(visualEntity);
		// update(new Callable<Object>() {
		//
		// /*
		// * (non-Javadoc)
		// *
		// * @see java.util.concurrent.Callable#call()
		// */
		// @Override
		// public Object call() throws Exception {
		// visualEntity.getNode().removeFromParent();
		// int count = 0;
		// while (count < capacity) {
		// if (entities.get(count) == null) {
		// break;
		// }
		// count++;
		// }
		// getNode().attachChild(visualEntity.getNode());
		// visualEntity.getNode()
		// .setLocalTranslation(
		// (Vector3f) positions.get(count).translation
		// .clone());
		// visualEntity.getNode().setLocalRotation(
		// new Quaternion(positions.get(count).rotation));
		// visualEntity.getNode().setIsCollidable(false);
		//					
		// ((DynamicPhysicsNode) visualEntity.getNode())
		// .setActive(false);
		// entities.set(count, visualEntity);
		// return null;
		// }
		//
		// });
		// itemCount++;
		// return;
		// }
		// throw new RuntimeException("Stupid!! Wrong type or full: "
		// + accepts(visualEntity) + " " + isFull());
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
			RifidiTag tag = tagStack.pop();
			Clothing clothing = new Clothing();
			clothing.setRifidiTag(tag);
			clothing.setName(clothing.getName() + " " + tag);
			clothing.setStartTranslation(getNode().getWorldTranslation()
					.clone());
			clothing.setStartRotation(getNode().getLocalRotation().clone());
			clothing.setProducer(this);
			entitiesService.addEntity(clothing, null, null);
			return clothing;
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
		return visualEntity instanceof Clothing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#setLOD(int)
	 */
	@Override
	public void setLOD(int lod) {
		if (switchNode != null) {
			switchNode.setActiveChild(lod);
		}
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
		Set<RifidiTagWithParent> add = new HashSet<RifidiTagWithParent>();
		for (RifidiTag tag : tags) {
			tag.addPropertyChangeListener(this);
			RifidiTagWithParent r = new RifidiTagWithParent();
			r.parent = this;
			r.tag = tag;
			add.add(r);
			update(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception {
					// TODO: Possible race condition
					Node clothing = new Node("cloth " + itemCount++);
					SharedNode shared = new SharedNode(clotModel);
					clothing.attachChild(shared);
					Position pos = new Position(calcPos(itemCount),
							calcRot(itemCount));
					clothing.getLocalTranslation().addLocal(
							(Vector3f) pos.translation.clone());
					clothing.getLocalRotation().set(
							new Quaternion(pos.rotation));
					clothing.setName("Clothing");
					getNode().attachChild(clothing);
					return null;
				}

			});

		}
		this.tags.addAll(tags);
		wrappers.addAll(add);
		tagStack.addAll(tags);
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
	public List<Clothing> getProducts() {
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
	public void productDestroied(Clothing product) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.IProducer#setProducts(java.util
	 * .List)
	 */
	@Override
	public void setProducts(List<Clothing> entities) {
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
