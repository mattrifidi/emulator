package org.rifidi.designer.library.basemodels.conveyor90;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.interfaces.Switch;
import org.rifidi.designer.entities.placement.BinaryPattern;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

public class Conveyor90Entity extends VisualEntity implements Switch,
		NeedsPhysics {
	Log logger = LogFactory.getLog(Conveyor90Entity.class);

	private static Node model = null;

	// private List<StaticPhysicsNode> rollers = new
	// ArrayList<StaticPhysicsNode>();

	private List<PhysicsNode> physicsParts;
	private boolean active = false;
	private float speed = 0;

	private Material rollerMaterial = null;

	private PhysicsSpace physicsSpace;

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
	@Property(displayName = "Speed", description = "guess what", readonly = false, unit = "feet/s")
	public void setSpeed(float speed) {
		this.speed = speed;
		if (getNode() != null) {
			rollerMaterial.setSurfaceMotion(new Vector3f(0.0f, -5.0f, 0.0f)
					.mult(speed));
		}
	}

	@Override
	public void destroy() {
		for (PhysicsNode physNode : physicsParts)
			physNode.setActive(false);
		getNode().removeFromParent();
	}

	@Override
	public void init() {
		if (getNode() == null) {
			BinaryPattern pattern = new BinaryPattern();
			pattern.setPattern(new boolean[][] {
					{ false, false, false, false, false, false, true, true,
							true, true },
					{ false, false, false, false, true, true, true, true, true,
							true },
					{ false, false, false, true, true, true, true, true, true,
							true },
					{ false, false, true, true, true, true, true, true, true,
							true },
					{ false, true, true, true, true, true, true, true, true,
							true },
					{ false, true, true, true, true, true, true, false, false,
							false },
					{ true, true, true, true, true, true, false, false, false,
							false },
					{ true, true, true, true, true, false, false, false, false,
							false },
					{ true, true, true, true, true, false, false, false, false,
							false },
					{ true, true, true, true, true, false, false, false, false,
							false } });
			setPattern(pattern);
			Node node = new Node();
			node.setName(getEntityId());
			try {
				if (model == null) {
					URI modelpath = null;
					try {
						modelpath = getClass()
								.getClassLoader()
								.getResource(
										"org/rifidi/designer/library/basemodels/conveyor90/convey90.jme")
								.toURI();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					model = (Node) BinaryImporter.getInstance().load(
							modelpath.toURL());
				}
				node.attachChild(new SharedNode("sharedConveyor90", model));
				node.setModelBound(new BoundingBox());
				setNode(node);
			} catch (IOException e) {
				logger.error("Unable to load jme: " + e);
			}
		}
		rollerMaterial = new Material("Roller");

		// System.out.println(rollerMaterial.getSpringPenetrationDepth());
		// MutableContactInfo cinf = new MutableContactInfo();
		// cinf.setMinimumBounceVelocity(3);
		// rollerMaterial.putContactHandlingDetails(Material.DEFAULT, cinf);
		//		
		// rollerMaterial.setContactHandlingDetails(cinf);

		// getWorldData().getPhysicsSpace().setAccuracy(.10f);
		// System.out.println(rollerMaterial.getContactHandlingDetails(null).getSlip(new
		// Vector2f()));
		// System.out.println(NodeHelper.printNodeHierarchy(getNode(), 5));

		physicsParts = new ArrayList<PhysicsNode>();
		for (Spatial n : ((SharedNode) getNode().getChild(0)).getChildren()) {
			if (n instanceof Node) {
				StaticPhysicsNode stat = physicsSpace.createStaticNode();
				physicsParts.add(stat);
				stat.setName("stat");
				Spatial mesh = ((Node) n).getChild(0);
				stat.attachChild(mesh);
				((Node) n).attachChild(stat);
				mesh.setModelBound(new BoundingBox());
				mesh.updateModelBound();
				stat.setMaterial(rollerMaterial);
				stat.generatePhysicsGeometry();
			}
		}
		// System.out.println(NodeHelper.printNodeHierarchy(getNode(), 5));

		if (active) {
			active = false;
			turnOn();
		} else {
			active = true;
			turnOff();
		}

	}

	/* (non-Javadoc)
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#isRunning()
	 */
	public boolean isRunning() {
		return active;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#turnOff()
	 */
	@Override
	public void turnOff() {
		for (Spatial spa : getNode().getChildren()) {
			if (spa instanceof StaticPhysicsNode) {
				System.out.print(((StaticPhysicsNode) spa).getMaterial());
			}
		}

		if (active) {
			System.out.println("GDammit.");
			rollerMaterial.setSurfaceMotion(new Vector3f(0.0f, 0.0f, 0.0f));
			active = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#turnOn()
	 */
	@Override
	public void turnOn() {
		if (!active) {
			System.out.println("Wait what?");
			rollerMaterial.setSurfaceMotion(new Vector3f(0.0f, -5.0f, 0.0f)
					.mult(speed));
			active = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.NeedsPhysics#setCollisionHandler(com.jme.input.InputHandler)
	 */
	public void setCollisionHandler(InputHandler collisionHandler) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.NeedsPhysics#setPhysicsSpace(com.jmex.physics.PhysicsSpace)
	 */
	public void setPhysicsSpace(PhysicsSpace physicsSpace) {
		this.physicsSpace = physicsSpace;
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

}
