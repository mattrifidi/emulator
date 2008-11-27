/*
 *  AntennaFieldEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.designer.library.basemodels.antennafield;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.ChildEntity;
import org.rifidi.designer.entities.interfaces.Field;
import org.rifidi.designer.entities.interfaces.ITagged;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.interfaces.Switch;
import org.rifidi.designer.library.basemodels.gate.GateEntity;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.events.EventsService;
import org.rifidi.designer.services.core.events.TagEvent;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.tags.impl.RifidiTag;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.SwitchNode;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * This entity detects collisions with entities and notifies the associated
 * reader if the colliding entity has an RFID tag
 * 
 * @author Dan West - 'Phoenix' - dan@pramari.com
 * @author Jochen Mader
 */
public class AntennaFieldEntity extends VisualEntity implements Switch,
		NeedsPhysics, Field, ChildEntity {
	/** Logger for this class. */
	private static Log logger = LogFactory.getLog(AntennaFieldEntity.class);
	/**
	 * The entity is the key and the value is the tag associated with the
	 * entity.
	 */
	private Map<Entity, RifidiTag> seenTags = new HashMap<Entity, RifidiTag>();
	/** Map of entities and a timestamp when they were lakst seen. */
	private Map<Entity, Long> seen = new HashMap<Entity, Long>();
	/** interface used for communicating with the reader */
	private ReaderModuleManagerInterface readerInterface;
	/** this antenna's index */
	private int antennaNum;
	/** True if the scene is in running state. */
	private boolean running;
	/** Reference to the physics space */
	private PhysicsSpace physicsSpace;
	/** Initial translation of the antenna. */
	private Vector3f baseTranslation;
	/** Initial rotation of the antenna. */
	private Vector3f baseRotation;
	/** The entity this antenna is attached to. */
	private VisualEntity parent;
	/** The factor by which the field gets resized. */
	private float factor;
	/** Transparency states. */
	private BlendState as;
	private MaterialState ms;

	/** Thread for checking the field for entites in it. */
	private AntennaFieldThread antennaFieldThread;
	/**
	 * Reference to the events service.
	 */
	private EventsService eventsService;
	/**
	 * Reference to the field service.
	 */
	private FieldService fieldService;
	/**
	 * LOD node.
	 */
	private SwitchNode switchNode;
	/**
	 * The shared model we create the shared meshes from.
	 */
	private static Node model = null;

	/**
	 * Default constructor (used by JAXB)
	 */
	public AntennaFieldEntity() {
		factor = 1.0f;
	}

	/**
	 * Instantiates a new antenna field
	 * 
	 * @param antNum
	 *            which antenna of the reader this field represents
	 * @param rmmi
	 *            the interface for communicating with this field's reader
	 */
	public AntennaFieldEntity(int antNum, ReaderModuleManagerInterface rmmi) {
		factor = 1.0f;
		readerInterface = rmmi;
		antennaNum = antNum;
		setCollides(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		prepare();
		if (getNode() == null) {
			setNode(physicsSpace.createStaticNode());
			getNode().setModelBound(new BoundingBox());
			getNode().updateModelBound();
			getNode().setLocalTranslation(baseTranslation);
			getNode().getLocalRotation().fromAngles(baseRotation.x,
					baseRotation.y, baseRotation.z);
		}
		loaded();

		running = false;
		fieldService.registerField(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		prepare();
		switchNode.setLocalScale(factor);

		// apply the transparency
		getNode().setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		getNode().setRenderState(ms);
		getNode().setRenderState(as);
		getNode().updateRenderState();

		// set up the collision properties for the field
		((PhysicsNode) getNode()).generatePhysicsGeometry();
		((StaticPhysicsNode) getNode()).setMaterial(Material.GHOST);
		fieldService.registerField(this);
	}

	private void prepare() {
		// Load the model if it idn't loaded yet.
		if (model == null) {
			URI modelpath = null;
			try {
				modelpath = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/basemodels/antennafield/field.jme")
						.toURI();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			try {
				model = (Node) BinaryImporter.getInstance().load(
						modelpath.toURL());
				model.setLocalRotation(new Quaternion(new float[] {
						(float) Math.toRadians(270), 0, 0 }));
			} catch (MalformedURLException e) {
				logger.fatal(e);
			} catch (IOException e) {
				logger.fatal(e);
			}
		}
		// Create the switchnode if it is not yet created.
		if (switchNode == null) {
			switchNode = new SwitchNode();
			switchNode.attachChild(new SharedNode(model));
			switchNode.attachChild(new Box("iii", Vector3f.ZERO.clone(), 1, 1,
					1));
			switchNode.setActiveChild(0);
		}
		// Enable transparency
		as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		as.setBlendEnabled(true);
		as.setSourceFunction(SourceFunction.SourceAlpha);
		as.setDestinationFunction(DestinationFunction.OneMinusSourceColor);
		as.setEnabled(true);
		// create material and alpha states for the field
		ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setDiffuse(new ColorRGBA(.1f, .1f, .1f, .1f));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#isRunning()
	 */
	public boolean isRunning() {
		return running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#turnOn()
	 */
	public void turnOn() {
		if (!running == true) {
			// attach the node to the scene and enable the collision checks
			update(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception {
					((PhysicsNode) getNode()).attachChild(switchNode);
					((PhysicsNode) getNode()).setActive(true);
					((PhysicsNode) getNode()).generatePhysicsGeometry();
					getNode().updateRenderState();
					return null;
				}

			});
			antennaFieldThread = new AntennaFieldThread("AntennaFieldThread",
					antennaNum, readerInterface);
			antennaFieldThread.start();
		}
		running = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.Switch#turnOff()
	 */
	public void turnOff() {
		if (!running == false) {
			// detach the node from the scene and disable the collision checks
			update(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception {
					((PhysicsNode) getNode()).setActive(false);
					getNode().detachAllChildren();
					getNode().updateGeometricState(0, true);
					return null;
				}

			});
			antennaFieldThread.setKeepRunning(false);
		}
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.Field#fieldEntered(org.rifidi
	 * .designer.entities.Entity)
	 */
	@Override
	public void fieldEntered(Entity entity) {
		if (entity instanceof ITagged) {
			if (((ITagged) entity.getUserData()).getRifidiTag() != null) {
				// add action to the thread for processing
				antennaFieldThread.addAction(new AntennaFieldAction(true,
						((ITagged) entity.getUserData()).getRifidiTag()));
				// publish collision event
				eventsService.publish(new TagEvent(((ITagged) entity
						.getUserData()).getRifidiTag(), readerInterface,
						antennaNum, true));
			}
			// inform the parent of the collision
			((GateEntity) getParent()).tagSeen();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.Field#fieldLeft(org.rifidi.designer
	 * .entities.Entity)
	 */
	@Override
	public void fieldLeft(Entity entity) {
		if (entity.getUserData() instanceof RifidiTag) {
			antennaFieldThread.addAction(new AntennaFieldAction(false,
					(RifidiTag) entity.getUserData()));
			eventsService.publish(new TagEvent(
					(RifidiTag) entity.getUserData(), readerInterface,
					antennaNum, false));
		}
	}

	/**
	 * @return a modifyable map of the currently seen entities and their tags
	 */
	public Map<Entity, RifidiTag> getSeenTags() {
		return seenTags;
	}

	/**
	 * @return a modifyable map of the currently seen entities and how long
	 *         they've been seen for
	 */
	public Map<Entity, Long> getSeen() {
		return seen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		fieldService.unregisterField(this);
		((PhysicsNode) getNode()).delete();
	}

	/**
	 * @return the readerModuleManagerInterface
	 */
	public ReaderModuleManagerInterface getReaderInterface() {
		return readerInterface;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.NeedsPhysics#setCollisionHandler
	 * (com.jme.input.InputHandler)
	 */
	public void setCollisionHandler(InputHandler collisionHandler) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.NeedsPhysics#setPhysicsSpace(
	 * com.jmex.physics.PhysicsSpace)
	 */
	public void setPhysicsSpace(PhysicsSpace physicsSpace) {
		this.physicsSpace = physicsSpace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.ChildEntity#getParent()
	 */
	@Override
	public VisualEntity getParent() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.ChildEntity#setParent(org.rifidi
	 * .designer.entities.VisualEntity)
	 */
	@Override
	@XmlIDREF
	public void setParent(VisualEntity entity) {
		this.parent = entity;
	}

	/**
	 * @param baseTranslation
	 *            the baseTranslation to set
	 */
	public void setBaseTranslation(Vector3f baseTranslation) {
		this.baseTranslation = baseTranslation;
	}

	/**
	 * @param baseRotation
	 *            the baseRotation to set
	 */
	public void setBaseRotation(Vector3f baseRotation) {
		this.baseRotation = baseRotation;
	}

	/**
	 * @return the factor
	 */
	public float getFactor() {
		return this.factor;
	}

	/**
	 * @param factor
	 *            the factor to set
	 */
	public void setFactor(float factor) {
		this.factor = factor;
	}

	/**
	 * Change the size factor of the field.
	 * 
	 * @param newFactor
	 */
	public void updateFactor(float newFactor) {
		this.factor = newFactor;
		update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				((PhysicsNode) getNode()).setActive(false);
				getNode().detachAllChildren();
				switchNode.setLocalScale(factor);
				getNode().attachChild(switchNode);
				getNode().updateModelBound();
				((PhysicsNode) getNode()).generatePhysicsGeometry();
				((PhysicsNode) getNode()).setActive(true);
				return null;
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#setLOD(int)
	 */
	@Override
	public void setLOD(int lod) {
		// switchNode.setActiveChild(lod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.VisualEntity#getBoundingNode()
	 */
	@Override
	public Node getBoundingNode() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param readerInterface
	 *            the readerInterface to set
	 */
	@XmlTransient
	public void setReaderInterface(ReaderModuleManagerInterface readerInterface) {
		this.readerInterface = readerInterface;
	}

	/**
	 * @param eventsService
	 *            the eventsService to set
	 */
	@Inject
	public void setEventsService(EventsService eventsService) {
		this.eventsService = eventsService;
	}

	/**
	 * @param fieldService
	 *            the fieldService to set
	 */
	@Inject
	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

}