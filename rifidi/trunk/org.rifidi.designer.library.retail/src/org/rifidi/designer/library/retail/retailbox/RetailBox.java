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
package org.rifidi.designer.library.retail.retailbox;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.placement.BinaryPattern;

import com.jme.input.InputHandler;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Apr 3, 2008
 * 
 */
public class RetailBox extends VisualEntity implements NeedsPhysics {

	/**
	 * Reference to the collision input handler.
	 */
	private InputHandler inputHandler;
	/**
	 * Reference to the current physics space.
	 */
	private PhysicsSpace physicsSpace;
	/**
	 * Model for shared meshes
	 */
	private static Node model = null;
	/**
	 * Translation for initial translation. Never used afterwards.
	 */
	private Vector3f startTranslation;

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
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		URI modelpath = null;
		try {
			modelpath = getClass().getClassLoader().getResource(
					"org/rifidi/designer/library/retail/retailbox/box.jme")
					.toURI();
			model = (Node) BinaryImporter.getInstance().load(modelpath.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		DynamicPhysicsNode phys=physicsSpace.createDynamicNode();
		phys.attachChild(model);
		phys.setLocalScale(0.5f);
		phys.setLocalTranslation(startTranslation);
		phys.updateModelBound();
		phys.generatePhysicsGeometry();
		phys.setActive(false);
		phys.setIsCollidable(false);
		setNode(phys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#loaded()
	 */
	@Override
	public void loaded() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.NeedsPhysics#setCollisionHandler(com.jme.input.InputHandler)
	 */
	@Override
	public void setCollisionHandler(InputHandler collisionHandler) {
		this.inputHandler = collisionHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.interfaces.NeedsPhysics#setPhysicsSpace(com.jmex.physics.PhysicsSpace)
	 */
	@Override
	public void setPhysicsSpace(PhysicsSpace physicsSpace) {
		this.physicsSpace = physicsSpace;
	}

	/**
	 * @param startTranslation
	 *            the startTranslation to set
	 */
	public void setStartTranslation(Vector3f startTranslation) {
		this.startTranslation = startTranslation;
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
