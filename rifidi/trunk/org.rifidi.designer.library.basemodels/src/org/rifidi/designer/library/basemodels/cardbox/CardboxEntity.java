/*
 *  CardboxEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.cardbox;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.annotations.Property;
import org.rifidi.designer.entities.databinding.annotations.MonitoredProperties;
import org.rifidi.designer.entities.interfaces.INeedsPhysics;
import org.rifidi.designer.entities.rifidi.ITagged;
import org.rifidi.services.tags.impl.RifidiTag;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Box;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;

/**
 * A simple cardboard box.
 * 
 * @author Jochen Mader Oct 8, 2007
 * 
 */
@MonitoredProperties(names = { "name" })
public class CardboxEntity extends VisualEntity implements INeedsPhysics,
		ITagged {
	/** logger for this class. */
	private static Log logger = LogFactory.getLog(CardboxEntity.class);
	/** Startposition. */
	private Vector3f startPos = new Vector3f(0, 10, 0);
	/** Roatation after creation. */
	private Matrix3f baseRotation;
	/** Model for shared meshes. */
	private static Node model;
	/** Reference to the physics space. */
	private PhysicsSpace physicsSpace;
	/** Tag associated with this entity. */
	private RifidiTag rifidiTag;

	/**
	 * Constructor.
	 */
	public CardboxEntity() {
		setName("Cardbox");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		if (model == null) {
			try {
				URI modelpath = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/basemodels/cardbox/cardboardbox.jme")
						.toURI();
				model = (Node) BinaryImporter.getInstance().load(
						modelpath.toURL());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				logger.error("Unable to load jme: " + e);
			}
		}
		DynamicPhysicsNode node = physicsSpace.createDynamicNode();
		node.setLocalTranslation(startPos);
		if (baseRotation != null) {
			node.getLocalRotation().apply(baseRotation);
		}
		node.attachChild(new SharedNode("sharedCBox_", model));
		// node.attachChild(model);
		node.generatePhysicsGeometry();
		node.setIsCollidable(true);
		node.setActive(true);
		node.setModelBound(new BoundingBox());
		node.updateModelBound();
		setNode(node);
		Node _node = new Node("hiliter");
		Box box = new Box("hiliter", new Vector3f(0, 0f, 0), 1.01f, 1.01f,
				1.01f);
		box.setModelBound(new BoundingBox());
		box.updateModelBound();
		_node.attachChild(box);
		_node.setModelBound(new BoundingBox());
		_node.updateModelBound();
		_node.setCullHint(CullHint.Always);
		getNode().attachChild(_node);
		setCollides(false);
		setName("Cardbox-"+getEntityId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		if (model == null) {
			try {
				URI modelpath = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/basemodels/cardbox/cardboardbox.jme")
						.toURI();
				model = (Node) BinaryImporter.getInstance().load(
						modelpath.toURL());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				logger.error("Unable to load jme: " + e);
			}
		}
	}

	/**
	 * @return the startPos
	 */
	@XmlTransient
	public Vector3f getStartPos() {
		return startPos;
	}

	/**
	 * @param startPos
	 *            the startPos to set
	 */
	public void setStartPos(Vector3f startPos) {
		this.startPos = startPos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		((DynamicPhysicsNode) getNode()).setActive(false);
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

	/**
	 * @param baseRotation
	 *            the baseRotation to set
	 */
	public void setBaseRotation(Matrix3f baseRotation) {
		this.baseRotation = baseRotation;
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
	 * @see org.rifidi.designer.entities.interfaces.ITagged#getRifidiTag()
	 */
	@Override
	@XmlIDREF
	public RifidiTag getRifidiTag() {
		return rifidiTag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.ITagged#setRifidiTag(org.rifidi
	 * .services.tags.impl.RifidiTag)
	 */
	@Override
	public void setRifidiTag(RifidiTag tag) {
		this.rifidiTag = tag;
	}

	@Property(displayName = "Tag", description = "tag assigned to this box", readonly = true, unit = "")
	public void setTagID(String tagID){
		
	}
	
	@XmlTransient
	public String getTagID(){
		if(rifidiTag!=null){
			return rifidiTag.toString();
		}
		return "";
	}
}
