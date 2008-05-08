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

import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.interfaces.VisualEntityHolder;
import org.rifidi.designer.entities.placement.BinaryPattern;
import org.rifidi.designer.library.retail.Position;
import org.rifidi.designer.library.retail.clothing.Clothing;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.tags.TagService;

import com.jme.input.InputHandler;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Apr 3, 2008
 * 
 */
public class ClothingRack extends VisualEntity implements VisualEntityHolder,
		NeedsPhysics {

	/**
	 * Reference to the collision input handler.
	 */
	private InputHandler inputHandler;
	/**
	 * Reference to the current physics space.
	 */
	private PhysicsSpace physicsSpace;
	/**
	 * Container for entities inside the holder.
	 */
	private List<VisualEntity> entities;
	/**
	 * List of available positions.
	 */
	private List<Position> positions;

	/**
	 * Capacity of the container.
	 */
	private int capacity = 10;
	/**
	 * Number of items currently in the list.
	 */
	private int itemCount = 0;
	/**
	 * Model for shared meshes
	 */
	private static Node model = null;
	/**
	 * Reference to the tag service.
	 */
	private TagService tagService;

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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		BinaryPattern pattern = new BinaryPattern();
		pattern
				.setPattern(new boolean[][] {
						{ false, false, false, true, true, true, true, false,
								false, false },
						{ false, false, true, true, true, true, true, true,
								false, false },
						{ false, true, true, true, true, true, true, true,
								true, false },
						{ true, true, true, true, true, true, true, true, true,
								true },
						{ true, true, true, true, true, true, true, true, true,
								true },
						{ true, true, true, true, true, true, true, true, true,
								true },
						{ true, true, true, true, true, true, true, true, true,
								true },
						{ false, true, true, true, true, true, true, true,
								true, false },
						{ false, false, true, true, true, true, true, true,
								false, false },
						{ false, false, false, true, true, true, true, false,
								false, false } });
		setPattern(pattern);
		URI modelpath = null;
		Node node = new Node();
		try {
			modelpath = getClass().getClassLoader().getResource(
					"org/rifidi/designer/library/retail/clothingrack/rack.jme")
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
		node.attachChild(model);
		entities = new ArrayList<VisualEntity>(capacity);
		positions = new ArrayList<Position>(capacity);
		for (int count = 0; count < capacity; count++) {
			Clothing clothing = new Clothing();
			Position pos = new Position(calcPos(count), calcRot(count));
			entities.add(clothing);
			positions.add(pos);
			clothing.setStartTranslation((Vector3f) pos.translation.clone());
			clothing.setStartRotation(new Quaternion(pos.rotation));
			clothing.setUserData(tagService.getRifidiTag(tagService.getTagSourceNames().get(0)));
			clothing.setName(clothing.getUserData().toString());
			itemCount++;
		}
		setNode(node);
	}

	private Vector3f calcPos(int count) {
		Vector3f ret = new Vector3f();
		float r = 4;
		float alpha = count * 30;
		float mult = 1;
		if (alpha > 180) {
			alpha -= 180;
			mult = -1;
		}
		ret.y = 3.7f;
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.VisualEntityHolder#addVisualEntity(org.rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public void addVisualEntity(final VisualEntity visualEntity) {
		if (accepts(visualEntity) && !isFull()) {
			entities.add(visualEntity);
			GameTaskQueueManager.getManager().update(new Callable<Object>() {

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
	 * @see org.rifidi.designer.entities.interfaces.VisualEntityHolder#getVisualEntity()
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
	 * @see org.rifidi.designer.entities.interfaces.VisualEntityHolder#getVisualEntity(org.rifidi.designer.entities.VisualEntity)
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
	 * @see org.rifidi.designer.entities.interfaces.VisualEntityHolder#getVisualEntitySet()
	 */
	@Override
	public List<VisualEntity> getVisualEntityList() {
		return Collections.unmodifiableList(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.VisualEntityHolder#isFull()
	 */
	@Override
	public boolean isFull() {
		return capacity <= itemCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.NeedsPhysics#setCollisionHandler(com.jme.input.InputHandler)
	 */
	@Override
	public void setCollisionHandler(InputHandler collisionHandler) {
		this.inputHandler = collisionHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.NeedsPhysics#setPhysicsSpace(com.jmex.physics.PhysicsSpace)
	 */
	@Override
	public void setPhysicsSpace(PhysicsSpace physicsSpace) {
		this.physicsSpace = physicsSpace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.VisualEntityHolder#accepts(org.rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public boolean accepts(VisualEntity visualEntity) {
		return visualEntity instanceof Clothing;
	}

	@Inject
	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}
}
