/*
 *  NeedsPhysics.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

import com.jme.input.InputHandler;
import com.jmex.physics.PhysicsSpace;

/**
 * This interface marks entites which need to have access to the physical world.
 * 
 * @author Jochen Mader Jan 20, 2008
 * 
 */
public interface NeedsPhysics {
	/**
	 * @param physicsSpace The currently used physicsspace.
	 */
	void setPhysicsSpace(PhysicsSpace physicsSpace);

	/**
	 * @param collisionHandler The currently registered InputHandler
	 */
	void setCollisionHandler(InputHandler collisionHandler);
}
