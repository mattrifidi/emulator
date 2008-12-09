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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.IContainer;
import org.rifidi.designer.library.retail.Position;
import org.rifidi.designer.library.retail.clothing.Clothing;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.factory.TagCreationPattern;
import org.rifidi.services.tags.id.TagType;
import org.rifidi.services.tags.impl.RifidiTag;
import org.rifidi.services.tags.registry.ITagRegistry;

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
import com.jmex.physics.DynamicPhysicsNode;

/**
 * FIXME: Class comment.  
 * 
 * @author Jochen Mader - jochen@pramari.com - Apr 3, 2008
 * 
 */
public class ClothingRack extends VisualEntity implements IContainer {
	/** Logger for this class. */
	@XmlTransient
	private static final Log logger = LogFactory.getLog(ClothingRack.class);
	/** Container for entities inside the holder. */
	private List<VisualEntity> entities;
	/** List of available positions. */
	@XmlTransient
	private List<Position> positions;
	/** Capacity of the container. */
	private int capacity = 10;
	/** Number of items currently in the list. */
	private int itemCount = 0;
	/** Model for shared meshes */
	@XmlTransient
	private static Node[] lod = null;
	/** Reference to the tag registry. */
	@XmlTransient
	private ITagRegistry tagRegistry;
	/** Node that contains the different lods. */
	@XmlTransient
	private SwitchNode switchNode;

	/**
	 * Constructor.
	 */
	public ClothingRack() {
		super();
		setName("Clothing rack");
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
		entities = new ArrayList<VisualEntity>(capacity);
		positions = new ArrayList<Position>(capacity);
		TagCreationPattern tagpattern = new TagCreationPattern();
		tagpattern.setTagGeneration(TagGen.GEN2);
		tagpattern.setTagType(TagType.GID96);
		tagpattern.setNumberOfTags(capacity);
		List<RifidiTag> tags = tagRegistry.createTags(tagpattern);
		for (int count = 0; count < capacity; count++) {
			Clothing clothing = new Clothing();
			Position pos = new Position(calcPos(count), calcRot(count));
			entities.add(clothing);
			positions.add(pos);
			clothing.setStartTranslation((Vector3f) pos.translation.clone());
			clothing.setStartRotation(new Quaternion(pos.rotation));
			clothing.setUserData(tags.get(count));
			clothing.setName(clothing.getUserData().toString());
			itemCount++;
		}
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IContainer#addVisualEntity
	 * (org.rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public void addVisualEntity(final VisualEntity visualEntity) {
		if (accepts(visualEntity) && !isFull()) {
			entities.add(visualEntity);
			update(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception {
					visualEntity.getNode().removeFromParent();
					int count = 0;
					while (count < capacity) {
						if (entities.get(count) == null) {
							break;
						}
						count++;
					}
					getNode().attachChild(visualEntity.getNode());
					visualEntity.getNode()
							.setLocalTranslation(
									(Vector3f) positions.get(count).translation
											.clone());
					visualEntity.getNode().setLocalRotation(
							new Quaternion(positions.get(count).rotation));
					visualEntity.getNode().setIsCollidable(false);
					((DynamicPhysicsNode) visualEntity.getNode())
							.setActive(false);
					entities.set(count, visualEntity);
					return null;
				}

			});
			itemCount++;
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
		VisualEntity ret = null;
		int count = 0;
		for (VisualEntity vs : entities) {
			if (vs != null) {
				ret = vs;
				ret.getNode().setIsCollidable(true);
				break;
			}
			count++;
		}
		count = count == capacity ? count - 1 : count;
		entities.set(count, null);
		itemCount--;
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IContainer#getVisualEntity
	 * (org.rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public VisualEntity getVisualEntity(VisualEntity visualEntity) {
		if (entities.contains(visualEntity)) {
			entities.set(entities.indexOf(visualEntity), null);
			itemCount--;
			visualEntity.getNode().setIsCollidable(true);
			return visualEntity;
		}
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
		return Collections.unmodifiableList(entities);
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

	/**
	 * @param tagRegistry
	 *            the tagRegistry to set
	 */
	@Inject
	public void setTagRegistry(ITagRegistry tagRegistry) {
		this.tagRegistry = tagRegistry;
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

}
