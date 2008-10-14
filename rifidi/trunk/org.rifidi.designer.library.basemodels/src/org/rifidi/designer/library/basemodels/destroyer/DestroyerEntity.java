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
package org.rifidi.designer.library.basemodels.destroyer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.services.annotations.Inject;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.BlendState.DestinationFunction;
import com.jme.scene.state.BlendState.SourceFunction;
import com.jme.system.DisplaySystem;
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
public class DestroyerEntity extends VisualEntity implements NeedsPhysics {
	/** Logger for this class. */
	private static Log logger = LogFactory.getLog(DestroyerEntity.class);
	/** Infrared trigger. */
	private StaticPhysicsNode triggerSpace = null;
	/** Reference to the physics space. */
	private PhysicsSpace physicsSpace;
	/** Reference to the sollision handler. */
	private InputHandler collisionHandler;
	/** Reference to the entities service. */
	private EntitiesService entitiesService;
	/** Reference to the finder service. */
	private FinderService finderService;

	/**
	 * Constructor
	 */
	public DestroyerEntity() {
		setName("Destroyer");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		Node mainNode = new Node();
		setNode(mainNode);
		mainNode.setModelBound(new BoundingBox());
		
		triggerSpace = physicsSpace.createStaticNode();
		triggerSpace.setName("triggerSpace");
		Box box = new Box("triggerSpace", new Vector3f(0, 3.5f, 0), 3f, 3.5f, 2.0f);
		triggerSpace.attachChild(box);
		mainNode.attachChild(triggerSpace);
		box.setRandomColors();
		BlendState as = DisplaySystem.getDisplaySystem().getRenderer()
		.createBlendState();
		as.setBlendEnabled(true);
		as.setSourceFunction(SourceFunction.SourceAlpha);
		as.setDestinationFunction(DestinationFunction.One);
		as.setEnabled(true);
		box.setRenderState(as);
		
		
		Node _node = new Node("hiliter");
		box = new Box("hiliter", new Vector3f(0, 3.5f, 0), 3f, 3.5f, 2.0f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);
		_node.setModelBound(new BoundingBox());
		_node.updateModelBound();
		_node.setCullHint(CullHint.Always);
		getNode().attachChild(_node);
		mainNode.getLocalTranslation().addLocal(new Vector3f(0, 3.5f, 0));
		prepare();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		triggerSpace = (StaticPhysicsNode) getNode().getChild("triggerSpace");
		prepare();
	}

	private Node oldCol = null;

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
				VisualEntity ent = finderService
						.getVisualEntityByNode(collider);
				if (ent != null) {
					List<Entity> entities = new ArrayList<Entity>();
					entities.add(ent);
					entitiesService.deleteEntities(entities);
				}
			}
		};
		SyntheticButton intersect = triggerSpace.getCollisionEventHandler();
		collisionHandler.addAction(triggerAction, intersect, false);
		collisionHandler.setEnabled(true);

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
	 * @see
	 * org.rifidi.designer.entities.interfaces.NeedsPhysics#setCollisionHandler
	 * (com.jme.input.InputHandler)
	 */
	public void setCollisionHandler(InputHandler collisionHandler) {
		this.collisionHandler = collisionHandler;
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

	/**
	 * @param entitiesService
	 *            the entitiesService to set
	 */
	@Inject
	public void setEntitiesService(EntitiesService entitiesService) {
		this.entitiesService = entitiesService;
	}

	/**
	 * @param finderService
	 *            the finderService to set
	 */
	@Inject
	public void setFinderService(FinderService finderService) {
		this.finderService = finderService;
	}
}