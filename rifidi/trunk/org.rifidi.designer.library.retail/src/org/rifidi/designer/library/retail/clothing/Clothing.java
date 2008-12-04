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

import javax.xml.bind.annotation.XmlTransient;

import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.INeedsPhysics;

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
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Apr 3, 2008
 * 
 */
public class Clothing extends VisualEntity implements INeedsPhysics {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			Quaternion quat = new Quaternion(new float[] {
					(float) Math.toRadians(270f), 0f, 0f });
			model.setLocalRotation(quat);
		}
		DynamicPhysicsNode physix = physicsSpace.createDynamicNode();
		physix.attachChild(new SharedNode("sharedcloth", model));
		setNode(physix);
		physix.setLocalTranslation(startTranslation);
		physix.setLocalRotation(startRotation);
		physix.updateModelBound();
		physix.generatePhysicsGeometry();
		physix.setIsCollidable(false);
		physix.setActive(false);
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
}
