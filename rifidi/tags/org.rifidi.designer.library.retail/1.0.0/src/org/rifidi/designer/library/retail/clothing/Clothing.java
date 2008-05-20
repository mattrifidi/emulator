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

import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.NeedsPhysics;
import org.rifidi.designer.entities.placement.BinaryPattern;

import com.jme.input.InputHandler;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
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
public class Clothing extends VisualEntity implements NeedsPhysics {

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
	 * Translation given on creation, ignored later on.
	 */
	private Vector3f startTranslation;
	/**
	 * Rotation given on creation, ignored later on.
	 */
	private Quaternion startRotation;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		((PhysicsNode)getNode()).setActive(false);
		((PhysicsNode)getNode()).delete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#init()
	 */
	@Override
	public void init() {
		BinaryPattern pattern = new BinaryPattern();
		pattern.setPattern(new boolean[][] { { true, true, true, true } });
		setPattern(pattern);
		setCollides(false);
		URI modelpath = null;
		try {
			modelpath = getClass().getClassLoader().getResource(
					"org/rifidi/designer/library/retail/clothing/clothing.jme")
					.toURI();
			model = (Node) BinaryImporter.getInstance().load(modelpath.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		DynamicPhysicsNode physix = physicsSpace.createDynamicNode();
		physix.attachChild(model);
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

	public void setStartTranslation(Vector3f startTranslation) {
		this.startTranslation = startTranslation;
	}

	public void setStartRotation(Quaternion startRotation) {
		this.startRotation = startRotation;
	}

}
