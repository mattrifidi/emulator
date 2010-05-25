/*
 *  CollisionOctree.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.octree;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * This interface defines the methods for an octree that can be used for
 * collision detection between two visual entites.
 * 
 * @author Jochen Mader - jochen@pramari.com - Jul 7, 2008
 * 
 */
public class CollisionOctree {
	/**
	 * Logger for this class.
	 */
	private static final Log logger = LogFactory.getLog(CollisionOctree.class);
	/**
	 * Root of the tree.
	 */
	private CubeOctreeNode root;

	/**
	 * @param minCuveSize
	 * @param startVolume
	 */
	public CollisionOctree(Float minCubeSize, BoundingBox startVolume) {
		logger.debug("Created new Octree");
		// create a cube
		float side = startVolume.xExtent > startVolume.zExtent ? startVolume.xExtent
				: startVolume.zExtent;
		int sideCount = (int) Math.ceil(side / minCubeSize);
		// no log2 in java, darn
		int count = 2;
		while (count < sideCount) {
			count *= 2;
		}
		sideCount = count;
		side = count * minCubeSize;
		root = new CubeOctreeNode(new Vector3f(side , side , side ),
				side, minCubeSize);
	}

	/**
	 * Insert the given entity into the collision tree.
	 * 
	 * @param entity
	 */
	public void insertEntity(VisualEntity entity) {
		if(root.intersects(entity.getBoundingNode())){
			root.addChild(entity);
		}
	}

	/**
	 * Remove the given entity from the collision tree.
	 * 
	 * @param entity
	 */
	public void removeEntity(VisualEntity entity) {
		root.removeChild(entity);
	}

	/**
	 * Find all colliders for the given entity.
	 * 
	 * @param entity
	 * @param colliders
	 */
	public void findCollisions(VisualEntity entity, Set<VisualEntity> colliders) {
		root.findCollision(entity.getBoundingNode(), colliders);
	}
	
	public Node getTreeAsNode(){
		Node node=new Node();
		root.getTreeAsNode(node);
		return node;
	}
}