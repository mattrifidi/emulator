/*
 *  FloorPartEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.internal;

import org.rifidi.designer.entities.VisualEntity;

import com.jme.scene.Node;

/**
 * This entity represents a part of the floor, like a wall.
 * This entity is not intended to be persisted!
 * 
 * @author Jochen Mader - jochen@pramari.com - Jul 13, 2008
 * 
 */
public class FloorPartEntity extends VisualEntity {

	/**
	 * Constructor.
	 * @param name
	 * @param floorNode
	 */
	public FloorPartEntity(String name, Node floorNode) {
		setNode(floorNode);
		setName(name);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.designer.entities.VisualEntity#getBoundingNode()
	 */
	@Override
	public Node getBoundingNode() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.designer.entities.VisualEntity#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.rifidi.designer.entities.VisualEntity#loaded()
	 */
	@Override
	public void loaded() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.rifidi.designer.entities.VisualEntity#setLOD(int)
	 */
	@Override
	public void setLOD(int lod) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
