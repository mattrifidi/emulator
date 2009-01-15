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
package org.rifidi.designer.library.retail.clothing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.INeedsPhysics;
import org.rifidi.designer.entities.interfaces.IProduct;
import org.rifidi.designer.entities.rifidi.ITagged;
import org.rifidi.designer.library.retail.clothingrack.ClothingRack;
import org.rifidi.tags.impl.RifidiTag;

import com.jme.bounding.BoundingBox;
import com.jme.input.InputHandler;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedNode;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;

/**
 * FIXME: Class comment.
 * 
 * @author Jochen Mader - jochen@pramari.com - Apr 3, 2008
 * 
 */
public class Clothing extends VisualEntity implements INeedsPhysics, ITagged,
		IProduct<ClothingRack> {
	/** Logger for this class. */
	private static Log logger = LogFactory.getLog(Clothing.class);
	/** Reference to the current physics space. */
	@XmlTransient
	private PhysicsSpace physicsSpace;
	/** Model for shared meshes */
	@XmlTransient
	private static Node model = null;
	/** Translation given on creation, ignored later on. */
	@XmlTransient
	private Vector3f startTranslation;
	/** Rotation given on creation, ignored later on. */
	@XmlTransient
	private Quaternion startRotation;
	/** Reference to the rack this cloth came out of. */
	@XmlIDREF
	private ClothingRack producer;
	/** Tag associated with this clothing. */
	@XmlIDREF
	private RifidiTag tag;
	
	/**
	 * 
	 */
	public Clothing() {
		super();
		setName("Clothing ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		destructible = true;
		((PhysicsNode) getNode()).setActive(false);
		((PhysicsNode) getNode()).delete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		setCollides(false);
		URI modelpath = null;
		if (model == null) {
			try {
				modelpath = getClass()
						.getClassLoader()
						.getResource(
								"org/rifidi/designer/library/retail/clothing/cloth0.jme")
						.toURI();
				model = (Node) BinaryImporter.getInstance().load(
						modelpath.toURL());
			} catch (MalformedURLException e) {
				logger.error("Can't load model: " + e);
			} catch (IOException e) {
				logger.error("Can't load model: " + e);
			} catch (URISyntaxException e) {
				logger.error("Can't load model: " + e);
			}
			Quaternion quat = new Quaternion(new float[] {
					(float) Math.toRadians(270f), 0f, 0f });
			model.setLocalRotation(quat);
		}
		DynamicPhysicsNode physix = physicsSpace.createDynamicNode();
		physix.setModelBound(new BoundingBox());
		physix.attachChild(new SharedNode("sharedcloth", model));
		setNode(physix);
		physix.setLocalTranslation(startTranslation);
		physix.setLocalRotation(startRotation);
		physix.updateModelBound();
		physix.generatePhysicsGeometry();
		physix.setIsCollidable(true);
		physix.setActive(true);
		physix.updateModelBound();
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
	 * org.rifidi.designer.entities.interfaces.INeedsPhysics#setCollisionHandler
	 * (com.jme.input.InputHandler)
	 */
	@Override
	public void setCollisionHandler(InputHandler collisionHandler) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.interfaces.INeedsPhysics#setPhysicsSpace(
	 * com.jmex.physics.PhysicsSpace)
	 */
	@Override
	public void setPhysicsSpace(PhysicsSpace physicsSpace) {
		this.physicsSpace = physicsSpace;
	}

	public void setStartTranslation(Vector3f startTranslation) {
		this.startTranslation = startTranslation;
	}

	public void setStartRotation(Quaternion startRotation) {
		this.startRotation = startRotation;
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
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.rifidi.ITagged#getRifidiTag()
	 */
	@Override
	public RifidiTag getRifidiTag() {
		return tag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.entities.rifidi.ITagged#setRifidiTag(org.rifidi.tags
	 * .impl.RifidiTag)
	 */
	@Override
	public void setRifidiTag(RifidiTag tag) {
		this.tag=tag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.IProduct#getProducer()
	 */
	@Override
	public ClothingRack getProducer() {
		return producer;
	}

	/**
	 * @param producer
	 *            the producer to set
	 */
	public void setProducer(ClothingRack producer) {
		this.producer = producer;
	}
}
