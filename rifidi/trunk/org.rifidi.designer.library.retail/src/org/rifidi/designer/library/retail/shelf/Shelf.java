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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlTransient;

import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.IContainer;
import org.rifidi.designer.library.retail.Position;
import org.rifidi.designer.library.retail.retailbox.RetailBox;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.DynamicPhysicsNode;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Apr 3, 2008
 * 
 */
public class Shelf extends VisualEntity implements IContainer {
	/** Container for entities inside the holder. */
	private List<VisualEntity> entities;
	/** List of available positions. */
	@XmlTransient
	private List<Position> positions;
	/** Capacity of the container. */
	private int capacity = 9;
	/** Number of currently stored items. */
	private int itemCount = 0;
	/** Model for shared meshes */
	@XmlTransient
	private static Node model = null;

	/**
	 * Constructor.
	 */
	public Shelf() {
		super();
		setName("Shelf");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		URI modelpath = null;
		Node mainNode = new Node();
		mainNode.setModelBound(new BoundingBox());
		Node node = new Node("maingeometry");
		node.setModelBound(new BoundingBox());
		mainNode.attachChild(node);
		try {
			modelpath = getClass().getClassLoader().getResource(
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
		model.setLocalTranslation(new Vector3f(0, 3.7f, 0));
		model.setLocalScale(5.0f);
		model.setLocalRotation(new Quaternion(new float[] {
				(float) Math.toRadians(270), 0, 0 }));
		;
		node.attachChild(model);
		entities = new ArrayList<VisualEntity>();
		positions = new ArrayList<Position>();
		positions
				.add(new Position(new Vector3f(-1.9f, 6f, 0), new Quaternion()));
		positions.add(new Position(new Vector3f(-0f, 6f, 0), new Quaternion()));
		positions
				.add(new Position(new Vector3f(1.9f, 6f, 0), new Quaternion()));
		positions.add(new Position(new Vector3f(-1.9f, 3.5f, 0),
				new Quaternion()));
		positions
				.add(new Position(new Vector3f(-0f, 3.5f, 0), new Quaternion()));
		positions.add(new Position(new Vector3f(1.9f, 3.5f, 0),
				new Quaternion()));
		positions
				.add(new Position(new Vector3f(-1.9f, 1f, 0), new Quaternion()));
		positions.add(new Position(new Vector3f(-0f, 1f, 0), new Quaternion()));
		positions
				.add(new Position(new Vector3f(1.9f, 1f, 0), new Quaternion()));
		for (int count = 0; count < capacity; count++) {
			RetailBox box = new RetailBox();
			box.setStartTranslation((Vector3f) positions.get(count).translation
					.clone());
			entities.add(box);
			itemCount++;
		}
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.IContainer#addVisualEntity
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
	 * @see
	 * org.rifidi.designer.entities.interfaces.IContainer#getVisualEntity
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
	 * @see
	 * org.rifidi.designer.entities.interfaces.IContainer#getVisualEntity
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
	 * org.rifidi.designer.entities.interfaces.IContainer#getVisualEntitySet
	 * ()
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
	 * @see
	 * org.rifidi.designer.entities.interfaces.IContainer#accepts(org
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

}
