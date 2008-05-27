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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.interfaces.Directional;
import org.rifidi.designer.entities.interfaces.GPI;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.interfaces.Switch;
import org.rifidi.designer.entities.placement.BinaryPattern;
import org.rifidi.jmeswt.utils.NodeHelper;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.state.AlphaState;
import com.jme.system.DisplaySystem;
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
public class ConveyorEntity extends VisualEntity implements Switch,
		Directional, NeedsPhysics, GPI {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(ConveyorEntity.class);
	/**
	 * Alphastate for the directional pointer.
	 */
	private AlphaState basicTrans = null;
	/**
	 * Node for the directional pointer.
	 */
	private static Node forwardIndicatorOrig;
	/**
	 * Flipped directional pointer.
	 */
	private static Node reverseIndicatorOrig;
	/**
	 * Modified forward indicator.
	 */
	private Node forwardIndicator;
	/**
	 * Modified reverse indicator.
	 */
	private Node reverseIndicator;
	/**
	 * Switch status on/off.
	 */
	private boolean active = false;
	/**
	 * Speed in feet per second.
	 */
	private float speed = 0;
	/**
	 * Model for shared meshes
	 */
	private static Node model = null;
	/**
	 * Material for the rollers
	 */
	private Material rollerMaterial = null;
	/**
	 * Reference to the physics space.
	 */
	private PhysicsSpace physicsSpace;

	/**
	 * Constructor.
	 */
	public ConveyorEntity() {
		basicTrans = DisplaySystem.getDisplaySystem().getRenderer()
				.createAlphaState();
		basicTrans.setBlendEnabled(true);
		basicTrans.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		basicTrans.setSrcFunction(AlphaState.SB_SRC_ALPHA);
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
			rollerMaterial.setSurfaceMotion(Vector3f.UNIT_Y.mult(speed));
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
		BinaryPattern pattern = new BinaryPattern();
		pattern.setPattern(new boolean[][] { { true, true, true, true },
				{ true, true, true, true }, { true, true, true, true },
				{ true, true, true, true }, { true, true, true, true },
				{ true, true, true, true }, { true, true, true, true },
				{ true, true, true, true }, { true, true, true, true },
				{ true, true, true, true } });
		setPattern(pattern);
		setNode(physicsSpace.createStaticNode());
		getNode().attachChild(new SharedNode("sharedConv_", model));

		((StaticPhysicsNode)getNode()).generatePhysicsGeometry();
		getNode().setModelBound(new BoundingBox());
		getNode().updateModelBound();

		logger.debug(NodeHelper.printNodeHierarchy(getNode(), 6));

		rollerMaterial = new Material("Roller");
		((PhysicsNode)getNode()).setMaterial(rollerMaterial);
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
		((PhysicsNode)getNode()).setMaterial(rollerMaterial);
		if (active) {
			active = false;
			turnOn();
		} else {
			active = true;
			turnOff();
		}
	}

	private void prepare() {
		if (model == null) {
			URI modelpath = null;
			try {
				modelpath = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/basemodels/conveyor/conveyor.jme")
						.toURI();
			} catch (URISyntaxException e) {
				logger.debug(e);
			}
			try {
				model = (Node) BinaryImporter.getInstance().load(
						modelpath.toURL());
				forwardIndicatorOrig = (Node) model.getChild("forwardArrows");
				reverseIndicatorOrig = (Node) model.getChild("backwardArrows");
				model.getChild("forwardArrows").removeFromParent();
				model.getChild("backwardArrows").removeFromParent();
			} catch (MalformedURLException e) {
				logger.debug(e);
			} catch (IOException e) {
				logger.debug(e);
			}
		}
		forwardIndicator = new SharedNode("shared_", forwardIndicatorOrig);
		reverseIndicator = new SharedNode("shared_", reverseIndicatorOrig);

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
	 * @see org.rifidi.designer.entities.interfaces.Switch#turnOff()
	 */
	@Override
	public void turnOff() {
		if (active) {
			((PhysicsNode)getNode()).getMaterial().setSurfaceMotion(Vector3f.ZERO);
			// rollers.setActive(true);
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
			rollerMaterial.setSurfaceMotion(Vector3f.UNIT_Y.mult(speed));
			// rollers.setActive(true);
			active = true;
		}
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
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		((PhysicsNode)getNode()).setActive(false);
		getNode().removeFromParent();
	}

	// TODO it would appear that unparenting things makes them lose their
	// localdata. Find a more graceful way of handling the fact

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Directional#showDirectionalIndicator()
	 */
	public void showDirectionalIndicator() {
		if (speed > 0) {
			forwardIndicator.setRenderState(basicTrans);
			forwardIndicator.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
			forwardIndicator.setLocalTranslation(0, 5, 0);
			forwardIndicator.setLocalRotation(new Quaternion().fromAngleAxis(
					90 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X));

			getNode().attachChild(forwardIndicator);
			// logger.debug(forwardIndicator.getName() + " " +
			// forwardIndicator.getLocalRotation());
		} else {
			reverseIndicator.setRenderState(basicTrans);
			reverseIndicator.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
			reverseIndicator.setLocalTranslation(0, 5, 0);
			reverseIndicator.setLocalRotation(new Quaternion().fromAngleAxis(
					90 * FastMath.DEG_TO_RAD, Vector3f.UNIT_X).multLocal(
					new Quaternion().fromAngleAxis(180 * FastMath.DEG_TO_RAD,
							Vector3f.UNIT_Z)));

			getNode().attachChild(reverseIndicator);
			// logger.debug(reverseIndicator.getName() + " " +
			// reverseIndicator.getLocalRotation());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Directional#hideDirectionalIndicator()
	 */
	public void hideDirectionalIndicator() {
		forwardIndicator.removeFromParent();
		reverseIndicator.removeFromParent();
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
	 * @see org.rifidi.designer.entities.interfaces.GPI#setHigh(int)
	 */
	@Override
	public void setHigh(int portNum) {
		// TODO Auto-generated method stub

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
	
}