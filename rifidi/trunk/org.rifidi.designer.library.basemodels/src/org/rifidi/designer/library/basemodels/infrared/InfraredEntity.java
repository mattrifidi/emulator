/*
 *  DestroyerEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.infrared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.gpio.GPIPort;
import org.rifidi.designer.entities.gpio.GPOPort;
import org.rifidi.designer.entities.gpio.IGPIO;
import org.rifidi.designer.entities.gpio.GPOPort.State;
import org.rifidi.designer.entities.interfaces.IField;
import org.rifidi.designer.entities.interfaces.INeedsPhysics;
import org.rifidi.designer.entities.interfaces.IHasSwitch;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.services.annotations.Inject;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * This is a simple infrared field. It has one GPO to trigger other entites if
 * something is passing through.
 * 
 * @author Jochen Mader Oct 8, 2007
 * @author Dan West
 */
@MonitoredProperties(names = { "name" })
@XmlRootElement
public class InfraredEntity extends VisualEntity implements IHasSwitch,
		INeedsPhysics, IGPIO, IField {
	/** Reference to the current physicsspace */
	private PhysicsSpace physicsSpace;
	/** Running state of the entity. */
	private boolean running;
	/** Output port of the entity */
	private GPOPort port;
	/** Reference to the field service. */
	private FieldService fieldService;

	/**
	 * Constructor
	 */
	public InfraredEntity() {
		setName("Infrared");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		port = new GPOPort();
		port.setNr(0);
		port.setId(getEntityId() + "-gpo-0");
		float len = 2.5f; // length of the trigger area

		// Create the material and alpha states for the trigger area
		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setDiffuse(new ColorRGBA(1, 1, 1, .6f));
		BlendState as = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		as.setBlendEnabled(true);
		as.setSourceFunction(SourceFunction.SourceAlpha);
		as.setDestinationFunction(DestinationFunction.One);
		as.setEnabled(true);

		// create the trigger area
		Box irGeom = new Box("triggerSpace_geom", new Vector3f(-3 - len + 3,
				6f, 0f), len, 1.5f, .15f);
		irGeom.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		StaticPhysicsNode triggerSpace = physicsSpace.createStaticNode();
		triggerSpace.setName("triggerSpace");
		triggerSpace.attachChild(irGeom);
		triggerSpace.setModelBound(new BoundingBox());
		triggerSpace.setRenderState(ms);
		triggerSpace.setRenderState(as);
		triggerSpace.updateModelBound();
		triggerSpace.updateRenderState();
		setNode(triggerSpace);

		prepare();
		Node _node = new Node("hiliter");
		Box hilit = new Box("hiliter", new Vector3f(-3 - len + 3, 6f, 0f), len,
				1.5f, .15f);
		hilit.setModelBound(new BoundingBox());
		hilit.updateModelBound();
		_node.attachChild(hilit);
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
	}

	private void prepare() {
		// set up collisions to trigger the pusharm
		((StaticPhysicsNode) getNode()).generatePhysicsGeometry();
		((StaticPhysicsNode) getNode()).setMaterial(Material.GHOST);
		fieldService.registerField(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IHasSwitch#turnOn()
	 */
	public void turnOn() {
		running = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IHasSwitch#turnOff()
	 */
	public void turnOff() {
		running = false;
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

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void fieldEntered(Entity entity) {
		if (isRunning()) {
			port.setState(State.HIGH);
		}
	}

	@Override
	public void fieldLeft(Entity entity) {
		port.setState(State.LOW);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.gpio.IGPIO#getGPIPorts()
	 */
	@Override
	public List<GPIPort> getGPIPorts() {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.gpio.IGPIO#getGPOPorts()
	 */
	@Override
	public List<GPOPort> getGPOPorts() {
		List<GPOPort> portList = new ArrayList<GPOPort>();
		portList.add(port);
		return portList;
	}

	/**
	 * @param fieldService
	 *            the fieldService to set
	 */
	@Inject
	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	/**
	 * @return the port
	 */
	public GPOPort getPort() {
		return this.port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(GPOPort port) {
		this.port = port;
	}

}