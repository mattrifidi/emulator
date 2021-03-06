/*
 *  ConveyorEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.conveyor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.interfaces.INeedsPhysics;
import org.rifidi.designer.entities.interfaces.IHasSwitch;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.input.InputHandler;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.SwitchNode;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * An entity that represents a conveyor belt.
 * 
 * @author Jochen Mader Oct 8, 2007
 * @author Jeremy Choens - "Ghost" - jeremy@pramari.com
 * @author Dan West - dan@pramari.com
 */
@XmlRootElement
@MonitoredProperties(names = { "name" })
public class ConveyorEntity extends VisualEntity implements IHasSwitch,
		INeedsPhysics {
	/** Logger for this class. */
	@XmlTransient
	private static Log logger = LogFactory.getLog(ConveyorEntity.class);
	/** Alphastate for the directional pointer. */
	@XmlTransient
	private BlendState basicTrans = null;
	/** IHasSwitch status on/off. */
	private boolean active = false;
	/** Speed in feet per second. */
	private float speed = 0;
	/** Model for shared meshes */
	@XmlTransient
	private static Node[] lod = null;
	/** Material for the rollers */
	@XmlTransient
	private Material rollerMaterial = null;
	/** Reference to the physics space. */
	@XmlTransient
	private PhysicsSpace physicsSpace;
	/** Node that contains the different lods. */
	@XmlTransient
	private SwitchNode switchNode;
	/** Quad that indicates the movement direction of the conveyor. */
	@XmlTransient
	private Quad quad;

	/**
	 * Constructor.
	 */
	public ConveyorEntity() {
		basicTrans = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		basicTrans.setBlendEnabled(true);
		basicTrans
				.setDestinationFunction(DestinationFunction.OneMinusSourceAlpha);
		basicTrans.setSourceFunction(SourceFunction.SourceAlpha);
		basicTrans.setEnabled(true);
		setName("Conveyor");
		setSpeed(4);
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
	@Property(displayName = "Speed", description = "guess what", readonly = false, unit = "feet/s")
	public void setSpeed(float speed) {
		this.speed = speed;
		if (getNode() != null) {
			rollerMaterial.setSurfaceMotion(Vector3f.UNIT_X.mult(speed));
			if (speed < 0) {
				quad.setLocalRotation(new Quaternion(new float[] {
						(float) Math.toRadians(-90), 0, 0 }));
			} else {
				quad.setLocalRotation(new Quaternion(new float[] {
						(float) Math.toRadians(-90),
						(float) Math.toRadians(180), 0 }));
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
		setNode(new Node());
		StaticPhysicsNode phys = physicsSpace.createStaticNode();
		phys.setName("maingeometry");
		getNode().attachChild(phys);
		switchNode = new SwitchNode("switchnode");
		switchNode.attachChildAt(new SharedNode("sharedConv_", lod[0]), 0);
		switchNode.attachChildAt(new SharedNode("sharedConv_", lod[1]), 1);
		switchNode.attachChildAt(new SharedNode("sharedConv_", lod[2]), 2);
		switchNode.setActiveChild(0);
		phys.attachChild(switchNode);
		phys.generatePhysicsGeometry();
		switchNode.attachChildAt(new SharedNode("sharedConv_", lod[3]), 3);

		rollerMaterial = new Material("Roller");

		Node _node = new Node("hiliter");

		phys.setLocalTranslation(new Vector3f(-0.14f, ((BoundingBox) switchNode
				.getWorldBound()).yExtent, 0));
		Box box = new Box("hiliter", ((BoundingBox) phys.getWorldBound())
				.getCenter().clone().subtractLocal(
						getNode().getLocalTranslation()).addLocal(
						phys.getLocalTranslation()).addLocal(0.04f, 0f, 0f),
				2f, ((BoundingBox) phys.getWorldBound()).yExtent + 0.01f, 5f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		quad = new Quad("arrows",
				((BoundingBox) box.getModelBound()).xExtent * 2 - .2f,
				((BoundingBox) box.getModelBound()).zExtent * 2);
		quad.setLocalTranslation(new Vector3f(0f, 4.44f, 0f));
		quad.setLocalRotation(new Quaternion(new float[] {
				(float) Math.toRadians(-90), (float) Math.toRadians(180), 0 }));
		
		TextureState text = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		text.setTexture(TextureManager.loadTexture("arrow",
				MinificationFilter.Trilinear, MagnificationFilter.Bilinear));
		quad.setRenderState(text);
		BlendState blend = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		blend.setBlendEnabled(true);
		blend.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		blend
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		blend.setEnabled(true);
		quad.setRenderState(blend);
		quad.updateRenderState();
		_node.attachChild(quad);
		
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
		rollerMaterial = new Material("Roller");
		((PhysicsNode) getNode().getChild("maingeometry"))
				.setMaterial(rollerMaterial);
		switchNode = (SwitchNode) getNode().getChild("switchnode");
		if (active) {
			active = false;
			turnOn();
		} else {
			active = true;
			turnOff();
		}
	}

	private void prepare() {
		if (lod == null) {
			lod = new Node[4];
			URI modelpath = null;
			for (int count = 0; count < 4; count++) {
				try {
					modelpath = getClass().getClassLoader().getResource(
							"org/rifidi/designer/library/basemodels/conveyor/conveyor"
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
	 * @see org.rifidi.designer.entities.interfaces.IHasSwitch#turnOff()
	 */
	@Override
	public void turnOff() {
		if (active) {
			rollerMaterial.setSurfaceMotion(Vector3f.ZERO);
			// rollers.setActive(true);
			active = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IHasSwitch#turnOn()
	 */
	@Override
	public void turnOn() {
		if (!active) {
			rollerMaterial.setSurfaceMotion(Vector3f.UNIT_X.mult(speed));
			// rollers.setActive(true);
			active = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IHasSwitch#isRunning()
	 */
	public boolean isRunning() {
		return active;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		getNode().removeFromParent();
		((PhysicsNode) getNode().getChild("maingeometry")).delete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.INeedsPhysics#setCollisionHandler
	 * (com.jme.input.InputHandler)
	 */
	public void setCollisionHandler(InputHandler collisionHandler) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.INeedsPhysics#setPhysicsSpace(
	 * com.jmex.physics.PhysicsSpace)
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
	 * @see org.rifidi.designer.entities.Entity#pause()
	 */
	@Override
	public void pause() {
		((PhysicsNode) getNode().getChild("maingeometry")).setMaterial(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#reset()
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#start()
	 */
	@Override
	public void start() {
		if (rollerMaterial != null) {
			((PhysicsNode) getNode().getChild("maingeometry"))
					.setMaterial(rollerMaterial);
		}
	}

}