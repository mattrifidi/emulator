/*
 *  PusharmEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.pusharm;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.interfaces.GPI;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.interfaces.SceneControl;
import org.rifidi.designer.entities.interfaces.Switch;
import org.rifidi.designer.entities.interfaces.Trigger;
import org.rifidi.designer.entities.placement.BinaryPattern;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.system.DisplaySystem;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.contact.ContactInfo;
import com.jmex.physics.material.Material;

/**
 * @author Jochen Mader Oct 8, 2007
 * @author Dan West
 */
@MonitoredProperties(names = { "name" })
@XmlRootElement
public class PusharmEntity extends VisualEntity implements SceneControl,
		Switch, Trigger, NeedsPhysics, GPI {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(PusharmEntity.class);
	/**
	 * Speed of the pusharm.
	 */
	private float speed;
	/**
	 * Transformer for the arm movement.
	 */
	private SpatialTransformer st;
	/**
	 * Not extended position.
	 */
	private Vector3f minpos = new Vector3f(-1.75f, 6, 0);
	/**
	 * Extended position.
	 */
	private Vector3f maxpos = minpos.add(new Vector3f(-4, 0, 0));
	/**
	 * Switch state.
	 */
	private boolean running = false;
	/**
	 * Paused state.
	 */
	private boolean paused = true;
	/**
	 * Infrared trigger.
	 */
	private StaticPhysicsNode triggerSpace = null;
	/**
	 * Physics of the push arm.
	 */
	private StaticPhysicsNode armPhysics = null;
	/**
	 * Reference to the physics space.
	 */
	private PhysicsSpace physicsSpace;
	/**
	 * Reference to the sollision handler.
	 */
	private InputHandler collisionHandler;
	/**
	 * Stack for activation signals.
	 */
	private Stack<Boolean> activationStack;

	/**
	 * Constructor
	 */
	public PusharmEntity() {
		setName("Pusharm");
		activationStack = new Stack<Boolean>();
		this.speed = 2;
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
	@Property(displayName = "Speed", description = "Speed the pusharm moves at", readonly = false, unit = "")
	public void setSpeed(float speed) {
		this.speed = speed;
		initController();
		activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		BinaryPattern pattern = new BinaryPattern();
		pattern.setPattern(new boolean[][] {
				{ true, true, true, true, true, true },
				{ true, true, true, true, true, true },
				{ true, true, true, true, true, true },
				{ true, true, true, true, true, true } });
		setPattern(pattern);
		Node node = new Node();
		try {
			URI body = null;
			URI arm = null;
			try {
				arm = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/basemodels/pusharm/pusher_arm.jme")
						.toURI();
				body = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/basemodels/pusharm/pusher_body.jme")
						.toURI();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}

			Node bodyNode = (Node) BinaryImporter.getInstance().load(
					body.toURL());
			bodyNode.setModelBound(new BoundingBox());
			bodyNode.updateModelBound();
			for (Spatial sp : bodyNode.getChildren()) {
				System.out.println("sp: "+sp);
				sp.clearRenderState(RenderState.RS_TEXTURE);
			}

			bodyNode.updateRenderState();
			Node armNode = (Node) BinaryImporter.getInstance()
					.load(arm.toURL());
			armPhysics = physicsSpace.createStaticNode();
			armPhysics.attachChild(armNode);
			armPhysics.setName("armPhysics");
			armPhysics.generatePhysicsGeometry();
			armPhysics.setLocalTranslation(minpos);
			armNode.setModelBound(new BoundingBox());
			armNode.updateModelBound();

			node.attachChild(bodyNode);
			node.attachChild(armPhysics);
			node.updateModelBound();
			setNode(node);

			// store pusharm dimensions for ease of calculation
			float xCent = armPhysics.getWorldBound().getCenter().x;
			float yCent = armPhysics.getWorldBound().getCenter().y;
			float xExt = ((BoundingBox) armPhysics.getWorldBound()).xExtent;
			float yExt = ((BoundingBox) armPhysics.getWorldBound()).yExtent;
			float len = 2.5f; // length of the trigger area

			// Create the material and alpha states for the trigger area
			MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
					.createMaterialState();
			ms.setDiffuse(new ColorRGBA(1, 1, 1, .6f));
			AlphaState as = DisplaySystem.getDisplaySystem().getRenderer()
					.createAlphaState();
			as.setBlendEnabled(true);
			as.setSrcFunction(AlphaState.SB_SRC_ALPHA);
			as.setDstFunction(AlphaState.DB_ONE);
			as.setEnabled(true);

			// create the trigger area
			Box irGeom = new Box("triggerSpace_geom", new Vector3f(-xExt - len
					+ xCent, yCent, 0f).add(minpos), len, yExt, .15f);
			irGeom.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
			irGeom.setRenderState(ms);
			irGeom.setRenderState(as);
			triggerSpace = physicsSpace.createStaticNode();
			triggerSpace.setName("triggerSpace");
			triggerSpace.attachChild(irGeom);
			triggerSpace.setModelBound(new BoundingBox());
			triggerSpace.updateModelBound();
			triggerSpace.updateRenderState();
			getNode().attachChild(triggerSpace);

		} catch (IOException e) {
			logger.error("Unable to load jme: " + e);
		}

		prepare();
		// initialize controller for moving the pusharm
		initController();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		triggerSpace = (StaticPhysicsNode) getNode().getChild("triggerSpace");
		armPhysics = (StaticPhysicsNode) getNode().getChild("armPhysics");
		prepare();
		if (running)
			turnOn();
	}
	private Node oldCol=null;
	private void prepare() {
		// set up collisions to trigger the pusharm
		triggerSpace.generatePhysicsGeometry();
		triggerSpace.setMaterial(Material.GHOST);
		InputAction triggerAction = new InputAction() {
			public void performAction(InputActionEvent evt) {
				Node collider = ((ContactInfo) evt.getTriggerData()).getNode1()
						.equals(triggerSpace) ? ((ContactInfo) evt
						.getTriggerData()).getNode2() : ((ContactInfo) evt
						.getTriggerData()).getNode1();

				if (collider != armPhysics && !collider.equals(oldCol)){
					oldCol=collider;
					trigger(collider);
				}
					
			}
		};
		SyntheticButton intersect = triggerSpace.getCollisionEventHandler();
		collisionHandler.addAction(triggerAction, intersect, false);
		collisionHandler.setEnabled(true);

		// initialize controller for moving the pusharm
		initController();
	}

	/**
	 * Creates the controller that moves the pusharm
	 */
	private void initController() {
		if (getNode() != null && st == null) {
			getNode().removeController(st);
			st = new SpatialTransformer(1);
			st.setObject(getNode().getChild("armPhysics"), 0, -1);
			st.setPosition(0, 0.0f, minpos);
			st.setPosition(0, 0.5f * speed, maxpos);
			st.setPosition(0, speed, minpos);
			st.interpolateMissing();
			st.setCurTime(st.getMaxTime());
			getNode().addController(st);
			st.setActive(false);
			return;
		}
		if (st != null) {
			getNode().removeController(st);
			st.setCurTime(st.getMaxTime());
			boolean active = st.isActive();
			st.setActive(false);
			st = new SpatialTransformer(1);
			st.setObject(getNode().getChild("armPhysics"), 0, -1);
			st.setPosition(0, 0.0f, minpos);
			st.setPosition(0, 0.5f * speed, maxpos);
			st.setPosition(0, speed, minpos);
			st.interpolateMissing();
			st.setCurTime(st.getMaxTime());
			getNode().addController(st);
			st.setActive(active);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#turnOn()
	 */
	public void turnOn() {
		running = true;
		activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.SceneControl#start()
	 */
	public void start() {
		paused = false;
		activate();
	}

	/**
	 * Starts the pusharm (whether turned on then started, or v.v.)
	 */
	private void activate() {
		if (running && !paused) {
			st.setActive(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Trigger#trigger(java.lang.Object)
	 */
	public void trigger(Object source) {
		
		if (running && !paused && !activationStack.isEmpty()
				&& activationStack.pop()) {
			if (st.getCurTime() == st.getMaxTime())
				st.setCurTime(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.SceneControl#pause()
	 */
	public void pause() {
		deactivate();
		st.setActive(false);
		paused = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.SceneControl#reset()
	 */
	public void reset() {
		// initController();
		st.setCurTime(st.getMaxTime());
		// armPhysics.setLocalTranslation(minpos);
		paused = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#turnOff()
	 */
	public void turnOff() {
		deactivate();
		activationStack.clear();
		running = false;
	}

	/**
	 * Stops the pusharm from moving
	 */
	private void deactivate() {
		if (running && !paused) {
			st.setActive(false);
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

	/**
	 * Sets whether the new running state of the pusharm
	 * 
	 * @param newrunning
	 *            the new state of running-ness
	 */
	public void setRunning(boolean newrunning) {
		running = newrunning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		((PhysicsNode) getNode().getChild("armPhysics")).setActive(false);
		getNode().removeFromParent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.NeedsPhysics#setCollisionHandler(com.jme.input.InputHandler)
	 */
	public void setCollisionHandler(InputHandler collisionHandler) {
		this.collisionHandler = collisionHandler;
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
	 * @see org.rifidi.designer.entities.interfaces.GPI#setHigh(int)
	 */
	@Override
	public void setHigh(int portNum) {
		activationStack.push(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.GPI#setLow(int)
	 */
	@Override
	public void setLow(int portNum) {
		// TODO Auto-generated method stub

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